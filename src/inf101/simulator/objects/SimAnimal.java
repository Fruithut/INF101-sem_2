package inf101.simulator.objects;
import inf101.simulator.*;
import inf101.simulator.objects.examples.SimRepellant;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SimAnimal extends AbstractMovingObject {
	private static final double defaultSpeed = 1.0;
	private Habitat habitat;
	private double health = 1;
    
	public SimAnimal(Position pos, Habitat hab) {
		super(new Direction(0), pos, defaultSpeed);
		this.habitat = hab;
		
		// anonymous class for event-listener implementation
        class SimListener implements ISimListener {
            @Override
            public void eventHappened(SimEvent event) {
                say(event.getType());
            }
        }
        
		this.habitat.addListener(this, new SimListener());
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

	/**
	 * //TODO COMMENT
	 * @return The IEdibleObject with the highest nutritional value
	 */
	public IEdibleObject getBestFood() {
	    ArrayList<IEdibleObject> foodList = new ArrayList();
	    
	    class foodSorter implements Comparator<IEdibleObject> {
            @Override
            public int compare(IEdibleObject o1, IEdibleObject o2) {
                if (Double.compare(o1.getNutritionalValue(),o2.getNutritionalValue()) > 0) return 1;
                else if (Double.compare(o1.getNutritionalValue(), o2.getNutritionalValue()) == 0) return 0;
                else return -1;
            }
        }
        
        for (ISimObject obj : habitat.nearbyObjects(this, getRadius()+275)) {
	        if (obj instanceof IEdibleObject) 
	            foodList.add((IEdibleObject) obj);
        }
        Collections.sort(foodList, new foodSorter());
        // return object with the largest nutritional-value
        return foodList.get(foodList.size()-1);
    }

	public IEdibleObject getClosestFood() {
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius()+275)) {
			if(obj instanceof IEdibleObject)
				return (IEdibleObject) obj;
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
        for (ISimObject obj : habitat.nearbyObjects(this, getRadius()+175)) {
            if(obj instanceof SimRepellant)
                dangers.add(obj);
        }
        for (ISimObject x : dangers) {
            double radian = directionTo(x).toRadians();
			cosSum += Math.cos(radian);
            sinSum += Math.sin(radian);
        }
		return Math.toDegrees(Math.atan2(sinSum, cosSum));
    }

	/**
	 * Searches through all 'ISimObjects' in a list retrieved from 'habitat' in search of a
	 * 'SimRepellant'-object
	 * @return the closest 'SimRepellant' in regards to a set radius
	 */
	public SimRepellant getClosestRepellant() {
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius()+175)) {
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

	/**
	 * One step of the behaviour that defines the SimAnimal
	 * Will try to evade dangers first, if no dangers are "found"
	 * it will try to find food, else it moves towards the center of the habitat.
	 * 
	 * Consuming food will increase health -> health decreases slowly without consumption
	 * A health-value of zero will kill the object
	 */
	@Override
	public void step() {
		IEdibleObject consumableFood = getClosestFood();
		ISimObject closestRepellant = getClosestRepellant();
		
		if (closestRepellant != null) {
			// turn away from danger hence '+180' opposite direction
            dir = dir.turnTowards(averageDangerAngle() + 180,2);
			// accelerate when the the object has turned away from the danger
			if (dir.angleDifference(new Direction(averageDangerAngle())) > 120) {
                accelerateTo(defaultSpeed * 2.0 , 0.4);
            }
		} else if (consumableFood != null && (dir.angleDifference(directionTo(consumableFood)) < 90 )) {
            // turn towards the "largest" food if it exits inside the 'field of view'
			dir = dir.turnTowards(directionTo(getBestFood()), 2);
			accelerateTo(defaultSpeed * 1.5, 0.3);
		} else {
			// moves slightly towards center if there does not exist consumables or dangers
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

		// eat the consumables on touch
		if (consumableFood != null && distanceToTouch(consumableFood) <= 0) {
			consumableFood.eat(1);
			if (getHealth() < 1.5) {
                increaseHealth();
                habitat.triggerEvent(new SimEvent(this, "yum", null, null));
			}
		} else if (health <= 0) {
		    destroy();
        } else {
			decreaseHealth();
		}
		
		accelerateTo(defaultSpeed, 0.1);
        super.step();
	}

    public double getHealth() {
        return health;
    }
    
    public void increaseHealth() {
	    health = health + 0.015;
    }
    
    public void decreaseHealth() {
	    health = health - 0.0005;
    }
}
