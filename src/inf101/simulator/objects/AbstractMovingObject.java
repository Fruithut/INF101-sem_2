package inf101.simulator.objects;

import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.util.Random;

public abstract class AbstractMovingObject extends AbstractSimObject implements IMovingObject {
	protected double speed;
	protected boolean exists = true;
	
	// new variables
    protected Random randomGen = new Random();
    protected Direction randomPath;
    protected int stepCount = 0;
    protected Habitat habitat;

	public AbstractMovingObject(Direction dir, Position pos, double speed, Habitat hab) {
		super(dir, pos);
		this.speed = speed;
		this.habitat = hab;
        this.randomPath = new Direction(randomGen.nextInt(360));
	}

	/**
	 * Adjust speed towards a target.
	 * 
	 * Use this method repeatedly to gradually increase or decrease speed until
	 * target speed is reached.
	 * 
	 * @param targetSpeed
	 *            The desired speed (can be higher or lower than current speed)
	 * @param increment
	 *            Maximum change
	 */
	protected void accelerateTo(double targetSpeed, double increment) {
		increment = Math.abs(increment);
		if (targetSpeed > speed) {
			speed += Math.min(increment, targetSpeed - speed);
		} else { // decelerating
			speed -= Math.min(increment, speed - targetSpeed);
		}
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		context.setLineWidth(2);
		context.setStroke(Color.CHARTREUSE);
		context.setFill(Color.LAWNGREEN);

		if (!hideAnnotations) {
			if (SimMain.getInstance().showDirection()) {
				context.fillArc(0, 0, getWidth(), getHeight(), -10, 20, ArcType.ROUND);
			}
			if (SimMain.getInstance().showSpeed()) {
				context.strokeArc(getSpeed() * 10, 0.0, getWidth(), getHeight(), -15, 30, ArcType.OPEN);
			}
		}
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AbstractMovingObject that = (AbstractMovingObject) o;

        if (Double.compare(that.speed, speed) != 0) return false;
        if (exists != that.exists) return false;
        if (stepCount != that.stepCount) return false;
        if (!randomPath.equals(that.randomPath)) return false;
        return habitat.equals(that.habitat);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(speed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (exists ? 1 : 0);
        result = 31 * result + randomPath.hashCode();
        result = 31 * result + stepCount;
        result = 31 * result + habitat.hashCode();
        return result;
    }

    @Override
	public double getSpeed() {
		return speed;
	}

	@Override
	public void step() {
	    // generate a random path every 200 steps
        if (stepCount % 200 == 0) {
            randomPath = new Direction(randomGen.nextInt(360));
        }
        stepCount++;
		reposition(getPosition().move(getDirection(), getSpeed()));
	}
}
