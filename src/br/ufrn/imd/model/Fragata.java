package br.ufrn.imd.model;

import java.util.ArrayList;

/**
 * Representa uma Fragata na batalha naval. E uma subclasse de {@link Navio},
 * especificamente projetada para representar a Fragata, que e um tipo de navio
 * com tamanho fixo de 4 unidades.
 * <p>
 * Esta classe herda atributos e comportamentos da classe {@link Navio},
 * incluindo a capacidade de ser posicionada no tabuleiro e de ser atingida por
 * tiros.
 */
public class Fragata extends Navio {

    //@ public invariant mTamanho == 4;

	/**
	 * Construtor da Fragata. Recebe uma lista de posições iniciais e uma
	 * direção (horizontal ou vertical) para o posicionamento inicial do navio.
	 *
	 * @param posicao Lista de posições iniciais onde a Fragata será posicionada
	 * no tabuleiro.
	 * @param direcao Direção inicial do navio (0 para horizontal, 1 para
	 * vertical).
	 */
    /*@ 
      @ requires posicao != null;
      @ requires posicao.size() == 4; 
      @ requires direcao == 0 || direcao == 1;
      @ ensures mTamanho == 4;
      @ ensures this.posicao == posicao;
      @ ensures this.direcao == direcao;
      @*/
	public Fragata(ArrayList<String> posicao, int direcao) {
		super(4, posicao, direcao);
	}
}