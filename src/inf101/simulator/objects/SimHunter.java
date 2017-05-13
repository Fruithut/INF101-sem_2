package inf101.simulator.objects;

import inf101.simulator.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * An implementation of a spaceship that hunts mainly after SimPrey-objects
 * and SimGoldStar-objects.
 */
public class SimHunter extends AbstractMovingObject {
    private static final double defaultSpeed = 2.0;
    private int deathTimer = 0;

    public SimHunter(Position pos, Habitat hab) {
        super(new Direction(0), pos, defaultSpeed, hab);

        // anonymous class for event-listener implementation
        class SimListener implements ISimListener {
            @Override
            public void eventHappened(SimEvent event) {
                say(event.getType());
            }
        }
        habitat.addListener(this, new SimListener());
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
            context.drawImage(MediaHelper.getImage("playerShip2_green.png"), 0, 0, getWidth(), getHeight());
            drawBar(context, getHealth(), -1, Color.RED, Color.GREEN);
        }
    }

    @Override
    public double getHeight() {
        return 70;
    }

    @Override
    public double getWidth() {
        return 50;
    }

    /**
     * The step method runs one iteration of the SimHunter's behaviour.
     * Will first try to avoid SimMeteors, then try to find SimGoldStars, then try to hunt SimPreys.
     * The object will turn towards the center of the habitat, unless the distance to the center is less than
     * 900 -> if this is the case it will use the randomPath that has been generated and move elsewhere.
     * (It will also move towards the center if the object is to close to the border of the habitat or outside of it.)
     * Fires SimProjectiles of type 0 -> (behaviour defined in SimProjectile-class).
     * Field of view = 180 degrees
     * If the health reaches zero the object is destroyed.
     */
    @Override
    public void step() {
        IEdibleObject consumableObject = SimObjectHelper.getClosestGoldPickup(this, habitat, 375);
        SimMeteor closestMeteor = SimObjectHelper.getClosestMeteor(this, habitat, 375);
        SimPrey closestPrey = SimObjectHelper.getClosestPrey(this, habitat, 375);
        
        if (closestMeteor != null && dir.angleDifference(directionTo(closestMeteor)) < 90) {
            dir = dir.turnTowards(SimObjectHelper.getAverageMeteorAngle(this, habitat, 375) + 180, 2);
            // accelerate when the the object has turned away from the meteors
            if (dir.angleDifference(new Direction(SimObjectHelper.getAverageMeteorAngle(this, habitat, 375) + 180)) > 120) {
                accelerateTo(defaultSpeed * 1.5, 0.3);
            }
        } else if (consumableObject != null && dir.angleDifference(directionTo(consumableObject)) < 90) {
            dir = dir.turnTowards(directionTo(SimObjectHelper.getBestGoldPickup(this, habitat, 375)), 2);
            accelerateTo(defaultSpeed * 1.5, 0.3);
        } else if (closestPrey != null && dir.angleDifference(directionTo(closestPrey)) < 90) {
            dir = dir.turnTowards(directionTo(closestPrey), 2);
            if (randomGen.nextInt(50) == 0) {
                // fun code for "system failure"
                if (randomGen.nextInt(200) == 0) {
                    if (SimMain.isSoundOn()) SimSounds.getSound(5).play();
                    say("%$@#!");
                    habitat.triggerEvent(new SimEvent(this, "HAHA", null, null), 500);
                } else {
                    habitat.addObject(new SimProjectile(getDirection(), getPosition(), habitat, 275, 0)); 
                }
            }
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
            if (getHealth() < 1.5) {
                increaseHealth();
            }
        } else if (health <= 0) {
            deathTimer++;
            accelerateTo(0,1);
            if (deathTimer == 1) if (SimMain.isSoundOn()) SimSounds.getSound(7).play();
            // lets a "death" animation play briefly before destroying the object
            if (deathTimer > 60) {
                destroy();
                deathTimer = 0;
            }
        }
        
        accelerateTo(defaultSpeed, 0.1);
        super.step();
    }
}
