package eu.dar3.borsch.qr;

import eu.dar3.borsch.recipe.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/qr")
@RestController
public class QRController {
    private final RecipeService recipeService;

    @GetMapping(value = "/recipe", headers = "Accept=image/jpeg, image/jpg, image/png, image/gif")
    public @ResponseBody byte[] getQR(UriComponentsBuilder uriComponentsBuilder, @RequestParam(name = "id") UUID id)
            throws Exception {
        BufferedImage bufferedImage = QRService.generateQRCodeImage(recipeService.getSharedLink(id,
                uriComponentsBuilder));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        return imageInByte;
    }
}
