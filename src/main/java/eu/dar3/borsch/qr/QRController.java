package eu.dar3.borsch.qr;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.RequiredArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

import eu.dar3.borsch.recipe.RecipeService;

@RequiredArgsConstructor
@RequestMapping("/qr")
@RestController
public class QRController {
    private final RecipeService recipeService;

    @GetMapping(value = "/recipe", headers = "Accept=image/jpeg, image/jpg, image/png, image/gif")
    public @ResponseBody byte[] getQR(UriComponentsBuilder uriComponentsBuilder, @RequestParam(name = "id") UUID id) throws Exception {
        BufferedImage bufferedImage = QRService.generateQRCodeImage(recipeService.getSharedLink(id, uriComponentsBuilder));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        return imageInByte;
    }
}
