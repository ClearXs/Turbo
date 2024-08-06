package cc.allio.turbo.modules.office.documentserver.util.service;

import cc.allio.turbo.extension.swift.Sequential;
import cc.allio.turbo.extension.swift.Swift;
import cc.allio.turbo.extension.swift.SwiftGenType;
import cc.allio.turbo.modules.office.documentserver.managers.jwt.JwtManager;
import cc.allio.turbo.modules.office.documentserver.models.enums.ConvertErrorType;
import cc.allio.turbo.modules.office.documentserver.util.file.FileUtility;
import cc.allio.turbo.modules.office.documentserver.vo.Convert;
import cc.allio.uno.core.util.JsonUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static cc.allio.turbo.modules.office.documentserver.util.Constants.*;

@Component
public class DefaultServiceConverter implements ServiceConverter {

    @Value("${turbo.office.document.docservice.header}")
    private String documentJwtHeader;

    @Value("${turbo.office.document.docservice.url.site}")
    private String docServiceUrl;

    @Value("${turbo.office.document.docservice.url.converter}")
    private String docServiceUrlConverter;

    @Value("${turbo.office.document.docservice.timeout}")
    private String docserviceTimeout;
    private int convertTimeout;

    private final JwtManager jwtManager;
    private final FileUtility fileUtility;
    private final Swift swift;

    public DefaultServiceConverter(Swift swift, FileUtility fileUtility, JwtManager jwtManager) {
        this.swift = swift;
        this.fileUtility = fileUtility;
        this.jwtManager = jwtManager;
    }

    @PostConstruct
    public void init() {
        int timeout = Integer.parseInt(docserviceTimeout);  // parse the dcoument service timeout value
        convertTimeout = timeout > 0 ? timeout : CONVERT_TIMEOUT_MS;
    }

    @SneakyThrows
    private String postToServer(final Convert body, final String headerToken) {  // send the POST request to the server

        String bodyString = JsonUtils.toJson(body);
        URL url = null;
        java.net.HttpURLConnection connection = null;
        InputStream response = null;
        String jsonString = null;

        byte[] bodyByte = bodyString.getBytes(StandardCharsets.UTF_8);  // convert body string into bytes
        try {
            // set the request parameters
            url = new URL(docServiceUrl + docServiceUrlConverter);
            connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setFixedLengthStreamingMode(bodyByte.length);
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(convertTimeout);

            // check if the token is enabled
            if (jwtManager.tokenEnabled()) {
                // set the JWT header to the request
                connection.setRequestProperty(StringUtils.isBlank(documentJwtHeader)
                        ? "Authorization" : documentJwtHeader, "Bearer " + headerToken);
            }

            connection.connect();

            int statusCode = connection.getResponseCode();
            if (statusCode != HttpStatus.OK.value()) {  // checking status code
                connection.disconnect();
                throw new RuntimeException("Convertation service returned status: " + statusCode);
            }

            try (OutputStream os = connection.getOutputStream()) {
                os.write(bodyByte);  // write bytes to the output stream
                os.flush();  // force write data to the output stream that can be cached in the current thread
            }

            response = connection.getInputStream();  // get the input stream
            jsonString = convertStreamToString(response);  // convert the response stream into a string
        } finally {
            connection.disconnect();
            return jsonString;
        }
    }

