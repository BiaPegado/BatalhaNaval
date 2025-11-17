package br.ufrn.imd.dao;

import java.util.ArrayList;
import br.ufrn.imd.model.Navio;

public class MalhaPC extends Malha {

	public MalhaPC() {
		this.navios = new ArrayList<Navio>();
		this.barcos = new int[10][10];
	}

	/**
	 * Realiza um disparo no tabuleiro de jogo com base nas coordenadas escolhidas
	 * pelo jogador. Representa o tiro do jogador na malha do computador.
	 * 
	 * @param coorX A coordenada X do tiro.
	 * @param coorY A coordenada Y do tiro.
	 */
	@Override
	public void atirar(int coorX, int coorY) {
		if (control.isJogoAcabou()) {
			control.setStatusLabel("O jogo ja foi finalizado!");
			return;
		}
		if (barcos[coorX][coorY] == 0) {
			barcos[coorX][coorY] = 3; // Errou barco
			System.out.println("Errou o tiro em " + coorX + ", " + coorY);
			control.modificarTabuleiro(coorX, coorY, "file:src/br/ufrn/imd/image/ondinhaExplodida.png", 1);
			System.out.println("");
		} else if (barcos[coorX][coorY] == 1) {
			barcos[coorX][coorY] = 2;
			System.out.println("Jogador atingiu navio em " + coorX + ", " + coorY);
			control.modificarTabuleiro(coorX, coorY, "file:src/br/ufrn/imd/image/barcoExplodido.png", 1);
			System.out.println("");
		} else if (barcos[coorX][coorY] == 2) { // Se ja foi atingido anteriormente
			System.out.println("Jogador atirou em " + coorX + ", " + coorY);
			control.modificarTabuleiro(coorX, coorY, "file:src/br/ufrn/imd/image/barcoExplodido.png", 1);
			System.out.println("");
			control.setStatusLabel("Ja atirou nessa posicao e acertou!");
		} else {
			control.modificarTabuleiro(coorX, coorY, "file:src/br/ufrn/imd/image/ondinhaExplodida.png", 1);
			control.setStatusLabel("Ja atirou nessa posicao e errou!");
		}
		imprimirBarcos();
	}

}