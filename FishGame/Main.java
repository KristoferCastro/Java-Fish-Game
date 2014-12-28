package siat.sfu.cs.Assignment3;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import processing.core.*;
import siat.sfu.cs.Assignment3.Fishes.PlayerFish;
import siat.sfu.cs.Assignment3.Screens.GameOverScreen;
import siat.sfu.cs.Assignment3.Screens.HUD;
import siat.sfu.cs.Assignment3.Screens.IntroScreen;
import siat.sfu.cs.Assignment3.Screens.WinScreen;
import siat.sfu.cs.Assignment3.SpecialEffects.SpecialEffectsManager;

/*
 * Main application entry point of the game.
 * 
 * @author Kristofer Ken Castro
 * @date 6/25/2013
 */
public class Main extends PApplet {

	private Pond myPond;
	private HUD hud;
	private GameOverScreen gameOverScreen;
	private WinScreen winScreen;
	private PlayerFish player;
	private IntroScreen introScreen;
	
	// control keys
	private boolean downKey, upKey, leftKey, rightKey;
	private boolean enterKey;
	
	public void setup(){
		frameRate(60);
		myPond = new Pond(this);
		player = myPond.playerFish;
		hud = new HUD(this, player.score, player.lives);
		gameOverScreen = new GameOverScreen(this);
		winScreen = new WinScreen(this);
		introScreen = new IntroScreen(this);
		smooth();
		myPond.drawPond();
		
		SpecialEffectsManager effectsManager = SpecialEffectsManager.getInstance();
		effectsManager.setPApplet(this);
		
		ChangeListener listener = new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println("recieved click!");
			}
			
		};
	}
	
	public void draw(){
		myPond.drawPond();
		if (introScreen.isVisible()){
			introScreen.draw();
			if(enterKey){
				introScreen.leaveScreen();
			}		
		}else{
			drawGamePlay();
		}
	}
	
	public void drawGamePlay(){
		myPond.drawFishes();
		
		if(player.hasWon()){
			winScreen.draw();
			checkPlayAgain();
		}else if (player.hasLost()){
			gameOverScreen.draw();
			checkPlayAgain();
		}else{
			myPond.drawPlayerFish();
			playerMovement();
			hud.draw();
			updateScores();
		}
	}
	
	public void updateScores(){
		hud.setLives(player.lives);
		hud.setScore(player.score);
	}
	
	public void checkPlayAgain(){
		if (enterKey){
			player.reset();
		}
	}
	
	public void playerMovement(){
		if (upKey) 
			player.moveUp();
		if (downKey) 
			player.moveDown();
		if (leftKey)
			player.moveLeft();
		if (rightKey) 
			player.moveRight();
	}
	
	public void keyPressed(){
		if (keyCode == ENTER || keyCode == RETURN){
			enterKey = true;
		}
		
		if (key != CODED) return;
		
		if (keyCode == UP)
			upKey = true;
		if (keyCode == DOWN)
			downKey = true;
		if (keyCode == LEFT)
			leftKey = true;
		if (keyCode == RIGHT)
			rightKey = true;
			
	}
	
	public void keyReleased(){
		
		if (keyCode == ENTER || keyCode == RETURN){
			enterKey = false;
		}
		
		if (key != CODED ) return;
		
		if (keyCode == UP)
			upKey = false;
		if (keyCode == DOWN)
			downKey = false;
		if (keyCode == LEFT)
			leftKey = false;
		if (keyCode == RIGHT)
			rightKey = false; 
	}
}
