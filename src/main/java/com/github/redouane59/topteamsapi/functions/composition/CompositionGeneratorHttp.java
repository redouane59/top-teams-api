package com.github.redouane59.topteamsapi.functions.composition;

import com.github.redouane59.topteamsapi.functions.AbstractHttpHelper;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.composition.AbstractComposition;
import com.github.redouane59.topteamsapi.model.composition.ComplexComposition;
import com.github.redouane59.topteamsapi.model.composition.Composition;
import com.github.redouane59.topteamsapi.model.composition.CompositionType;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import java.util.List;
import java.util.Optional;
import lombok.extern.java.Log;

@Log
public class CompositionGeneratorHttp extends AbstractHttpHelper implements HttpFunction {

  // curl -v "localhost:8080/?split_best_players=true&split_worst_players=true&split_goal_keepers=true&split_defenders=true&split_strikers=true&expected_team_number=2&expected_composition_number=1&composition_type=REGULAR" -H "Content-Type: application/json" -d "[{\"id\":\"player1\",\"rating\":54,\"position\":\"GK\"},{\"id\":\"player2\",\"rating\":77},{\"id\":\"player3\",\"rating\":59,\"position\":\"GK\"},{\"id\":\"player4\",\"rating\":81},{\"id\":\"player5\",\"rating\":52},{\"id\":\"player6\",\"rating\":84},{\"id\":\"player7\",\"rating\":55},{\"id\":\"player8\",\"rating\":45},{\"id\":\"player9\",\"rating\":60},{\"id\":\"player10\",\"rating\":88}]"
  @Override
  public void service(final HttpRequest request, final HttpResponse response) throws Exception {
    String contentType = request.getContentType().orElse("");
    if(contentType.equals("application/json")){
      AbstractComposition composition;
      String body = this.getBody(request);
      if (body.isEmpty()) log.severe("body empty");
      if(this.checkJsonCompatibility(body, Composition.class)){
        composition = AbstractHttpHelper.MAPPER.readValue(body, Composition.class);
      } else if (this.checkJsonCompatibility(body, ComplexComposition.class)){
        composition = AbstractHttpHelper.MAPPER.readValue(body, ComplexComposition.class);
      } else{
        composition = Composition.builder().availablePlayers(List.of(AbstractHttpHelper.MAPPER.readValue(body, Player[].class))).build();
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
      AbstractHttpHelper.MAPPER.writeValue(response.getWriter(), generator.getBestCompositions(composition));
    }
  }

}
