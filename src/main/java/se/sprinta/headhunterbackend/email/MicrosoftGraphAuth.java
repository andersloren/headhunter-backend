package se.sprinta.headhunterbackend.email;

import com.microsoft.aad.msal4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.system.exception.TokenDoesNotExistException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
public class MicrosoftGraphAuth {
    private final Set<String> SCOPE = Collections.singleton("https://graph.microsoft.com/.default");

    @Value("${server.ssl.certificate.path}")
    private String PKCS12Certificate;
    @Value("${server.ssl.certificate.password}")
    private String PKCS12Password;
    @Value("${azure.client-id}")
    private String clientId;
    @Value("${azure.tenant-id}")
    private String tenantId;
    @Value("${email.service.service-address}")
    private String serviceAddress;

    private final TokenCache tokenCache;

    public MicrosoftGraphAuth(TokenCache tokenCache) {
        this.tokenCache = tokenCache;
    }

    public String getAccessToken() throws IOException {
        try {
            IAuthenticationResult cachedResult = this.tokenCache.loadToken();
            return cachedResult.accessToken();
        } catch (TokenDoesNotExistException e) {

            IClientCredential credential;
            try (InputStream pkcs12Certificate = new FileInputStream(this.PKCS12Certificate)) {
                credential = ClientCredentialFactory.createFromCertificate(pkcs12Certificate, this.PKCS12Password);

                ConfidentialClientApplication cca = ConfidentialClientApplication
                        .builder(this.clientId, credential)
                        .authority("https://login.microsoftonline.com/" + this.tenantId)
                        .build();

                ClientCredentialParameters parameters = ClientCredentialParameters
                        .builder(SCOPE)
                        .build();

                IAuthenticationResult result;
                CompletableFuture<IAuthenticationResult> future = cca.acquireToken(parameters);

                result = future.join();

//                this.tokenCache.saveToken(result);
                return result.accessToken();
            } catch (MsalException msalException) {
                throw new RuntimeException("Unable to acquire token", msalException);
            } catch (UnrecoverableKeyException | CertificateException | NoSuchAlgorithmException | KeyStoreException |
                     NoSuchProviderException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void sendEmail(String email) throws IOException, URISyntaxException {

        URL url = new URI("https://graph.microsoft.com/v1.0/users/" + this.serviceAddress + "/sendMail").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String emailPayload = "{"
                + "\"message\": {"
                + "  \"subject\": \"Test Email from Java App\","
                + "  \"body\": {"
                + "    \"contentType\": \"Text\","
                + "    \"content\": \"Hello, this is a test email sent from Java using Microsoft Graph API.\""
                + "  },"
                + "  \"toRecipients\": ["
                + "    {"
                + "      \"emailAddress\": {"
                + "        \"address\": \"" + email + "\""
                + "      }"
                + "    }"
                + "  ]"
                + "},"
                + "\"saveToSentItems\": \"true\""
                + "}";

        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = emailPayload.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_ACCEPTED) {
            System.out.println("Email sent successfully");
        } else {
            BufferedReader errorStream = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder errorResponse = new StringBuilder();

            while ((inputLine = errorStream.readLine()) != null) {
                errorResponse.append(inputLine);
            }
            errorStream.close();

            System.out.println("Failed to send email. Response Code: " + responseCode);
            System.out.println("Error Response: " + errorResponse);
        }
        connection.disconnect();
    }
}