    // get the URL to the converted file
    public String getConvertedUri(final String documentUri, final String fromExtension,
                                  final String toExtension, final String documentRevisionId,
                                  final String filePass, final Boolean isAsync, final String lang) {
        // check if the fromExtension parameter is defined; if not, get it from the document url
        String fromExt = fromExtension == null || fromExtension.isEmpty()
                ? fileUtility.getFileExtension(documentUri) : fromExtension;

        // check if the file name parameter is defined; if not, get random uuid for this file
        String title = fileUtility.getFileName(documentUri);
        title = title == null || title.isEmpty() ? UUID.randomUUID().toString() : title;

        String documentRevId = documentRevisionId == null || documentRevisionId.isEmpty()
                ? documentUri : documentRevisionId;

        documentRevId = generateRevisionId(documentRevId);  // create document token

        // write all the necessary parameters to the body object
        Convert body = new Convert();
        body.setLang(lang);
        body.setUrl(documentUri);
        body.setOutputtype(toExtension.replace(".", ""));
        body.setFiletype(fromExt.replace(".", ""));
        body.setTitle(title);
        body.setKey(documentRevId);
        body.setFilePass(filePass);
        if (Boolean.TRUE.equals(isAsync)) {
            body.setAsync(true);
        }

        String headerToken = "";
        if (jwtManager.tokenEnabled()) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("region", lang);
            map.put("url", body.getUrl());
            map.put("outputtype", body.getOutputtype());
            map.put("filetype", body.getFiletype());
            map.put("title", body.getTitle());
            map.put("key", body.getKey());
            map.put("password", body.getFilePass());
            if (Boolean.TRUE.equals(isAsync)) {
                map.put("async", body.getAsync());
            }

            // add token to the body if it is enabled
            String token = jwtManager.createToken(map);
            body.setToken(token);

            Map<String, Object> payloadMap = new HashMap<String, Object>();
            payloadMap.put("payload", map);  // create payload object
            headerToken = jwtManager.createToken(payloadMap);  // create header token
        }

        String jsonString = postToServer(body, headerToken);

        return getResponseUri(jsonString);
    }

    // generate document key
    public String generateRevisionId(final String expectedKey) {
        /* if the expected key length is greater than 20
        then he expected key is hashed and a fixed length value is stored in the string format */
        String formatKey = expectedKey.length() > MAX_KEY_LENGTH
                ? Integer.toString(expectedKey.hashCode()) : expectedKey;

        Sequential sequential = new Sequential(
                swift,
                "document",
                0,
                4,
                formatKey,
                "",
                1,
                SwiftGenType.ALWAYS);
        return sequential.nextNo();
    }

    // todo: Replace with a registry (callbacks package for reference)
    // create an error message for an error code
    private void processConvertServiceResponceError(final int errorCode) {
        String errorMessage = CONVERTATION_ERROR_MESSAGE_TEMPLATE + ConvertErrorType.labelOfCode(errorCode);

        throw new RuntimeException(errorMessage);
    }

    // get the response URL
    @SneakyThrows
    private String getResponseUri(final String jsonString) {
        JsonNode jsonObj = convertStringToJSON(jsonString);

        Long error = jsonObj.get("error").asLong();
        if (error != null) {
            processConvertServiceResponceError(Math.toIntExact(error));  // then get an error message
        }

        // check if the conversion is completed and saves the result to a variable
        boolean isEndConvert = jsonObj.get("endConvert").asBoolean();

        Long resultPercent;
        String responseUri = null;

        if (isEndConvert) {  // if the conversion is completed
            resultPercent = FULL_LOADING_IN_PERCENT;
            responseUri = jsonObj.get("fileUrl").asText();  // get the file URL
        } else {  // if the conversion isn't completed
            resultPercent = jsonObj.get("percent").asLong();

            // get the percentage value of the conversion process
            resultPercent = resultPercent >= FULL_LOADING_IN_PERCENT ? FULL_LOADING_IN_PERCENT - 1 : resultPercent;
        }

        return resultPercent >= FULL_LOADING_IN_PERCENT ? responseUri : "";
    }

    // convert stream to string
    @SneakyThrows
    public String convertStreamToString(final InputStream stream) {
        // create an object to get incoming stream
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        // create a string builder object
        StringBuilder stringBuilder = new StringBuilder();

        // create an object to read incoming streams
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        // get incoming streams by lines
        String line = bufferedReader.readLine();

        while (line != null) {
            stringBuilder.append(line);  // concatenate strings using the string builder
            line = bufferedReader.readLine();
        }

        return stringBuilder.toString();
    }

    // convert string to json
    @SneakyThrows
    public JsonNode convertStringToJSON(final String jsonString) {
        return JsonUtils.readTree(jsonString);
    }
}
