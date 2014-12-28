package siat.sfu.cs.Assignment3.Screens;
import processing.core.*;

/*
 * Handles dynamic display of score and life of player.
 * 
 * @author Kristofer Ken Castro
 * @date 6/25/2013
 */
public class HUD {
	private int score;
	private int lives;
	private PApplet p;
	private PFont arial;
	private final float ALIGN_Y = 30;
	
	public HUD(PApplet p, int score, int lives){
		this.p = p;
		this.score = score;
		this.lives = lives;
		
		// create the font object
		arial = p.createFont("Arial", 44, true);
	}
	
	public void draw(){
		p.fill(0,100);
		p.textAlign(p.CENTER);
		p.textFont(arial, 24);
		p.text("Score: " + score, p.width/2, ALIGN_Y);
		p.text("Lives: " + lives, p.width-60, ALIGN_Y);
		p.noFill();
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public void setLives(int lives){
		this.lives = lives;
	}
}
