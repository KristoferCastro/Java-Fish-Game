package siat.sfu.cs.Assignment3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import processing.core.PApplet;
import processing.core.PVector;
import siat.sfu.cs.Assignment3.Fishes.EnemyFish;
import siat.sfu.cs.Assignment3.Fishes.PlayerFish;
import siat.sfu.cs.Assignment3.SpecialEffects.SpecialEffectsManager;

/*
 * Holds fishes and handles placement of the fishes.
 * 
 * @author	Kristofer Ken Castro
 * @date	6/15/2013
 */
public class Pond {

	PApplet p;
	private ArrayList<EnemyFish> fishes;
	private final int FISH_TOTAL = 5;
	private final int WIDTH = 1200;
	private final int HEIGHT = 900;
	private final int SPAWN_RATE = 2000;
	public PlayerFish playerFish;
	public final static int MARGIN = 10;
	private ListIterator<EnemyFish> fishIterator;
	SpecialEffectsManager effectsManager;
	Timer timer;
	
	public Pond(PApplet p){	
		this.p = p;
		p.smooth();
		p.background(50,50,100);
		p.size(WIDTH, HEIGHT);
		// initialize the array of Fish
		fishes = new ArrayList<EnemyFish>();
		for (int i = 0 ; i < FISH_TOTAL ; i++){
			fishes.add(new EnemyFish(p));
		}
		playerFish = new PlayerFish(p);
		drawPond();
		
		// spawn fish every 2 seconds
		setupIntervalFishSpawn(SPAWN_RATE);
		
		// get the special effects manager
		effectsManager = SpecialEffectsManager.getInstance();
		
	}
	
	public void drawPond(){
		p.background(50,50,100);
		p.fill(82,127,166);
		p.rect(MARGIN, MARGIN, WIDTH-2*MARGIN, HEIGHT-2*MARGIN);
		
	}
	
	public void drawFishes(){
		
		// remove any dead fishes first.  We need to do this in its own for loop because
		// the fishes size will change after removing.  Solves fish flickering when
		// drawing
		for(int i = 0; i < fishes.size() ; i++){
			EnemyFish currentFish = fishes.get(i);
			if ( !currentFish.alive){
				fishes.remove(i);
			}
		}
		
		for(int i = 0 ; i < fishes.size() ; i++){
			EnemyFish currentFish = fishes.get(i);
			if ( !currentFish.alive){
				fishes.remove(i);
			}
			currentFish.drawFish();
			currentFish.update();
			currentFish.checkBoundaryCollisions();
			
			for(int j = 0; j < fishes.size(); j++){
				EnemyFish otherFish = fishes.get(j);
				if(currentFish != otherFish)
					currentFish.detectCollision(otherFish);		
			}
		}
		effectsManager.drawExplosions();
		balanceFishPool();
	}
	
	public void drawPlayerFish(){
		playerFish.drawFish();
		playerFish.checkBoundaryCollisions();
		
		for (EnemyFish enemyFish : fishes){
			if(playerFish.isFishEatable(enemyFish) && playerFish.detectHeadOnCollision(enemyFish)){
				playerFish.eat(enemyFish);
				effectsManager.createExplosion(enemyFish.position);
			}else if( !playerFish.isFishEatable(enemyFish) && enemyFish.detectHeadOnCollision(playerFish)){
		
				// player dies so lose life
				playerFish.loseLife();
				
			}else{
				// perform normal detection handling otherwise (i.e bouncing off each other)
				playerFish.detectCollision(enemyFish);
			}
		}
	}
	
	/**
	 * Make sure we have more than 5 fish alive at a time.
	 */
	public void balanceFishPool(){
		if (fishes.size() < 5){
			spawnNewFish();
		}
	}
	
	/**
	 * this will create fishes at a fixed interval
	 * @param interval in milliseconds (1000 ms = 1 second)
	 */
	public void setupIntervalFishSpawn(long interval){
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				spawnNewFish();
			}
		}, 0, interval);	
	}
	
	public void spawnNewFish(){
		PVector position = null;
		PVector velocity = null;
	
		if ( p.random(0,2) < 1 )
		{
			position = new PVector(p.width, p.random(p.height));
			velocity = new PVector(p.random(-4, -1), p.random(-2, -1));
		}
		else
		{
			position = new PVector(1, p.random(p.height));
			velocity = new PVector(p.random(1, 4), p.random(1, 2));
		}
		
		fishes.add(new EnemyFish(p, position, velocity)); // add at the end;
	}
}
