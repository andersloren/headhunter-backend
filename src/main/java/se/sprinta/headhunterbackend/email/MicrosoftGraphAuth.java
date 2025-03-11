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
    private final Messages messages;

    public MicrosoftGraphAuth(TokenCache tokenCache, Messages messages) {
        this.tokenCache = tokenCache;
        this.messages = messages;
    }

    private HttpURLConnection getHttpURLConnection() throws URISyntaxException, IOException {
        URL url = new URI("https://graph.microsoft.com/v1.0/users/" + this.serviceAddress + "/sendMail").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        String accessToken;

        try {
            accessToken = getAccessToken();
        } catch (IOException e) {
            System.out.println("Failed to get the access token: " + e.getMessage());
            throw e;
        }

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        return connection;
    }

    public void sendVerificationEmail(String email, String verificationCode) throws IOException, URISyntaxException {

        System.out.println("1. Entered microsoftGraph.sendVerificationEmail");
        HttpURLConnection connection = getHttpURLConnection();
        System.out.println("2. " + connection);

        String emailPayload = this.messages.verificationEmail(email, verificationCode);

        System.out.println("DID WE GET AN EMAIL PAYLOAD?");

        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = emailPayload.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }

        System.out.println("DID WE GE AN OUTPUT STREAM?");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_ACCEPTED) {
            System.out.println("Email sent successfully");
        } else {
            System.out.println("DID WE GE END UP HERE?????????");
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

    public void sendConfirmationEmail(String email) throws IOException, URISyntaxException {

        HttpURLConnection connection = getHttpURLConnection();

        String emailPayload = this.messages.confirmationEmail(email);

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

    public String getAccessToken() throws IOException {
        try {
            System.out.println("1. getAccessToken <-- THIS");
            IAuthenticationResult cachedResult = this.tokenCache.loadToken();
            return cachedResult.accessToken();
        } catch (TokenDoesNotExistException e) {
            System.out.println("3. getAccessToken");
            IClientCredential credential;
            System.out.println("4. getAccessToken <-- THIS");
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
}
