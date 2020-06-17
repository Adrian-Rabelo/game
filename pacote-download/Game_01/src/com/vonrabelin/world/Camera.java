package com.vonrabelin.world;

public class Camera {

	public static int x = 0;
	public static int y = 0;
	
	public static int clamp(int Atual, int Min, int Max) {
		if (Atual < Min) { // se atual for menor que o minimo, atual recebe mínimo
			Atual = Min;
		} 
		if (Atual > Max) { // se atual for maior que o máximo, atual recebe máximo
			Atual = Max;
		}
		return Atual; // retorna o valor atual
	}
}
