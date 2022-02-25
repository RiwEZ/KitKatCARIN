package carin.entities;

import carin.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void setCreditCorrectly() {
        Player.instance().setCredit(Config.initial_credits);
        assertEquals(Player.instance().getCredit(), Config.initial_credits);
    }

}