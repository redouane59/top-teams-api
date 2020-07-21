package com.github.redouane59.topteamsapi.functions;

import com.google.cloud.functions.HttpRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class AbstractHttpHelper {
  public String getBody(HttpRequest request) throws IOException {
    StringBuilder  buffer = new StringBuilder();
    BufferedReader reader = request.getReader();
    String         line;
    while ((line = reader.readLine()) != null) {
      buffer.append(line);
    }
    return buffer.toString();
  }
}
