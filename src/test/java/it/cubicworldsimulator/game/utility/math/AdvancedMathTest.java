package it.cubicworldsimulator.game.utility.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdvancedMathTest {

    @Test
    void logInBase() {
        assertEquals(4,AdvancedMath.logInBase(16,2));
    }
}