package inf101.tests;

import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.SimHunter;
import inf101.simulator.objects.SimMeteor;
import inf101.simulator.objects.SimPrey;
import inf101.simulator.objects.SimProjectile;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * A class for testing the various behaviours of the SimProjectile class
 */
public class SimProjectileTest {
    private SimMain main;

    @Before
    public void setup() {
        main = new SimMain();
    }

    /**
     * Tests to see if the SimProjectile moves after a few steps
     */
    @Test
    public void projectileMovesTest() {
        Habitat hab = new Habitat(main, 2000, 1000);

        SimProjectile sim1 = new SimProjectile(new Direction(0), new Position(1000, 500), hab, 9000, 0);
        hab.addObject(sim1);

        Position startPosition = sim1.getPosition();
        for (int i = 0; i < 100; i++) {
            hab.step();
        }
        assertTrue(sim1.getPosition().distanceTo(startPosition) > 0);
    }

    /**
     * Tests to see if the SimProjectile is destroyed after exiting the
     * habitat after a certain "range" -> see step() in SimProjectile
     */
    @Test
    public void projectileHabitatBorderTest() {
        Habitat hab = new Habitat(main, 1000, 1000);

        SimProjectile sim1 = new SimProjectile(new Direction(0), new Position(900, 500), hab, 9000, 0);
        hab.addObject(sim1);

        for (int i = 0; i < 200; i++) {
            hab.step();
        }
        assertTrue(!sim1.exists());
    }

    /**
     * Tests to see that the object is destroyed when max range
     * has been reached
     */
    @Test
    public void projectileRangeTest() {
        Habitat hab = new Habitat(main, 1000, 1000);

        SimProjectile sim1 = new SimProjectile(new Direction(0), new Position(500, 500), hab, 100, 0);
        hab.addObject(sim1);
        
        //should have been destroyed after 100 steps
        for (int i = 0; i < 200; i++) {
            hab.step();
        }
        assertTrue(!sim1.exists());
    }

    /**
     * Tests to see that the object (type 0) can damage a SimPrey object
     */
    @Test
    public void projectileType0DamagesPrey() {
        Habitat hab = new Habitat(main, 1000, 1000);

        SimProjectile sim1 = new SimProjectile(new Direction(0), new Position(500, 500), hab, 100, 0);
        SimPrey sim2 = new SimPrey(new Position(550, 500), hab);
        hab.addObject(sim1);
        hab.addObject(sim2);

        double preyStartHealth = sim2.getHealth();
        System.out.println(preyStartHealth);
        for (int i = 0; i < 200; i++) {
            hab.step();
        }
        assertTrue(sim2.getHealth() < preyStartHealth);
    }

    /**
     * Tests to see that the object (type 0) does not damage SimHunter object
     */
    @Test
    public void projectileType0NoDamageHunter() {
        Habitat hab = new Habitat(main, 1000, 1000);

        SimProjectile sim1 = new SimProjectile(new Direction(0), new Position(500, 500), hab, 100, 0);
        SimHunter sim2 = new SimHunter(new Position(550, 500), hab);
        hab.addObject(sim1);
        hab.addObject(sim2);

        double preyStartHealth = sim2.getHealth();
        for (int i = 0; i < 200; i++) {
            hab.step();
        }
        assertTrue(sim2.getHealth() == preyStartHealth);
    }

    /**
     * Tests to see that the object (type 0) can damage a SimMeteor object
     */
    @Test
    public void projectileType0DamagesMeteor() {
        Habitat hab = new Habitat(main, 1000, 1000);

        SimProjectile sim1 = new SimProjectile(new Direction(0), new Position(500, 500), hab, 300, 0);
        SimMeteor sim2 = new SimMeteor(new Direction(180), new Position(700, 500), hab);
        hab.addObject(sim1);
        hab.addObject(sim2);

        double meteorStartHealth = sim2.getHealth();
        for (int i = 0; i < 200; i++) {
            hab.step();
        }
        assertTrue(sim2.getHealth() < meteorStartHealth);
    }

    /**
     * Test to see that the object (type 1) can damage a SimHunter object
     */
    @Test
    public void projectileType1DamagesHunter() {
        Habitat hab = new Habitat(main, 1000, 1000);

        SimProjectile sim1 = new SimProjectile(new Direction(0), new Position(500, 500), hab, 100, 1);
        SimHunter sim2 = new SimHunter(new Position(550, 500), hab);
        hab.addObject(sim1);
        hab.addObject(sim2);

        double hunterStartHealth = sim2.getHealth();
        for (int i = 0; i < 200; i++) {
            hab.step();
        }
        assertTrue(sim2.getHealth() < hunterStartHealth);
    }

    /**
     * Test to see that the object (type 1) does not damage SimPrey objects
     */
    @Test
    public void projectileType1NoDamagePrey() {
        Habitat hab = new Habitat(main, 1000, 1000);

        SimProjectile sim1 = new SimProjectile(new Direction(0), new Position(500, 500), hab, 100, 1);
        SimPrey sim2 = new SimPrey(new Position(550, 500), hab);
        hab.addObject(sim1);
        hab.addObject(sim2);

        double hunterStartHealth = sim2.getHealth();
        for (int i = 0; i < 200; i++) {
            hab.step();
        }
        assertTrue(sim2.getHealth() == hunterStartHealth);
    }

    /**
     * Test to see that the object (type 1) can damage a SimMeteor object
     */
    @Test
    public void projectileType1DamagesMeteor() {
        Habitat hab = new Habitat(main, 1000, 1000);

        SimProjectile sim1 = new SimProjectile(new Direction(0), new Position(500, 500), hab, 300, 1);
        SimMeteor sim2 = new SimMeteor(new Direction(180), new Position(700, 500), hab);
        hab.addObject(sim1);
        hab.addObject(sim2);

        double meteorStartHealth = sim2.getHealth();
        for (int i = 0; i < 200; i++) {
            hab.step();
        }
        assertTrue(sim2.getHealth() < meteorStartHealth);
    }
    
}
