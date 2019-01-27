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

    public Team(List<Player> players){
        this.players = players;
        this.updateRating();
    }

    public Team(Player... players){
        Collections.addAll(this.players, players);
        this.updateRating();
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
        this.ratingAverage = ratingSum/this.players.size();
    }

    public void setPlayers(List<Player> players){
        this.players = players;
        this.updateRating();
    }

    public double getRatingSum(){
            return this.players.size()*this.ratingAverage;
    }

    public double getRatingSum(int nbPlayersOnField){
        if(nbPlayersOnField<=this.players.size() && nbPlayersOnField>0){
            return this.getRatingAverage()*nbPlayersOnField;
        } else{
            return this.getRatingAverage()*this.players.size();
        }
    }

    public double getRatingAverage(int nbPlayersOnField){
        if(nbPlayersOnField<=this.players.size()){
            return this.ratingAverage;
        } else{
            return (this.ratingAverage /(nbPlayersOnField))*this.players.size();
        }
    }

    public boolean isPlayerOnTeam(String playerId){
        for(Player p : players){
            if(p.getId().equals(playerId)){
                return true;
            }
        }
        return false;
    }

    public List<Player> getPlayersByPosition(PlayerPosition position){
        List<Player> result = new ArrayList<>();
        for(Player p : this.players){
            if(p.getPosition()==position){
                result.add(p);
            }
        }
        return result;
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
