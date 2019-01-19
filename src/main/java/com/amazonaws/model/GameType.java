package com.amazonaws.model;

public enum GameType {

    SAME_NB_OF_PLAYERS_PER_TEAM, // 5vs5 on field, two teams of 5 players
    SAME_NB_OF_PLAYERS_ON_FIELD, // 5vs5 on field, one team of 5 players, one team of 6 players with one substitute
    FREE                         // 5vs6 on field, one team of 5, one team of 6
}
