package org.redouane59.topteamsapi.model.composition;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.redouane59.topteamsapi.model.Player;

@Getter
@Setter
public abstract class AbstractComposition implements IComposition, Comparable<AbstractComposition>, Cloneable  {

    private List<Player> availablePlayers;
    private int          nbPlayersOnField;

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
