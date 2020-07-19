package org.redouane59.topteamsapi.functions.compositionGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import org.redouane59.topteamsapi.functions.AbstractHttpHelper;
import org.redouane59.topteamsapi.model.Player;
import org.redouane59.topteamsapi.model.composition.CompositionType;

public class CompositionGeneratorHttp extends AbstractHttpHelper implements HttpFunction {

  private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  // curl -v "localhost:8080/?split_best_players=true&split_worst_players=true&split_goal_keepers=true&split_defenders=true&split_strikers=true&expected_team_number=2&expected_composition_number=1&composition_type=REGULAR" -H "Content-Type: application/json" -d "[{\"id\":\"player1\",\"rating_value\":54,\"position\":\"GK\"},{\"id\":\"player2\",\"rating_value\":77},{\"id\":\"player3\",\"rating_value\":59,\"position\":\"GK\"},{\"id\":\"player4\",\"rating_value\":81},{\"id\":\"player5\",\"rating_value\":52},{\"id\":\"player6\",\"rating_value\":84},{\"id\":\"player7\",\"rating_value\":55},{\"id\":\"player8\",\"rating_value\":45},{\"id\":\"player9\",\"rating_value\":60},{\"id\":\"player10\",\"rating_value\":88}]"
  @Override
  public void service(final HttpRequest request, final HttpResponse response) throws Exception {
    String contentType = request.getContentType().orElse("");
    if(contentType.equals("application/json")){
      Player[] players = null;
      String body = this.getBody(request);
      if (!body.isEmpty()) {
        players = OBJECT_MAPPER.readValue(body, Player[].class);
      }

      Optional<String> split_best_players = request.getFirstQueryParameter("split_best_players");
      Optional<String> split_worst_players = request.getFirstQueryParameter("split_best_players");
      Optional<String> split_goal_keepers = request.getFirstQueryParameter("split_goal_keepers");
      Optional<String> split_defenders = request.getFirstQueryParameter("split_defenders");
      Optional<String> split_strikers = request.getFirstQueryParameter("split_strikers");
      Optional<String> expected_team_number = request.getFirstQueryParameter("expected_team_number");
      Optional<String> expected_composition_number = request.getFirstQueryParameter("expected_composition_number");
      Optional<String> composition_type = request.getFirstQueryParameter("composition_type");

      GeneratorConfiguration config = GeneratorConfiguration.builder()
                                                     .splitBestPlayers(Boolean.parseBoolean(split_best_players.orElse("true")))
                                                     .splitWorstPlayers(Boolean.parseBoolean(split_worst_players.orElse("true")))
                                                     .splitGoalKeepers(Boolean.parseBoolean(split_goal_keepers.orElse("true")))
                                                     .splitDefenders(Boolean.parseBoolean(split_defenders.orElse("true")))
                                                     .splitStrikers(Boolean.parseBoolean(split_strikers.orElse("true")))
                                                     .nbTeamsNeeded(Integer.parseInt(expected_team_number.orElse("2")))
                                                     .nbCompositionsNeeded(Integer.parseInt(expected_composition_number.orElse("1")))
                                                     .compositionType(CompositionType.valueOf(composition_type.orElse("REGULAR")))
                                                     .build();
      ICompositionGenerator generator;
      if(config.getNbTeamsNeeded()==2) {
        generator = new CompositionGenerator(config);
      } else{
        generator = new ComplexCompositionGenerator(config);
      }

      for(Player p : players){
        response.getWriter().write("\n"+p.getId());
      }
    }
  }

}
