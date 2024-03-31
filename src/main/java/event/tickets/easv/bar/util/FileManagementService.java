package event.tickets.easv.bar.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class FileManagementService {
    public static void downloadImageToDir(String imageUrl, String destDir, String imageName, int width, int height) throws IOException {
        try (InputStream imageStream = URI.create(imageUrl).toURL().openStream()) {
            BufferedImage originalImage = ImageIO.read(imageStream);
            BufferedImage resizedImage = resizeImage(originalImage, width, height);

            File dir = new File(destDir);
            if (!dir.exists()) dir.mkdirs();

            File outputFile = new File(dir, imageName);
            String fileExtension = getFileExtension(imageUrl);
            ImageIO.write(resizedImage, fileExtension, outputFile);
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();

        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();

        return resizedImage;
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "jpg"; // standard extension hvis vi ikke kan finde en
    }
}
