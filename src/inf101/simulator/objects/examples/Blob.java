package inf101.simulator.objects.examples;

import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractMovingObject;
import javafx.scene.canvas.GraphicsContext;
import java.util.Random;

public class Blob extends AbstractMovingObject {
	private Random r = new Random();

	public Blob(Direction dir, Position pos, double speed, Habitat hab) {
		super(dir, pos, speed, hab);
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
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
		dir = dir.turn(r.nextDouble()-0.2);
		
		super.step();
	}
}
