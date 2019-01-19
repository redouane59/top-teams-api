package com.amazonaws.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class Team {

    private List<Player> players = new ArrayList<>();
    private double ratingAverage;
    private int nbPlayersOnField;

    public Team(List<Player> players){
        this.players = players;
        this.nbPlayersOnField = players.size();
    }

    public Team(Player... players){
        Collections.addAll(this.players, players);
        this.updateRating();
    }

    public Team(int nbPlayersOnField){
        this.nbPlayersOnField = nbPlayersOnField;
    }

    public void addPlayer(Player p){
        this.players.add(p);
        this.updateRating();
    }

    public void updateRating(){
        double ratingSum = 0;
        for(Player p : this.players){
            ratingSum += p.getRatingValue();
        }
        if(this.nbPlayersOnField!=0 && this.nbPlayersOnField>this.players.size()){
            this.ratingAverage = ratingSum/this.nbPlayersOnField;
        } else{
            this.ratingAverage = ratingSum/this.players.size();
        }
    }

    public void setPlayers(List<Player> players){
        this.players = players;
        this.updateRating();
    }

    public void setNbPlayersOnField(int nbPlayersOnField){
        this.nbPlayersOnField = nbPlayersOnField;
    }

    public double getRatingAverage(){
        if(this.nbPlayersOnField!=0 && nbPlayersOnField>this.players.size()){
            return this.getRatingAverage(this.nbPlayersOnField);
        } else{
            return this.ratingAverage;
        }
    }

    public double getRatingSum(){
        if(this.nbPlayersOnField!=0 && nbPlayersOnField<this.players.size()) {
            return this.getRatingAverage()*nbPlayersOnField;
        } else{
            return this.players.size()*this.ratingAverage;
        }
    }

    private double getRatingAverage(int nbPlayersOnField){
        return (this.ratingAverage /(nbPlayersOnField))*this.players.size();
    }

    public boolean isPlayerOnTeam(String playerId){
        for(Player p : players){
            if(p.getId().equals(playerId)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Team)
        {
            Team other = (Team) obj;
            boolean equal = true;
            for(Player p : other.getPlayers()){
                if(!this.isPlayerOnTeam(p.getId())){
                    equal = false;
                }
            }
            return equal;
        }
        return false;
    }
}
