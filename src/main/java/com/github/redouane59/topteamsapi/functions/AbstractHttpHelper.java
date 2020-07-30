package com.github.redouane59.topteamsapi.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.cloud.functions.HttpRequest;
import java.io.BufferedReader;
import java.io.IOException;
import lombok.Getter;

@Getter
public class AbstractHttpHelper {

  public static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

  public String getBody(HttpRequest request) throws IOException {
    StringBuilder  buffer = new StringBuilder();
    BufferedReader reader = request.getReader();
    String         line;
    while ((line = reader.readLine()) != null) {
      buffer.append(line);
    }
    return buffer.toString();
  }

  public boolean checkJsonCompatibility(String jsonStr, Class<?> valueType) {
    try {
      MAPPER.readValue(jsonStr, valueType);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

}
