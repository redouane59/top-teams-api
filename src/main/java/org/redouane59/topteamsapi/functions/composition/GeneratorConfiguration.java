package org.redouane59.topteamsapi.functions.composition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.redouane59.topteamsapi.model.composition.CompositionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratorConfiguration {

    private boolean         splitBestPlayers     = true;
    private boolean         splitWorstPlayers    = true;
    private boolean         splitGoalKeepers     = true;
    private boolean         splitDefenders       = true;
    private boolean         splitStrikers        = true;
    private int             nbTeamsNeeded        = 2;
    private int             nbCompositionsNeeded = 1;
    private CompositionType compositionType      = CompositionType.REGULAR;
}

