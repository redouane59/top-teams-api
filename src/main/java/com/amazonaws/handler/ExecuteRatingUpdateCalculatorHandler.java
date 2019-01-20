package com.amazonaws.handler;

import com.amazonaws.model.*;
import com.amazonaws.model.libraries.UtilsValidate;
import com.amazonaws.ratingUpdateCalculator.CalculatorConfiguration;
import com.amazonaws.ratingUpdateCalculator.RatingUpdatesCalculator;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ExecuteRatingUpdateCalculatorHandler extends AbstractHandler {


    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        super.handleRequest(inputStream, outputStream, context);
        JSONObject inputObject = this.asJSONObject(inputStream);
        Composition composition = this.getCompositionFromJsonObject(inputObject);
        Score score = this.getScoreFromJsonObject(inputObject);
        Game game = new Game(composition, score);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(this.getConfigurationFromJsonObject(inputObject));
        new ObjectMapper().writeValue(outputStream, calculator.getRatingUpdates(game));
    }

    private CalculatorConfiguration getConfigurationFromJsonObject(JSONObject request){
        CalculatorConfiguration config = new CalculatorConfiguration();
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

    private Score getScoreFromJsonObject(JSONObject request){
        Object scoreAobj = request.get(RequestConstants.SCORE_A);
        Object scoreBobj = request.get(RequestConstants.SCORE_B);
        int scoreA = 0;
        int scoreB = 0;
        if(scoreAobj instanceof Integer && scoreBobj instanceof Integer){
            scoreA = (int)scoreAobj;
            scoreB = (int)scoreBobj;
        } else{
            System.err.println("Enable to parse score.");
        }
        return new Score(scoreA, scoreB);
    }

    private Composition getCompositionFromJsonObject(JSONObject request){
        LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>)request.get(RequestConstants.COMPOSITION);
        ArrayList<LinkedHashMap<String, Object>> teamAobject = (ArrayList<LinkedHashMap<String, Object>>)result.get(RequestConstants.TEAM_A);
        ArrayList<LinkedHashMap<String, Object>> teamBobject = (ArrayList<LinkedHashMap<String, Object>>)result.get(RequestConstants.TEAM_B);
        Team teamA = new Team();
        Team teamB = new Team();
        for(LinkedHashMap o : teamAobject){
            teamA.addPlayer(this.getPlayerFromLinkedHashMap(o));
        }
        for(LinkedHashMap o : teamBobject){
            teamB.addPlayer(this.getPlayerFromLinkedHashMap(o));
        }
        int nbPlayers = teamA.getPlayers().size() + teamB.getPlayers().size();

        Object gameTypeObj = request.get(RequestConstants.GAME_TYPE);
        GameType gameType = null;
        if(gameTypeObj instanceof String && ((String)(gameTypeObj)).length()>0){
            gameType = GameType.valueOf((String)gameTypeObj);
        }

        teamA.setNbPlayersOnField(this.getMaxNbPlayerPerTeamOnField(nbPlayers, gameType));
        teamB.setNbPlayersOnField(this.getMaxNbPlayerPerTeamOnField(nbPlayers, gameType));
        Composition composition = new Composition(teamA, teamB);
        this.getLogger().log("composition : " + composition);
        return composition;
    }

    private int getMaxNbPlayerPerTeamOnField(int nbPlayers, GameType gameType){
        if (gameType == GameType.ODD && nbPlayers%2==1){
            return nbPlayers/2 + 1;
        } else{
            return nbPlayers/2;
        }
    }

}
