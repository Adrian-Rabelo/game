package com.vonrabelin.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.vonrabelin.main.Game;
import com.vonrabelin.world.Camera;

public class Entity {
	
	public static BufferedImage PLAYER_DAMAGED_UP = Game.spritesheet.getSprite(16, 32, 16, 16);
	public static BufferedImage PLAYER_DAMAGED_DOWN = Game.spritesheet.getSprite(16, 48, 16, 16);
	public static BufferedImage PLAYER_DAMAGED_LEFT = Game.spritesheet.getSprite(0, 48, 16, 16);
	public static BufferedImage PLAYER_DAMAGED_RIGHT = Game.spritesheet.getSprite(0, 32, 16, 16);
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6 * 16, 0, 16, 16);
	
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7 * 16, 0, 16, 16);
	
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6 * 16, 16, 16, 16);
	
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(112, 16, 16, 16);
	public static BufferedImage ENEMY_EN2 = Game.spritesheet.getSprite(128, 16, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(113, 32, 16, 16);
	
	public static BufferedImage GUN_RIGHT= Game.spritesheet.getSprite(128, 0, 16, 16);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(128+16, 0, 16, 16);
	public static BufferedImage GUN_UP = Game.spritesheet.getSprite(128+16, 16, 16, 16);
	public static BufferedImage GUN_DOWN = Game.spritesheet.getSprite(145, 32, 15, 16);
	public static BufferedImage GUN_UP_FEEDBACK = Game.spritesheet.getSprite(128, 32, 16, 16);
	public static BufferedImage GUN_LEFT_FEEDBACK = Game.spritesheet.getSprite(112, 48, 16, 16);
	public static BufferedImage GUN_RIGHT_FEEDBACK = Game.spritesheet.getSprite(112-16, 48, 16, 16);
	public static BufferedImage GUN_DOWN_FEEDBACK = Game.spritesheet.getSprite(128, 48, 16, 16);
	
	public static BufferedImage BULLET = Game.spritesheet.getSprite(96, 32, 17, 5);
	public static BufferedImage BULLET_UP = Game.spritesheet.getSprite(150, 48, 5, 17);
	public static BufferedImage BULLET_DOWN = Game.spritesheet.getSprite(150, 64, 5, 17);
	
	public static BufferedImage ENEMY_NEY = Game.spritesheet.getSprite(0, 64, 16, 16);
	public static BufferedImage ENEMY_NEY_2 = Game.spritesheet.getSprite(0, 80, 16, 16);
	public static BufferedImage ENEMY_NEY_DAMAGE = Game.spritesheet.getSprite(0, 96, 16, 16);
	
	public static BufferedImage BOSS_NEY = Game.spritesheet.getSprite(17, 66, 15, 14);
	public static BufferedImage BOSS_NEY_LEFT = Game.spritesheet.getSprite(33, 66, 15, 14);
	public static BufferedImage BOSS_NEY_RIGHT = Game.spritesheet.getSprite(49, 66, 15, 14);
	public static BufferedImage BOSS_NEY_CENTER = Game.spritesheet.getSprite(65, 66, 15, 14);
	public static BufferedImage BOSS_NEY_DAMAGE = Game.spritesheet.getSprite(17, 82, 15, 14);
	
	
	protected double x, y, z;
	protected int width;
	protected int height;
	
	private int maskx, masky, mwidth, mheight;
	
	private BufferedImage sprite;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
//		g.setColor(Color.red);
//		g.fillRect(this.getX() - Camera.x + maskx, this.getY() - Camera.y + masky, mwidth, mheight);
	}
	
	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX()+e1.maskx, e1.getY() + e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e1.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
		if (e1Mask.intersects(e2Mask) && e1.z == e2.z) {
			return true;
		} else {
			
			return false;
		}
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	public void setY(int newY) {
		this.y = newY;
	}
	public void setWidth(int newWidth) {
		this.width = newWidth;
	}
	public void setHeight(int newHeight) {
		this.height = newHeight;
	}
	public int getX() {
		return (int) this.x;
	}
	public int getY() {
		return (int) this.y;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}

}
