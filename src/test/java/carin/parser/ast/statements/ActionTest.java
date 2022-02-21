package carin.parser.ast.statements;

import carin.Config;
import carin.GameStates;
import carin.entities.Antibody;
import carin.entities.IGeneticEntity;
import org.junit.jupiter.api.*;

import java.awt.geom.Point2D;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {
    private static final int width = Config.tile_width;
    private static final int height = Config.tile_height;
    private static final Point2D center = new Point2D.Double(width, height);

    void movingTester(GameStates states, Action a, Point2D expected) {
        // spawn Antibody at center and test if they're moving to right place
        IGeneticEntity host = new Antibody();
        host.setGeneticCode(null);
        states.spawnGeneticEntity(center, host);
        a.evaluate(new HashMap<>(), host);

        assertEquals(expected, host.getLocation());
        assertEquals(states.entityMap().get(expected), host);
    }

    @Nested
    class MovingTest {
        static GameStates st = GameStates.states();

        @BeforeAll
        static void setup() {
            st.initTest(3, 3);
        }

        @Test
        void downleft() {
            Action downleft = new Action('m', 1);
            movingTester(st, downleft, new Point2D.Double(0, height*2));
        }

        @Test
        void down() {
            Action down = new Action('m', 2);
            movingTester(st, down, new Point2D.Double(width, height*2));
        }

        @Test
        void downright() {
            Action downright = new Action('m', 3);
            movingTester(st, downright, new Point2D.Double(width*2, height*2));
        }

        @Test
        void left() {
            Action left = new Action('m', 4);
            movingTester(st, left, new Point2D.Double(0, height));
        }

        @Test
        void right() {
            Action right = new Action('m', 6);
            movingTester(st, right, new Point2D.Double(width*2, height));
        }

        @Test
        void upleft() {
            Action upleft = new Action('m', 7);
            movingTester(st, upleft, new Point2D.Double(0, 0));
        }

        @Test
        void up() {
            Action up = new Action('m', 8);
            movingTester(st, up, new Point2D.Double(width, 0));
        }

        @Test
        void upright() {
            Action upright = new Action('m', 9);
            movingTester(st, upright, new Point2D.Double(width*2, 0));
        }
    }

    // Attack damage is depend on Config
    void attackingTester(GameStates states, IGeneticEntity host, Action a, Point2D expected) {
        // spawn Antibody at center and test if they're moving to right place
        IGeneticEntity target = new Antibody();
        target.setGeneticCode(null);
        states.spawnGeneticEntity(expected, target);

        int prevHp = target.getCurrHP();
        a.evaluate(new HashMap<>(), host);

        assertTrue(target.getCurrHP() < prevHp);
    }

    // this should not depend on Config
    @Nested
    class AttackingTest {
        static GameStates st = GameStates.states();
        static IGeneticEntity host = new Antibody();

        @BeforeAll
        static void setup() {
            st.initTest(3, 3);
            host.setGeneticCode(null);
            st.spawnGeneticEntity(center, host);
        }

        @Test
        void downleft() {
            Action downleft = new Action('a', 1);
            attackingTester(st, host, downleft, new Point2D.Double(0, height*2));
        }

        @Test
        void down() {
            Action down = new Action('a', 2);
            attackingTester(st, host, down, new Point2D.Double(width, height*2));
        }

        @Test
        void downright() {
            Action downright = new Action('a', 3);
            attackingTester(st, host, downright, new Point2D.Double(width*2, height*2));
        }

        @Test
        void left() {
            Action left = new Action('a', 4);
            attackingTester(st, host, left, new Point2D.Double(0, height));
        }

        @Test
        void right() {
            Action right = new Action('a', 6);
            attackingTester(st, host, right, new Point2D.Double(width*2, height));
        }

        @Test
        void upleft() {
            Action upleft = new Action('a', 7);
            attackingTester(st, host, upleft, new Point2D.Double(0, 0));
        }

        @Test
        void up() {
            Action up = new Action('a', 8);
            attackingTester(st, host, up, new Point2D.Double(width, 0));
        }

        @Test
        void upright() {
            Action upright = new Action('a', 9);
            attackingTester(st, host, upright, new Point2D.Double(width*2, 0));
        }
    }
}