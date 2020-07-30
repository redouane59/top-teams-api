package com.github.redouane59.topteamsapi.functions.evaluation;

import com.github.redouane59.topteamsapi.functions.AbstractHttpHelper;
import com.github.redouane59.topteamsapi.model.Game;
import com.github.redouane59.topteamsapi.model.Score;
import com.github.redouane59.topteamsapi.model.composition.Composition;
import com.github.redouane59.topteamsapi.model.composition.CompositionType;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import java.util.Optional;
import lombok.extern.java.Log;

@Log
public class RatingUpdateCalculatorHttp extends AbstractHttpHelper implements HttpFunction {

    @Override
    public void service(final HttpRequest request, final HttpResponse response) throws Exception {
        String contentType = request.getContentType().orElse("");
        if (contentType.equals("application/json")) {
            Composition composition;
            String       body    = this.getBody(request);
            if (body.isEmpty()) log.severe("empty body");
            composition = AbstractHttpHelper.MAPPER.readValue(body, Composition.class);
            Optional<String> scoreA               = request.getFirstQueryParameter("score_A");
            Optional<String> scoreB               = request.getFirstQueryParameter("score_B");
            Optional<String> splitPointsByTeam    = request.getFirstQueryParameter("split_points_by_team");
            Optional<String> relativeDistribution = request.getFirstQueryParameter("relative_distribution");
            Optional<String> kf                   = request.getFirstQueryParameter("kf");
            Optional<String> compositionType      = request.getFirstQueryParameter("composition_type");
            Score            score                = new Score(Integer.parseInt(scoreA.orElse("0")), Integer.parseInt(scoreB.orElse("0")));
            CalculatorConfiguration calculatorConfiguration =
                new CalculatorConfiguration()
                    .withSplitPointsByTeam(Boolean.parseBoolean(splitPointsByTeam.orElse("true")))
                    .withRelativeDistribution(RelativeDistribution.valueOf(relativeDistribution.orElse("MEDIUM")))
                    .withKf(Double.parseDouble(kf.orElse("4")));
            RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(calculatorConfiguration);
            composition.setNbPlayersOnField(this.getMaxNbPlayerPerTeamOnField(composition.getTeamA().getPlayers().size() + composition.getTeamB().getPlayers().size(),
                                                                              CompositionType.valueOf(compositionType.orElse("REGULAR"))));

            Game                    game       = new Game().withComposition(composition).withScore(score);
            AbstractHttpHelper.MAPPER.writeValue(response.getWriter(), calculator.getUpdatedPlayers(game));
        }
    }

    private int getMaxNbPlayerPerTeamOnField(int nbPlayers, CompositionType compositionType){
        if (compositionType == CompositionType.ODD && nbPlayers % 2 == 1){
            return nbPlayers/2 + 1;
        } else{
            return nbPlayers/2;
        }
    }

}