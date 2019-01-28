package com.amazonaws.handler;

import com.amazonaws.functions.ratingUpdatesCalculator.RelativeDistribution;
import com.amazonaws.model.*;
import com.amazonaws.functions.ratingUpdatesCalculator.CalculatorConfiguration;
import com.amazonaws.functions.ratingUpdatesCalculator.RatingUpdatesCalculator;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExecuteRatingUpdateCalculatorHandler extends AbstractHandler {

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        super.handleRequest(inputStream, outputStream, context);
        JSONObject inputObject = this.asJSONObject(inputStream);
        Composition composition = this.getCompositionFromJsonObject(inputObject);
        Score score = this.getScoreFromJsonObject(inputObject);
        Game game = new Game(composition, score);
        int nbPlayersPerTeam = game.getComposition().getTeamA().getPlayers().size();
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(this.getCalculatorConfigurationFromJsonObject(inputObject, nbPlayersPerTeam));
        new ObjectMapper().writeValue(outputStream, calculator.getRatingUpdates(game));
    }

    private CalculatorConfiguration getCalculatorConfigurationFromJsonObject(JSONObject request, int nbPlayersPerTeam){
        CalculatorConfiguration config = new CalculatorConfiguration(nbPlayersPerTeam);
        Object splitPointsByTeamObj = request.get(RequestConstants.SPLIT_POINTS_BY_TEAM);
        if(splitPointsByTeamObj instanceof Boolean){
            config.setSplitPointsByTeam((Boolean)splitPointsByTeamObj);
        }
        Object relativeDistributionObj = request.get(RequestConstants.RELATIVE_DISTRIBUTION);
        if(relativeDistributionObj instanceof String && ((String)(relativeDistributionObj)).length()>0){
            config.setRelativeDistribution(RelativeDistribution.valueOf((String)relativeDistributionObj));
        }
        this.getLogger().log("config : " + config);
        return config;
    }

}
