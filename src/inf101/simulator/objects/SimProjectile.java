package inf101.simulator.objects;

import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

/**
 * Created by Olav Gjerde on 06/05/2017.
 * 
 * Class to represent a projectile, constructed with a "target" direction, start "pos", habitat and a range
 * The object will terminate when its range has been reached or if it hits an object.
 */
public class SimProjectile extends AbstractMovingObject implements ISimObject {

    private static final double defaultSpeed = 3;
    private Habitat habitat;
    private int counter = 0, range;

    public SimProjectile (Direction targetDirection, Position startPos, Habitat hab, int range) {
        super(targetDirection, startPos, defaultSpeed);
        this.habitat = hab;
        this.range = range;
    }
    
    @Override
    public void draw(GraphicsContext context) {
        super.draw(context);
        context.setFill(Color.GREEN);
        context.fillOval(0, 0, getWidth(), getHeight());
    }

    @Override
    public boolean shootable() {
        return false;
    }

    @Override
    public double getHeight() {
        return 10;
    }

    @Override
    public double getWidth() {
        return 10;
    }
    
    @Override
    public void step() {
        List<ISimObject> targetList = habitat.nearbyObjects(this, getWidth()*2);
        ISimObject target = null;
        for (ISimObject x : targetList) {
            if (x.shootable()) {
                target = x;
            }
        }
        
        if (counter < range) {
            counter++;
            super.step();
            if (!targetList.isEmpty() && target != null) {
                if (distanceToTouch(target) < 1){
                    destroy();
                }
            }
        } else {
            destroy();
        }
    }
}
