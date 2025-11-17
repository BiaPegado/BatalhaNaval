package br.ufrn.imd.model;

import java.util.ArrayList;

/**
 * Representa um Submarino na batalha naval. E uma subclasse de {@link Navio},
 * especificamente projetada para representar o Submarino, que e um tipo de
 * navio com tamanho fixo de 3 unidades.
 * <p>
 * Esta classe herda atributos e comportamentos da classe {@link Navio},
 * incluindo a capacidade de ser posicionada no tabuleiro e de ser atingida por
 * tiros.
 */
public class Submarino extends Navio {

	/**
	 * Construtor do Submarino. * Recebe uma lista de posicoes iniciais e uma
	 * direcao (horizontal ou vertical) para o posicionamento inicial do navio.
	 *
	 * @param posicao Lista de posicoes iniciais onde o Submarino sera posicionado
	 *                no tabuleiro.
	 * @param direcao Direcao inicial do navio (0 para horizontal, 1 para vertical).
	 */
	public Submarino(ArrayList<String> posicao, int direcao) {
		super(3, posicao, direcao);
	}
}
