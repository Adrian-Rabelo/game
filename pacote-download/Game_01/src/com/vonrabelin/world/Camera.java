package com.vonrabelin.world;

public class Camera {

	public static int x = 0;
	public static int y = 0;
	
	public static int clamp(int Atual, int Min, int Max) {
		if (Atual < Min) { // se atual for menor que o minimo, atual recebe m�nimo
			Atual = Min;
		} 
		if (Atual > Max) { // se atual for maior que o m�ximo, atual recebe m�ximo
			Atual = Max;
		}
		return Atual; // retorna o valor atual
	}
}
