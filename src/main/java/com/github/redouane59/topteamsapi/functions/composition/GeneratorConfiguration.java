package com.github.redouane59.topteamsapi.functions.composition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.github.redouane59.topteamsapi.model.composition.CompositionType;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratorConfiguration {

    @Builder.Default
    private boolean         splitBestPlayers     = true;
    @Builder.Default
    private boolean         splitWorstPlayers    = true;
    @Builder.Default
    private boolean         splitGoalKeepers     = true;
    @Builder.Default
    private boolean         splitDefenders       = true;
    @Builder.Default
    private boolean         splitStrikers        = true;
    @Builder.Default
    private int             nbTeamsNeeded        = 2;
    @Builder.Default
    private int             nbCompositionsNeeded = 1;
    @Builder.Default
    private CompositionType compositionType      = CompositionType.REGULAR;
}

