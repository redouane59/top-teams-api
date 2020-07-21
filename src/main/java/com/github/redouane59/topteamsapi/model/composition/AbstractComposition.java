package com.github.redouane59.topteamsapi.model.composition;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import com.github.redouane59.topteamsapi.model.Player;

@Getter
@Setter
@Log
public abstract class AbstractComposition implements IComposition, Comparable<AbstractComposition>, Cloneable  {

    private List<Player> availablePlayers;
    private int          nbPlayersOnField;

    @Override
    public int compareTo(AbstractComposition o) {
        try{
            return Double.compare(Math.abs(this.getRatingDifference()), Math.abs(o.getRatingDifference()));
        }
        catch(Exception e){
            log.severe(e.getMessage());
            return 0;
        }
    }

    @Override
    public abstract boolean equals (Object obj);

}
