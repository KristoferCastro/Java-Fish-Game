package siat.sfu.cs.Assignment3.Fishes;

import java.util.Timer;
import java.util.TimerTask;

import processing.core.*;
import siat.sfu.cs.Assignment2.CollisionCircle;

/*
 * A sub-class that extends the GeneralFish and implements the movable
 * interface.
 *
 * @author	Kristofer Ken Castro
 * @date	6/23/2013
 */
public class PlayerFish extends GeneralFish implements IMovable{
	
	public int score;
	public int lives;
	private boolean collidable, visible;
	
	public PlayerFish(PApplet p) {
		super(p);
		maxSpeed = 4;
		velocity.x = 4;
		velocity.y = 4;
		scaleFactor = 0.20f;
		// default position is the center
		position.x = p.width/2;
		position.y = p.height/2;
		
		score = 0;
		lives = 5;
		collidable = true;
		visible = true;
		initializeDefaultCollision();
	}

	@Override
	public void drawFish(){
		
		// needed for blinking effect after respawning
		if ( !visible ) 
			return;
		
		p.pushMatrix();
		
		p.translate(position.x, position.y);
		if ( velocity.x < 0){
			p.rotate(p.PI);
			p.scale(1,-1);
		}
		
		p.scale(scaleFactor);
		drawFins();
		drawTail();
		drawBody();
		drawSpikes();
		drawHead();
		p.popMatrix();
		
		// DEBUG PURPOSES: uncomment to see the collision in action!
		//drawCollisionShapeReal();
	}
	
	public void updateCollisionCircles(PVector byPosition){
		// make sure we update the collision circles as well
		for ( String key : collisionShapes.keySet()){
			CollisionCircle circle = collisionShapes.get(key);
			circle.centerX += byPosition.x;
			circle.centerY += byPosition.y;
		}
	}
	
	private void drawBody(){
		p.stroke(0);
		p.fill(red+50,green+50,blue+50);
		p.ellipse(0, 0, bodyW, bodyW);
		p.noFill();
		p.noStroke();
	}
	
	private void drawHead(){
		p.fill(100,100,100);
		
		p.fill(255);
		p.stroke(0);
		PVector offset = new PVector(60,-15);
		PVector eye1 = new PVector(0, offset.y);
		PVector eye2 = new PVector(0, offset.y);
		int eyeSize = 50;
		p.ellipse(eye2.x+eyeSize/2 + offset.x, eye2.y, eyeSize, eyeSize);
		p.ellipse(eye1.x + offset.x, eye1.y, eyeSize, eyeSize);
		p.fill(0);
		p.ellipse(eye2.x + eyeSize/2 + offset.x + 13, eye2.y, eyeSize/3, eyeSize/3);
		p.ellipse(eye1.x + offset.x + 10, eye1.y, eyeSize/3, eyeSize/3);
		p.noStroke();
		p.noFill();
		
		drawMouth();
	}

	private void drawSpikes(){
		drawASpike(new PVector(-30,bodyW/2 - 20),0);
		drawASpike(new PVector(-12,bodyW/2 - 20),p.PI/3);
		drawASpike(new PVector(-25,bodyW/2 -10),-p.PI/8);
		drawASpike(new PVector(-40,bodyW/2 - 20),-p.PI/3);
		drawASpike(new PVector(-25,bodyW/2 -50),-p.PI/8 - p.PI/15);
		drawASpike(new PVector(-12,bodyW/2 - 50),p.PI/3 - p.PI/7);
		drawASpike(new PVector(-12,bodyW/2 - 20),p.PI/2);
	}
	
