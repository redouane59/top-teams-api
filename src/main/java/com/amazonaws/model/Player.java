package com.amazonaws.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Player implements Comparable<Player> {

    private String id;
    private double ratingValue;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PlayerPosition position;
    @JsonIgnore
    private int nbGamesPlayed;

    public Player(String id, double ratingValue){
        this.id = id;
        this.ratingValue = ratingValue;
        this.position = null;
    }

    public Player(String id, double ratingValue, PlayerPosition position){
        this.id = id;
        this.ratingValue = ratingValue;
        this.position = position;
    }

    public Player(String id, double ratingValue, int nbGamesPlayed){
        this.id = id;
        this.ratingValue = ratingValue;
        this.nbGamesPlayed = nbGamesPlayed;
    }

    public Player(Player player){
        this.id = player.getId();
        this.ratingValue = player.getRatingValue();
        this.position = player.getPosition();
        this.nbGamesPlayed = player.nbGamesPlayed;
    }

    @Override
    public int compareTo(Player o) {
        return Double.compare(this.getRatingValue(), o.getRatingValue());
    }
}
