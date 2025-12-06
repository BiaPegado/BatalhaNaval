package br.ufrn.imd.model;

import java.util.ArrayList;

/**
 * Representa uma Corveta na batalha naval.
 */
public class Corveta extends Navio {

	public static final int TAMANHO = 2;
    //@ public invariant mTamanho == 2;

    /**
     * Construtor da Corveta.
     *
     * @param posicao Lista de posicoes iniciais.
     * @param direcao Direcao inicial (0 ou 1).
     */
	/**
     * Construtor da Corveta.
     *
     * @param posicao Lista de posicoes iniciais.
     * @param direcao Direcao inicial (0 ou 1).
     */
    /*@ 
      @ requires posicao != null;
      @ requires posicao.size() == 2; 
      @ requires direcao == 0 || direcao == 1;
      @ ensures mTamanho == 2;
      @ ensures this.tamanho == 2;
      @ ensures this.posicao == posicao;
      @ ensures this.direcao == direcao;
      @ pure
      @*/
    public Corveta(ArrayList<String> posicao, int direcao) {
        super(TAMANHO, posicao, direcao);
    }
}