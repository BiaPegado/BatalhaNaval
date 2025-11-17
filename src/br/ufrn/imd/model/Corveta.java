package br.ufrn.imd.model;

import java.util.ArrayList;

/**
 * Representa uma Corveta na batalha naval. E uma subclasse de {@link Navio},
 * especificamente projetada para representar a Corveta, que e um tipo de navio
 * com tamanho fixo de 2 unidades.
 * <p>
 * Esta classe herda atributos e comportamentos da classe {@link Navio},
 * incluindo a capacidade de ser posicionada no tabuleiro e de ser atingida por
 * tiros.
 */
public class Corveta extends Navio {

	/**
	 * Construtor da Corveta. Recebe uma lista de posicoes iniciais e uma direcao
	 * (horizontal ou vertical) para o posicionamento inicial do navio.
	 *
	 * @param posicao Lista de posicoes iniciais onde a Corveta sera posicionada no
	 *                tabuleiro.
	 * @param direcao Direcao inicial do navio (0 para horizontal, 1 para vertical).
	 */
	public Corveta(ArrayList<String> posicao, int direcao) {
		super(2, posicao, direcao);
	}
}
