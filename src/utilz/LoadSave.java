package utilz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class LoadSave {

    public static final String PLAYER_ATLAS = "RED_SPRITESHEET.png";
    public static final String PLAYER_ATLAS_2 = "BLUE_SPRITESHEET.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;

        try (InputStream spriteStream = LoadSave.class.getResourceAsStream("/" + fileName)) {
            if (spriteStream == null) {
                throw new IOException("Sprite file not found: " + fileName);
            }
            img = ImageIO.read(spriteStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }
}
