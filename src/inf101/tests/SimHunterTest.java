package inf101.tests;

import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A Class for testing the various behaviours of the SimHunter class
 */
public class SimHunterTest {
    private SimMain main;

    @Before
    public void setup() {
        main = new SimMain();
    }

    /**
     * Tests to see if the SimHunter moves after a few steps
     */
    @Test
    public void hunterMoves() {
        Habitat hab = new Habitat(main, 2000, 1000);
        
        SimHunter sim1 = new SimHunter(new Position(1000, 500), hab);
        hab.addObject(sim1);
        
        Position startPosition = sim1.getPosition();
        for (int i = 0; i < 100; i++) {
            hab.step();
        }
        assertTrue(sim1.getPosition().distanceTo(startPosition) > 0);
    }
    
    /**
     * Tests to see if the SimHunter is able to avoid meteors
     */
    @Test
    public void avoidMeteorTest() {
        Habitat hab = new Habitat(main, 2000, 1000);
        
        //placed within sense range and field of view (180 fov, 375 distance)
        SimHunter sim1 = new SimHunter(new Position(1000, 500), hab);
        SimMeteor met1 = new SimMeteor(new Direction(180), new Position(1350, 500), hab);
        hab.addObject(sim1);
        hab.addObject(met1);

        for (int i = 0; i < 500; i++) {
            hab.step();
        }
        assertEquals(true, sim1.exists());
    }

    /**
     * Tests to see if the SimHunter can find an edible object
     * and consume it.
     */
    @Test
    public void findConsumableTest() {
        Habitat hab = new Habitat(main, 2000, 1000);
        
        //placed within sense range and field of view (180 fov, 375 distance)
        SimHunter sim1 = new SimHunter(new Position(1000, 600), hab);
        SimGoldStar starGold1 = new SimGoldStar(new Position(1350, 500), 5, 1200);
        hab.addObject(sim1);
        hab.addObject(starGold1);
        
        double lifeBefore = sim1.getHealth();
        for (int i = 0; i < 500; i++) {
            hab.step();
            assertTrue(lifeBefore < sim1.getHeight());
        }
    }

    /**
     * Tests to see if the SimHunter can find the best edible object.
     */
    @Test
    public void findBestConsumableTest() {
        Habitat hab = new Habitat(main, 2000, 1000);

        //placed within sense range and field of view (180 fov, 375 distance)
        SimHunter sim1 = new SimHunter(new Position(1000, 500), hab);
        SimGoldStar starGold1 = new SimGoldStar(new Position(1350, 600), 1, 1200);
        SimGoldStar starGold2 = new SimGoldStar(new Position(1350, 500), 3, 1200);
        hab.addObject(sim1);
        hab.addObject(starGold1);
        hab.addObject(starGold2);
        
        Direction correct = sim1.getPosition().directionTo(starGold2.getPosition());
        for (int i = 0; i < 100; i++) {
            hab.step();
        }
        
        //direction did not change
        assertTrue(sim1.getDirection().equals(correct));
    }

    /**
     * Tests to see if the SimHunter turns towards the SimPrey
     */
    @Test
    public void hunterFindsPrey () {
        Habitat hab = new Habitat(main, 2000, 1000);
        
        SimHunter sim1 = new SimHunter(new Position(1000, 550), hab);
        SimPrey simPrey1 = new SimPrey(new Position(1150, 600), hab);
        hab.addObject(sim1);
        hab.addObject(simPrey1);

        for (int i = 0; i < 200; i++) {
            hab.step();
        }
        
        Direction preyToHunter = simPrey1.getPosition().directionTo(sim1.getPosition());
        //Hunter facing towards prey (with some deviation)
        assertTrue(sim1.getDirection().toAngle() - (preyToHunter.toAngle() + 180) < 1 
                         || sim1.getDirection().toAngle() - (preyToHunter.toAngle() + 180) > 1);
    }
    
}
