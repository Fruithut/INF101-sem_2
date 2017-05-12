package inf101.tests;

import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.*;
import inf101.simulator.objects.SimPrey;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A Class for testing the various behaviours of the SimPrey class
 */
public class SimPreyTest {
    private SimMain main;

    @Before
    public void setup() {
        main = new SimMain();
    }

    /**
     * Tests to see if the SimPrey moves after a few steps
     */
    @Test
    public void preyMoves() {
        Habitat hab = new Habitat(main, 2000, 1000);

        SimPrey sim1 = new SimPrey(new Position(1000, 500), hab);
        hab.addObject(sim1);

        Position startPosition = sim1.getPosition();
        for (int i = 0; i < 100; i++) {
            hab.step();
        }
        assertTrue(sim1.getPosition().distanceTo(startPosition) > 0);
    }

    /**
     * Tests to see if the SimPrey is able to avoid meteors
     */
    @Test
    public void avoidMeteorTest() {
        Habitat hab = new Habitat(main, 2000, 1000);

        //placed within sense range and field of view (180 fov, 325 distance)
        SimPrey sim1 = new SimPrey(new Position(1000, 500), hab);
        SimMeteor met1 = new SimMeteor(new Direction(180), new Position(1200, 500), hab);
        hab.addObject(sim1);
        hab.addObject(met1);

        for (int i = 0; i < 500; i++) {
            hab.step();
        }
        assertEquals(true, sim1.exists());
    }

    /**
     * Tests to see if the SimPrey can find an edible object
     * and consume it.
     */
    @Test
    public void findConsumableTest() {
        Habitat hab = new Habitat(main, 2000, 1000);

        //placed within sense range and field of view (180 fov, 325 distance)
        SimPrey sim1 = new SimPrey(new Position(1000, 500), hab);
        SimSilverStar starSilver1 = new SimSilverStar(new Position(1300, 600), 5, 1200);
        hab.addObject(sim1);
        hab.addObject(starSilver1);

        double lifeBefore = sim1.getHealth();
        for (int i = 0; i < 500; i++) {
            hab.step();
            assertTrue(lifeBefore < sim1.getHeight());
        }
    }

    /**
     * Tests to see if the SimPrey can find the best edible object.
     */
    @Test
    public void findBestConsumableTest() {
        Habitat hab = new Habitat(main, 2000, 1000);

        //placed within sense range and field of view (180 fov, 325 distance)
        SimPrey sim1 = new SimPrey(new Position(1000, 500), hab);
        SimSilverStar starSilver1 = new SimSilverStar(new Position(1320, 550), 1, 1200);
        SimSilverStar starSilver2 = new SimSilverStar(new Position(1320, 500), 3, 1200);
        hab.addObject(sim1);
        hab.addObject(starSilver1);
        hab.addObject(starSilver2);

        Direction correct = sim1.getPosition().directionTo(starSilver2.getPosition());
        for (int i = 0; i < 50; i++) {
            hab.step();
        }
        
        //direction did not change
        assertTrue(sim1.getDirection().equals(correct));
    }

    /**
     * Tests to see if the SimPrey changes direction when encountering SimHunter.
     */
    @Test
    public void preyFindsHunter () {
        Habitat hab = new Habitat(main, 2000, 1000);

        SimPrey sim1 = new SimPrey(new Position(1000, 550), hab);
        SimHunter simHunter1 = new SimHunter(new Position(1150, 600), hab);
        hab.addObject(sim1);
        hab.addObject(simHunter1);

        Direction directionBefore = sim1.getPosition().directionTo(simHunter1.getPosition());
        for (int i = 0; i < 100; i++) {
            hab.step();
        }
        // changes direction immediately (within the random-path generation interval of 200)
        assertTrue(!directionBefore.equals(sim1.getPosition().directionTo(simHunter1.getPosition())));
    }
}
