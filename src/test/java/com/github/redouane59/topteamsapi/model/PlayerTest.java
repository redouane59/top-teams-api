package com.github.redouane59.topteamsapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class PlayerTest {

  private static final ObjectMapper MAPPER      = new ObjectMapper();

  @Test
  void testDeserializeWithoutPosition() throws JsonProcessingException {
    final String playerJson = "{\"id\":\"player1\",\"rating\":77.0,\"nb_games_played\":4}";
    Player       player      = MAPPER.readValue(playerJson, Player.class);
      assertEquals("player1",player.getId());
      assertEquals(77,player.getRating());
      assertEquals(4,player.getNbGamesPlayed());
      assertNull(player.getPosition());
  }

  @Test
  void testDeserializeWithoutGames() throws JsonProcessingException {
    final String playerJson = "{\"id\":\"player1\",\"rating\":77.0, \"position\":\"GK\"}";
    Player       player      = MAPPER.readValue(playerJson, Player.class);
    assertEquals("player1",player.getId());
    assertEquals(77,player.getRating());
    assertEquals(PlayerPosition.GK, player.getPosition());
    assertEquals(0,player.getNbGamesPlayed());
  }
}
