package com.vonrabelin.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.vonrabelin.main.Game;
import com.vonrabelin.world.Camera;
import com.vonrabelin.world.World;

public class Player extends Entity{

	public boolean right, up, left, down, jump = false;
	public int rightDir = 0, leftDir = 1, upDir = 2, downDir = 3;
	public int dir = rightDir;
	public double speed = 1.4;
	
	private static int frames = 0, maxFrames = 10, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer, downPlayer;
	private BufferedImage[] leftPlayer, upPlayer;
	
	private BufferedImage playerDamage;
	private BufferedImage playerRightStatic = Game.spritesheet.getSprite(32, 0, 16, 16), 
						  playerLeftStatic = Game.spritesheet.getSprite(32, 16, 16, 16),
						  playerUpStatic = Game.spritesheet.getSprite(32, 48, 16, 16),
						  playerDownStatic = Game.spritesheet.getSprite(32, 32, 16, 16);
	
	public double life = 100, maxlife = 100;
	public int mx, my;
	public int ammo = 0;
	public int z = 0;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	private boolean hasGun = false;
	public boolean shoot = false, mouseShoot = false;

	public int jumpFrames = 55, jumpCur = 0;
	public boolean isJumping = false;
	
	public boolean jumpUp = false, jumpDown = false;
	public int jumpSpeed;
	
	public int shootDirection;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.rightPlayer = new BufferedImage[4];
		this.leftPlayer = new BufferedImage[4];
		this.upPlayer = new BufferedImage[4];
		this.downPlayer = new BufferedImage[4];
		this.playerDamage = Game.spritesheet.getSprite(0, 32, 16, 16);
		
