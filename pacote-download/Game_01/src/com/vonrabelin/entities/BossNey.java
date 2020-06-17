package com.vonrabelin.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.vonrabelin.main.Game;
import com.vonrabelin.main.Sound;
import com.vonrabelin.world.Camera;
import com.vonrabelin.world.World;

public class BossNey extends Enemy{

	private double speed = 0.7;
	private int maskx = 6, masky = 3, maskw = 10, maskh = 10; // posição de x, y, largura e altura da máscara	
	private static int frames = 0, maxFrames = 900, index = 0, maxIndex = 3;
	
	private BufferedImage[] sprites;
	
	private int life = 1; // 35
	
	private boolean isDamaged = false;
	private int damageFrames = 10, damageCurrent = 0;
	
	public BossNey(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[4];
		sprites[0] = Entity.BOSS_NEY;
		sprites[1] = Entity.BOSS_NEY_LEFT;
		sprites[2] = Entity.BOSS_NEY_CENTER;
		sprites[3] = Entity.BOSS_NEY_RIGHT;
	}

	public void tick() {
			if (isCollidingWithPlayer() == false) {
				// Se não estiver colidindo com o jogador ele se aproxima, a partir de x e y
				/*
				 * Se a Entidade Inimigo na posiçao X for menor que a entidade Player na posição X,
				 * e se na Entidade World estiver livre para andar 
				 * e se a Entidade Inimigo não estiver colidindo:
				 * A Entidade Inimigo se aproxima do jogador aumentando a sua posição em X e Y
				 */
				if ((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY(), Game.player.z)
						&& !isColliding((int)(x+speed), this.getY())) {
					x+= speed;
				} else if ((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY(), Game.player.z)
						&& !isColliding((int)(x-speed), this.getY())) {
					x-=speed;
				}
				if ((int) y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed), Game.player.z)
						&& !isColliding(this.getX(), (int)(y+speed))) {
					y+= speed;
				} else if ((int) y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed), Game.player.z)
						&& !isColliding(this.getX(), (int)(y-speed))) {
					y-=speed;
				} 
			} else {
				// Se a Entidade Inimigo colidir com o jogador, aleatoriamente ela reduz uma quantidade de vida de 0 à 3;
				if(Game.rand.nextInt(100) < 50) {
					Sound.hurtEffect.play();
					Game.player.life-= Game.rand.nextInt(10);
					Game.player.isDamaged = true;
					
				}
				
			}
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
			
			this.collidingBullet();
			
			if (life <= 0) {
				this.destroySelf();
				return;
			}
			if (isDamaged) {
				this.damageCurrent++;
				if (this.damageCurrent == this.damageFrames) {
					this.damageCurrent = 0;
					this.isDamaged = false;
				}
			}
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	public void collidingBullet() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if (e instanceof BulletShoot) {
				if (Entity.isColliding(this, e)) {
					isDamaged = true;
					
					life -= 4;
					Game.bullets.remove(e);
					return;
				}
			}
		}
		
	}
	public boolean isCollidingWithPlayer() {
		// verifica se está colidindo com o jogador a partir da máscara
		// inimigo atual recebe a si próprio mais uma máscara
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY()+ masky,maskw,maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return enemyCurrent.intersects(player);
		// retorna True ou False caso o jogador esteja colidindo com um inimigo
	}
	public boolean isColliding(int xnext, int ynext) {
		// inimigo atual recebe uma máscara formata a partir de um retângulo 
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh); 
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e= Game.enemies.get(i);
			// Percorre a lista de inimigos criados na classe Game
			if (e == this) {
				// se uma entidade Inimigo se encontrar com outra, o loop continua 
				continue;
			}
			// Alvo inimigo recebe a mesma máscara
			Rectangle targetEnemy = new Rectangle(e.getX()+ maskx, e.getY()+ masky, maskw,maskh);
			if (enemyCurrent.intersects(targetEnemy)) {
				// Se o inimigo atual cruzar com outro inimigo, retorna True
				return true;
			}
		}
		// retorna falso
		return false;
	}
	
	public void render(Graphics g) {
		// animação do inimigo
		if (!isDamaged) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else {
			g.drawImage(Entity.BOSS_NEY_DAMAGE, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
//		g.setColor(Color.blue); //-- VISUALIZAR MÁSCARA
//		g.fillRect(this.getX() + maskx- Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}
}
