package siat.sfu.cs.Assignment3.Screens;

import processing.core.PApplet;

/*
 * Screen that is displayed when the player wins.
 * 
 * @author Kristofer Ken Castro
 * @date 6/25/2013
 */
public class WinScreen extends EndScreen{
	
	public WinScreen(PApplet p){
		super(p);
		textTitle = "You Won!";
		textExtra = "You've reached 500 score points! \n\nPress the 'enter' key to play again.";
	}

}
