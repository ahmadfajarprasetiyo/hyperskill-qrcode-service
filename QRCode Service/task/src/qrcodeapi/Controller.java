package qrcodeapi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @GetMapping("/api/health")
    public ResponseEntity<Object> health() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<BufferedImage> qrcode(@RequestParam(required = false, defaultValue = "250", name = "size") Integer size,
                                                @RequestParam(required = false, defaultValue = "png", name = "type") String type,
                                                @RequestParam(required = false, name = "contents") String contents,
                                                @RequestParam(required = false, defaultValue = "L", name = "correction") String correction) {


        if (contents == null || contents.trim().isEmpty()) {
            throw new RuntimeException("Contents cannot be null or blank");
        }

        if (size == null || size < 150 || size > 350) {
            throw new RuntimeException("Image size must be between 150 and 350 pixels");
        }

        List<String> validCorrection = List.of("L", "M", "Q", "H");
        HashMap<String, ErrorCorrectionLevel> mapCorrection = new HashMap<>();
        mapCorrection.put("L", ErrorCorrectionLevel.L);
        mapCorrection.put("M", ErrorCorrectionLevel.M);
        mapCorrection.put("Q", ErrorCorrectionLevel.Q);
        mapCorrection.put("H", ErrorCorrectionLevel.H);
        if (correction == null || !validCorrection.contains(correction)) {
            throw new RuntimeException("Permitted error correction levels are L, M, Q, H");
        }

        List<String> validType = List.of("png", "jpeg", "gif");
        if (type == null || !validType.contains(type)) {
            throw new RuntimeException("Only png, jpeg and gif image types are supported");
        }

        BufferedImage bufferedImage;

        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, mapCorrection.get(correction));
        try {
            BitMatrix bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, size, size, hints);
            bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            throw new RuntimeException("Contents cannot be null or blank");
        }

        MediaType mediaType = MediaType.IMAGE_PNG;
        if (type.equals("jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if (type.equals("gif")) {
            mediaType = MediaType.IMAGE_GIF;
        }
        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(bufferedImage);
    }


}
