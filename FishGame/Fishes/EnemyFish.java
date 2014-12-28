package siat.sfu.cs.Assignment3.Fishes;

import processing.core.PApplet;
import processing.core.PVector;
import siat.sfu.cs.Assignment2.CollisionCircle;

/*
 * Sub-class of GeneralFish, this fish is the CPU opponent of the Player.
 * All enemy fish shares common visual properties (i.e. color).  And may
 * have special effects.
 * 
 * @author	Kristofer Ken Castro
 * @date	6/23/2013
 */
public class EnemyFish extends GeneralFish{

	public EnemyFish(PApplet p) {
		super(p);
		setupFishSizeDistribution();
	}
	
	public EnemyFish(PApplet p, PVector position, PVector velocity){
		super(p, position, velocity);
		setupFishSizeDistribution();
	}
	
	/**
	 * Returns boolean if this enemy fish has collided with the player fish
	 * @param playerFish the player fish that collided the enemy fish
	 * @return boolean if the enemy head has collided with the player fish
	 */
	public boolean detectHeadOnCollision(PlayerFish playerFish){
		if ( !playerFish.isCollidable() ) 
			return false;
		
		CollisionCircle enemyHead = collisionShapes.get("head");
		
		for( CollisionCircle playerBodyPart : playerFish.collisionShapes.values() ){
			if(p.dist(enemyHead.getCenterX(), enemyHead.getCenterY(), playerBodyPart.getCenterX(), playerBodyPart.getCenterY())
					< enemyHead.radius + playerBodyPart.radius)
				return true;
		}
		return false;
	}
	
	/**
	 * Distribution of fishes in the pool.  We have more smaller fishes than bigger fishes.
	 * 80% between 5-20% original size
	 * 10% between 20%-80% original size
	 * 10% between 90%-100% original size
	 */
	public void setupFishSizeDistribution(){
		float region = p.random(100);
		if (region <= 80){
			scaleFactor = p.random(0.05f, 0.20f);
		}else if (region > 80 && region <= 90){
			scaleFactor = p.random(0.20f, 0.80f);
		}else{
			scaleFactor = p.random(0.9f, 1f);
		}
		
		// when we change scale factor we must call this again.
		super.initializeDefaultCollision();
	}

}
