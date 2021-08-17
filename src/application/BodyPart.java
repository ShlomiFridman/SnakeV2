package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;

public class BodyPart {
	/**
	 * Body part coordinates (x,y)
	 */
	private double x,y;
	/**
	 * Body part width and height
	 */
	private double width, height;
	/**
	 * Body part paint, used in draw fill
	 */
	private Paint paint;

	/**
	 * base constructor, used to create apple shape
	 * @param x - x coordinate value
	 * @param y - Y coordinate value
	 * @param width - Body part width value
	 * @param height - Body part height value
	 * @param paint - Body part paint, used in graphics fill
	 */
	public BodyPart(double x, double y, double width, double height, Paint paint) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.paint = paint;
	}

	/**
	 * constructor used to create snake's body parts
	 * @param x - x coordinate value
	 * @param y - Y coordinate value
	 * @param radius - Body's width and height values
	 * @param paint - Body part paint, used in graphics fill
	 */
	public BodyPart(double x, double y, double radius, Paint paint) {
		this(x, y, radius, radius, paint);
	}

	/**
	 * clone constructor
	 * @param other - the BodyPart to clone
	 */
	public BodyPart(BodyPart other) {
		this(other.x, other.y, other.width, other.height, other.paint);
	}

	public boolean addOffset(double x, double y) {
		this.x += x;
		this.y += y;
		return true;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public boolean drawArc(GraphicsContext gc) {
		gc.setFill(this.paint);
		gc.fillArc(x, y, width, height, 0, 360, ArcType.ROUND);
		gc.fill();
		return true;
	}

	public boolean isOverlapping(BodyPart other, double radius) {
		return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2)) <= radius;
	}
}