package com.vonrabelin.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.vonrabelin.entities.BulletShoot;
import com.vonrabelin.entities.Enemy;
import com.vonrabelin.entities.Entity;
import com.vonrabelin.entities.Player;
import com.vonrabelin.graficos.Spritesheet;
import com.vonrabelin.graficos.UI;
import com.vonrabelin.world.World;


public class Game extends Canvas implements Runnable, KeyListener, MouseListener {
	
	// Variáveis do jogo
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private boolean isRunning = false;
	private Thread thread;
	public static int WIDTH = 240;
	public static int HEIGHT = 160;
	public static int SCALE = 3;
	
	private int CUR_LEVEL = 1, MAX_LEVEL = 3;
	private BufferedImage image;

//	private BufferedImage layer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	
	public static Spritesheet spritesheet;
	public static Player player;
	
	public static World world;
	
	public static Random rand;
	
	public UI ui;
	
	public static String gameState = "VICTORY";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	public Menu menu;
	public Game() {
		Sound.musicBackground.loop();
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		
		// INICIALIZANDO OBJETOS
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("/Spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		
		menu = new Menu();
	}
	
	public void initFrame() {
		// inicialização da janela
		frame = new JFrame("Game #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
		
	}
	
	public void tick() {
		if (gameState.equals("NORMAL")) {
			this.restartGame = false;
			// Atualizações do jogo
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
			}
			
			if (enemies.size() == 0) {
				// Para o próximo level
				CUR_LEVEL++;
				if (CUR_LEVEL > MAX_LEVEL) {
					Game.gameState = "VICTORY";
				}
				if (!Game.gameState.equals("VICTORY")) {
					String newWorld = "level"+CUR_LEVEL+".png";
					System.out.println(newWorld);
					World.restartGame(newWorld);
				}
			}
			
		} else if (gameState.equals("GAME_OVER")) {
			this.framesGameOver++;
			if (this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if (this.showMessageGameOver) {
					this.showMessageGameOver = false;
				} else { 
					this.showMessageGameOver = true;
				}
			}
			if (restartGame) {
				this.restartGame = false;
				Game.gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
			}
		} else if (gameState.equals("MENU")) {
			menu.tick();
		} else if (gameState.equals("VICTORY")) {
//			victory.tick();
		}
	}
	
	public void render() {
		// Renderizações do jogo
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		
		world.render(g);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		
		ui.render(g);
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("Munição: " + player.ammo, 560, 20);
		if (gameState.equals("GAME_OVER")) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial", Font.BOLD, 36));
			g.setColor(Color.white);
			g.drawString("Game Over", (WIDTH*SCALE) / 2 - 95, (HEIGHT*SCALE) / 2 - 20);
			g.setFont(new Font("arial", Font.BOLD, 32));
			if (showMessageGameOver) {
				g.drawString(">Pressione ENTER para reiniciar<", (WIDTH*SCALE) / 2 - 245, (HEIGHT*SCALE) / 2 + 40);
			}
			
		} else if (gameState.equals("MENU")) {
			menu.render(g);
		} else if (gameState.equals("VICTORY")) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.black);
			g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
			
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, 32));
			g.drawString("Parabéns fdp, tu ganho", (Game.WIDTH*Game.SCALE) / 4, 105);
			g.drawString("Pode sair agr, flw", (Game.WIDTH*Game.SCALE) / 4 + 30, 205);
			
			g.setFont(new Font("arial", Font.BOLD, 8));
			g.drawString("aperta enter pra sair fdp", (Game.WIDTH*Game.SCALE) / 4 - 175, Game.HEIGHT*SCALE - 5);
		}
		bs.show(); // exibe os objetos
	}
	
	
	
	
	
	public void run() {
		// Loop para o funcionamento do jogo
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer+= 1000;
			}
		}
		stop();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				player.right = true;
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				player.left = true;
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				player.up = true;
				if (gameState.equals("MENU")) {
					Sound.select.play();
					menu.up = true;
				}
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				player.down = true;
				if (gameState.equals("MENU")) {
					Sound.select.play();
					menu.down = true;
				}
				break;
			case KeyEvent.VK_X:
				player.shoot = true;
				break;
			case KeyEvent.VK_ENTER:
				this.restartGame = true;
				if (gameState == "MENU") {
					menu.enter = true;
				} else if (gameState == "VICTORY") {
					System.exit(1);
				}
				break;
			case KeyEvent.VK_ESCAPE:
				if (gameState.equals("MENU")) {
					menu.pause = false;
				} else {
					gameState = "MENU";
					menu.pause = true;
					
				}
				break;
	//		case KeyEvent.VK_SPACE:
	//			player.jump = true;
	//			break;
		}
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				player.right = false;
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				player.left = false;
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				player.up = false;
				if (gameState.equals("MENU")) {
					menu.up = false;
				}
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				player.down = false;
				if (gameState.equals("MENU")) {
					menu.down = false;
				}
				break;
//			case KeyEvent.VK_X:
//				player.shoot = false;
//				break;
			
	}
}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
//		player.mouseShoot = true;
//		player.mx = (e.getX() / 3);
//		player.my = (e.getY() / 3);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

