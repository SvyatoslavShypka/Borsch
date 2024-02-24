package eu.dar3.borsch.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

import static eu.dar3.borsch.utils.Constants.QR_HEIGHT;
import static eu.dar3.borsch.utils.Constants.QR_WIDTH;

@Data
@Service
@RequiredArgsConstructor
public class QRService {

    public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
