package inf101.simulator.objects;
import inf101.simulator.*;
import inf101.simulator.objects.examples.SimRepellant;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class SimAnimal extends AbstractMovingObject {
	private static final double defaultSpeed = 1.0;
	private Habitat habitat;
	private double health = 1;

	public SimAnimal(Position pos, Habitat hab) {
		super(new Direction(0), pos, defaultSpeed);
		this.habitat = hab;
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		// flips the image to keep the 'graphic' upright when a certain angle is reached
		if (getDirection().toAngle() <= 90 && getDirection().toAngle() >= -90) {
			context.translate(0, getHeight());
			context.scale(1.0,-1.0);
		}
		context.drawImage(MediaHelper.getImage("pipp.png"),0,0, getWidth(), getHeight());
        // represents the field of view
        context.setStroke(Color.CADETBLUE.deriveColor(0.0, 1.0, 1.0, 0.75));
        GraphicsHelper.strokeArcAt(context, getWidth()/2, getHeight()/2, 30, 0, 180);
		// represents the health
		drawBar(context, getHealth(), -1, Color.RED, Color.GREEN);
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
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius()+275)) {
			if(obj instanceof SimRepellant)
				return (SimRepellant) obj;
		}
		return null;
	}

    /**
     * Finds the average angle from 'this' object to the dangers around it, 
     * (limited by: distanceLimit-param in nearbyObjects-method)
     * 
     * @return The average angle, converted from radians to degrees
     */
	public double averageDangerAngle() {
        ArrayList<ISimObject> dangers = new ArrayList<>();
        double cosSum = 0, sinSum = 0;
        for (ISimObject obj : habitat.nearbyObjects(this, getRadius()+275)) {
            if(obj instanceof SimRepellant)
                dangers.add(obj);
        }
        for (ISimObject x : dangers) {
            //System.out.println("DIRECTION: " + directionTo(x).toAngle());
            double radian = directionTo(x).toRadians();
            cosSum += Math.cos(radian);
            sinSum += Math.sin(radian);
        }
        //System.out.println(Math.toDegrees(Math.atan2(sinSum, cosSum)));
        return Math.toDegrees(Math.atan2(sinSum, cosSum));
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
		    // turn away from danger hence '-' opposite direction
            dir = dir.turnTowards(-averageDangerAngle(),2);
			// accelerate when the the object has turned away from the danger
			if (dir.angleDifference(new Direction(averageDangerAngle())) > 120) {
                accelerateTo(defaultSpeed * 2 , 0.2);
            }
		}
		
        // moves slightly towards center if there does not exist consumables
		if (consumableFood != null && (dir.angleDifference(directionTo(consumableFood)) < 90 )){
		    // turn towards the food if it is inside the 'field of view'
			dir = dir.turnTowards(directionTo(consumableFood), 2);
			accelerateTo(defaultSpeed * 1.5, 0.2);
			
			// eat the consumable on touch
			if (distanceToTouch(consumableFood) <= 0) {
				consumableFood.eat(1);
				if (getHealth() < 1.5) {
				    increaseHealth();
				    say("yum");
                }
            }
		} else {
		    Random x = new Random();
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
		
		// kills animal if health reaches zero
		if (health <= 0) {
		    destroy();
        }
        // health decreases as time passes
		decreaseHealth();
		
		accelerateTo(defaultSpeed, 0.1);
        super.step();
	}

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }
    
    public void increaseHealth() {
	    health = health + 0.015;
    }
    
    public void decreaseHealth() {
	    health = health - 0.0005;
    }
}
