package com.github.redouane59.topteamsapi.functions.evaluation;

public enum RelativeDistribution {
    NONE, // each player will win/loose the same quantity of points
    LOW,
    MEDIUM,
    HIGH // a player with a high number of played games will be less modificated than newbies
}
