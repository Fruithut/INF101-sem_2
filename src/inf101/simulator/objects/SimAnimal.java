package inf101.simulator.objects;
import inf101.simulator.Direction;
import inf101.simulator.GraphicsHelper;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.AbstractMovingObject;
import inf101.simulator.objects.IEdibleObject;
import inf101.simulator.objects.ISimObject;
import inf101.simulator.objects.SimEvent;
import inf101.simulator.objects.examples.SimRepellant;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SimAnimal extends AbstractMovingObject {
	private static final double defaultSpeed = 1.0;
	private Habitat habitat;

	public SimAnimal(Position pos, Habitat hab) {
		super(new Direction(0), pos, defaultSpeed);
		this.habitat = hab;
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		
		//Flips the image to keep the 'graphic' upright when a certain angle is reached
		if (getDirection().toAngle() <= 90 && getDirection().toAngle() >= -90) {
			context.translate(0, getHeight());
			context.scale(1.0,-1.0);
		}
		
		context.drawImage(MediaHelper.getImage("pipp.png"),0,0, getWidth(), getHeight());
	}

	public IEdibleObject getBestFood() {
		return getClosestFood();
	}

	public IEdibleObject getClosestFood() {
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius()+400)) {
			if(obj instanceof IEdibleObject)
				return (IEdibleObject) obj;
		}
		
		return null;
	}

    /**
     * Searches through all 'ISimObjects' in a list retrieved from 'habitat' in search of a
     * 'SimRepellant'-object
     * @return the closest 'SimRepllant' in regards to a set radius
     */
	public SimRepellant getClosestRepellant() {
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius()+200)) {
			if(obj instanceof SimRepellant)
				return (SimRepellant) obj;
		}
		return null;
	}

	@Override
	public double getHeight() {
		return 50;
	}

	@Override
	public double getWidth() {
		return 50;
	}
	
	@Override
	public void step() {
		IEdibleObject consumableFood = getClosestFood();
		ISimObject closestRepellant = getClosestRepellant();
		
		if (closestRepellant != null) {
			dir = dir.turnTowards(-directionTo(closestRepellant).toAngle(),3);
		}
		
        // moves slightly towards center if there does not exist consumables
		if (consumableFood != null && (dir.angleDifference(directionTo(consumableFood)) < 90 )){
			dir = dir.turnTowards(directionTo(consumableFood), 2);
			accelerateTo(defaultSpeed * 2, 0.3);
			if (distanceToTouch(consumableFood) <= 0) {
				consumableFood.eat(1);
			}
		} else {
			dir = dir.turnTowards(directionTo(habitat.getCenter()), 0.5);
		}
		
		// go towards center if we're close to the border
		if (!habitat.contains(getPosition(), getRadius() * 1.2)) {
			dir = dir.turnTowards(directionTo(habitat.getCenter()), 5);
			if (!habitat.contains(getPosition(), getRadius())) { 
				// we're actually outside
				accelerateTo(5 * defaultSpeed, 0.3);
			}
		}
        
		accelerateTo(defaultSpeed, 0.1);
		super.step();
	}
}
