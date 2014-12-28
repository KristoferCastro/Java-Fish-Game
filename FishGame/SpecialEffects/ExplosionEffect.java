package siat.sfu.cs.Assignment3.SpecialEffects;

import processing.core.*;

/*
 * This class is used to generate an effect/animation of an explosion at a given location.
 * Uses generative art instead of image files or shape sprites.  
 * 
 * @author Kristofer Castro
 * @date 6/26/2013
 */
public class ExplosionEffect {

	private PApplet p;
	private float radiusNoise;
	private float radius;

	private PVector center;
	private PVector position;
	private PVector lastPosition;

	private int timer;

	private boolean finished;

	public ExplosionEffect(PApplet p, PVector center) {
		this.p = p;
		position = new PVector();
		this.center = center;
		lastPosition = new PVector(-200, -200);
		finished = false;
		p.strokeWeight(1.5f);
	}

	public void draw() {
		p.pushMatrix();
		radiusNoise = p.random(10);
		radius = 5;
		p.stroke(p.random(200, 255), p.random(50, 100), p.random(70), 80);
		float startangle = p.random(360);
		float anglestep = 0.15f;// 1 + (int) random(2);

		for (float ang = startangle; ang <= 500 + p.random(500); ang += anglestep) {
			radiusNoise += 0.01;
			radius += 0.0001;

			float noiseRadius = radius + (p.noise(radiusNoise) * 200) - 100;
			float rad = p.radians(ang); // converts angle to radians

			p.pushMatrix();
			p.translate(center.x, center.y);
			position.x = noiseRadius * p.cos(rad);
			position.y = noiseRadius * p.sin(rad);
			if (lastPosition.x > -999)
				p.line(position.x, position.y, lastPosition.x, lastPosition.y);
			p.popMatrix();
			lastPosition.x = position.x;
			lastPosition.y = position.y;
		}

		// controls when to stop the animation
		timer++;
		if (timer == 3) {
			timer = 0;
			finished = true;
		}
		p.popMatrix();
	}

	public boolean isAnimationDone() {
		return finished;
	}
}
