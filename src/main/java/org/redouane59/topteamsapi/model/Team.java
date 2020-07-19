package org.redouane59.topteamsapi.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Builder.Default
    private List<Player> players = new ArrayList<>();

    public double getRatingAverage(){
        double ratingSum = 0;
        for(Player p : this.players){
            ratingSum += p.getRatingValue();
        }
        return ratingSum/this.players.size();
    }


    public double getRatingSum(){
        double ratingSum = 0;
        for(Player p : this.players){
            ratingSum += p.getRatingValue();
        }
        return ratingSum;
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
            return this.getRatingAverage();
        } else{
            return (this.getRatingAverage() /(nbPlayersOnField))*this.players.size();
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
            boolean                  equal = true;
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
