package com.vonrabelin.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.vonrabelin.main.Game;
import com.vonrabelin.world.Camera;

public class BulletShoot extends Entity{

	private double dx;
	private double dy;
	private double spd = 4;
	
	private int life = 67, curLife = 0;
	public int dir;
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy, int dir) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
		this.dir = dir;
		
	}
	
	public void tick() {
		x += dx * spd;
		y += dy * spd;
		curLife++;
		if (curLife == life) {
			Game.bullets.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		switch (dir) {
			case 1:
				g.drawImage(Entity.BULLET_UP, this.getX() - Camera.x + 5, this.getY() - Camera.y - 10, null);
				break;
			case 4:
				g.drawImage(Entity.BULLET_DOWN, this.getX() - Camera.x + 5, this.getY() - Camera.y - 10, null);
				break;
			case 3:
			case 2:
				g.drawImage(Entity.BULLET, this.getX() - Camera.x, this.getY() - Camera.y, null);
				break;
					
		}
//		g.setColor(Color.yellow);
//		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3, 3);
	}
}
