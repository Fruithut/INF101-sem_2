package inf101.simulator.objects;

import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import javafx.scene.canvas.GraphicsContext;

/**
 * An implementation of a projectile, constructed with a "target" direction, 
 * start "pos", habitat and a range.
 */
public class SimProjectile extends AbstractMovingObject {
    private static final double defaultSpeed = 3;
    private int range, type;

    /**
     * Constructs a SimProjectile
     * @param targetDirection where to be launched to
     * @param startPos where to be launched from
     * @param hab habitat which the object belongs to
     * @param range of the projectile, must be greater than 0
     * @param type of projectile, either 0 or 1
     */
    public SimProjectile(Direction targetDirection, Position startPos, Habitat hab, int range, int type) {
        super(targetDirection, startPos, defaultSpeed, hab);
        this.habitat = hab;
        this.range = range;
        this.type = type;
        checkState(this);
    }
    
    @Override
    public void draw(GraphicsContext context) {
        super.draw(context);
        if (type == 0) {
            context.drawImage(MediaHelper.getImage("laserBlue07.png"), 0, 0, getWidth(), getHeight());
        } else {
            context.drawImage(MediaHelper.getImage("laserRed07.png"), 0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public double getHeight() {
        return 10;
    }

    @Override
    public double getWidth() {
        return 25;
    }

    /**
     * Checks the current state of the object
     * Rules -> range must be greater than 0
     *       -> type must 0 or 1
     *       
     * @param projectile under consideration
     */
    private static void checkState(SimProjectile projectile) {
        if (projectile.range <= 0)
            throw new IllegalArgumentException("The range must me greater than zero: " + projectile.range);
        if (projectile.type < 0)
            throw new IllegalArgumentException("No type less than 0 exists: " + projectile.type);
        if (projectile.type > 1)
            throw new IllegalArgumentException("No type larger than 1 exists: " + projectile.type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SimProjectile that = (SimProjectile) o;
        if (range != that.range) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + range;
        result = 31 * result + type;
        return result;
    }

    /**
     * The step method runs one step of the SimProjectile's behaviour.
     * If the projectile is of type 0 then it can damage SimPrey-objects and secondly
     * damage SimMeteor-objects.
     * If the projectile is of type 1 then is can damage SimMeteor-objects and secondly
     * damage SimHunter-objects.
     * The projectile will be destroyed upon impact, upon reaching maximal range or exiting the habitat
     */
    @Override
    public void step() {
        ISimObject closestPrey = SimObjectHelper.getClosestPrey(this, habitat, 40),
                   closestMeteor = SimObjectHelper.getClosestMeteor(this, habitat, 40),
                   closestHunter = SimObjectHelper.getClosestHunter(this, habitat, 40);
        
        if (type == 0) {
            if (closestPrey != null && distanceToTouch(closestPrey) <= -1) {
                closestPrey.decreaseHealth(0.20);
                destroy();
            } else if (closestMeteor != null && distanceToTouch(closestMeteor) <= -1) {
                closestMeteor.decreaseHealth();
                destroy();
            }
        } else if (type == 1) {
            if (closestMeteor != null && distanceToTouch(closestMeteor) <= -1) {
                closestMeteor.decreaseHealth();
                destroy();
            } else if (closestHunter != null && distanceToTouch(closestHunter) <= -1) {
                closestHunter.decreaseHealth(0.20);
                destroy();
            }
        }
        
        // destroy outside of habitat
        if (!habitat.contains(getPosition(), -getRadius()*4)) {
            destroy();
        }
        
        if (stepCount < range) {
            super.step();
        } else {
            destroy();
        }
    }
}
