package cc.allio.turbo.modules.office.controller;

import cc.allio.turbo.modules.office.documentserver.callbacks.CallbackHandler;
import cc.allio.turbo.modules.office.documentserver.managers.jwt.JwtManager;
import cc.allio.turbo.modules.office.documentserver.vo.Track;
import cc.allio.uno.core.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/office/callback")
@Tag(name = "回调")
public class CallbackController {

    @Value("${turbo.office.document.docservice.header}")
    private String documentJwtHeader;

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private CallbackHandler callbackHandler;

    @PostMapping(path = "/track/{docId}")
    public String track(HttpServletRequest request,
                        @PathVariable("docId") Long docId,
                        @RequestParam("fileId") Long fileId,
                        @RequestParam("filename") String filename,
                        @RequestParam("filepath") String filepath,
                        @RequestBody final Track body) {
        // callback parameter https://api.onlyoffice.com/editors/callback
        Track track;
        try {
            // write the request body to the object mapper as a string
            String bodyString = JsonUtils.toJson(body);
            // get the request header
            String header = request.getHeader(documentJwtHeader == null
                    || documentJwtHeader.isEmpty() ? "Authorization" : documentJwtHeader);

            // if the request body is empty, an error occurs
            if (bodyString.isEmpty()) {
                throw new RuntimeException("{\"error\":1,\"message\":\"Request payload is empty\"}");
            }
            // parse the request body
            JsonNode bodyCheck = jwtManager.parseBody(bodyString, header);
            // read the request body
            track = JsonUtils.parse(bodyCheck.toString(), Track.class);
        } catch (Exception ex) {
            log.error("Failed to handle callback track", ex);
            return ex.getMessage();
        }

        int error = callbackHandler.handle(docId, fileId, filename, track);

        return "{\"error\":" + error + "}";
    }
}
