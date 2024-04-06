package event.tickets.easv.bar.gui.util;

import event.tickets.easv.bar.util.AppConfig;
import javafx.scene.image.Image;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {
    private static final Map<String, Image> cache = new HashMap<>();
    private static final Map<String, Image> profileCache = new HashMap<>();

    public static Image getImage(String imagePath) {
        Image img = cache.get(imagePath);
        if (img == null) {
            String absolutePath = "file:///" + Paths.get("").toAbsolutePath() + AppConfig.EVENT_IMAGES_DIR + imagePath.replace("\\", "/");
            img = new Image(absolutePath, true);
            cache.put(imagePath, img);
        }

        return img;
    }

    public static Image getProfileImage(String imagePath) {
        Image img = profileCache.get(imagePath);
        if (img == null) {
            String absolutePath = "file:///" + Paths.get("").toAbsolutePath() + AppConfig.PROFILE_IMAGES_DIR + imagePath.replace("\\", "/");
            img = new Image(absolutePath, true);
            profileCache.put(imagePath, img);
        }

        return img;
    }
}
