package event.tickets.easv.bar.util;

import event.tickets.easv.bar.util.Result.Failure;
import event.tickets.easv.bar.util.Result.Success;

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
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class FileManagementService {
    public static Result<Boolean> copyImageToDir(String imagePath, String destDir, String imageName) {
        if (imagePath.startsWith("file:/")) {
            imagePath = imagePath.substring(6);
        }

        Path sourcePath = Path.of(imagePath);

        if (!Files.exists(sourcePath)) return Failure.of(FailureType.IO_FAILURE, "FileManagementService.copyImageToDir - couldn't find sorce file : " + imagePath);

        Path dirPath = Path.of(destDir);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                return Failure.of(FailureType.IO_FAILURE, "FileManagementService.copyImageToDir - couldn't create directory", e);
            }
        }

        Path outputPath = dirPath.resolve(imageName);

        try {
            Files.copy(sourcePath, outputPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "FileManagementService.copyImageToDir - couldn't copy file", e);
        }

        return Success.of(true);
    }

    public static Result<Boolean> moveFile(Path currentPath, Path destPath, StandardCopyOption standardCopyOption) {
        if (!Files.exists(destPath)) {
            try {
                Files.createDirectories(destPath);
            } catch (IOException e) {
                return Failure.of(FailureType.IO_FAILURE, "FileManagementService.moveFile - couldn't create directory", e);
            }
        }

        try {
            Files.move(currentPath, destPath, standardCopyOption);
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "FileManagementService.moveFile - couldn't move file", e);
        }

        return Success.of(true);
    }
}
