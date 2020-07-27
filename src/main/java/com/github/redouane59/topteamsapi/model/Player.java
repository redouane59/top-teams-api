package com.github.redouane59.topteamsapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player implements Comparable<Player> {

    private String         id;
    private double         rating;
    @JsonProperty("previous_rating")
    @JsonInclude(Include.NON_DEFAULT)
    private double         previousRating;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PlayerPosition position;
    @JsonInclude(Include.NON_DEFAULT)
    @JsonProperty("nb_games_played")
    private int nbGamesPlayed;

    public Player(Player player){
        this.id       = player.getId();
        this.rating   = player.getRating();
        this.position = player.getPosition();
        this.nbGamesPlayed = player.nbGamesPlayed;
    }

    @Override
    public int compareTo(Player other) {
        return Double.compare(this.getRating(), other.getRating());
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof Player)) return false;
        Player otherPlayer = (Player)other;
        return this.id.equals(otherPlayer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return this.id + " (" + this.rating + ")";
    }
}
