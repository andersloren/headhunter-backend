package se.sprinta.headhunterbackend.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.system.exception.TokenDoesNotExistException;

import java.io.File;
import java.io.IOException;

@Component
public class TokenCache {

    @Value("${token.path}")
    private String cacheFilePath;

    private final ObjectMapper objectMapper;

    public TokenCache(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void saveToken(IAuthenticationResult result) throws IOException {
        System.out.println("***WE MADE IT EVEN THIS FAR***");
        objectMapper.writeValue(new File(this.cacheFilePath), result);
    }

    public IAuthenticationResult loadToken() throws IOException {
        File cacheFile = new File(this.cacheFilePath);
        if (cacheFile.exists() && cacheFile.length() > 0) {
            return objectMapper.readValue(cacheFile, IAuthenticationResult.class);
        }
        throw new TokenDoesNotExistException();
    }

    public void clearTokenCache() {
        File cacheFile = new File(this.cacheFilePath);
        if (cacheFile.exists()) {
            cacheFile.delete();
        }
    }
}
