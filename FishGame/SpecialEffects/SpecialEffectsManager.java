package siat.sfu.cs.Assignment3.SpecialEffects;
import java.util.ArrayList;
import java.util.ListIterator;

import processing.core.PApplet;
import processing.core.PVector;

/*
 * Singleton Class to handle special effects.  Use this class to generate
 * and display special effects such as explosions (ExplosionEffect.java)
 * (I am against using this pattern since it practices global like objects making
 * the system tightly coupled to this class.  However, I am using this for
 * learning purposes)
 * 
 * @author Kristofer Ken Castro
 * @date 6/25/2013
 */
public class SpecialEffectsManager {
	private ArrayList<ExplosionEffect> explosions;
	private ArrayList<DustBall> dustBalls;
	private PApplet p;
	// initialize when its first used.
	private static final SpecialEffectsManager manager = new SpecialEffectsManager();
	private Thread drawExplosionsThread;
	
	private SpecialEffectsManager(){
		explosions = new ArrayList<ExplosionEffect>();
		dustBalls = new ArrayList<DustBall>();
	}
	
	public static SpecialEffectsManager getInstance(){
		return manager;
	}
	
	public void createExplosion(PVector position){
		explosions.add(new ExplosionEffect(p, position));
	}
	
	public void createDustBall(PVector position){
		dustBalls.add(new DustBall(p, position));
	}
	
	public void drawExplosionsThread(){
		if (drawExplosionsThread != null) return;
		drawExplosionsThread = new Thread(new Runnable(){
			@Override
			public void run() {
				drawExplosions();
			}		
		});
		
		drawExplosionsThread.start();
	}
	
	public void drawExplosions(){
		ListIterator<ExplosionEffect> iter = explosions.listIterator();
		while(iter.hasNext()){
			ExplosionEffect explosion = iter.next();
			explosion.draw();
			
			if(explosion.isAnimationDone()){
				iter.remove();
			}
		}
	}
	
	public void drawDustBall(){
		ListIterator<DustBall> iter = dustBalls.listIterator();
		while(iter.hasNext()){
			DustBall dustBall = iter.next();
			dustBall.draw();
		}
	}
	
	public DustBall getDustBall(){
		return dustBalls.get(0);
	}
	
	public void setPApplet(PApplet p){
		this.p = p;
	}
}
