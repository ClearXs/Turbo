package cc.allio.turbo.modules.office.documentserver.managers.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import org.primeframework.jwt.domain.JWT;

import java.util.Map;

// specify the jwt manager functions
public interface JwtManager {
    boolean tokenEnabled();  // check if the token is enabled

    String createToken(Map<String, Object> payloadClaims);  // create document token

    JWT readToken(String token);  // read document token

    JsonNode parseBody(String payload, String header);  // parse the body
}
