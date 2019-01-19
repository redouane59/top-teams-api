package com.amazonaws.handler;

import com.amazonaws.compositionGenerator.ComplexCompositionGenerator;
import com.amazonaws.compositionGenerator.CompositionGenerator;
import com.amazonaws.compositionGenerator.GeneratorConfiguration;
import com.amazonaws.compositionGenerator.ICompositionGenerator;
import com.amazonaws.model.GameType;
import com.amazonaws.model.Player;
import com.amazonaws.model.PlayerPosition;
import com.amazonaws.model.libraries.UtilsValidate;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class ExecuteCompoAlgorithmHandler implements RequestStreamHandler {

    private LambdaLogger logger;

    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        this.logger = context.getLogger();
        this.logger.log("inputStream : " + inputStream);
        JSONObject inputObject = UtilsValidate.asJSONObject(inputStream);
        GeneratorConfiguration config = this.getCompoGeneratorConfiguration(inputObject);
        List<Player> players = this.getPlayersFromJsonObject(inputObject);
        ICompositionGenerator generator;
        if(config.getNbTeamsNeeded()==2) {
            generator = new CompositionGenerator();
        } else{
            generator = new ComplexCompositionGenerator();
        }
        new ObjectMapper().writeValue(outputStream, generator.getNBestCompositions(players, config));
    }

    private List<Player> getPlayersFromJsonObject(JSONObject request){
        ArrayList<LinkedHashMap<String, Object>> result = (ArrayList<LinkedHashMap<String, Object>>)request.get(RequestConstants.PLAYERS);
        if(result==null || result.size()==0){
            System.err.println("No players found");
            return new ArrayList<>();
        }
        List<Player> players = new ArrayList<>();
        for(LinkedHashMap<String, Object> p : result){
            players.add(this.getPlayerFromLinkedHashMap(p));
        }
        this.logger.log("players found : " + players);
        return players;
    }

    private Player getPlayerFromLinkedHashMap(LinkedHashMap<String, Object> p){
        try {
            Player player = new Player();
            // id
            String playerId = p.get(RequestConstants.PLAYER_ID).toString();
            player.setId(playerId);
            // ratingAverage
            Object playerRatingObject = p.get(RequestConstants.PLAYER_RATING_VALUE);
            Double playerRatingValue = UtilsValidate.asDouble(playerRatingObject);
            player.setRatingValue(playerRatingValue);
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

    private GeneratorConfiguration getCompoGeneratorConfiguration(JSONObject request){
        GeneratorConfiguration config = new GeneratorConfiguration();

        Object splitBestObject = request.get(RequestConstants.SPLIT_BEST_PLAYERS);
        if(splitBestObject instanceof Boolean){
            Boolean splitBestPlayers = (Boolean)splitBestObject;
            config.setSplitBestPlayers(splitBestPlayers);
        }
        Object splitWorstObject = request.get(RequestConstants.SPLIT_WORST_PLAYERS);
        if(splitWorstObject instanceof  Boolean){
            Boolean splitWorstPlayers = (Boolean)splitWorstObject;
            config.setSplitWorstPlayers(splitWorstPlayers);
        }
        Object splitGKObject = request.get(RequestConstants.SPLIT_GOAL_KEEPERS);
        if(splitGKObject instanceof Boolean){
            Boolean splitGK = (Boolean)splitGKObject;
            config.setSplitGoalKeepers(splitGK);
        }
        Object nbTeamsObject = request.get(RequestConstants.NB_TEAMS_NEEDED);
        if(nbTeamsObject instanceof String && ((String)nbTeamsObject).length()>0){
            int nbTeamsNeeded = Integer.valueOf((String)nbTeamsObject);
            config.setNbTeamsNeeded(nbTeamsNeeded);
        }
        Object nbCompoObject = request.get(RequestConstants.NB_COMPOSITIONS_NEEDED);
        if(nbCompoObject instanceof String && ((String)nbCompoObject).length()>0){
            int nbCompoNeeded = Integer.valueOf((String)nbCompoObject);
            config.setNbCompositionsNeeded(nbCompoNeeded);
        }
        Object gameTypeObject = request.get(RequestConstants.GAME_TYPE);
        if(gameTypeObject instanceof String && ((String)nbCompoObject).length()>0
                && Arrays.asList(GameType.values()).toString().contains((String)gameTypeObject)) {
            GameType gameType = GameType.valueOf((String)gameTypeObject);
            config.setGameType(gameType);
        }
        this.logger.log("config = " + config);
        return config;
    }
}