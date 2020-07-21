package com.github.redouane59.topteamsapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player implements Comparable<Player> {

    private String id;
    @JsonProperty("rating_value")
    private double ratingValue;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PlayerPosition position;
    @JsonInclude(Include.NON_DEFAULT)
    @JsonProperty("nb_games_played")
    private int nbGamesPlayed;

    public Player(Player player){
        this.id = player.getId();
        this.ratingValue = player.getRatingValue();
        this.position = player.getPosition();
        this.nbGamesPlayed = player.nbGamesPlayed;
    }

    @Override
    public int compareTo(Player other) {
        return Double.compare(this.getRatingValue(), other.getRatingValue());
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof Player)) return false;
        Player otherPlayer = (Player)other;
        return this.id.equals(otherPlayer.getId());
    }

    @Override
    public String toString() {
        return this.id + " ("+this.ratingValue+")";
    }
}
