package eu.dar3.borsch.recipe;

import eu.dar3.borsch.user.User;
import eu.dar3.borsch.user.UserOptionsService;
import eu.dar3.borsch.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static eu.dar3.borsch.utils.Constants.REDIRECT_URL_404;

@RequestMapping("/recipe")
@RequiredArgsConstructor
@Controller
public class RecipeController {

    private static final int PAGE_COUNT_ZERO = 0;
    private static final String RECIPE_UPDATE_TEMPLATE = "recipe/update";
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final RecipeService recipeService;
    private final UserService userService;
    private final UserOptionsService userOptionsService;

    @Value("${recipe.page.size}")
    @PostMapping("/create")
    public RedirectView createRecipe(@RequestParam(value = "title") String title,
                                     @RequestParam(value = "note") String note,
                                     @RequestParam(value = "publicRecipe", required = false) String publicRecipe) {
        RecipeAccessType accessType = RecipeAccessType.PRIVATE;
        if (publicRecipe != null) {
            accessType = RecipeAccessType.PUBLIC;
        }
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setNote(note);
        recipeDto.setTitle(title);
        recipeDto.setRecipeAccessType(accessType);
        RecipeDto saveDto = recipeService.add(recipeDto);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/recipe/view?id=" + saveDto.getId());
        return redirect;
    }

    @GetMapping("/create")
    public ModelAndView createRecipeViewPage() {
        ModelAndView result = new ModelAndView("recipe/create");
        result.addObject("options", userOptionsService.getOptions());
        return result;
    }

    @GetMapping("/list")
    public ModelAndView getRecipes(
            @RequestParam(required = false, name = "page") Optional<Integer> page,
            @RequestParam(required = false, name = "size") Optional<Integer> size,
            @RequestParam(required = false, name = "searchText", defaultValue = "") String searchText) {
        System.out.println("recipe/list................................");
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);
        ModelAndView result = new ModelAndView("recipe/recipe");
        Page<RecipeDto> recipePage = recipeService.findAllByRecipeOwnerFriendgroup(
                PageRequest.of(currentPage - 1, pageSize), searchText);
        int totalPages = recipePage.getTotalPages();
        result.addObject("recipePage", recipePage);
        result.addObject("previousPage", currentPage > 1 ? currentPage - 1 : 1);
        result.addObject("currentPage", currentPage);
        result.addObject("nextPage", currentPage < totalPages ? currentPage + 1 : totalPages);
        result.addObject("searchText", searchText);
        if (totalPages > PAGE_COUNT_ZERO) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().toList();
            result.addObject("pageNumbers", pageNumbers);
        }
        result.addObject("options", userOptionsService.getOptions());
        return result;
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam UUID id) {
        try {
            RecipeDto recipeDto = recipeService.getById(id);
            model.addAttribute("recipe", recipeDto);
            model.addAttribute("options", userOptionsService.getOptions());
        } catch (EntityNotFoundException e) {
            return REDIRECT_URL_404;
        }
        return RECIPE_UPDATE_TEMPLATE;
    }

    @GetMapping("/view")
    public String view(Model model, UriComponentsBuilder uriComponentsBuilder,
                       @RequestParam UUID id) {
        try {
            RecipeDto recipeDto = recipeService.getById(id);
            model.addAttribute("recipe", recipeDto);
            model.addAttribute("sharedLink", recipeService.getSharedLink(
                    id, uriComponentsBuilder));
            model.addAttribute("options", userOptionsService.getOptions());
        } catch (EntityNotFoundException e) {
            return REDIRECT_URL_404;
        }
        return "recipe/view";
    }

    @PostMapping("/edit")
    public RedirectView editRecipe(
            @RequestParam(value = "id") UUID id,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "note") String note,
            @RequestParam(value = "publicRecipe", required = false) String publicRecipe) {

        RecipeAccessType accessType = RecipeAccessType.PRIVATE;
        if (publicRecipe != null) {
            accessType = RecipeAccessType.PUBLIC;
        }
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(id);
        recipeDto.setNote(note);
        recipeDto.setTitle(title);
        recipeDto.setRecipeAccessType(accessType);
        recipeService.update(recipeDto);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/recipe/view?id=" + id);
        return redirect;
    }

    @GetMapping("/delete")
    public RedirectView delete(@RequestParam UUID id) {
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/recipe/list");
        recipeService.deleteById(id);
        return redirect;
    }


    @GetMapping("/share/{id}")
    public String shareRecipe(@PathVariable(name = "id") UUID id, Model model) {
        RecipeDto recipeDto = null;
        try {
            recipeDto = recipeService.getById(id);
            model.addAttribute("recipe", recipeDto);
            model.addAttribute("options", userOptionsService.getOptions());
        } catch (EntityNotFoundException e) {
            return REDIRECT_URL_404;
        }

        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser.getFriendgroup() != null) {
                List<User> friendgroupUsers = userService.getFriendgroupUsers(currentUser.getFriendgroup());
                if (friendgroupUsers.contains(recipeDto.getRecipeOwner())) {
                    return "recipe/share";
                }
            }
            if (Objects.equals(recipeDto.getRecipeAccessType(), "private")) {
                return REDIRECT_URL_404;
            }
        } catch (Exception e) {
            if (Objects.equals(recipeDto.getRecipeAccessType(), "private")) {
                return REDIRECT_URL_404;
            }
        }
        return "recipe/share";
    }

    @PostMapping("/share/{id}")
    public String getLink(@PathVariable("id") UUID id, Model model, HttpServletRequest request) {
        RecipeDto recipeDto = recipeService.getById(id);
        model.addAttribute("recipe", recipeDto);
        String fullUrl = request.getRequestURL().toString();
        recipeService.copyLink(fullUrl);
        return "redirect:recipe/view?id=" + id;
    }

    @PostMapping("/tag/add")
    public String addTag(Model model, @RequestParam UUID id, @RequestParam String tagTitle) {
        RecipeDto recipeDto = recipeService.addTag(id, tagTitle);
        model.addAttribute("recipe", recipeDto);
        model.addAttribute("options", userOptionsService.getOptions());
        return (RECIPE_UPDATE_TEMPLATE);
    }

    @GetMapping("/tag/delete")
    public String deleteTag(Model model, @RequestParam UUID recipeId, @RequestParam UUID tagId) {
        RecipeDto recipeDto = recipeService.deleteTag(recipeId, tagId);
        model.addAttribute("recipe", recipeDto);
        model.addAttribute("options", userOptionsService.getOptions());
        return (RECIPE_UPDATE_TEMPLATE);
    }
}
