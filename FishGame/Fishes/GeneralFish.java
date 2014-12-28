package siat.sfu.cs.Assignment3.Fishes;
import java.io.Console;
import java.io.IOException;
import java.util.HashMap;

import javax.print.attribute.standard.MediaSize.Other;

import processing.core.*;
import siat.sfu.cs.Assignment1.Fish;
import siat.sfu.cs.Assignment2.CollisionCircle;
import siat.sfu.cs.Assignment3.Pond;

/*
 * An abstract class that is meant to be derived from, not initialized.  This is 
 * the parent class of other fish classes (Player and Enemy).
 * 
 * @version remade version of "siat.sfu.cs.Assignment1.Fish" using PVector
 * 			and fixing a bug with collision shapes not aligning accurately
 * 			with actual rotation of the image.
 *  
 * @author	Kristofer Ken Castro
 * @date	6/23/2013
 */
public abstract class GeneralFish {
	protected static final int FRAME_MARGIN = Pond.MARGIN;

	PApplet p;
	
	// pixel coordinate location of the center of the fish
	public PVector position;
	
	// speed of the fish (2D x,y only)
	PVector velocity;
	
	// RGB value - will have randomize values
	float red, blue, green;
	
	// Sin/Cos movement variables sin(x)*amp => sin(wave)*amp
	float wave, waveSpeed, amp;
	
	public float scaleFactor;
	
	
	// data structure to hold collisions shapes: ex. key = body part location
	HashMap<String, CollisionCircle> collisionShapes;
	
	// body parts dimensions
	float bodyW, headW, headH, tailW, tailH;

	protected float maxSpeed = 2;
	
	public boolean alive = true;
	
	/**
	 * Constructor 1: position is set by default.
	 * 
	 * @param p PApplet 
	 */
	public GeneralFish(PApplet p){
		this.p = p;

		initializeDefaultBodyDimensions();
		initializeDefaultPosition(null);
		initializeDefaultSpeed(null);
		initializeDefaultColor();
		initializeDefaultSinCosMovement();
		initializeDefaultCollision();
	}
	
	/**
	 * Constructor 2: position and velocity needs to be passed as arguments.
	 * 
	 * @param p PApplet
	 * @param position PVector coordinate of where to spawn
	 * @param velocity PVector velocity of its initialze direction and speed of movement
	 */
	public GeneralFish(PApplet p, PVector position, PVector velocity){
		this.p = p;
		initializeDefaultBodyDimensions();
		initializeDefaultPosition(position);
		initializeDefaultSpeed(velocity);
		initializeDefaultColor();
		initializeDefaultSinCosMovement();
		initializeDefaultCollision();
	}

	public void initializeDefaultBodyDimensions(){
		scaleFactor = p.random(0.15f, 1f);
		// initialize body dimensions
		bodyW = 230;
		headW = 200;
		headH = 100;
		tailW = 80;
		tailH = 100;
	}
	
	public void initializeDefaultPosition(PVector pos){
		position = new PVector();
		
		if (pos != null)
		{
			position.x = pos.x;
			position.y = pos.y;
		}else{
			position.x = p.random( bodyW/2 + tailW -10 , p.width + bodyW/2);
			position.x *=  scaleFactor;
			position.y = p.random(140,  p.height - 110);
		}
	}
	
	public void initializeDefaultSpeed(PVector v){
		velocity = new PVector();
		
		if(v != null){
			velocity.x = v.x;
			velocity.y = v.y;
		}else{
			velocity.x = p.random(1,maxSpeed);
			velocity.y = p.random(-4,maxSpeed);
		}
	}
	
	public void initializeDefaultColor(){
		red = p.random(50,200);
		blue = p.random(0, 100);
		green = p.random(50,120);
	}
	
	public void initializeDefaultSinCosMovement(){
		wave = p.random(p.TWO_PI);
		waveSpeed = p.random(0.1f, 0.5f);
		amp = p.random(0.5f, 1.5f);
	}
	
