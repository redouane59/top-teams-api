package com.github.redouane59.topteamsapi.functions.composition;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class CompositionGeneratorHttpTest {

  @Test
  public void testCompositionGeneratorHttp() throws Exception {
    CompositionGeneratorHttp compositionGeneratorHttp = new CompositionGeneratorHttp();
    HttpRequest  httpRequest  = this.getHttpRequest();
    HttpResponse httpResponse = this.getHttpResponse();
    compositionGeneratorHttp.service(httpRequest, httpResponse);
    assertNotNull(httpResponse.getWriter()); // @todo how to test it properly ?
  }

  private HttpRequest getHttpRequest(){
      return new HttpRequest() {
        @Override
        public String getMethod() {
          return null;
        }

        //   //
        @Override
        public String getUri() {
          return "localhost:8080/";
        }

        @Override
        public String getPath() {
          return null;
        }

        @Override
        public Optional<String> getQuery() {
          return Optional.empty();
        }

        @Override
        public Map<String, List<String>> getQueryParameters() {
          Map<String, List<String>> map = new HashMap<>();
          map.put("split_best_players", List.of("true"));
          map.put("split_worst_players", List.of("true"));
          map.put("split_goal_keepers", List.of("true"));
          map.put("split_defenders", List.of("true"));
          map.put("split_strikers", List.of("true"));
          map.put("expected_team_number", List.of("2"));
          map.put("expected_composition_number", List.of("1"));
          map.put("composition_type", List.of("REGULAR"));
          return map;
        }

        @Override
        public Map<String, HttpPart> getParts() {
          return null;
        }

        @Override
        public Optional<String> getContentType() {
          return Optional.of("application/json");
        }

        @Override
        public long getContentLength() {
          return 0;
        }

        @Override
        public Optional<String> getCharacterEncoding() {
          return Optional.empty();
        }

        @Override
        public InputStream getInputStream() throws IOException {
          return null;
        }

        @Override
        public BufferedReader getReader() throws IOException {
          return new BufferedReader(new StringReader("{\n"
                                                     + "    \"availablePlayers\":[\n"
                                                     + "    {\n"
                                                     + "        \"id\":\"player1\",\n"
                                                     + "        \"rating\":54,\n"
                                                     + "        \"position\":\"GK\"\n"
                                                     + "    },\n"
                                                     + "    {\n"
                                                     + "        \"id\":\"player2\",\n"
                                                     + "        \"rating\":77\n"
                                                     + "    },\n"
                                                     + "    {\n"
                                                     + "        \"id\":\"player3\",\n"
                                                     + "        \"rating\":59,\n"
                                                     + "        \"position\":\"GK\"\n"
                                                     + "    },\n"
                                                     + "    {\n"
                                                     + "        \"id\":\"player4\",\n"
                                                     + "        \"rating\":81\n"
                                                     + "    },\n"
                                                     + "    {\n"
                                                     + "        \"id\":\"player5\",\n"
                                                     + "        \"rating\":52\n"
                                                     + "    },\n"
                                                     + "    {\n"
                                                     + "        \"id\":\"player6\",\n"
                                                     + "        \"rating\":84\n"
                                                     + "    },\n"
                                                     + "    {\n"
                                                     + "        \"id\":\"player7\",\n"
                                                     + "        \"rating\":55\n"
                                                     + "    },\n"
                                                     + "    {\n"
                                                     + "        \"id\":\"player8\",\n"
                                                     + "        \"rating\":45\n"
                                                     + "    },\n"
                                                     + "    {\n"
                                                     + "        \"id\":\"player9\",\n"
                                                     + "        \"rating\":60\n"
                                                     + "    },\n"
                                                     + "    {\n"
                                                     + "        \"id\":\"player10\",\n"
                                                     + "        \"rating\":88\n"
                                                     + "    }\n"
                                                     + "    ]\n"
                                                     + "}"));
        }

        @Override
        public Map<String, List<String>> getHeaders() {
          return null;
        }
      };
  }

  private HttpResponse getHttpResponse(){
    return new HttpResponse() {
      @Override
      public void setStatusCode(final int i) {

      }

      @Override
      public void setStatusCode(final int i, final String s) {

      }

      @Override
      public void setContentType(final String s) {

      }

      @Override
      public Optional<String> getContentType() {
        return Optional.empty();
      }

      @Override
      public void appendHeader(final String s, final String s1) {

      }

      @Override
      public Map<String, List<String>> getHeaders() {
        return null;
      }

      @Override
      public OutputStream getOutputStream() throws IOException {
        return null;
      }

      @Override
      public BufferedWriter getWriter() throws IOException {
        return new BufferedWriter(new StringWriter());
      }
    };
  }

}
