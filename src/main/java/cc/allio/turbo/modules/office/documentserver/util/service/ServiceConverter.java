package cc.allio.turbo.modules.office.documentserver.util.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.InputStream;

// specify the converter service functions
public interface ServiceConverter {

    String getConvertedUri(String documentUri, String fromExtension,  // get the URL to the converted file
                           String toExtension, String documentRevisionId,
                           String filePass, Boolean isAsync, String lang);

    /**
     * generate document key
     *
     * @param expectedKey  the pre key
     */
    String generateRevisionId(String expectedKey);

    String convertStreamToString(InputStream stream);  // convert stream to string

    JsonNode convertStringToJSON(String jsonString);  // convert string to json
}
