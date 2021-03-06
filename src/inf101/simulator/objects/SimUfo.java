package inf101.simulator.objects;

import inf101.simulator.*;
import javafx.scene.canvas.GraphicsContext;

/**
 * An implementation of a ufo that spawns IEdibleObjects every few steps
 * (Acts as a rare bonus)
 */
public class SimUfo extends AbstractMovingObject {
    private static final double defaultSpeed = 2.5;

    /**
     * Spawns to the left of the habitat, and floats into view -> then exiting on the other side
     * @param hab habitat to be placed in
     */
    public SimUfo(Habitat hab) {
        super(new Direction(0), new Position(-100, 
                SimMain.getInstance().getRandom().nextInt((int) hab.getHeight() - 100) + 100), defaultSpeed, hab);
    }

    @Override
    public void draw(GraphicsContext context) {
        super.draw(context);
        context.drawImage(MediaHelper.getImage("ufoColors.gif"), 0, 0, getWidth(), getHeight());
    }

    @Override
    public double getHeight() {
        return 100;
    }

    @Override
    public double getWidth() {
        return 100;
    }

    /**
     * The step method runs one iteration of the SimUfo's behaviour.
     * The goal of the ufo is to spawn IEdibleObjects as is travels over the
     * habitat.
     */
    @Override
    public void step() {
        if (stepCount == 1) if (SimMain.isSoundOn()) SimSounds.getSound(9).play();
        if (stepCount % 400 == 0) habitat.addObject(new SimGoldStar(getPosition(), 3, 1200));
        else if (stepCount % 200 == 0) habitat.addObject(new SimSilverStar(getPosition(), 3, 1200));
        super.step();
    }
}
