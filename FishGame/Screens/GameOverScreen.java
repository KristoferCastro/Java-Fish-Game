package siat.sfu.cs.Assignment3.Screens;

import processing.core.PApplet;
import processing.core.PFont;

/*
 * Screen that is displayed when he loses"
 * @author Kristofer Ken Castro
 * @date 6/25/2013
 */
public class GameOverScreen extends EndScreen {
	
	public GameOverScreen(PApplet p){
		super(p);
		
		textTitle = "GAME OVER";
		textExtra = "Press the 'enter' key to play again.";
		
	}
}
