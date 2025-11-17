package br.ufrn.imd.dao;

import java.util.Random;
import java.util.ArrayList;
import br.ufrn.imd.model.Navio;

public class MalhaJogador extends Malha {

	public MalhaJogador() {
		this.navios = new ArrayList<Navio>();
		this.barcos = new int[10][10];
	}

	/**
	 * Realiza um disparo no tabuleiro de jogo. Representa o tiro do computador na
	 * malha do jogador. Dispara em coordenadas aleatorias.
	 * 
	 * @param coorX A coordenada X do tiro.
	 * @param coorY A coordenada Y do tiro.
	 */
	@Override
	public void atirar(int coorX, int coorY) {

		Random random = new Random();
		coorX = -1;
		while ((coorX == -1) || ((this.barcos[coorX][coorY] == 2) || (this.barcos[coorX][coorY] == 3))) {
			coorX = random.nextInt(10);
			coorY = random.nextInt(10);
		}

		if (barcos[coorX][coorY] == 0) {
			barcos[coorX][coorY] = 3; // Errou barco
			System.out.println("Adversario atingiu a agua em " + coorX + ", " + coorY);
			System.out.println("");
			control.modificarTabuleiro(coorX, coorY, "file:src/br/ufrn/imd/image/ondinhaExplodida.png", 0);
		} else if (barcos[coorX][coorY] == 1) {
			barcos[coorX][coorY] = 2;
			System.out.println("Adversario atingiu navio em " + coorX + ", " + coorY);
			System.out.println("");
			control.modificarTabuleiro(coorX, coorY, "file:src/br/ufrn/imd/image/barcoExplodido.png", 0);
		} else { // Se ja foi atingido anteriormente
			System.out.println("Adversario ja atirou em " + coorX + ", " + coorY);
			System.out.println("");
		}

	}

}
