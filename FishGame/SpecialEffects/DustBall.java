package siat.sfu.cs.Assignment3.SpecialEffects;

import processing.core.PApplet;
import processing.core.PVector;

public class DustBall {
	private PApplet p;
	private float radiusNoise;
	private float radius;

	public PVector center;
	public PVector position;
	private PVector lastPosition;

	private int timer;

	private boolean finished;

	public DustBall(PApplet p, PVector center) {
		this.p = p;
		position = new PVector();
		this.center = center;
		lastPosition = new PVector(-200, -200);
		finished = false;
		p.strokeWeight(0);
	}

	public void draw() {
		p.pushMatrix();
		radiusNoise = p.random(10);
		radius = 5;
		p.stroke(219,161, 22);
		p.strokeWeight(0);
		float startangle = p.random(360);
		float anglestep =  1 + (int) p.random(2);

		for (float ang = startangle; ang <= 500 + p.random(800); ang += anglestep) {
			radiusNoise += 0.005;
			radius += 0.05;

			float noiseRadius = radius + (p.noise(radiusNoise) * 200) - 100;
			float rad = p.radians(ang); // converts angle to radians

			p.pushMatrix();
			p.translate(center.x, center.y);
			position.x = noiseRadius * p.cos(rad);
			position.y = noiseRadius * p.sin(rad);
			p.line(position.x, position.y, lastPosition.x, lastPosition.y);
			p.popMatrix();
			lastPosition.x = position.x;
			lastPosition.y = position.y;
		}
		p.noStroke();
		// controls when to stop the animation
		timer++;
		if (timer == 10) {
			p.background(0);
			timer = 0;
			finished = true;
		}
		p.popMatrix();
	}

	public boolean isAnimationDone() {
		return finished;
	}
	
	public void setAnimationDone(boolean done){
		this.finished = done;
	}
}
