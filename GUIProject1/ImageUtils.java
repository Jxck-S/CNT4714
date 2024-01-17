import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.Base64;

public class ImageUtils {
    public static ImageIcon createImageIconFromBase64(String base64Icon) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Icon);
            Image image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            return new ImageIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
