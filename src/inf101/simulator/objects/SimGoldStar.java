package inf101.simulator.objects;

import inf101.simulator.Direction;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import javafx.scene.canvas.GraphicsContext;

/**
 * An implementation of an IEdibleObject. Constructed with a position, size and a set expiration-point.
 * Has 2 set final values for the ENERGY_FACTOR and the DIAMETER of the object.
 */
public class SimGoldStar extends AbstractSimObject implements IEdibleObject {
    private static final double ENERGY_FACTOR = 10;
    private static final double DIAMETER = 25;
    private double size = 1.0;
    private int counter = 0, expirationTimer;

    /**
     * Constructs a SimGoldStar object
     * @param pos where the object will be placed
     * @param size of the object, must be larger than 0
     * @param expiration how long the object will last, must be larger than 0
     */
    public SimGoldStar(Position pos, double size, int expiration) {
        super(new Direction(0), pos);
        this.size = size;
        this.expirationTimer = expiration;
        checkState(this);
    }

    @Override
    public void draw(GraphicsContext context) {
        super.draw(context);
        context.translate(0, getHeight());
        context.scale(1.0,-1.0);
        if (expirationTimer - counter < 550 && expirationTimer - counter >= 350) {
            context.drawImage(MediaHelper.getImage("star_gold_slow.gif"),0,0,getWidth(),getHeight());
        } else if (expirationTimer - counter < 350) {
            context.drawImage(MediaHelper.getImage("star_gold_fast.gif"),0,0,getWidth(),getHeight());
        } else {
            context.drawImage(MediaHelper.getImage("star_gold.png"),0,0,getWidth(),getHeight());
        }
    }

    @Override
    public double eat(double howMuch) {
        double deltaSize = Math.min(size, howMuch / ENERGY_FACTOR);
        size -= deltaSize;
        if (size == 0) {
            destroy(); 
        }
        return deltaSize * ENERGY_FACTOR;
    }

    @Override
    public double getHeight() {
        return DIAMETER * size;
    }

    @Override
    public double getWidth() {
        return DIAMETER * size;
    }

    @Override
    public double getNutritionalValue() {
        return size * ENERGY_FACTOR;
    }

    /**
     * Checks the current state of the object
     * Rules -> expiration value must be larger than 0
     *       -> size must be larger than 0
     * @param goldStar under consideration
     */
    private static void checkState(SimGoldStar goldStar) {
        if (goldStar.expirationTimer <= 0)
            throw new IllegalArgumentException("The expiration value must be larger than 0: " + goldStar.expirationTimer);
        if (goldStar.size <= 0)
            throw new IllegalArgumentException("The size of the SimGoldStar must be larger than 0: " + goldStar.size);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SimGoldStar that = (SimGoldStar) o;

        if (Double.compare(that.size, size) != 0) return false;
        if (counter != that.counter) return false;
        return expirationTimer == that.expirationTimer;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(size);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + counter;
        result = 31 * result + expirationTimer;
        return result;
    }

    /**
     * Increases the counter so that the object gets closer to the expiration-point
     * by each step that passes.
     */
    @Override
    public void step() {
        if (expirationTimer < counter) {
            destroy();
        }
        counter++;
    }
}
