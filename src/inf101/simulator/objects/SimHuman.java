package inf101.simulator.objects;

import inf101.simulator.*;
import inf101.simulator.objects.examples.SimRepellant;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Olav Gjerde on 04/05/2017.
 */
public class SimHuman extends AbstractMovingObject implements ISimObject {
    private static final double defaultSpeed = 1.0;
    private Habitat habitat;
    private double health = 1;

    public SimHuman(Position pos, Habitat hab) {
        super(new Direction(0), pos, defaultSpeed);
        this.habitat = hab;
    }

    @Override
    public void draw(GraphicsContext context) {
        super.draw(context);

        // flips the image to keep the 'graphic' upright when a certain angle is reached
        if (getDirection().toAngle() <= 90 && getDirection().toAngle() >= -90) {
            context.translate(0, getHeight());
            context.scale(1.0, -1.0);
        }
        context.drawImage(MediaHelper.getImage("pipp.png"), 0, 0, getWidth(), getHeight());
        // represents the health
        drawBar(context, getHealth(), -1, Color.RED, Color.GREEN);
    }

    

    /**
     * Uses the foodSorter class which implements the Comparator interface to sort
     * the IEdibleObjects from lowest to highest in regards to the nutritional value
     * of the objects.
     *
     * @return The IEdibleObject with the highest nutritional value
     */
    private IEdibleObject getBestFood() {
        ArrayList<IEdibleObject> foodList = new ArrayList();

        class foodSorter implements Comparator<IEdibleObject> {
            @Override
            public int compare(IEdibleObject o1, IEdibleObject o2) {
                if (Double.compare(o1.getNutritionalValue(), o2.getNutritionalValue()) > 0) return 1;
                else if (Double.compare(o1.getNutritionalValue(), o2.getNutritionalValue()) == 0) return 0;
                else return -1;
            }
        }

        for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 275)) {
            if (obj instanceof IEdibleObject)
                foodList.add((IEdibleObject) obj);
        }
        Collections.sort(foodList, new foodSorter());
        // return object with the largest nutritional-value
        return foodList.get(foodList.size() - 1);
    }

    private IEdibleObject getClosestFood() {
        for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 275)) {
            if (obj instanceof IEdibleObject)
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
    private double getDangerAngle() {
        ArrayList<ISimObject> dangers = new ArrayList<>();
        double cosSum = 0, sinSum = 0;
        for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 175)) {
            if (obj instanceof SimRepellant)
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
     *
     * @return the closest 'SimRepellant' in regards to a set radius
     */
    private SimRepellant getClosestRepellant() {
        for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 175)) {
            if (obj instanceof SimRepellant)
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

    private double getHealth() {
        return health;
    }

    private void increaseHealth() {
        health = health + 0.015;
    }

    private void decreaseHealth() {
        health = health - 0.0005;
    }

    @Override
    public boolean shootable() {
        return false;
    }

    @Override
    public void step() {
        IEdibleObject consumableFood = getClosestFood();
        ISimObject closestRepellant = getClosestRepellant();

        if (closestRepellant != null) {
            // turn away from danger hence '+180' opposite direction
            dir = dir.turnTowards(getDangerAngle() + 180, 2);
            // accelerate when the the object has turned away from the danger
            if (dir.angleDifference(new Direction(getDangerAngle())) > 120) {
                accelerateTo(defaultSpeed * 2.0, 0.4);
            }
        } else if (consumableFood != null && (dir.angleDifference(directionTo(consumableFood)) < 90)) {
            // turn towards the "largest" food if it exits inside the 'field of view'
            dir = dir.turnTowards(directionTo(getBestFood()), 2);
            accelerateTo(defaultSpeed * 1.5, 0.3);
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

        // eat the consumables on touch
        if (consumableFood != null && distanceToTouch(consumableFood) <= 0) {
            consumableFood.eat(1);
            if (getHealth() < 1.5) {
                increaseHealth();
            }
        } else if (health <= 0) {
            destroy();
        } else {
            decreaseHealth();
        }

        accelerateTo(defaultSpeed, 0.1);
        super.step();
    }
}
