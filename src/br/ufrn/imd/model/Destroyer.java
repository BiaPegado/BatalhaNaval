package br.ufrn.imd.model;

import java.util.ArrayList;

/**
 * Representa um Destroyer na batalha naval. E uma subclasse de {@link Navio},
 * especificamente projetada para representar Destroyer, que e um tipo de navio
 * com tamanho fixo de 5 unidades.
 * <p>
 * Esta classe herda atributos e comportamentos da classe {@link Navio},
 * incluindo a capacidade de ser posicionada no tabuleiro e de ser atingida por
 * tiros.
 */
public class Destroyer extends Navio {

	/**
	 * Construtor do Destroyer. Recebe uma lista de posicoes iniciais e uma direcao
	 * (horizontal ou vertical) para o posicionamento inicial do navio.
	 *
	 * @param posicao Lista de posicoes iniciais onde o Destroyer sera posicionado
	 *                no tabuleiro.
	 * @param direcao Direcao inicial do navio (0 para horizontal, 1 para vertical).
	 */
	public Destroyer(ArrayList<String> posicao, int direcao) {
		super(5, posicao, direcao);
	}
}
