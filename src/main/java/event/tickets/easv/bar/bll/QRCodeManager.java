package event.tickets.easv.bar.bll;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.resend.services.emails.model.Email;
import event.tickets.easv.bar.be.Ticket.TicketGenerated;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class QRCodeManager {

    private static final int CODE1D_WIDTH = 400;
    private static final int CODE1D_HEIGHT = 100;

    private static final int CODE2D_SIZE = 200;
    private static final String TICKETS_FOLDER = "tickets";

    public QRCodeManager() {

    }

    public static UUID generateUniqueUUID(String args) {
        return UUID.nameUUIDFromBytes(args.getBytes());
    }

    public static BufferedImage generate2DQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, CODE2D_SIZE, CODE2D_SIZE);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public static BufferedImage generate1DCodeImage(String barcodeText) throws WriterException {
        Code128Writer barcodeWriter = new Code128Writer();
        BitMatrix bitMatrix;
        try {
            bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, CODE1D_WIDTH, CODE1D_HEIGHT);
        } catch (WriterException e) {
            System.out.println("Error occurred: \n" + e.getMessage());
            throw e;
        }

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public static BufferedImage combineImagesVertically(BufferedImage image1, BufferedImage image2, String text) {
        int maxWidth = Math.max(image1.getWidth(), image2.getWidth());
        int totalHeight = image1.getHeight() + image2.getHeight() + 20 + 10;

        BufferedImage combinedImage = new BufferedImage(maxWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = combinedImage.createGraphics();

        int image1X = (maxWidth - image1.getWidth()) / 2;
        int image1Y = 10 + 10;

        int image2X = (maxWidth - image2.getWidth()) / 2;
        int image2Y = image1Y + image1.getHeight() + 20; // space mellem billederne

        g2d.drawImage(image1, image1X, image1Y, null);
        g2d.drawImage(image2, image2X, image2Y, null);

        Font font = new Font("Arial", Font.BOLD, 16);
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        int x = (maxWidth - textWidth) / 2;
        int y = image1Y - 5;
        g2d.drawString(text, x, y);

        g2d.dispose();

        return combinedImage;
    }

    public static File saveImageToFile(BufferedImage image, String format, String filePath) {
        File folder = new File(TICKETS_FOLDER);
        if (!folder.exists())
            folder.mkdirs();

        File outputFile = new File(folder, filePath);
        if (!folder.exists()) {
            folder.mkdirs(); // Create the folder if it doesn't exist
        }

        try {
            ImageIO.write(image, format, outputFile);
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }

        return outputFile;
    }

    public static BufferedImage combinedImages(BufferedImage imageOne, BufferedImage imageTwo, String title) {
        return combineImagesVertically(imageOne, imageTwo, title);
    }

    public static File getQrCodeFilePath(UUID uuid, String title) throws Exception {
        String unique = uuid.toString();

        BufferedImage barCode2D = generate2DQRCodeImage(unique);
        BufferedImage barCode1D = generate1DCodeImage(unique);

        BufferedImage combined = combineImagesVertically(barCode2D, barCode1D, title);
        return saveImageToFile(combined, "PNG", unique + ".png");
    }

    public static String getFileFolder() {
        return TICKETS_FOLDER;
    }

    public static void main(String[] args) throws Exception {
        UUID unique = generateUniqueUUID("ticket2");
        File file = getQrCodeFilePath(unique, "Ticket til event");

        System.out.println(file.getAbsolutePath());

        EmailSender emailSender = new EmailSender();
        //emailSender.sendTicket("patrickrefsing@hotmail.dk", "event", "Patrick", file);
    }

}
