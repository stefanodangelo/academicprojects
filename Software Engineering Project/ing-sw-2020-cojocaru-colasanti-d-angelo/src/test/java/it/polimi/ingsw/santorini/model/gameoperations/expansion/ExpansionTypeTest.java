package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpansionTypeTest {

    @Test
    void byBooleanTest() {
        assertEquals(ExpansionType.EXPANSIVE, ExpansionType.byBoolean(true),
                "byBooleanTest true does not match EXPANSIVE");
        assertEquals(ExpansionType.RESTRICTIVE, ExpansionType.byBoolean(false),
                "byBooleanTest false does not match RESTRICTIVE");
    }
}