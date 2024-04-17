package event.tickets.easv.bar.bll;

import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import event.tickets.easv.bar.util.AppConfig;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

public class EmailSender {

    private Resend resend;
    public EmailSender() throws IOException {
        this(AppConfig.CONFIG_FILE);
    }
    public EmailSender(String propertiesFilePath) throws IOException {
        var properties = new Properties();
        properties.load(new FileInputStream(propertiesFilePath));

        resend = new Resend(properties.getProperty(AppConfig.RESEND_API));
    }

    private String loadCssFromResource() {
        StringBuilder contentBuilder = new StringBuilder();
        try (InputStream inputStream = this.getClass().getResourceAsStream("/css/email.css");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null)
                contentBuilder.append(line).append("\n");

        } catch (IOException e) {
            throw new RuntimeException("Error trying to read /css/email.css file\n " + e);
        }

        return contentBuilder.toString();
    }

    private TemplateEngine templateEngine() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("html/");
        templateResolver.setSuffix(".html");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;
    }

    private boolean sendEmail(String recipient, String subject, String content) throws ResendException {
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("EASV Event <EASV@leet.dk>")
                .to(recipient)
                .subject(subject)
                .html(content)
                .build();
        try {
            SendEmailResponse data = resend.emails().send(sendEmailRequest);
            return true;
        } catch (ResendException e) {
            throw new ResendException("Error occurred trying to send email:\n" + e);
        }
    }

    private boolean sendEmailWithAttachment(String recipient, String subject, String content, Attachment attachment) throws ResendException {
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("EASV Event <EASV@leet.dk>")
                .to(recipient)
                .subject(subject)
                .addAttachment(attachment)
                .html(content)
                .build();
        try {
            SendEmailResponse data = resend.emails().send(sendEmailRequest);
            return true;
        } catch (ResendException e) {
            throw new ResendException("Error occurred trying to send email:\n" + e);
        }
    }

    private boolean sendEmailWithAttachments(String recipient, String subject, String content, List<File> files) throws ResendException {
        List<Attachment> attachments = new ArrayList<>();
        for (File file : files) {
            try {
                attachments.add(addImageAttachment(file));
            } catch (IOException e) {
                // Handle IOException if needed
                e.printStackTrace();
            }
        }

        SendEmailRequest.Builder emailRequestBuilder = SendEmailRequest.builder()
                .from("EASV Event <EASV@leet.dk>")
                .to(recipient)
                .subject(subject)
                .html(content);

        for (Attachment attachment : attachments) {
            emailRequestBuilder.addAttachment(attachment);
        }

        SendEmailRequest sendEmailRequest = emailRequestBuilder.build();

        try {
            SendEmailResponse data = resend.emails().send(sendEmailRequest);
            return true;
        } catch (ResendException e) {
            throw new ResendException("Error occurred trying to send email:\n" + e);
        }
    }


    public void sendResetCode(String recipient, String name, String code) throws ResendException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("generatedCode", code);
        context.setVariable("cssContent", loadCssFromResource());

        String emailContent = templateEngine().process("resetCode", context);
        sendEmail(recipient, "EASV Password Reset Request", emailContent);
    }

    public Attachment addImageAttachment(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        String encodedContent = Base64.getEncoder().encodeToString(fileContent);
        return Attachment.builder()
                .fileName(file.getName())
                .content(encodedContent)
                .build();
    }

    public void sendMultipleTickets(String recipient, String eventName, String ticketName, String type, List<File> ticketFiles) throws ResendException {
        Context context = new Context();
        context.setVariable("event", eventName);
        context.setVariable("ticketName", ticketName);

        Boolean isEvent = type.equals("Paid");

        if (!isEvent) {
            context.setVariable("ticketValidation", eventName.equals("Unassigned") ? "Can be used for all events." : "For event: " + eventName);
            context.setVariable("ticketName", ticketName);
        } else {
            context.setVariable("event", eventName);
            context.setVariable("ticketName", ticketName);
        }

        String emailContent = templateEngine().process(isEvent ? "ticketDetails" : "ticketPromotional", context);
        sendEmailWithAttachments(recipient, "Ticket for event: " + eventName, emailContent, ticketFiles);
    }

    public void sendTicket(String recipient, String eventName, String name, File ticketFile, String type) throws Exception {
        Context context = new Context();
        Boolean isEvent = type.equals("Paid");

        if (!isEvent) {
            context.setVariable("ticketValidation", eventName.equals("Unassigned") ? "Can be used for all events." : "For event: " + eventName);
            context.setVariable("ticketName", name);
        } else {
            context.setVariable("name", eventName);
            context.setVariable("ticketName", name);
        }

        String emailContent = templateEngine().process(isEvent ? "ticketDetails" : "ticketPromotional", context);
        sendEmailWithAttachment(recipient, "Ticket(s) for event: " + eventName, emailContent, addImageAttachment(ticketFile));
    }

    public void sendPassword(String recipient, String name, String username, String temporaryPassword) throws ResendException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("username", username);
        context.setVariable("temporaryPassword", temporaryPassword);

        String emailContent = templateEngine().process("signupDetails", context);
        sendEmail(recipient, "EASV Password Reset Request", emailContent);
    }

    public static void main(String[] args) throws IOException, ResendException {
        var mail = new EmailSender();
    }
}
