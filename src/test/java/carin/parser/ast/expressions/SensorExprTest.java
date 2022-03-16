package carin.parser.ast.expressions;

import carin.Config;
import carin.GameStates;
import carin.entities.Antibody;
import carin.entities.IGeneticEntity;
import carin.entities.Virus;
import org.junit.jupiter.api.*;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

// this shit depend on so much of things
class SensorExprTest {

    /*
    @BeforeAll
    static void checkConfig() {
        if (Config.tile_height <= 0 || Config.tile_width <= 0) {
            System.exit(0);
        }
    }
    */
    private final int width = Config.tile_width;
    private final int height = Config.tile_height;
    private final Point2D center = new Point2D.Double(width*3, height*3);
    private final GameStates st = GameStates.states();
    private final Antibody main = new Antibody();

    void resetMap(GameStates st, IGeneticEntity main) {
        for (IGeneticEntity e : st.entities()) {
            st.addToRemove(e);
        }
        st.clearToRemove();
        st.spawnGeneticEntity(center, main);
    }

    void spawnAntibody(int exclude) {
        int m = (exclude / 8) + 1;
        exclude = exclude % 8;

        for (int i = 1; i <= 3; i++) {
            if (i >= m) {
                if (exclude < 1) st.spawnGeneticEntity(pos(0, -i), new Antibody());
                if (exclude < 2) st.spawnGeneticEntity(pos(i, -i), new Antibody());
                if (exclude < 3) st.spawnGeneticEntity(pos(i, 0), new Antibody());
                if (exclude < 4) st.spawnGeneticEntity(pos(i, i), new Antibody());
                if (exclude < 5) st.spawnGeneticEntity(pos(0, i), new Antibody());
                if (exclude < 6) st.spawnGeneticEntity(pos(-i, i), new Antibody());
                if (exclude < 7) st.spawnGeneticEntity(pos(-i, 0), new Antibody());
                st.spawnGeneticEntity(pos(-i, -i), new Antibody());
            }
        }
    }

    void spawnVirus(int exclude) {
        int m = (exclude / 8) + 1;
        exclude = exclude % 8;

        for (int i = 1; i <= 3; i++) {
            if (i >= m) {
                if (exclude < 1) st.spawnGeneticEntity(pos(0, -i), new Virus());
                if (exclude < 2) st.spawnGeneticEntity(pos(i, -i), new Virus());
                if (exclude < 3) st.spawnGeneticEntity(pos(i, 0), new Virus());
                if (exclude < 4) st.spawnGeneticEntity(pos(i, i), new Virus());
                if (exclude < 5) st.spawnGeneticEntity(pos(0, i), new Virus());
                if (exclude < 6) st.spawnGeneticEntity(pos(-i, i), new Virus());
                if (exclude < 7) st.spawnGeneticEntity(pos(-i, 0), new Virus());
                st.spawnGeneticEntity(pos(-i, -i), new Virus());
            }
        }
    }

    Point2D pos(int x, int y) {
        return new Point2D.Double(main.getX() + x*width, main.getY() + y*height);
    }

    @Nested
    class AntibodySensorTest {
        private String cmd;

        @BeforeEach
        void setup() {
            cmd = "antibody";
            st.initTest(7, 7);
            resetMap(st, main);
        }

        @Test
        void noInRange() {
            SensorExpr sensor = new SensorExpr(cmd);
            assertEquals(0, sensor.evaluate(new HashMap<>(), main));
        }

        @TestFactory
        Stream<DynamicTest> isLocCorrect() {
            List<Integer> testCases = new LinkedList<>();
            List<Integer> testNum = new LinkedList<>();
            int c = 0;
            for (int i = 1; i <= 3; i++) {
                for (int j = 1; j <= 8; j++) {
                    testCases.add(i * 10 + j);
                    testNum.add(c);
                    c++;
                }
            }

            return testNum.stream().map(i -> dynamicTest("at location" + testCases.get(i), () -> {
                resetMap(st, main);
                spawnAntibody(i);
                SensorExpr sensor = new SensorExpr(cmd);
                assertEquals(testCases.get(i), sensor.evaluate(new HashMap<>(), main));
            }));
        }

        @Test
        @DisplayName("there's virus before closest antibody")
        void test_with_other() {
            st.spawnGeneticEntity(pos(0, -1), new Virus());
            st.spawnGeneticEntity(pos(1, -1), new Antibody());
            SensorExpr sensor = new SensorExpr(cmd);
            assertEquals(12, sensor.evaluate(new HashMap<>(), main));
        }
    }

    @Nested
    class VirusSensorTest {
        private String cmd;

        @BeforeEach
        void setup() {
            cmd = "virus";
            st.initTest(7, 7);
            resetMap(st, main);
        }

