package siat.sfu.cs.Assignment3.Screens;

import processing.core.PApplet;
import processing.core.PFont;

/*
 * Parent abstract class of Game Over and Win Screen
 * @author Kristofer Ken Castro
 * @date 9/25/2013
 */
public abstract class EndScreen {
	
	PApplet p;
	PFont ar;
	String textTitle;
	String textExtra;
	private final float SCREEN_HEIGHT;
	private final float SCREEN_WIDTH;
	
    boolean enterKey;
	
	public EndScreen(PApplet p){
		this.p = p;
		SCREEN_HEIGHT = p.height/2;
		SCREEN_WIDTH= p.width/2;
		ar = p.createFont("Arial", 44, true);
	}
	
	public void draw(){
		drawSubScreen();
	}
	
	public void drawSubScreen(){
		p.fill(0,100);
		p.rectMode(p.CENTER);
		p.rect(p.width/2, p.height/2,SCREEN_WIDTH, SCREEN_HEIGHT);
		
		p.fill(0,200);
		p.textAlign(p.CENTER);
		p.textFont(ar, 48);
		p.text(textTitle, SCREEN_WIDTH, SCREEN_HEIGHT-20);
		p.fill(255, 100);
		p.textFont(ar,24);
		p.text(textExtra, SCREEN_WIDTH, SCREEN_HEIGHT);
		p.noFill();
		p.rectMode(p.CORNER); // reset back to default;
	}
}
