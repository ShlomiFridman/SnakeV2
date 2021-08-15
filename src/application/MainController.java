package application;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;

public class MainController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Canvas canvas;
    
    private Scene scene;
    
    // graphic and color properties
    private GraphicsContext gc;
    /**
     * snake's body colors
     * { GREEN , DARKGREEN , FORESTGREEN }
     */
    private Queue<Paint> bodyColors;
    /**
     * apple color
     * LinearGradient { RED , FORESTGREEN }
     */
    private LinearGradient appleColor;
    
    /**
     * snake's body
     */
    private LinkedList<BodyPart> body;
    /**
     * snake's body size
     */
    private int bodySize;
    /**
     * apple
     */
    private BodyPart apple;
    /**
     * move direction
     */
    private Direction dir;
    /**
     * move event timeline, infinite loop
     */
    private Timeline timeline;
    /**
     * game speed, 30ms
     */
    private int speed;
    
    public void initialize() {
    	// initialize graphics
    	this.gc = canvas.getGraphicsContext2D();
    	// initial speed value
    	this.speed = 30;
    	
    	// initialize color variables
    	this.bodyColors = new LinkedList<Paint>();
    	while (this.bodyColors.size() < 10)
    		this.bodyColors.add(Color.GREEN);
    	while (this.bodyColors.size() < 20)
    		this.bodyColors.add(Color.DARKGREEN);
    	while (this.bodyColors.size() < 30)
    		this.bodyColors.add(Color.FORESTGREEN);
    	Stop[] stop = {new Stop(0,Color.RED), new Stop(1,Color.FORESTGREEN)};
    	this.appleColor = new LinearGradient(0,1,0,0,true,CycleMethod.REFLECT,stop);
    	
    	// initialize body and apple
    	this.body = new LinkedList<BodyPart>();
    	this.bodySize = 5;
    	this.body.add(new BodyPart(220,220,30,this.bodyColors.peek()));
    	this.apple = this.generateApple();
    	
    	// Initial draw
    	draw();
    	
    	// initialize timeline
		this.timeline = new Timeline();
		this.timeline.setCycleCount(Timeline.INDEFINITE);
		this.timeline.getKeyFrames().add(new KeyFrame(Duration.millis(this.speed), (event)->move()));
		this.timeline.playFromStart();
    }
    
    // set the scene, and the onKeyPressed event
    public void setScene(Scene scene) {
    	this.scene = scene;
    	this.scene.setOnKeyPressed(ev->this.onKeyPressed(ev));
    }
    
    // draw the snake's body and the apple
    private void draw() {
    	this.gc.clearRect(0, 0, this.anchorPane.getWidth(), this.anchorPane.getHeight());
    	this.apple.drawArc(this.gc);
    	Iterator<BodyPart> it = body.iterator();
    	BodyPart bp;
    	while (it.hasNext()) {
    		bp = it.next();
    		bp.drawArc(this.gc);
    	}
    }
    
    // generate new apple in range of 10-490
    private BodyPart generateApple() {
    	double x = Math.floor(Math.random() * 480) + 10;
    	double y = Math.floor(Math.random() * 480) + 10;
    	return new BodyPart(x,y,19,20,this.appleColor);
    }
    
    // check if the snake has eaten itself, check after the 10th body part
    private boolean hasEatenSelf() {
    	BodyPart head = this.body.peekFirst();
    	Iterator<BodyPart> it = this.body.iterator();
    	int index = 0;
    	// get rid of the first 10 parts
    	while (it.hasNext() && index++ < 16)
    		it.next();
    	// start the check
    	while (it.hasNext())
    		if (head.isOverlapping(it.next(),10))
    			return true;
    	return false;
    }
    
    // add a new head to the body, and check if eatenSelf, or eaten the apple
    // reDraw at the end
    private void move() {
    	// no direction, do nothing
    	if (dir == null)
    		return;
    	// initialize new head
    	BodyPart newHead = new BodyPart(this.body.peekFirst());
    	// set the newHead color, advance the color queue
    	Paint color = this.bodyColors.remove();
    	newHead.setPaint(color);
    	this.bodyColors.add(color);
    	// set the offset to add
    	int offset = 5;
    	// add the offset by the direction
    	switch (this.dir.index) {
    		// left
	    	case (0):
	    		newHead.addOffset(-offset, 0);
	    		if (newHead.getX() < -10)
	    			newHead.setX(500);
	    		break;
	    	// up
	    	case (1):
	    		newHead.addOffset(0, -offset);
	    		if (newHead.getY() < -10)
	    			newHead.setY(500);
	    		break;
	    	// right
	    	case (2):
	    		newHead.addOffset(offset, 0);
	    		if (newHead.getX() > 500)
	    			newHead.setX(-10);
	    		break;
	    	// down
	    	case (3):
	    		newHead.addOffset(0, offset);
	    		if (newHead.getY() > 500)
	    			newHead.setY(-10);
	    		break;
    	}
    	// add the new head
    	this.body.addFirst(newHead);
    	// check if the snake eaten itself
    	if (hasEatenSelf()) {
    		// set eaten self color (orange)
    		Iterator<BodyPart> it = this.body.iterator();
    		while (it.hasNext())
    			it.next().setPaint(Color.ORANGERED);
    		// cut the body size
    		this.bodySize = 5;
    	}
    	// else if has eaten the apple
    	else if (newHead.isOverlapping(this.apple,18)) {
    		// set eaten apple color (lightgreen)
    		newHead.setPaint(Color.YELLOWGREEN);
    		// get new apple
    		this.apple = generateApple();
    		// Increase body size by 3
    		this.bodySize += 3;
    	}
    	
    	// resize the body
    	while (this.body.size() > this.bodySize)
    		this.body.removeLast();
    	
    	// reDraw the canvas
    	draw();
    }

    // get the new direction on key pressed
    @FXML
    void onKeyPressed(KeyEvent event) {
    	// get the direction from the key typed
    	Direction key = Direction.getDirection(event.getCode());
    	// if the key isn't valid, the same key as dir, or is opposite to current direction, do nothing
    	if (key == null ||	// invalid key
    			key == this.dir ||	// same key
    			(this.dir != null && this.dir.isOpposite(key)))	// opposite key
    		return;
    	// set new direction
    	this.dir = key;
    }
}
