package qrcodeapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

@RestController
public class Controller {

    @GetMapping("/api/health")
    public ResponseEntity<Object> health() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<BufferedImage> qrcode(@RequestParam(required = false, name = "size") Integer size, @RequestParam(required = false, name = "type") String type) {
        List<String> validType = List.of("png", "jpeg", "gif");
        if (size == null || size < 150 || size > 350) {
            throw new RuntimeException("Image size must be between 150 and 350 pixels");
        }

        if (type == null || !validType.contains(type)) {
            throw new RuntimeException("Only png, jpeg and gif image types are supported");
        }
        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);

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
