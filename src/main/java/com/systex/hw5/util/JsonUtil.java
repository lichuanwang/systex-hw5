package com.systex.hw5.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonUtil {

    public static Map<String, Object> parseJsonRequest(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = request.getReader().lines().collect(Collectors.joining());
        return objectMapper.readValue(jsonBody, Map.class);
    }
}
