package com.vonrabelin.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.vonrabelin.entities.Player;
import com.vonrabelin.main.Game;

public class UI {

	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(8, 4, 85, 8);
		
		g.setColor(Color.green);
		g.fillRect(8, 4,(int) ((Game.player.life/Game.player.maxlife)*85), 8);
		
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxlife, 35, 11);
	}
}
