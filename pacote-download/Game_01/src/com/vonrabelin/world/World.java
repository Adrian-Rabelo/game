package com.vonrabelin.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.vonrabelin.entities.*;
import com.vonrabelin.graficos.Spritesheet;
import com.vonrabelin.main.Game;

public class World {
	
	private int frames = 0, maxFrames = 1, index = 0,maxIndex = 1;
	
	public static Tile[] tiles;
	public static int WIDTH;
	public static int HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			
			// tiles recebe a classe Tileno com o valor da área do mapa(20*20)
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++) {
				
				for(int yy = 0; yy < map.getHeight(); yy++) {
					// pixel atual recebe a pixels na posição XX do loop somado à posição YY do segundo loop multiplicado por 16
					int pixelAtual = pixels [xx + (yy*map.getWidth())];
					// TILES na posição XX do loop mais YY * largura do mundo recebe um novo Tile do piso
					// O novo tile é construído na posição XX do loop vezes 16, YY vezes 16, e a sprite escolhida
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					
					if (pixelAtual == 0xFF000000) { // se o pixel atual for preto, desenhe o piso
						// FLOOR 
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
						
					} else if (pixelAtual == 0xFF2B2B2B) { // se o pixel atual for 0xFF2B2B2B, desenhe a variação do piso
						
						BufferedImage[] buf = new BufferedImage[2];
						buf[0] = Game.spritesheet.getSprite(0, 16, 16, 16);
						buf[1] = Game.spritesheet.getSprite(16, 16, 16, 16);
						frames++;
						if(frames == maxFrames) {
							frames = 0;
							index++;
							if (index > maxIndex) {
								index = 0;
							}
						}
						
						tiles[xx + (yy * WIDTH)] = new VariationFloorTile(xx * 16, yy * 16, buf[index]);
						
						
					}else if (pixelAtual == 0xFFFFFFFF){ // se o pixel atual for branco, desenhe as paredes
						// PAREDE
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
						
					} else if (pixelAtual == 0xFF0026FF) { // se o pixel atual for 0xFF0026FF, desenhe o jogador
						// PLAYER
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
						
					} else if (pixelAtual == 0xFFFF0000) { // se o pixel atual for vermelho, desenhe o inimigo 
						// ENEMY
						Enemy en = new Enemy(xx*16, yy*16, 16,16, null);
						Game.entities.add(en);
						Game.enemies.add(en);
						
					} else if (pixelAtual == 0xFFFF006E) { // se o pixel atual for 0xFFFF006E, desenhe a arma
						// WEAPON
						Game.entities.add(new Weapon(xx*16, yy*16, 16,16, Entity.WEAPON_EN));
						
					} else if (pixelAtual == 0xFF00FF21) { // se o pixel atual for 0xFF00FF21, desenhe a o kit médico
						// LIFE PACK
						Game.entities.add(new LifePack(xx*16, yy*16, 16,16, Entity.LIFEPACK_EN));
//						LifePack pack = new LifePack(xx*16, yy*16, 16,16, Entity.LIFEPACK_EN);
//						Game.entities.add(pack);
//						pack.setMask(0,0,-8,-8);
						
						
					} else if (pixelAtual == 0xFFFFD800) { // se o pxiel atual for 0xFFFFD800, desenhe munição 
						// AMMO
						Game.entities.add(new Ammo(xx*16, yy*16, 16,16, Entity.BULLET_EN));
						
					} else if (pixelAtual == 0xFF00FFFF) {
						// MINI NEY
						EnemyNey en = new EnemyNey(xx*16, yy*16, 16,16, null);
						Game.enemies.add(en);
						Game.entities.add(en);
					} else if (pixelAtual == 0xFFFF6A00) {
						// BOSS NEY
						BossNey en = new BossNey(xx*16, yy*16, 16, 16, null);
						Game.enemies.add(en);
						Game.entities.add(en);
					}
					
					else { // se não encontrar nenhuma cor diferente, desenhe o piso principal
						// FLOOR/CHÃO
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean isFree(int xnext, int ynext, int zplayer) {
		
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		if(!((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile))) {
			return true;
		} 
		if (zplayer > 0) {
			return true;
		} else {
			return false;
		}
		
		
		
	}
	
	public static void restartGame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/Spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return;
		/*
		 * Se a vida for menor ou igual a zero, o jogo é resetado
		 * Para isso, todas as suas variáveis e listas são zeradas
		 */
	}
	public void render(Graphics g) {
		int xstart = (Camera.x) >> 4;
		int ystart = (Camera.y) >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		for(int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				frames++;
				if(frames == maxFrames) {
					frames = 0;
					index++;
					if (index > maxIndex) {
						index = 0;
					}
				}
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
}
