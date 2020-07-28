package com.github.redouane59.topteamsapi.functions.evaluation;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.redouane59.topteamsapi.functions.composition.CompositionGeneratorHttp;
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

class RatingUpdatesCalculatorHttpTest {

  @Test
  void testCompositionGeneratorHttp() throws Exception {
    RatingUpdateCalculatorHttp ratingUpdateCalculatorHttp = new RatingUpdateCalculatorHttp();
    HttpRequest              httpRequest              = this.getHttpRequest();
    HttpResponse httpResponse = this.getHttpResponse();
    ratingUpdateCalculatorHttp.service(httpRequest, httpResponse);
    assertNotNull(httpResponse.getWriter()); // @todo how to test it properly ?
  }

  private HttpRequest getHttpRequest(){
      return new HttpRequest() {
        @Override
        public String getMethod() {
          return null;
        }

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
          map.put("score_A", List.of("10"));
          map.put("score_B", List.of("6"));
          map.put("split_points_by_team", List.of("true"));
          map.put("relative_distribution", List.of("MEDIUM"));
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
                                                     + "   \"team_A\":{\n"
                                                     + "      \"players\":[\n"
                                                     + "         {\n"
                                                     + "            \"id\":\"player1\",\n"
                                                     + "            \"rating\":54,\n"
                                                     + "            \"nb_games_played\":31\n"
                                                     + "         },\n"
                                                     + "         {\n"
                                                     + "            \"id\":\"player2\",\n"
                                                     + "            \"rating\":77,\n"
                                                     + "            \"nb_games_played\":80\n"
                                                     + "         },\n"
                                                     + "         {\n"
                                                     + "            \"id\":\"player3\",\n"
                                                     + "            \"rating\":59,\n"
                                                     + "            \"nb_games_played\":14\n"
                                                     + "         },\n"
                                                     + "         {\n"
                                                     + "            \"id\":\"player4\",\n"
                                                     + "            \"rating\":81,\n"
                                                     + "            \"nb_games_played\":27\n"
                                                     + "         },\n"
                                                     + "         {\n"
                                                     + "            \"id\":\"player5\",\n"
                                                     + "            \"rating\":52,\n"
                                                     + "            \"nb_games_played\":9\n"
                                                     + "         }\n"
                                                     + "      ]\n"
                                                     + "   },\n"
                                                     + "   \"team_B\":{\n"
                                                     + "      \"players\":[\n"
                                                     + "         {\n"
                                                     + "            \"id\":\"player6\",\n"
                                                     + "            \"rating\":84,\n"
                                                     + "            \"nb_games_played\":102\n"
                                                     + "         },\n"
                                                     + "         {\n"
                                                     + "            \"id\":\"player7\",\n"
                                                     + "            \"rating\":55,\n"
                                                     + "            \"nb_games_played\":42\n"
                                                     + "         },\n"
                                                     + "         {\n"
                                                     + "            \"id\":\"player8\",\n"
                                                     + "            \"rating\":45,\n"
                                                     + "            \"nb_games_played\":77\n"
                                                     + "         },\n"
                                                     + "         {\n"
                                                     + "            \"id\":\"player9\",\n"
                                                     + "            \"rating\":60,\n"
                                                     + "            \"nb_games_played\":12\n"
                                                     + "         },\n"
                                                     + "         {\n"
                                                     + "            \"id\":\"player10\",\n"
                                                     + "            \"rating\":88,\n"
                                                     + "            \"nb_games_played\":92\n"
                                                     + "         }\n"
                                                     + "      ]\n"
                                                     + "   }\n"
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
