package br.ufrn.imd.model;

import java.util.ArrayList;

/**
 * Classe abstrata que representa um navio generico em um jogo de batalha naval.
 * Define propriedades comuns a todos os tipos de navios, como tamanho, posicao,
 * direcao e estado de destruicao. Subclasses especificas de navios (como
 * Corveta, Destroyer, Fragata) devem estender esta classe.
 */
public class Navio {
	protected boolean destruido;
	protected int tamanho;
	protected ArrayList<String> posicao;
	protected int direcao;

	/**
	 * Constroi um novo navio com o tamanho especificado, nas posicoes e direcao
	 * indicadas.
	 *
	 * @param tamanho Numero de celulas ocupadas pelo navio.
	 * @param posicao Lista de posicoes onde o navio sera colocado no tabuleiro.
	 * @param direcao Direcao inicial do navio (0 para horizontal, 1 para vertical).
	 */
	public Navio(int tamanho, ArrayList<String> posicao, int direcao) {
		this.destruido = false;
		this.tamanho = tamanho;
		this.posicao = posicao;
		this.direcao = direcao;
	}

	/**
	 * Retorna a direcao atual do navio.
	 *
	 * @return Direcao do navio (0 para horizontal, 1 para vertical).
	 */
	public int getDirecao() {
		return direcao;
	}

	/**
	 * Define a direcao do navio.
	 *
	 * @param direcao Nova direcao do navio (0 para horizontal, 1 para vertical).
	 */
	public void setDirecao(int direcao) {
		this.direcao = direcao;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public void setPosicao(ArrayList<String> posicao) {
		this.posicao = posicao;
	}

	// Getter para 'destruido'
	public boolean isDestruido() {
		return destruido;
	}

	// Setter para 'destruido'
	public void setDestruido(boolean destruido) {
		this.destruido = destruido;
	}

	// Getter para 'tamanho'
	public int getTamanho() {
		return tamanho;
	}

	// Getter para 'posicao'
	public ArrayList<String> getPosicao() {
		return posicao;
	}

	/**
	 * Verifica se o navio foi destruido.
	 *
	 * @param sitNavio Estado atual do navio (array de inteiros indicando se cada
	 *                 parte do navio foi atingida).
	 */
	public void verificarDestruido(int[] sitNavio) {
		int partesRestantes = 0;
		boolean destruido = true;
		for (int s : sitNavio) {
			if (s == 1) {
				partesRestantes++;
				destruido = false;
				break;
			}
		}
		if (destruido) {
			setDestruido(true);
		}
	}

}
