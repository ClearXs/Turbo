package cc.allio.uno.turbo.auth.userdetails;

import cc.allio.uno.turbo.modules.auth.provider.TurboUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TurboUserTest {

    @Autowired
    private JwtDecoder jwtDecoder;

    String token = "eyJraWQiOiJ0dXJibyBqd3QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0b2tlbiIsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6Ly9hbGxpby5jYyIsInVzZXJJZCI6MTE2NjAxMDcyMTM5MDAzNDk0NCwiYXV0aG9yaXRpZXMiOltdLCJlbmFibGVkIjp0cnVlLCJwYXNzd29yZCI6ImZlRVNvblJjSXNuamdYYUZ4QlY0QUE9PSIsImFjY291bnROb25FeHBpcmVkIjp0cnVlLCJleHAiOjE2OTk1MTM3MjAsImlhdCI6MTY5OTQyNzMyMCwianRpIjoiMTA0MzJiZjE1OGM4MDAwMCIsImFjY291bnROb25Mb2NrZWQiOnRydWUsInVzZXJuYW1lIjoiYWRtaW4ifQ.iCkBb6GbELUu-Vv49JDC-PzgPhTG-w17KpOCi_FtIU2QszdPZ9K318-svMDAR6E5tCyQ5pMppBUx0_NDATA86U8brP7S1r2dI2dKFJfA0RnyLA4WegpB15m4V7YeqGMEzvXjpKgV6QuHpANt9L7ehook4Mc9lZwtVYrqg-qcuwrMv8KvjmqDmsoevWnWAvN2RlqcTE9D8qYCzqH81QXwi_pubSYbcOB1Qk_-xcdqG4DnznQcbcUoCUkmz-X5pxd7BWLKXWuYn9a1p9s2vCpCIT4wx_UQXqNZW_RUXXXfq8Wv1be51AqvwTjsrhMzp0onCCe2BHNI1efa0-j0Tjx03Q";

    @Test
    void testTurboUserByJwtCreate() {
        Jwt jwt = jwtDecoder.decode(token);
        TurboUser turboUser = new TurboUser(jwt);
        assertThat(turboUser).isNotNull();

        assertThat(turboUser.getUsername()).isEqualTo("admin");

    }
}
