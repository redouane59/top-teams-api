package com.github.redouane59.topteamsapi.functions.composition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.github.redouane59.topteamsapi.functions.AbstractHttpHelper;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.composition.CompositionType;

public class CompositionGeneratorHttp extends AbstractHttpHelper implements HttpFunction {

  private static final ObjectMapper MAPPER = new ObjectMapper();
  // curl -v "localhost:8080/?split_best_players=true&split_worst_players=true&split_goal_keepers=true&split_defenders=true&split_strikers=true&expected_team_number=2&expected_composition_number=1&composition_type=REGULAR" -H "Content-Type: application/json" -d "[{\"id\":\"player1\",\"rating_value\":54,\"position\":\"GK\"},{\"id\":\"player2\",\"rating_value\":77},{\"id\":\"player3\",\"rating_value\":59,\"position\":\"GK\"},{\"id\":\"player4\",\"rating_value\":81},{\"id\":\"player5\",\"rating_value\":52},{\"id\":\"player6\",\"rating_value\":84},{\"id\":\"player7\",\"rating_value\":55},{\"id\":\"player8\",\"rating_value\":45},{\"id\":\"player9\",\"rating_value\":60},{\"id\":\"player10\",\"rating_value\":88}]"
  @Override
  public void service(final HttpRequest request, final HttpResponse response) throws Exception {
    String contentType = request.getContentType().orElse("");
    if(contentType.equals("application/json")){
      List<Player> players = new ArrayList<>();
      String body = this.getBody(request);
      if (!body.isEmpty()) {
        players = List.of(MAPPER.readValue(body, Player[].class));
      }

      Optional<String> splitBestPlayers = request.getFirstQueryParameter("split_best_players");
      Optional<String> splitWorstPlayers = request.getFirstQueryParameter("split_best_players");
      Optional<String> splitGoalKeepers = request.getFirstQueryParameter("split_goal_keepers");
      Optional<String> splitDefenders = request.getFirstQueryParameter("split_defenders");
      Optional<String> splitStrikers = request.getFirstQueryParameter("split_strikers");
      Optional<String> expectedTeamNumber = request.getFirstQueryParameter("expected_team_number");
      Optional<String> expectedCompositionNumber = request.getFirstQueryParameter("expected_composition_number");
      Optional<String> compositionType = request.getFirstQueryParameter("composition_type");

      GeneratorConfiguration config = GeneratorConfiguration.builder()
                                                     .splitBestPlayers(Boolean.parseBoolean(splitBestPlayers.orElse("true")))
                                                     .splitWorstPlayers(Boolean.parseBoolean(splitWorstPlayers.orElse("true")))
                                                     .splitGoalKeepers(Boolean.parseBoolean(splitGoalKeepers.orElse("true")))
                                                     .splitDefenders(Boolean.parseBoolean(splitDefenders.orElse("true")))
                                                     .splitStrikers(Boolean.parseBoolean(splitStrikers.orElse("true")))
                                                     .nbTeamsNeeded(Integer.parseInt(expectedTeamNumber.orElse("2")))
                                                     .nbCompositionsNeeded(Integer.parseInt(expectedCompositionNumber.orElse("1")))
                                                     .compositionType(CompositionType.valueOf(compositionType.orElse("REGULAR")))
                                                     .build();
      ICompositionGenerator generator;
      if(config.getNbTeamsNeeded()==2) {
        generator = new CompositionGenerator(config);
      } else{
        generator = new ComplexCompositionGenerator(config);
      }
      MAPPER.writeValue(response.getWriter(), generator.getNBestCompositions(players));
    }
  }

}