		for (int i = 0; i < 4;i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
		}
		for (int i = 0; i < 4;i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
		}
		for (int i = 0; i < 4;i++) {
			downPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 32, 16, 16);
		}
		for (int i = 0; i < 4;i++) {
			upPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 48, 16, 16);
		}
		
	}
	
	public void tick() {
		
		moved = false; // se movendo é igual a falso
		if (right && World.isFree((int)(x+speed)+1, this.getY()+1, z)) { 
			// se o botão direito foi pressionado e possui espaço para andar ele anda
			moved = true; 
			dir = rightDir;
			x += speed;
			
		} else if (left && World.isFree((int)(x-speed-1), this.getY()+1, z)) {
			moved = true;
			dir = leftDir;
			x -= speed;
		}
		
		if (up && World.isFree((int)(x-speed+1), this.getY()-1, z)) {
			moved = true;
			dir = upDir;
			y -= speed;
			
		} else if (down && World.isFree((int)(x+speed-1), this.getY()+3, z)) {
			dir = downDir;
			moved = true;
			y += speed;
		}
		if (moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		}
		if (jump) {
			if (isJumping == false) {
				jump = false;
				isJumping = true;
				jumpUp = true;
			}
			
		}
		
		if (isJumping) {
			if (jumpUp) {
				jumpCur+=2;
			} else if (jumpDown) {
				jumpCur -= 2;
				if (jumpCur <= 0) {
					isJumping = false;
					jumpDown = false;
					jumpUp = false;
				}
			}
			z = jumpCur;
			if (jumpCur >= jumpFrames) {
				jumpUp = false;
				jumpDown = true;
			}
		}
		// -- Checagem de colisões -- 
		this.checkCollisionGun();
		this.checkCollisionLifePack();
		this.checkCollisionAmmo();
		
		if (isDamaged) {
			
			this.damageFrames++;
			if (this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
				/*
				 * Se a variável 'isDamaged' for verdadeira:
				 * A variável 'damageFrames' auto incrementa;
				 * Se 'damageFrames' for igual a 8, 'damageFrames' recebe 0 e 'isDamaged' recebe false;
				 */
			}
		}
		if (shoot) {
			// CRIAR BALA E ATIRAR
			shoot = false;
			if (hasGun && ammo > 0) {
				ammo--;
				int dx = 0;
				int dy = 0;
				int px = 0;
				int py = 0;
				if (dir == rightDir) {
					dx = 1;
					py = 6;
					px = 28;
					shootDirection = 2;
				} else if (dir == leftDir){
					dx = -1;
					py = 6;
					px = -28;
					shootDirection = 3;
				} else if (dir == upDir) {
					dy = -1;
					py = -8;
					px = 6;
					shootDirection = 1;
				} else if (dir == downDir) {
					dy = 1;
					py = 23;
					px = -6;
					shootDirection = 4;
				}
				Game.bullets.add(new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy, shootDirection));
				/*
				 * Se a variável 'shoot' for verdadeira:
				 *   'shoot' recebe falso (para que não atire infinitamente);
				 *   Se as variáveis 'hasGun' for verdadeira e 'ammo' for maior que 0 (possui arma e tem balas suficientes):
				 *     'dx', 'dy', 'px', 'py' são as variáveis que controlam a direção e a posição das balas, respectivamente
				 *     As direções são alteradas de acordo com a direção do Player;
				 */
			}
				
		}
		if (mouseShoot) {
			mouseShoot = false;
			double angle = Math.atan2(my - (this.getY()+8 - Camera.y), mx - (this.getX()+8 - Camera.x));
			if (hasGun && ammo > 0) {
				ammo--;
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				int px = 0;
				int py = 0;
				if (dir == rightDir) {
					py = 8;
					px = 18;
				} else if (dir == leftDir){
					py = 8;
					px = -8;
				} else if (dir == upDir) {
					py = 4;
					px = 12;
				} else if (dir == downDir) {
					py = 10;
					px = 0;
				}
				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy, shootDirection);
				Game.bullets.add(bullet);
			}
		}
		
		if (this.life <= 0) {
			// Game over
			this.life = 0;
			Game.gameState = "GAME_OVER";
			
		}
		this.updateCamera();
		
	}
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2) , 0, (World.WIDTH * 16) - Game.WIDTH); // fazer a camera acompanhar jogador
		/* Camera na posição x recebe o valor de clamp: 
		 * ATUAL recebe a diferença da posicão de X do player pela metade da largura do jogo(10) (altura total = 20), 
		 * MÍNIMO recebe 0,
		 * MÁXIMO recebe a diferença da largura de World(20) vezes 16 (pixel) pela largura total do jogo (240)
		 */
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, (World.HEIGHT * 16 )- Game.HEIGHT); //
		/* Camera na posição Y recebe o valor de clamp: 
		 * ATUAL recebe a diferença da posição de Y do player pela metade da altua do jogo (10) (altura total = 20), 
		 * MÍNIMO RECEBE 0
		 * MÁXIMO recebe a diferença da altura de World(20) vezes 16 (pixel) pela altura total do jogo (160)
		 */
	}
	
	public void render(Graphics g) {
		// renderização do jogador, o sprite muda de acordo com a posiçao que o personagem está olhando
		if (!isDamaged) {
			if (!moved) {
				if (dir == rightDir) {
					g.drawImage(playerRightStatic, this.getX()-Camera.x, this.getY()-Camera.y - z, null);
					if (hasGun) {
						g.drawImage(Entity.GUN_RIGHT,this.getX() - Camera.x + 11, this.getY() - Camera.y + 3 - z, null);
					}
				} else if (dir == leftDir) {
					g.drawImage(playerLeftStatic, this.getX()-Camera.x, this.getY()-Camera.y - z, null);
					if (hasGun) {
						g.drawImage(Entity.GUN_LEFT,this.getX() - Camera.x - 13, this.getY() - Camera.y + 3 - z, null);
					}
				} else if (dir == upDir) {
					if (hasGun) {
						g.drawImage(Entity.GUN_UP,this.getX() - Camera.x + 6, this.getY() - Camera.y - 4 - z, null);
					}
					g.drawImage(playerUpStatic, this.getX()-Camera.x, this.getY()-Camera.y - z, null);
				} else if (dir == downDir) {
					g.drawImage(playerDownStatic, this.getX()-Camera.x, this.getY()-Camera.y - z, null);
					if (hasGun) {
						g.drawImage(Entity.GUN_DOWN,this.getX() - Camera.x - 5, this.getY() - Camera.y + 8 - z, null);
					}
				}
			} else {
				
				if (dir == rightDir) {
					g.drawImage(rightPlayer[index], this.getX()-Camera.x, this.getY()-Camera.y - z, null);
					if (hasGun) {
						// DESENHAR ARMA PARA DIREITA
						g.drawImage(Entity.GUN_RIGHT,this.getX() - Camera.x + 11, this.getY() - Camera.y + 3 - z, null);
					}
				} else if (dir == leftDir) {
					g.drawImage(leftPlayer[index], this.getX()-Camera.x, this.getY()-Camera.y - z, null);
					if (hasGun) {
						// DESENHAR ARMA PARA ESQUERDA
						g.drawImage(Entity.GUN_LEFT,this.getX() - Camera.x - 13, this.getY() - Camera.y + 3 - z, null);
					}
				} else if (dir == upDir) {
					if (hasGun) {
						// DESENHAR ARMA PARA CIMA
						g.drawImage(Entity.GUN_UP,this.getX() - Camera.x + 6, this.getY() - Camera.y - 4 - z, null);
					}
					g.drawImage(upPlayer[index], this.getX()-Camera.x, this.getY()-Camera.y - z, null);
				} else if (dir == downDir) {
					g.drawImage(downPlayer[index], this.getX()-Camera.x, this.getY()-Camera.y - z, null);
					if (hasGun) {
						// DESENHAR ARMA PARA BAIXO
						g.drawImage(Entity.GUN_DOWN,this.getX() - Camera.x - 5, this.getY() - Camera.y + 8 - z, null);
					}
				}
			}
			
		} else {
			if (dir == rightDir) {
				g.drawImage(Entity.PLAYER_DAMAGED_RIGHT, this.getX()-Camera.x, this.getY()-Camera.y - z, null);
				if (hasGun) {
					// DESENHAR ARMA PARA DIREITA
					g.drawImage(Entity.GUN_RIGHT_FEEDBACK,this.getX() - Camera.x + 11, this.getY() - Camera.y + 3 - z, null);
				}
			} else if (dir == leftDir) {
				g.drawImage(Entity.PLAYER_DAMAGED_LEFT, this.getX()-Camera.x, this.getY()-Camera.y - z, null);
				if (hasGun) {
					// DESENHAR ARMA PARA ESQUERDA
					g.drawImage(Entity.GUN_LEFT_FEEDBACK,this.getX() - Camera.x - 13, this.getY() - Camera.y + 2 - z, null);
				}
			} else if (dir == upDir) {
				if (hasGun) {
					// DESENHAR ARMA PARA CIMA
					g.drawImage(Entity.GUN_UP_FEEDBACK,this.getX() - Camera.x + 6, this.getY() - Camera.y - 4 -z, null);
				}
				g.drawImage(Entity.PLAYER_DAMAGED_UP, this.getX()-Camera.x, this.getY()-Camera.y - z, null);
			} else if (dir == downDir) {
				g.drawImage(Entity.PLAYER_DAMAGED_DOWN, this.getX()-Camera.x, this.getY()-Camera.y - z, null);
				if (hasGun) {
					// DESENHAR ARMA PARA BAIXO
					g.drawImage(Entity.GUN_DOWN_FEEDBACK,this.getX() - Camera.x - 5, this.getY() - Camera.y + 8 - z, null);
				}
			}
//			g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		if (isJumping) {
			g.setColor(Color.black);
			g.fillOval(this.getX()-Camera.x + 4, this.getY()-Camera.y + 16, 8, 8);
		}
		
	}
	public void checkCollisionGun() { // Verifica colisão com Entidade Arma
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i); // Atual recebe a uma entidade da lista de entidades
			if (atual instanceof Weapon) {
				if (Entity.isColliding(this, atual)) {
					hasGun = true;
//					System.out.println("Pegou arma");
					Game.entities.remove(atual);
					/*
					 * Se atual for uma instância da Entidade Weapon e colidir com o Player:
					 * A variável 'hasGun' recebe verdadeiro;
					 * A entidade atual é removida da lista;
					 */
				}
			}
		}
	}
	public void checkCollisionAmmo() { // Verifica colisão com Entidade Ammo
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i); // Atual recebe uma entidade da lista de entidades
			if (atual instanceof Ammo) {
				if (Entity.isColliding(this, atual)) {
					ammo+=25;
//					System.out.println("Munição atual: " + ammo);
					Game.entities.remove(atual);
					/*
					 * Se Atual for uma instância da Entidade Ammo e colidir com o Player:
					 * A variável 'ammo' auto-incrementa mais 25;
					 * A entidade atual é removida da lista 
					 */
				}
			}
		}
	}
	public void checkCollisionLifePack() { // Verifica colisão com Entidade LifePack
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i); // Atual recebe uma entidade da lista de entidades;
			if (atual instanceof LifePack) {
				if (Entity.isColliding(this, atual)) {
					life += 10;
					if (life > 100) {
						life = 100;
					}
					Game.entities.remove(atual);
					/*
					 * Se Atual for uma instância da Entidade LifePack e colidir com o Player:
					 * A variável 'life' auto-incrementa mais 10;
					 * Se a variável 'life' for maior que 100, 'life' recebe 100;
					 * A entidade atual é removida da lista/
					 */
				}
			}
		}
	}

}
