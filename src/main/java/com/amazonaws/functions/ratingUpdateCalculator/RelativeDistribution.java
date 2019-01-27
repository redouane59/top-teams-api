package com.amazonaws.functions.ratingUpdateCalculator;

public enum RelativeDistribution {

    NONE, // each player will win/loose the same quantity of points
    LOW,
    MEDIUM,
    HIGH // a player with a hight number of played games will be less modificated than newbies
}
