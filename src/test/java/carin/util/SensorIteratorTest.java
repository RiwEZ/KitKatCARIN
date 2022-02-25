package carin.util;

import carin.GameStates;
import carin.entities.Antibody;
import carin.entities.IGeneticEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SensorIteratorTest {
    GameStates states = mock(GameStates.class);
    Antibody host = mock(Antibody.class);
    SensorIterator it;

    @BeforeEach
    void setup() {
        when(host.getLocation()).thenReturn(new Point2D.Double(4, 4));
        it = new SensorIterator(host.getLocation(), states, 1, 1);
    }

    @Test
    void hasNextBehaviour() {
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }

    @Test
    void nextOnNoNearby() {
        assertNull(it.next());
    }

    @Test
    void onlyOnceNext() {
        Point2D p = new Point2D.Double(4, 3);
        IGeneticEntity temp = new Antibody();

        Map<Point2D, IGeneticEntity> entityMap = mock(ConcurrentHashMap.class);

        when(states.isInMap(p)).thenReturn(true);
        when(states.isUnOccupied(p)).thenReturn(false);
        when(states.entityMap()).thenReturn(entityMap);
        when(entityMap.get(p)).thenReturn(temp);

        assertEquals(temp, it.next());
        assertEquals(11, it.getLookAt());
    }
}