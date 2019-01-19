package com.amazonaws.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public abstract class AbstractComposition implements IComposition, Comparable<AbstractComposition>, Cloneable  {

    private List<Player> availablePlayers;

    public AbstractComposition(List<Player> players){
        this.availablePlayers = players;
    }

    public abstract double getRatingDifference();

    @Override
    public int compareTo(AbstractComposition o) {
        try{
            return Double.compare(Math.abs(this.getRatingDifference()), Math.abs(o.getRatingDifference()));
        }
        catch(Exception e){
            return 0;
        }
    }

    @Override
    public abstract boolean equals (Object obj);

}
