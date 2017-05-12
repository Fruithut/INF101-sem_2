package inf101.tests;

import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.SimHunter;
import inf101.simulator.objects.SimMeteor;
import inf101.simulator.objects.SimPrey;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * A class for testing the various behaviours of the SimMeteor class
 */
public class SimMeteorTest {
    private SimMain main;

    @Before
    public void setup() {
        main = new SimMain();
    }

    /**
     * Tests to see if the SimMeteor moves after a few steps
     */
    @Test
    public void meteorMovesTest() {
        Habitat hab = new Habitat(main, 2000, 1000);
        
        SimMeteor sim1 = new SimMeteor(new Direction(0), new Position(1000, 500), hab);
        hab.addObject(sim1);

        Position startPosition = sim1.getPosition();
        for (int i = 0; i < 100; i++) {
            hab.step();
        }
        assertTrue(sim1.getPosition().distanceTo(startPosition) > 0);
    }

    /**
     * Tests to see if the SimMeteor changes direction when colliding with
     * another meteor
     */
    @Test
    public void meteorCollidesMeteor() {
        Habitat hab = new Habitat(main, 2000, 1000);

        SimMeteor sim1 = new SimMeteor(new Direction(0), new Position(1000, 500), hab);
        SimMeteor sim2 = new SimMeteor(new Direction(180), new Position(1200, 500), hab);
        hab.addObject(sim1);
        hab.addObject(sim2);
        
        Direction startDirection = sim1.getDirection();
        for (int i = 0; i < 200; i++) {
            hab.step();
        }
        assertTrue(!sim1.getDirection().equals(startDirection));
    }

    /**
     * Test to see if the SimMeteor collides with the SimPrey
     * and changes direction
     */
    @Test
    public void meteorCollidesPrey() {
        Habitat hab = new Habitat(main, 2000, 1000);

        SimMeteor sim1 = new SimMeteor(new Direction(180), new Position(1300, 500), hab);
        SimPrey sim2 = new SimPrey(new Position(1100, 500), hab);
        hab.addObject(sim1);
        hab.addObject(sim2);

        Direction startDirection = sim1.getDirection();
        for (int i = 0; i < 100; i++) {
            hab.step();
        }
        assertTrue(!sim1.getDirection().equals(startDirection));
    }

    /**
     * Test to see if the SimMeteor collides with the SimPrey
     * and changes direction
     */
    @Test
    public void meteorCollidesHunter() {
        Habitat hab = new Habitat(main, 2000, 1000);

        SimMeteor sim1 = new SimMeteor(new Direction(0), new Position(1000, 500), hab);
        SimHunter sim2 = new SimHunter(new Position(1100, 500), hab);
        hab.addObject(sim1);
        hab.addObject(sim2);

        Direction startDirection = sim1.getDirection();
        for (int i = 0; i < 100; i++) {
            hab.step();
        }
        assertTrue(!sim1.getDirection().equals(startDirection));
    }

    /**
     * Test to see if the SimMeteor damages the SimPrey
     * on collision
     */
    @Test
    public void meteorDamagesPrey() {
        Habitat hab = new Habitat(main, 2000, 1000);

        SimMeteor sim1 = new SimMeteor(new Direction(180), new Position(1100, 500), hab);
        SimPrey sim2 = new SimPrey(new Position(1000, 500), hab);
        hab.addObject(sim1);
        hab.addObject(sim2);

        double startHealth = sim2.getHealth();
        for (int i = 0; i < 100; i++) {
            hab.step();
        }
        assertTrue(sim2.getHealth() < startHealth);
    }

    /**
     * Test to see if the SimMeteor damages the SimHunter
     * on collision
     */
    @Test
    public void meteorDamagesHunter() {
        Habitat hab = new Habitat(main, 2000, 1000);

        SimMeteor sim1 = new SimMeteor(new Direction(180), new Position(1100, 500), hab);
        SimHunter sim2 = new SimHunter(new Position(1000, 500), hab);
        hab.addObject(sim1);
        hab.addObject(sim2);

        double startHealth = sim2.getHealth();
        for (int i = 0; i < 100; i++) {
            hab.step();
        }
        assertTrue(sim2.getHealth() < startHealth);
    }

    /**
     * Tests to see if the original meteor is destroyed after the
     * meteorExplode() method is called.
     */
    @Test
    public void meteorExplodesTest() {
        Habitat hab = new Habitat(main, 2000, 1000);

        SimMeteor sim1 = new SimMeteor(new Direction(0), new Position(1000, 500), hab);
        hab.addObject(sim1);
        sim1.meteorExplode();
        for (int i = 0; i < 100; i++) {
            hab.step();
        }
        assertTrue(!sim1.exists());
    }

    /**
     * Tests to see if the meteor is destroyed after exiting the
     * habitat after a certain "range" -> see step() in SimMeteor
     */
    @Test
    public void meteorHabitatBorderTest() {
        Habitat hab = new Habitat(main, 1000, 1000);

        SimMeteor sim1 = new SimMeteor(new Direction(0), new Position(900, 500), hab);
        hab.addObject(sim1);
        
        for (int i = 0; i < 500; i++) {
            hab.step();
        }
        assertTrue(!sim1.exists());
    }
}
