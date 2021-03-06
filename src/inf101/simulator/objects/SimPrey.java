package inf101.simulator.objects;

import inf101.simulator.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * An implementation of a spaceship that hunts mainly after SimMeteor-objects
 * and SimSilverStar-objects.
 */
public class SimPrey extends AbstractMovingObject {
    private static final double defaultSpeed = 2.5;
    private int deathTimer = 0;
    
    public SimPrey(Position pos, Habitat hab) {
        super(new Direction(0), pos, defaultSpeed, hab);
    }
    
    @Override
    public void draw(GraphicsContext context) {
        super.draw(context);
        if (getDirection().toAngle() <= 90 && getDirection().toAngle() >= -90) {
            context.translate(0, getHeight());
            context.scale(1.0, -1.0);
        }
        if (deathTimer > 0) {
            context.drawImage(MediaHelper.getImage("smokeScreen.gif"), 0, 0, getWidth(), getWidth());
        } else {
            context.drawImage(MediaHelper.getImage("spaceShips_006.png"), 0, 0, getWidth(), getHeight());
            drawBar(context, getHealth(), -1, Color.RED, Color.GREEN);
        }
    }

    @Override
    public double getHeight() {
        return 40;
    }

    @Override
    public double getWidth() {
        return 60;
    }

    /**
     * The step method runs one iteration of the SimPrey's behaviour.
     * Will first move towards meteors (in range) and try to shoot them, if it gets too close it will try to avoid
     * them. Then it will try to find SimSilverStars, finally it will try to avoid SimHunters.
     * The object will turn towards the center of the habitat, unless the distance to the center is less than
     * 900 -> if this is the case it will use the randomPath that has been generated and move elsewhere.
     * (It will also move towards the center if the object is to close to the border of the habitat or outside of it.)
     * Fires SimProjectiles of type 1 -> (behaviour defined in SimProjectile-class).
     * Field of view = 180 degrees
     * If the health reaches zero the object is destroyed.
     */
    @Override
    public void step() {
        IEdibleObject consumableObject = SimObjectHelper.getClosestSilverPickup(this, habitat, 325);
        SimMeteor closestMeteor = SimObjectHelper.getClosestMeteor(this, habitat, 325);
        SimHunter closestHunter = SimObjectHelper.getClosestHunter(this, habitat, 325);

        if (closestMeteor != null && dir.angleDifference(directionTo(closestMeteor)) < 90 && distanceToTouch(closestMeteor) > 100) {
            dir = dir.turnTowards(directionTo(closestMeteor), 2);
            accelerateTo(defaultSpeed * 1.25, 0.8);
            if (randomGen.nextInt(70) == 0) {
                habitat.addObject(new SimProjectile(getDirection(), getPosition(), habitat, 300, 1));
            }
        } else if (closestMeteor != null && dir.angleDifference(directionTo(closestMeteor)) < 90 && distanceToTouch(closestMeteor) <= 100) {
            dir = dir.turnTowards(SimObjectHelper.getAverageMeteorAngle(this, habitat, 325) + 180, 3);
            accelerateTo(defaultSpeed * 1.25, 0.8);
        } else if (consumableObject != null && dir.angleDifference(directionTo(consumableObject)) < 90) {
            dir = dir.turnTowards(directionTo(SimObjectHelper.getBestSilverPickup(this, habitat, 325)), 3);
            accelerateTo(defaultSpeed * 1.5, 0.8);
        } else if (closestHunter != null) {
            dir = dir.turnTowards(SimObjectHelper.getAverageHunterAngle(this, habitat, 325) + 180, 3);
            accelerateTo(defaultSpeed * 1.5, 0.8);
        } else if (getPosition().distanceTo(habitat.getCenter()) < 900) {
            dir = dir.turnTowards(randomPath, 0.5);
        } else {
            dir = dir.turnTowards(directionTo(habitat.getCenter()), 0.35);
        }
        
        // go towards center if we're close to the border
        if (!habitat.contains(getPosition(), getRadius() * 1.2)) {
            dir = dir.turnTowards(directionTo(habitat.getCenter()), 5);
            if (!habitat.contains(getPosition(), getRadius())) {
                // we're actually outside
                accelerateTo(2.5 * defaultSpeed, 0.3);
            }
        }

        if (consumableObject != null && distanceToTouch(consumableObject) <= 0) {
            consumableObject.eat(1);
            if (getHealth() < 2) {
                increaseHealth();
            }
        } else if (health <= 0) {
            deathTimer++;
            accelerateTo(0,1);
            if (deathTimer == 1) if (SimMain.isSoundOn()) SimSounds.getSound(6).play();
            // lets a "death" animation play briefly before destroying the object
            if (deathTimer > 60) {
                habitat.addObject(new SimGoldStar(getPosition(), randomGen.nextDouble()*2+0.5, 1200));
                destroy();
                deathTimer = 0;
            }
        }
        
        accelerateTo(defaultSpeed, 0.1);
        super.step();
    }
}
