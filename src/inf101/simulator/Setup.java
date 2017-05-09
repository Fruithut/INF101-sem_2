package inf101.simulator;

import inf101.simulator.objects.*;
import inf101.simulator.objects.examples.Blob;
import inf101.simulator.objects.examples.SimAnimal;
import inf101.simulator.objects.examples.SimFeed;
import inf101.simulator.objects.examples.SimRepellant;

public class Setup {
	
	/** This method is called when the simulation starts */
	public static void setup(SimMain main, Habitat habitat) {
	    
	    // anonymous class alternative for a factory
		/*class SimFactory implements ISimObjectFactory {
            @Override
            public ISimObject create(Position pos, Habitat hab) {
                return new SimAnimal(pos,hab);
            }
        }*/
		
		// Code for part 1 commented out
		/*habitat.addObject(new SimAnimal(new Position(400, 400), habitat));
		habitat.addObject(new Blob(new Direction(0), new Position(400, 400), 1));
		
		for (int i = 0; i < 3; i++)
			habitat.addObject(new SimRepellant(main.randomPos()));

		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimFeed(pos,
                main.getRandom().nextDouble()*2+0.5), "SimFeed™", SimFeed.PAINTER);
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimRepellant(pos),
                "SimRepellant™", SimRepellant.PAINTER);
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimAnimal(pos,hab), 
				"SimAnimal", "pipp.png");*/
		
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimHunter(pos,hab),
				"SimHunter", "playerShip2_green.png");
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimPrey(pos,hab),
				"SimPrey", "spaceShips_006.png");
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimGoldStar(pos, 
				main.getRandom().nextDouble()*2+0.5, 1200),"SimGoldStar", "star_gold.png");
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimSilverStar(pos,
				main.getRandom().nextDouble()*2+0.5, 1200),"SimSilverStar", "star_silver.png");
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimMeteor(hab),
				"SimMeteor", "spaceMeteors_004.png");
	}

	/**
	 * This method is called for each step, you can use it to add objects at
	 * random intervals
	 */
	public static void step(SimMain main, Habitat habitat) {
		/*if (main.getRandom().nextInt(100) == 0) {
			habitat.addObject(new SimMeteor(habitat));
		}

		if (main.getRandom().nextInt(1000) == 0) {
			for (int i = 0; i < 5; i++) {
				habitat.addObject(new SimPrey(new Position(main.getRandom().nextInt(1900),
						main.getRandom().nextInt(950)), habitat));
			}
			for (int i = 0; i < 2; i++) {
				habitat.addObject(new SimHunter(new Position(main.getRandom().nextInt(1900),
						main.getRandom().nextInt(950)), habitat));
			}
		}*/
	}
}
