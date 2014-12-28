package siat.sfu.cs.Assignment3.Screens;

import java.util.Timer;
import java.util.TimerTask;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import siat.sfu.cs.Assignment3.Fishes.EnemyFish;
import siat.sfu.cs.Assignment3.Fishes.PlayerFish;

/*
 * First screen of the game explaining how to play the game and
 * how to proceed to play the game.
 * 
 * @author Kristofer Ken Castro
 * @date 6/25/2013
 */
public class IntroScreen {

	PApplet p;
	PFont ar;
	private final int SCREEN_HEIGHT;
	private final int SCREEN_WIDTH;
	private boolean visible = false;
	private String howToPlayText;
	private String playText;
	private PlayerFish demoPlayer;
	private EnemyFish demoEnemy;
	private Timer timer;
	
	public IntroScreen(PApplet p){
		this.p = p;
		PVector enemyPosition = new PVector(p.width/2+100, p.height/2+50);
		PVector playerPosition = new PVector(p.width/2-100, p.height/2+50);
		
		demoPlayer = new PlayerFish(p);
		demoPlayer.position = playerPosition;
		demoPlayer.scaleFactor = 0.25f;
		demoEnemy = new EnemyFish(p, enemyPosition, null);
		demoEnemy.scaleFactor = 0.25f;
		
		SCREEN_HEIGHT = p.height/2;
		SCREEN_WIDTH= p.width/2;
		ar = p.createFont("Arial", 44, true);
		visible = true;
		
		howToPlayText = "1. Use arrow keys to move.\n";
		howToPlayText += "2. Reach 500 score to win. \n";
		howToPlayText += "3. Eat smaller fishes to earn score points\n";
		howToPlayText += "4. You grow when you eat fishes. \n";
		howToPlayText += "5. Dodge bigger fishes, they take lives! \n";
		howToPlayText += "6. You start with 5 lives, don't lose it all!";
		
		playText = "Press 'enter' to start the game.";
		
        timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask(){

			@Override
			public void run() {
				if(playText.length() == 0)
					playText = "Press 'enter' to start the game.";
				else 
					playText ="";
			}
        	
        },100, 300);
	}
	
	public void draw(){
		demoPlayer.drawFish();
		demoEnemy.drawFish();
		drawSubScreen();
	}
	
	public void drawSubScreen(){
		p.fill(50,50,50,100);
		p.rectMode(p.CENTER);
		p.rect(p.width/2, p.height/2,SCREEN_WIDTH, SCREEN_HEIGHT);
		
		p.fill(200);
		p.textAlign(p.CENTER);
		p.textFont(ar, 48);
		p.text("Some Fish Game", SCREEN_WIDTH, SCREEN_HEIGHT-SCREEN_HEIGHT/2+48);
		p.fill(255, 100);
		p.textFont(ar,16);
		p.text(howToPlayText, SCREEN_WIDTH, SCREEN_HEIGHT-150);
		
		p.fill(50,200,50);
		p.textFont(ar,14);
		p.text("YOU", demoPlayer.position.x, demoPlayer.position.y + 50 );
		
		p.fill(200,100,100);
		p.text("ENEMY", demoEnemy.position.x, demoEnemy.position.y + 50);
		
		p.fill(200);
		p.textFont(ar,16);
		p.text(playText, SCREEN_WIDTH, SCREEN_HEIGHT+200);
		p.noFill();
		
		p.rectMode(p.CORNER); // reset back to default;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void leaveScreen(){
		visible = false;
	}
	
	public void enterScreen(){
		visible = true;
	}
}