	private void drawASpike(PVector position, float angle){
		p.pushMatrix();
		p.fill(red, green, blue, 155);
		p.rotate(angle);
		PVector p1 = new PVector(0 + position.x,0 + position.y);
		PVector p2 = new PVector(20 + position.x, 0 + position.y);
		PVector p3 = new PVector(10 + position.x, 30 + position.y);
		p.triangle(p1.x,p1.y , p2.x, p2.y, p3.x, p3.y);
		p.fill(0);
		p.curve(p3.x, p3.y, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
		p.noFill();
		p.popMatrix();
	}
	
	private void drawFins(){
		p.fill(red-70,green-70, blue-100);
		PVector p1 = new PVector(-bodyW/2 + 30, -50);
		PVector p2 = new PVector(bodyW/2 - 30, -50);
		PVector ctrlP1 = new PVector(bodyW/2+30, 600);
		PVector ctrlP2 = new PVector(bodyW/2, 400);
		p.curve(ctrlP1.x, ctrlP1.y, p1.x, p1.y, p2.x, p2.y, ctrlP2.x, ctrlP2.y);
		
		p.noFill();
	}
	
	private void drawMouth(){
		p.stroke(0);
		PVector mouthPos = new PVector(bodyW/2 - 10, 40);
		p.fill(255, 200, 200);
		p.ellipse(mouthPos.x-2, mouthPos.y-10, 40, 35);
		p.ellipse(mouthPos.x-2, mouthPos.y+10, 40, 35);
		p.fill(0);
		p.ellipse(mouthPos.x, mouthPos.y, 37, 40);
		p.noStroke();
		p.noFill();
	}
	
	private void drawTail(){
		p.stroke(0);
		PVector offsetX = new PVector(60,0);
		PVector tailPos = new PVector(-bodyW/2, 0);
		
		// move the tail a lil bit inside the body
		tailPos.add(offsetX);
		p.fill(red,green,blue);
		p.curve(tailPos.x, tailPos.y, tailPos.x, tailPos.y, tailPos.x-tailW, tailPos.y-tailH/2, tailPos.x+100, tailPos.y+150);
		p.fill(0);
		p.curve(tailPos.x, tailPos.y, tailPos.x, tailPos.y, tailPos.x-tailW, tailPos.y-tailH/2+10, tailPos.x+100, tailPos.y+150);
		p.fill(red,green,blue);
		p.curve(tailPos.x, tailPos.y, tailPos.x, tailPos.y, tailPos.x-tailW, tailPos.y-tailH/2+20, tailPos.x+100, tailPos.y+150);
		p.fill(0);
		p.curve(tailPos.x, tailPos.y, tailPos.x, tailPos.y, tailPos.x-tailW, tailPos.y-tailH/2+30, tailPos.x+100, tailPos.y+150);
		p.fill(red,green,blue);
		p.curve(tailPos.x, tailPos.y, tailPos.x, tailPos.y, tailPos.x-tailW, tailPos.y-tailH/2+40, tailPos.x+100, tailPos.y+150);
		p.fill(0);
		p.curve(tailPos.x, tailPos.y, tailPos.x, tailPos.y, tailPos.x-tailW, tailPos.y-tailH/2+50, tailPos.x+100, tailPos.y+150);
		p.fill(red,green,blue);
		p.curve(tailPos.x, tailPos.y, tailPos.x, tailPos.y, tailPos.x-tailW, tailPos.y-tailH/2+60, tailPos.x+100, tailPos.y+150);
		p.fill(0);
		p.curve(tailPos.x, tailPos.y, tailPos.x, tailPos.y, tailPos.x-tailW, tailPos.y-tailH/2+70, tailPos.x+100, tailPos.y+150);
		p.fill(red,green,blue);
		p.curve(tailPos.x, tailPos.y, tailPos.x, tailPos.y, tailPos.x-tailW, tailPos.y-tailH/2+80, tailPos.x+100, tailPos.y+150);
		p.fill(0);
		p.curve(tailPos.x, tailPos.y, tailPos.x, tailPos.y, tailPos.x-tailW, tailPos.y-tailH/2+90, tailPos.x+100, tailPos.y+150);
		p.fill(red,green,blue);
		p.curve(tailPos.x, tailPos.y, tailPos.x, tailPos.y, tailPos.x-tailW, tailPos.y-tailH/2+100, tailPos.x+100, tailPos.y+150);
		p.noFill();
		p.noStroke();
	}

	@Override
	protected void initializeCollisionShapes(){
		collisionShapes.put("body", new CollisionCircle(position.x, position.y, 0, 0, (bodyW+30)*scaleFactor));
		collisionShapes.put("mouth",  new CollisionCircle(position.x, position.y, (bodyW/2-15)*scaleFactor, 40*scaleFactor, 80*scaleFactor));
	}
	
	private void drawCollisionShapes(){
		p.fill(255,50,50, 50);
		p.stroke(255,0,0,100);
		p.ellipse(0, 0, bodyW+30, bodyW+30);
		// mouth hit box
		p.ellipse(bodyW/2-15, 40, 60, 60);
		p.noStroke();
		p.noFill();
	}
	
	@Override
	public boolean detectCollision(GeneralFish enemyFish){
		super.detectCollision(enemyFish);
		
		float signX = this.velocity.x;
		float signY = this.velocity.y;
		
		// make sure we don't inherit the effects of changed velocity after getting hit
		this.velocity.x = (signX < 0)? -maxSpeed : maxSpeed;
		this.velocity.y = (signY < 0)? -maxSpeed : maxSpeed;
		
		return false;
	}
	
	/**
	 * Check if the player fish has collided with the head of an enemy fish.
	 * 
	 * @param enemyFish fish that the player is colliding with
	 * @return if the player collided with the enemy fish's head
	 */
	public boolean detectHeadOnCollision(EnemyFish enemyFish){
		CollisionCircle playerMouth = collisionShapes.get("mouth");
		CollisionCircle enemyHead = enemyFish.collisionShapes.get("head");
		
		for (CollisionCircle collisionShape : enemyFish.collisionShapes.values()){
			if(p.dist(playerMouth.getCenterX(), playerMouth.getCenterY(), collisionShape.getCenterX(), collisionShape.getCenterY())
					<= playerMouth.radius + enemyHead.radius){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void checkBoundaryCollisions() {
		CollisionCircle body = collisionShapes.get("body");
		CollisionCircle mouth = collisionShapes.get("mouth");
		// right wall
		if (body.centerX + body.radius > p.width - FRAME_MARGIN){
			velocity.x *= -1;
			
			// reset back to frame if way past the frame
			position.x = p.width - FRAME_MARGIN - body.radius;
			
			for ( String key : collisionShapes.keySet()){
				CollisionCircle circle = collisionShapes.get(key);
				circle.changeFishX(position.x);
			}	
			rotateCollisionShapes();
		}
		// left wall
		else if (body.centerX - body.radius < FRAME_MARGIN){
			velocity.x *= -1;
			position.x = FRAME_MARGIN + body.radius;
			for ( String key : collisionShapes.keySet()){
				CollisionCircle circle = collisionShapes.get(key);
				circle.changeFishX(position.x);
			}	
			//rotateCollisionShapes();
		}
		// bottom wall
		else if (mouth.centerY + mouth.radius >= p.height - FRAME_MARGIN){
			velocity.y *= -1;
			position.y = p.height - FRAME_MARGIN - body.radius;
			for ( String key : collisionShapes.keySet()){
				CollisionCircle circle = collisionShapes.get(key);
				circle.changeFishY(position.y);
			}	
		}
		
		// top wall
		else if(mouth.centerY - mouth.radius <= FRAME_MARGIN){
			velocity.y *= -1;
			position.y = FRAME_MARGIN + body.radius;
			for ( String key : collisionShapes.keySet()){
				CollisionCircle circle = collisionShapes.get(key);
				circle.changeFishY(position.y);
			}	
		}
	}

	/**
	 * Eat the enemy fish that is passed in and update player fields.
	 * @param enemyFish the fish to eat
	 */
	public void eat(EnemyFish enemyFish) {
		enemyFish.alive = false;
		
		updateSizeAfterEating(enemyFish.scaleFactor/this.scaleFactor);
		updateScore(enemyFish.scaleFactor/this.scaleFactor);
	}
	
	/**
	 * A fish is only eatable when the player is bigger than the fish.
	 * 
	 * @param enemyFish enemy fish to check if eatable
	 * @return eatable or not
	 */
	public boolean isFishEatable(EnemyFish enemyFish){
		if(this.scaleFactor > enemyFish.scaleFactor){
			return true;
		}
		return false;
	}
	
	/**
	 * You grow bigger eating bigger fishes
	 * @param relativeSize
	 */
	private void updateSizeAfterEating(float relativeSize){
		float growth = 1;
		if (relativeSize >= 0.90) 
			growth = 1.1f; // grow by 10%
		else if (relativeSize >= 0.50 && relativeSize < 0.90)
			growth = 1.05f; // grow by 5%
		else
			growth = 1.025f; // grow by 2.5%
		
		updateCollisionShapeScale(growth);
		scaleFactor *= growth;
	}

	/**
	 * Update the score based on size difference between enemy and player fish.
	 * @param relativeSize (enemy fish size / player fish size)
	 */
	private void updateScore(float relativeSize){
		if (relativeSize >= 0.90) 
			score += 40;
		else if (relativeSize >= 0.50 && relativeSize < 0.90)
			score += 20;
		else
			score += 10;
	}
	
	/**
	 * We need this function to update the collision shape scale factors
	 * when we change the scalef actor of the fish's actual graphics. 
	 * @param scaleFactor
	 */
	public void updateCollisionShapeScale(float scaleFactor){
		for(CollisionCircle shape : collisionShapes.values()){
			shape.offsetX *= scaleFactor;
			shape.offsetY *= scaleFactor;
			shape.radius *= scaleFactor;
			shape.size *= scaleFactor;
			shape.changeFishX(position.x);
			shape.changeFishY(position.y);
		}
		if (velocity.x < 0){
			rotateCollisionShapes();
		}
	}
	
	public boolean isCollidable(){
		return collidable;
	}

	public boolean hasWon(){
		return score >= 500;
	}
	
	public boolean hasLost(){
		return lives == 0;
	}
	
	/**
	 * Relocate the fish to the center of the screen.
	 */
	public void respawn(){
		// default position is the center
		position.x = p.width/2;
		position.y = p.height/2;
		initializeDefaultCollision();
		
		// Make the fish invincible for 2 seconds and do a blinking effect
		// while invincible
		collidable = false;
		final Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				if ( visible )
					visible = false;
				else
					visible = true;		
			}		
		},0,200);
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				collidable = true;	
				visible = true;
				timer.cancel();
			}		
		}, 2000);
	}
	
	@Override
	public void moveUp() {
		if(velocity.y > 0){
			velocity.y *= -1;
		}
		position.y += velocity.y;
		updateCollisionCircles(new PVector(0, velocity.y));
	}

	@Override
	public void moveDown() {
		if(velocity.y < 0){
			velocity.y *= -1;
		}
		position.y += velocity.y;
		updateCollisionCircles(new PVector(0, velocity.y));
	}

	@Override
	public void moveLeft() {
		if ( velocity.x > 0){
			velocity.x *= -1;
			rotateCollisionShapes();
		}
		position.x += velocity.x;
		updateCollisionCircles(new PVector(velocity.x, 0));
	}

	@Override
	public void moveRight() {
		if ( velocity.x < 0){
			velocity.x *= -1;
			rotateCollisionShapes();
		}
		position.x += velocity.x;
		updateCollisionCircles(new PVector(velocity.x, 0));
	}

	
	public void loseLife() {
		this.lives -= 1;
		respawn();
	}
	
	public void reset(){
		scaleFactor = 0.2f;
		respawn();
		lives = 5;
		score = 0;
	}
}
