package com.amazonaws.handler;

import com.amazonaws.model.Player;
import com.amazonaws.model.PlayerPosition;
import com.amazonaws.model.libraries.UtilsValidate;
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
import java.util.LinkedHashMap;


public abstract class AbstractHandler implements RequestStreamHandler {

    @Getter
    private LambdaLogger logger;

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException{
        this.logger = context.getLogger();
        this.logger.log("inputStream : " + inputStream);
    }

    public JSONObject asJSONObject(InputStream inputStream){
        try {
            return new ObjectMapper().readValue(IOUtils.toString(inputStream), JSONObject.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    Player getPlayerFromLinkedHashMap(LinkedHashMap<String, Object> p){
        try {
            Player player = new Player();
            // id
            String playerId = p.get(RequestConstants.PLAYER_ID).toString();
            player.setId(playerId);
            // ratingAverage
            Object playerRatingObject = p.get(RequestConstants.PLAYER_RATING_VALUE);
            Double playerRatingValue = UtilsValidate.asDouble(playerRatingObject);
            player.setRatingValue(playerRatingValue);
            // nbGames
            int playerNbGames = (int)p.get(RequestConstants.PLAYER_NB_GAMES_PLAYED);
            player.setNbGamesPlayed(playerNbGames);
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

}
