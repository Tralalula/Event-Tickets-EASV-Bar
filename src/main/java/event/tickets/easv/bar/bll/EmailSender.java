package event.tickets.easv.bar.bll;

import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import event.tickets.easv.bar.util.AppConfig;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.*;
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
                .from("EASV Event <EASV@resend.dev>")
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

    public void sendResetCode(String recipient, String name, String code) throws ResendException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("generatedCode", code);
        context.setVariable("cssContent", loadCssFromResource());

        String emailContent = templateEngine().process("resetCode", context);
        sendEmail(recipient, "EASV Password Reset Request", emailContent);
    }

}
