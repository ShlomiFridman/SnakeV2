package application;

import javafx.scene.input.KeyCode;

public enum Direction {
	
	LEFT(KeyCode.LEFT,KeyCode.A,0),UP(KeyCode.UP,KeyCode.W,1),RIGHT(KeyCode.RIGHT,KeyCode.D,2),DOWN(KeyCode.DOWN,KeyCode.S,3);
	
	private final KeyCode arrow,letter;
	public final int index;
	
	private Direction(KeyCode arrow, KeyCode letter, int index) {
		this.arrow = arrow;
		this.letter = letter;
		this.index = index;
	}
	
	public boolean isOpposite(Direction other) {
		return Math.abs(this.arrow.ordinal() - other.arrow.ordinal()) == 2;
	}
	
	@Override
	public String toString() {
		return String.format("Direction: '%s' | Arror: '%s' | Direction: '%s'",this.name() ,this.arrow, this.letter);
	}
	
	public static Direction getDirection(KeyCode key) {
		for (Direction dir : Direction.values())
			if (key.equals(dir.arrow) || key.equals(dir.letter))
				return dir;
		return null;
	}

}
