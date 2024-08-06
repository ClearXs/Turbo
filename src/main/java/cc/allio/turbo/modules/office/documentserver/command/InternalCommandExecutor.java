package cc.allio.turbo.modules.office.documentserver.command;

import cc.allio.turbo.modules.office.documentserver.managers.jwt.JwtManager;
import cc.allio.turbo.modules.office.documentserver.util.service.ServiceConverter;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class InternalCommandExecutor {

    private final String docserviceUrlSite;
    private final String docserviceUrlCommand;
    private final String documentJwtHeader;
    private final JwtManager jwtManager;
    private final ServiceConverter serviceConverter;

    /**
     * execute command request
     *
     * @param method the command method
     * @param docKey  the doc key
     * @param params the params
     */
    @SneakyThrows
    public Result doExecute(Method method, String docKey, Map<String, Object> params) {
        String documentCommandUrl = docserviceUrlSite + docserviceUrlCommand;
        Command.Request request = new Command.Request();
        request.setC(method.getName());
        request.setKey(docKey);
        // set params
        if (CollectionUtils.isNotEmpty(params)) {
            request.setParams(params);
        }
        Map<String, Object> requestParams = request.returnMap();

        URL url = new URL(documentCommandUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        String headerToken;
        if (jwtManager.tokenEnabled()) {  // check if a secret key to generate token exists or not
            Map<String, Object> payloadMap = request.returnMap();
            payloadMap.put("payload", requestParams);
            headerToken = jwtManager.createToken(payloadMap);  // encode a payload object into a header token

            // add a header Authorization with a header token and Authorization prefix in it
            connection.setRequestProperty(documentJwtHeader.isEmpty() ? "Authorization" : documentJwtHeader, "Bearer " + headerToken);

            // encode a payload object into a body token
            String token = jwtManager.createToken(requestParams);
            request.setToken(token);
        }

        String paramJson = request.returnJson();

        if (log.isInfoEnabled()) {
            log.info("Request Onlyoffice command url {}, the doc key = {} the command = '{}' \\n request params is {}", documentCommandUrl, docKey, method, paramJson);
        }

        byte[] bodyByte = paramJson.getBytes(StandardCharsets.UTF_8);

        connection.setRequestMethod("POST");  // set the request method
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");  // set the Content-Type header
        connection.setDoOutput(true);  // set the doOutput field to true
        connection.connect();

        try (OutputStream os = connection.getOutputStream()) {
            os.write(bodyByte);  // write bytes to the output stream
        }

        InputStream stream = connection.getInputStream();  // get input stream

        if (stream == null) {
            throw new RuntimeException("Could not get an answer");
        }

        String jsonString = serviceConverter.convertStreamToString(stream);  // convert stream to json string
        connection.disconnect();

        // convert json string to json object
        JsonNode response = serviceConverter.convertStringToJSON(jsonString);

        if (log.isInfoEnabled()) {
            log.info("Response the doc key = {} the command = '{}' \\n response {}", docKey, method, response.toString());
        }

        return formatResult(response);
    }

    /**
     * parse result
     *
     * @param response the response
     * @return
     */
    Result formatResult(JsonNode response) {
        // convert json string to json object
        String responseCode = response.get("error").toString();
        ResultCode resultCode = switch (responseCode) {
            case "1" -> ResultCode.notFoundDocKey;
            case "2" -> ResultCode.callbackUrlCorrect;
            case "3" -> ResultCode.intervalServerError;
            case "4" -> ResultCode.noChange;
            case "5" -> ResultCode.commandCorrect;
            case "6" -> ResultCode.invalidToken;
            default -> ResultCode.noError;
        };

        String msg = response.toString();
        // set result model
        Result result = new Result();
        result.setCode(resultCode);
        result.setMsg(msg);
        return result;
    }

}