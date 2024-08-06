package cc.allio.turbo.modules.office.documentserver.managers.jwt;

import cc.allio.uno.core.util.JsonUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.primeframework.jwt.Signer;
import org.primeframework.jwt.Verifier;
import org.primeframework.jwt.domain.JWT;
import org.primeframework.jwt.hmac.HMACSigner;
import org.primeframework.jwt.hmac.HMACVerifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultJwtManager implements JwtManager {

    @Value("${turbo.office.document.docservice.secret}")
    private String tokenSecret;

    // create document token
    public String createToken(final Map<String, Object> payloadClaims) {
        try {
            // build a HMAC signer using a SHA-256 hash
            Signer signer = HMACSigner.newSHA256Signer(tokenSecret);
            JWT jwt = new JWT();
            for (String key : payloadClaims.keySet()) {  // run through all the keys from the payload
                jwt.addClaim(key, payloadClaims.get(key));  // and write each claim to the jwt
            }
            return JWT.getEncoder().encode(jwt, signer);  // sign and encode the JWT to a JSON string representation
        } catch (Exception e) {
            return "";
        }
    }

    // check if the token is enabled
    public boolean tokenEnabled() {
        return tokenSecret != null && !tokenSecret.isEmpty();
    }

    // read document token
    public JWT readToken(final String token) {
        try {
            // build a HMAC verifier using the token secret
            Verifier verifier = HMACVerifier.newVerifier(tokenSecret);

            // verify and decode the encoded string JWT to a rich object
            return JWT.getDecoder().decode(token, verifier);
        } catch (Exception exception) {
            return null;
        }
    }

    // parse the body
    public JsonNode parseBody(final String payload, final String header) {
        JsonNode body = JsonUtils.readTree(payload);
        if (tokenEnabled()) {  // check if the token is enabled
            String token = body.get("token").asText();  // get token from the body
            if (token == null) {  // if token is empty
                if (header != null && StringUtils.isNotEmpty(header)) {  // and the header is defined

                    // get token from the header (it is placed after the Bearer prefix if it exists)
                    token = header.startsWith("Bearer ") ? header.substring("Bearer ".length()) : header;
                }
            }
            if (token == null || StringUtils.isEmpty(token)) {
                throw new RuntimeException("{\"error\":1,\"message\":\"JWT expected\"}");
            }

            JWT jwt = readToken(token);  // read token
            if (jwt == null) {
                throw new RuntimeException("{\"error\":1,\"message\":\"JWT validation failed\"}");
            }
            if (jwt.getObject("payload") != null) {  // get payload from the token and check if it is not empty
                try {
                    @SuppressWarnings("unchecked") LinkedHashMap<String, Object> jwtPayload =
                            (LinkedHashMap<String, Object>) jwt.getObject("payload");

                    jwt.claims = jwtPayload;
                } catch (Exception ex) {
                    throw new RuntimeException("{\"error\":1,\"message\":\"Wrong payload\"}");
                }
            }
            try {

                return JsonUtils.readTree(JsonUtils.toJson(jwt.claims));
            } catch (Exception ex) {
                throw new RuntimeException("{\"error\":1,\"message\":\"Parsing error\"}");
            }
        }

        return body;
    }
}