        @Test
        void noInRange() {
            SensorExpr sensor = new SensorExpr(cmd);
            assertEquals(0, sensor.evaluate(new HashMap<>(), main));
        }

        @TestFactory
        Stream<DynamicTest> isLocCorrect() {
            List<Integer> testCases = new LinkedList<>();
            List<Integer> testNum = new LinkedList<>();
            int c = 0;
            for (int i = 1; i <= 3; i++) {
                for (int j = 1; j <= 8; j++) {
                    testCases.add(i * 10 + j);
                    testNum.add(c);
                    c++;
                }
            }

            return testNum.stream().map(i -> dynamicTest("at location" + testCases.get(i), () -> {
                resetMap(st, main);
                spawnVirus(i);
                SensorExpr sensor = new SensorExpr(cmd);
                assertEquals(testCases.get(i), sensor.evaluate(new HashMap<>(), main));
            }));
        }

        @Test
        @DisplayName("there's antibody before closest antibody")
        void test_with_other() {
            st.spawnGeneticEntity(pos(0, -1), new Antibody());
            st.spawnGeneticEntity(pos(1, -1), new Virus());
            SensorExpr sensor = new SensorExpr(cmd);
            assertEquals(12, sensor.evaluate(new HashMap<>(), main));
        }
    }

    @Nested
    class NearbySensorTest {
        private String cmd;

        @BeforeEach
        void setup() {
            cmd = "nearby";
            st.initTest(7, 7);
            resetMap(st, main);
        }

        @Test
        void noInRange() {
            SensorExpr sensor = new SensorExpr(cmd, 6);
            assertEquals(0, sensor.evaluate(new HashMap<>(), main));
        }

        @Test
        void up() {
            SensorExpr sensor = new SensorExpr(cmd, 8);
            spawnAntibody(16);
            assertEquals(32, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(8);
            assertEquals(22, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(0);
            assertEquals(12, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
            spawnVirus(16);
            assertEquals(31, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(8);
            assertEquals(21, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(0);
            assertEquals(11, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
        }

        @Test
        void left() {
            SensorExpr sensor = new SensorExpr(cmd, 4);
            spawnAntibody(16);
            assertEquals(32, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(8);
            assertEquals(22, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(0);
            assertEquals(12, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
            spawnVirus(16);
            assertEquals(31, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(8);
            assertEquals(21, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(0);
            assertEquals(11, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
        }

        @Test
        void right() {
            SensorExpr sensor = new SensorExpr(cmd, 6);
            spawnAntibody(16);
            assertEquals(32, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(8);
            assertEquals(22, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(0);
            assertEquals(12, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
            spawnVirus(16);
            assertEquals(31, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(8);
            assertEquals(21, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(0);
            assertEquals(11, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
        }

        @Test
        void down() {
            SensorExpr sensor = new SensorExpr(cmd, 2);
            spawnAntibody(16);
            assertEquals(32, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(8);
            assertEquals(22, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(0);
            assertEquals(12, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
            spawnVirus(16);
            assertEquals(31, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(8);
            assertEquals(21, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(0);
            assertEquals(11, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
        }

        @Test
        void downleft() {
            SensorExpr sensor = new SensorExpr(cmd, 1);
            spawnAntibody(16);
            assertEquals(32, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(8);
            assertEquals(22, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(0);
            assertEquals(12, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
            spawnVirus(16);
            assertEquals(31, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(8);
            assertEquals(21, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(0);
            assertEquals(11, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
        }

        @Test
        void downright() {
            SensorExpr sensor = new SensorExpr(cmd, 3);
            spawnAntibody(16);
            assertEquals(32, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(8);
            assertEquals(22, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(0);
            assertEquals(12, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
            spawnVirus(16);
            assertEquals(31, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(8);
            assertEquals(21, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(0);
            assertEquals(11, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
        }

        @Test
        void upright() {
            SensorExpr sensor = new SensorExpr(cmd, 9);
            spawnAntibody(16);
            assertEquals(32, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(8);
            assertEquals(22, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(0);
            assertEquals(12, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
            spawnVirus(16);
            assertEquals(31, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(8);
            assertEquals(21, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(0);
            assertEquals(11, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
        }

        @Test
        void upleft() {
            SensorExpr sensor = new SensorExpr(cmd, 1);
            spawnAntibody(16);
            assertEquals(32, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(8);
            assertEquals(22, sensor.evaluate(new HashMap<>(), main));
            spawnAntibody(0);
            assertEquals(12, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
            spawnVirus(16);
            assertEquals(31, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(8);
            assertEquals(21, sensor.evaluate(new HashMap<>(), main));
            spawnVirus(0);
            assertEquals(11, sensor.evaluate(new HashMap<>(), main));

            resetMap(st, main);
        }

    }
}