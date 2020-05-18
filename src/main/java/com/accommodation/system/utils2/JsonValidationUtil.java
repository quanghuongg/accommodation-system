package com.accommodation.system.utils2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import viettel.vtcc.common.model.ValidationResult;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * User: nguyentrung
 * Date: 11/7/19
 * Time: 10:52 AM
 * To change this template use File | Settings | File and Code Templates.
 */
public class JsonValidationUtil {
    public static final String JSON_V4_SCHEMA_IDENTIFIER = "http://json-schema.org/draft-04/schema#";
    public static final String JSON_SCHEMA_IDENTIFIER_ELEMENT = "$schema";

    public static JsonNode getJsonNode(String jsonText) throws IOException {
        return JsonLoader.fromString(jsonText);
    }

    public static JsonNode getJsonNode(File jsonFile) throws IOException {
        return JsonLoader.fromFile(jsonFile);
    }

    private static JsonSchema _getSchemaNode(JsonNode jsonNode) throws ProcessingException {
        final JsonNode schemaIdentifier = jsonNode.get(JSON_SCHEMA_IDENTIFIER_ELEMENT);
        if (null == schemaIdentifier) {
            ((ObjectNode) jsonNode).put(JSON_SCHEMA_IDENTIFIER_ELEMENT, JSON_V4_SCHEMA_IDENTIFIER);
        }

        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        return factory.getJsonSchema(jsonNode);
    }

    public static JsonSchema getJsonSchema(File jsonFile) throws ProcessingException, IOException {
        JsonNode jsonNode = getJsonNode(jsonFile);
        return _getSchemaNode(jsonNode);
    }

    public static ValidationResult isJsonValid(File schemaFile, String jsonText) throws ProcessingException, IOException {
        JsonSchema jsonSchemaNode = getJsonSchema(schemaFile);
        JsonNode jsonNode = getJsonNode(jsonText);
        return isJsonValid(jsonSchemaNode, jsonNode);
    }

    public static ValidationResult isJsonValid(JsonSchema jsonSchemaNode, JsonNode jsonNode)
            throws ProcessingException {

        ProcessingReport report = jsonSchemaNode.validate(jsonNode);

        String error = "";
        if (!report.isSuccess()) {
            Iterator<ProcessingMessage> iterator = report.iterator();
            while (iterator.hasNext()) {
                try {
                    ProcessingMessage next = iterator.next();
                    switch (next.getLogLevel()) {
                        case WARNING:
                            System.out.println(next.asJson().get("message").asText());
                            break;
                        case FATAL:
                        case ERROR:
                            String text = next.asJson().get("instance").get("pointer").asText();
                            if (text.length() > 1) {

                                error += "property [" + text.substring(1) + "] " + next.getMessage();
                            } else {
                                error += next.asJson().get("message").asText();
                            }
                            error += "\n";
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        ValidationResult result = new ValidationResult();
        result.setValid(report.isSuccess());
        result.setError(error);
        return result;
    }
}
