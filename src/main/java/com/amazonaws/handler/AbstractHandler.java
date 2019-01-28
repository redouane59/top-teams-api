package com.amazonaws.handler;

import com.amazonaws.model.*;
import com.amazonaws.model.utils.UtilValidate;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public abstract class AbstractHandler implements RequestStreamHandler {

    @Getter
    private LambdaLogger logger;

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException{
        this.logger = context.getLogger();
        this.logger.log("inputStream : " + inputStream);
    }

    JSONObject asJSONObject(InputStream inputStream){
        try {
            return new ObjectMapper().readValue(IOUtils.toString(inputStream), JSONObject.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Player getPlayerFromLinkedHashMap(LinkedHashMap<String, Object> p){
        try {
            Player player = new Player();
            // id
            String playerId = p.get(RequestConstants.PLAYER_ID).toString();
            player.setId(playerId);
            // ratingAverage
            Object playerRatingObject = p.get(RequestConstants.PLAYER_RATING_VALUE);
            Double playerRatingValue = UtilValidate.asDouble(playerRatingObject);
            player.setRatingValue(playerRatingValue);
            // nbGames
            Object playerNbGamesObject = p.get(RequestConstants.PLAYER_NB_GAMES_PLAYED);
            if(!UtilValidate.isEmpty(playerNbGamesObject)){
                player.setNbGamesPlayed((int)p.get(RequestConstants.PLAYER_NB_GAMES_PLAYED));
            }
            // position
            if (p.get(RequestConstants.PLAYER_POSITION) instanceof String) {
                PlayerPosition position = PlayerPosition.valueOf(p.get(RequestConstants.PLAYER_POSITION).toString());
                player.setPosition(position);
            }
            return player;
        } catch(Exception e){
            System.err.println("error parsing player " + p + " : " + e +". ");
            return new Player("ErrorPlayer", 0);
        }
    }

    Score getScoreFromJsonObject(JSONObject request){
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

    Composition getCompositionFromJsonObject(JSONObject request){
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
        CompositionType compositionType = null;
        if(gameTypeObj instanceof CompositionType){
            compositionType = CompositionType.valueOf((String)gameTypeObj);
        }

        Composition composition = new Composition(teamA, teamB);
        composition.setNbPlayersOnField(this.getMaxNbPlayerPerTeamOnField(nbPlayers, compositionType));
        this.getLogger().log("composition : " + composition);
        return composition;
    }

    private int getMaxNbPlayerPerTeamOnField(int nbPlayers, CompositionType compositionType){
        if (compositionType == CompositionType.ODD && nbPlayers%2==1){
            return nbPlayers/2 + 1;
        } else{
            return nbPlayers/2;
        }
    }

    List<Player> getPlayerListFromJsonObject(JSONObject request){
        ArrayList<LinkedHashMap<String, Object>> result = (ArrayList<LinkedHashMap<String, Object>>)request.get(RequestConstants.PLAYERS);
        if(result==null || result.size()==0){
            System.err.println("No players found");
            return new ArrayList<>();
        }
        List<Player> players = new ArrayList<>();
        for(LinkedHashMap<String, Object> p : result){
            players.add(this.getPlayerFromLinkedHashMap(p));
        }
        this.getLogger().log("players found : " + players);
        return players;
    }

}
