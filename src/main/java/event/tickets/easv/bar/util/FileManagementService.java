package event.tickets.easv.bar.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class FileManagementService {
    public static void copyImageToDir(String imagePath, String destDir, String imageName) throws IOException {
        if (imagePath.startsWith("file:/")) {
            imagePath = imagePath.substring(6);
        }

        Path sourcePath = Path.of(imagePath);
        System.out.println("sourceFile: " + sourcePath.getFileName());

        if (!Files.exists(sourcePath)) throw new FileNotFoundException("Source file not found: " + imagePath);

        Path dirPath = Path.of(destDir);
        if (!Files.exists(dirPath)) Files.createDirectories(dirPath);

        Path outputPath = dirPath.resolve(imageName);

        Files.copy(sourcePath, outputPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Image copied to: " + outputPath);
    }
}
