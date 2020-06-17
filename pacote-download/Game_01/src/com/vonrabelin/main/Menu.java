package com.vonrabelin.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {

	public String[] options = {"Novo Jogo", "Carregar Jogo", "Sair"};
	
	public int currentOption = 0;
	public int maxOption = options.length -1;
	
	public boolean up, down, enter;
	
	public boolean pause = false;
	
	public Menu() {
		
	}
	
	public void tick() {
		if (up) {
			up = false;
			currentOption--;
			if (currentOption < 0) {
				currentOption = maxOption;
			}
		}
		if (down) {
			down = false;
			currentOption++;
			if (currentOption > maxOption) {
				currentOption = 0;
			}
		}
		if (enter) {
			enter = false;
			if (options[currentOption] == "Novo Jogo" || options[currentOption] == "Continuar") {
				Game.gameState = "NORMAL";
				pause = false;
			} else if (options[currentOption] == "Sair") {
				System.exit(1);
			}
		}
	}
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0 , 0));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		
		g.setColor(Color.BLUE);
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString(">Lanso a braba fdp", (Game.WIDTH*Game.SCALE) / 2-175, (Game.WIDTH*Game.SCALE) / 2 - 300);
		// opções do menu
		g.setFont(new Font("arial", Font.BOLD, 24));
		g.setColor(Color.white);
		if (pause == false) {
			g.drawString("Novo Jogo", (Game.WIDTH*Game.SCALE) / 2-75, 150);
		} else {
			g.drawString("Continuar", (Game.WIDTH*Game.SCALE) / 2-70, 150);
			
		}
		g.drawString("Carregar Jogo", (Game.WIDTH*Game.SCALE) / 2-100, 200);
		g.drawString("Sair", (Game.WIDTH*Game.SCALE) / 2-49, 250);
		
		if (options[currentOption] == "Novo Jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2-95, 150);
			
		} else if (options[currentOption] == "Carregar Jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2-120, 200);
		} else if (options[currentOption] == "Sair") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2-70, 250);
		}
		g.setFont(new Font("arial", Font.BOLD, 24));
		g.setColor(Color.white);
		g.drawString("Como jogar:", (Game.WIDTH*Game.SCALE) / 2 - 80, 350);
		g.drawString("usa as seta", (Game.WIDTH*Game.SCALE) / 2 - 80, 380); 
		g.drawString("se vira ai bixo", (Game.WIDTH*Game.SCALE) / 2 - 80, 410);
		g.drawString("atira com x", (Game.WIDTH*Game.SCALE) / 2 - 80, 440);
	}
	
}
