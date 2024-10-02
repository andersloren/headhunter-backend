package se.sprinta.headhunterbackend.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Messages {

    private final ObjectMapper objectMapper;

    public Messages(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String emailTemplate(String email) throws JsonProcessingException {

        Map<String, Object> emailPayload = new HashMap<>();

        Map<String, Object> message = new HashMap<>();
        // TODO: 10/2/2024 Change this to verification email at some point...
        message.put("subject", "Headhunter confirmation email");

        Map<String, Object> body = new HashMap<>();
        body.put("contentType", "Text");
        // TODO: 10/2/2024 Externalize the message at some point?
        body.put("content", "Welcome to Headhunter! \n\n We hope you'll enjoy our services.");
        message.put("body", body);

        Map<String, Object> emailAddress = new HashMap<>();
        emailAddress.put("address", email);

        Map<String, Object> recipient = new HashMap<>();
        recipient.put("emailAddress", emailAddress);

        message.put("toRecipients", new Object[]{recipient});

        emailPayload.put("message", message);
        emailPayload.put("saveToSentItems", "true");

        try {
            return this.objectMapper.writeValueAsString(emailPayload);
        } catch (JsonProcessingException e) {
            System.out.println("Transform JSON email to string failed");
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