	public void drawFish(){
		p.pushMatrix();
		p.translate(position.x, position.y);
		
		if ( velocity.x < 0){
			p.rotate(p.PI);
			p.scale(1,-1);
		}
		p.scale(scaleFactor);
		
		drawTail();
		drawFins();
		drawBody();
		drawHead();
		p.popMatrix();
		
		// DEBUG PURPOSES: uncomment to see the collision in action!
		//drawCollisionShapeReal();
	}
	
	public void initializeDefaultCollision(){
		// initialize our HashMap of collision circles
		collisionShapes = new HashMap<String, CollisionCircle>();
		initializeCollisionShapes();
		
		// if we start going from right to left, make sure we sync
		// that with the collision shpaes for the fish
		if (velocity.x < 0){
			rotateCollisionShapes();
		}
	}
	
	public void update(){
		
		wave += waveSpeed;
		position.x += velocity.x;
		position.y += velocity.y + p.sin(wave) * amp;
		
		// make sure we update the collision circles as well
		for ( String key : collisionShapes.keySet()){
			CollisionCircle circle = collisionShapes.get(key);
			circle.centerX += velocity.x;
			circle.centerY += velocity.y + p.sin(wave)*amp;
		}
	}
	
	/**
	 * We check boundary conditions against collision hit boxes/circles.
	 */
	public void checkBoundaryCollisions() {
		CollisionCircle head = collisionShapes.get("head");
		CollisionCircle body = collisionShapes.get("body");
		CollisionCircle tail = collisionShapes.get("tail");

		// right - when entire body is out of the picture (check tail and head depending on direction)
		if ( tail.centerX - tail.radius > p.width-FRAME_MARGIN 
				&& (head.centerX - head.radius) > p.width-FRAME_MARGIN){
			alive = false;
		}
		
		// left - when the entire body is out of the picture (check tail and head depending on direction)
		if ((tail.centerX + tail.radius)< FRAME_MARGIN
				&&  (head.centerX + head.radius) < FRAME_MARGIN){
			alive = false;
		}

		// top
		if (body.centerY - body.radius < FRAME_MARGIN){
			velocity.y *= -1;
			
			// reset back to frame if it goes too far
			position.y = (135)*scaleFactor + FRAME_MARGIN;
			for ( String key : collisionShapes.keySet()){
				CollisionCircle circle = collisionShapes.get(key);
				circle.changeFishY(position.y);
			}		
		}
		
		// bottom
		if (body.centerY + body.radius > p.height-FRAME_MARGIN){
			velocity.y *= -1;
			
			// reset back to frame if it goes too far
			position.y = p.height-FRAME_MARGIN-(95)*scaleFactor;
			// make sure we update the collision circles as well
			for ( String key : collisionShapes.keySet()){
				CollisionCircle circle = collisionShapes.get(key);
				circle.changeFishY(position.y);
			}	
		}
		
	}
	
