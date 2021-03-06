package inf101.tests;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.SimGoldStar;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * A class for testing the various behaviours of the SimGoldStar class
 */
public class SimGoldStarTest {
    private SimMain main;

    @Before
    public void setup() {
        main = new SimMain();
    }

    /**
     * Test to see that the SimGoldStar is destroyed when expire point
     * is reached.
     */
    @Test
    public void foodExpireTest() {
        Habitat hab = new Habitat(main, 2000, 1000);

        SimGoldStar sim1 = new SimGoldStar(new Position(200,200), 3, 100);
        hab.addObject(sim1);

        for (int i = 0; i < 102; i++) {
            hab.step();
        }
        assertTrue(!sim1.exists());
    }

    /**
     * Tests to see that the SimGoldStar's size is decreased after eat()-method
     * has been called.
     */
    @Test
    public void foodSizeDecrease() {
        Habitat hab = new Habitat(main, 2000, 1000);

        SimGoldStar sim1 = new SimGoldStar(new Position(200,200), 3, 100);
        hab.addObject(sim1);
        
        double startSize = sim1.getRadius();
        System.out.println(startSize);
        sim1.eat(1);
        hab.step();
        assertTrue(startSize > sim1.getRadius());
    }
}