	public boolean detectCollision(GeneralFish other){
		
		for ( String key : collisionShapes.keySet()){
			
			CollisionCircle thisCircle = collisionShapes.get(key);
			
			for ( String otherKey : other.collisionShapes.keySet()){
				CollisionCircle otherCircle = other.collisionShapes.get(otherKey);
				
				if ( p.dist(thisCircle.getCenterX(), thisCircle.getCenterY(), otherCircle.getCenterX(), otherCircle.getCenterY())
						<= ((thisCircle.radius + otherCircle.radius))){
					
					// old velocities to check if a flip happens (that is, they changed signs)
					boolean isOldVelocityNegative = (velocity.x < 0)? true: false;
					boolean isOldOtherVelocityNegative = (other.velocity.x < 0)? true: false;
					
					// angle of collision
					float angle = p.atan2( thisCircle.getCenterY()-otherCircle.getCenterY() , thisCircle.getCenterX()-otherCircle.getCenterX());
					
					// change velocity of our ball  (Basic triginometry: SOH CAH TOA)
					velocity.x = maxSpeed*p.cos(angle);
					velocity.y = maxSpeed*p.sin(angle);
					
					// now change the velocity of the other ball
					other.velocity.x = maxSpeed*p.cos( angle - p.PI); // minus PI since opposite
					other.velocity.y = maxSpeed*p.sin( angle - p.PI); // minus PI since opposite
					
					/* we only rotate when we change direction from the bounce.
					 this fixes synchronization with actual "flipping" of the
					 fish vs the flipping of collision circles*/
					if(isOldVelocityNegative != velocity.x < 0)
						rotateCollisionShapes();
					if(isOldOtherVelocityNegative != other.velocity.x < 0)
						other.rotateCollisionShapes();
					
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * This function draws the collision shapes in the drawing space for debugging purposes
	 */
	private void drawCollisionShape(){
		p.fill(0);
				// general hit area
		float bodyCollisionSize = bodyW-20;
		p.ellipse(0,-20, bodyCollisionSize, bodyCollisionSize);
		
		float headCollisionSize = headW/2+30;
		p.ellipse(bodyW/2,-20,headCollisionSize, headCollisionSize);
		
		float tailCollisionSize = tailW*1.5f;
		p.ellipse(-bodyW/2, 0, tailCollisionSize, tailCollisionSize);
	}
	
	protected void drawCollisionShapeReal(){
		p.fill(100,0,0,100);
		
		for ( String key : collisionShapes.keySet()){
			CollisionCircle circle = collisionShapes.get(key);
			p.ellipse(circle.getCenterX(), circle.getCenterY(), circle.size, circle.size);
		}
	}
	
	/**
	 * Rotate the collision shape by 180 degrees and then flip vertically to do a proper 180 flip.
	 */
	protected void rotateCollisionShapes() {
		
		float angle = p.PI;
		for ( String key : collisionShapes.keySet()){
			CollisionCircle circle = collisionShapes.get(key);
			
			// do some geometry to rotate the circle's center by pi
			circle.centerX = position.x + (circle.centerX - position.x) * p.cos(angle) - (circle.centerY - position.y) * p.sin(angle); 
			circle.centerY = position.y + (circle.centerX - position.x) * p.sin(angle) + (circle.centerY - position.y) * p.cos(angle);
		
			// flip vertically, first bring y coordinate back to origin, then flip (-1), then translate back
			circle.centerY = (circle.centerY - position.y)*-1  + position.y ;	
		}	
	}

	protected void initializeCollisionShapes() {
		float offsetX = (-bodyW/2)*scaleFactor;
		float offsetY = 0;
		float tailCollisionSize = (tailW*1.5f)*scaleFactor;
		collisionShapes.put("tail",  new CollisionCircle(position.x, position.y, offsetX, offsetY, tailCollisionSize));
		
		offsetX = 0;
		offsetY = (-20)*scaleFactor;
		float bodyCollisionSize = (bodyW-20)*scaleFactor;
		collisionShapes.put("body", new CollisionCircle(position.x, position.y, offsetX, offsetY, bodyCollisionSize));
		
		offsetX = (bodyW/2)*scaleFactor;
		offsetY = (-20)*scaleFactor;
		float headCollisionSize = (headW/2 + 10)*scaleFactor;
		collisionShapes.put("head", new CollisionCircle(position.x, position.y, offsetX, offsetY, headCollisionSize));	
		
	}
	
	private void drawBody(){
		p.fill(red,green,blue);
		p.stroke(0);
		float bodyX1 = -bodyW/2;
		int bodyY1 = 0;
		float bodyX2 = bodyW/2;
		int bodyY2 = bodyY1;
		
		float ctrlptX1 = bodyX1;
		int ctrlptY1 = 500;
		float ctrlptX2 = bodyX2;
		int ctrlptY2 = 500;
		p.curve(ctrlptX1,ctrlptY1+400, bodyX1, bodyY1, bodyX2, bodyY2, ctrlptX2 ,ctrlptY2+400);
		p.curve(ctrlptX1,-ctrlptY1, bodyX1, bodyY1, bodyX2, bodyY2, ctrlptX2 ,-ctrlptY2);
		
		// scale decorations
		// triangle scales
		int shiftY = -10;
		int shiftX = 30;
		for(int k = 0; k < 3; k++){
			p.fill(200,125,125,125);
			p.noStroke();
			p.pushMatrix();
			p.translate(-bodyW/2 + shiftX,shiftY-15);
			shiftX += 15;
			shiftY += 25;
			p.triangle(0,0,0,30,20,15);
			
			for(int i = 0; i < 5 - k; i++){
				p.translate(40,0);
				p.pushMatrix();
				p.rotate((float)Math.toRadians(30+i*20));
				p.triangle(0,0,0,30,20,15);
				p.popMatrix();
			}
			p.popMatrix();
			
		}
		shiftY = -20;
		shiftX = 30;
		for(int k = 0; k < 3; k++){
			p.fill(200,125,125,125);
			p.noStroke();
			p.pushMatrix();
			p.translate(-bodyW/2 + shiftX,shiftY);
			shiftX += 15;
			shiftY -= 25;
			p.triangle(0,0,0,30,20,15);
			
			for(int i = 0; i < 5 - k; i++){
				p.translate(40,0);
				p.pushMatrix();
				p.rotate((float)Math.toRadians(30+i*20));
				p.triangle(0,0,0,30,20,15);
				p.popMatrix();
			}
			p.popMatrix();
			
		}
		p.noFill();
	}
	
	private void drawHead(){
		p.fill(red,green,blue);
		p.stroke(0);
		float headX = bodyW/2-headW/4;
		int headY = -20;		
		p.arc(headX,headY,headW,headH,3*p.PI/2, 2*p.PI);
		p.arc(headX,headY,headW,headH,0, p.PI/2);
		
		// draw eyes
		float eyesX = headX + headW/4;
		int eyesY = headY - 25;
		final int eyeSize = 50;
		p.fill(255);
		p.ellipse(eyesX,eyesY, eyeSize, eyeSize);
		
		// pupils
		p.fill(0);
		final int pupilSize = 15;
		p.ellipse(eyesX,eyesY, pupilSize, pupilSize);
		
		// some head decoration
		float gillsX = headX + 10;
		int gillsY = headY;

		p.noFill();
		p.stroke(0);
		p.strokeWeight(2);

		int size = 70;
		// gill 1
		p.arc(gillsX,gillsY, size/2, size/2, 3*p.PI/2 + 1*p.PI/4, 2*p.PI);
		p.arc(gillsX,gillsY, size/2, size/2, 0, p.PI/2);
		
		// gill 2
		p.arc(gillsX+10,gillsY+10, size/2, size/2, 3*p.PI/2 + 2*p.PI/4, 2*p.PI);
		p.arc(gillsX+10,gillsY+10, size/2, size/2, 0, p.PI/2);
		p.noStroke();
	}
	
	private void drawTail(){
		p.fill(red,green,blue);
		p.stroke(0);
		
		float tailX = -bodyW/2 + 30;
		int tailY = 0;
		
		p.triangle(tailX, tailY, tailX-tailW, tailY + tailH/2, tailX-tailW, tailY - tailH/2);
		
		// tail decorations
		p.noFill();
		p.stroke(0);
		p.fill(red,green,blue);
		p.curve(tailX, tailY, tailX, tailY, tailX-tailW, tailY-tailH/2, tailX+100, tailY+150);
		p.fill(0);
		p.curve(tailX, tailY, tailX, tailY, tailX-tailW, tailY-tailH/2+10, tailX+100, tailY+150);
		p.fill(red,green,blue);
		p.curve(tailX, tailY, tailX, tailY, tailX-tailW, tailY-tailH/2+20, tailX+100, tailY+150);
		p.fill(0);
		p.curve(tailX, tailY, tailX, tailY, tailX-tailW, tailY-tailH/2+30, tailX+100, tailY+150);
		p.fill(red,green,blue);
		p.curve(tailX, tailY, tailX, tailY, tailX-tailW, tailY-tailH/2+40, tailX+100, tailY+150);
		p.fill(0);
		p.curve(tailX, tailY, tailX, tailY, tailX-tailW, tailY-tailH/2+50, tailX+100, tailY+150);
		p.fill(red,green,blue);
		p.curve(tailX, tailY, tailX, tailY, tailX-tailW, tailY-tailH/2+60, tailX+100, tailY+150);
		p.fill(0);
		p.curve(tailX, tailY, tailX, tailY, tailX-tailW, tailY-tailH/2+70, tailX+100, tailY+150);
		p.fill(red,green,blue);
		p.curve(tailX, tailY, tailX, tailY, tailX-tailW, tailY-tailH/2+80, tailX+100, tailY+150);
		p.fill(0);
		p.curve(tailX, tailY, tailX, tailY, tailX-tailW, tailY-tailH/2+90, tailX+100, tailY+150);
		p.fill(red,green,blue);
		p.curve(tailX, tailY, tailX, tailY, tailX-tailW, tailY-tailH/2+100, tailX+100, tailY+150);
		p.fill(0);
	}
	
	private void drawFins(){
		int ctrlptX1 = 60;
		int ctrlptY1 = 500;
		int ctrlptX2 = 0;
		int ctrlptY2 = 500;
		int finWidth = 40;
		int finX = -58;
		int finY = 40;
		p.stroke(0);
		p.fill(red,green,blue);
		p.curve(ctrlptX1,ctrlptY1-800, finX, finY, finX+finWidth+5, 60, ctrlptX2 ,ctrlptY2-500);
		finX = finX + 80;
		finY = finY - 10;
		finWidth = 100;
		p.curve(ctrlptX1+300,ctrlptY1-1200, finX+15, finY, finX+finWidth-30, finY-10, ctrlptX2 ,ctrlptY2-800);
				
		// fin decorations
		p.noFill();
		p.stroke(0);
		// bottom closest to head fin
		p.line(finX+10, finY, finX+10, finY+58);
		p.line(finX+10, finY, finX+20, finY+58);
		p.line(finX+10, finY, finX+30, finY+55);
		p.line(finX+10, finY, finX+40, finY+50);
		p.line(finX+10, finY, finX+50, finY+40);
		p.line(finX+10, finY, finX+60, finY+28);
		p.line(finX+10, finY, finX+69, finY+10);
		
		// bottom closest to tail fin
		finX = finX - 50;
		p.line(finX, finY, finX, finY+40);
		p.line(finX, finY, finX-10, finY+43);
		p.line(finX, finY, finX-20, finY+45);
		p.line(finX, finY, finX-30, finY+40);
		
		// head fin
		p.fill(0);
		finWidth = 150;
		int finX1 = -80;
		int finY1 = -80;
		int finX2 = finX1+finWidth+20;
		int finY2 = -50;
		ctrlptX1 = finX1 + finWidth/2;
		ctrlptY1 = finY1 + 500;
		ctrlptX2 = ctrlptX1;
		ctrlptY2 = ctrlptY1;
		p.curve(ctrlptX1, ctrlptY1, finX1, finY1, finX2, finY2, ctrlptX2, ctrlptY2);
		p.noStroke();		
	}
	
	public void stopMoving(){
		this.velocity.x = 0;
		this.amp = 0;
		this.waveSpeed = 0;
		this.wave = 0;
		this.velocity.y = 0;
	}

}
