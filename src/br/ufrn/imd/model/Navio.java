package br.ufrn.imd.model;

import java.util.ArrayList;

public class Navio {

    //@ public model instance int mTamanho;
    //@ public model instance boolean mDestruido;

    /*@ spec_public @*/ protected int tamanho;   //@ in mTamanho;
    /*@ spec_public @*/ protected boolean destruido; //@ in mDestruido;
    /*@ spec_public @*/ protected ArrayList<String> posicao;
    /*@ spec_public @*/ protected int direcao;

    //@ public represents mTamanho = tamanho;
    //@ public represents mDestruido = destruido;

    //@ public invariant mTamanho > 0;
    //@ public invariant direcao == 0 || direcao == 1;
    //@ public invariant posicao != null;

    /*@ 
      @ public normal_behavior
      @ requires pos != null;
      @ requires tam > 0;
      @ requires pos.size() == tam;
      @ requires dir == 0 || dir == 1;
      @ ensures mTamanho == tam;
      @ ensures posicao == pos;
      @ ensures direcao == dir;
      @ ensures mDestruido == false;
      @ pure
      @*/
    public Navio(int tam, ArrayList<String> pos, int dir) {
        this.destruido = false;
        this.tamanho = tam;
        this.posicao = pos;
        this.direcao = dir;
    }

    //@ ensures \result == direcao;
    public /*@ pure @*/ int getDirecao() {
        return direcao;
    }

    //@ requires d == 0 || d == 1;
    //@ ensures this.direcao == d;
    public void setDirecao(int d) {
        this.direcao = d;
    }

    //@ requires t > 0;
    //@ ensures this.tamanho == t;
    public void setTamanho(int t) {
        this.tamanho = t;
    }

    //@ requires p != null;
    //@ ensures this.posicao == p;
    public void setPosicao(ArrayList<String> p) {
        this.posicao = p;
    }

    //@ ensures \result == destruido;
    public /*@ pure @*/ boolean isDestruido() {
        return destruido;
    }

    //@ ensures this.destruido == d;
    public void setDestruido(boolean d) {
        this.destruido = d;
    }

    //@ ensures \result == mTamanho;
    public /*@ pure @*/ int getTamanho() {
        return tamanho;
    }

    //@ ensures \result == posicao;
    public /*@ pure @*/ ArrayList<String> getPosicao() {
        return posicao;
    }

    /*@ 
    @ requires sitNavio != null;
    @ requires sitNavio.length == mTamanho;
    @ assignable destruido; 
    @ ensures (\forall int i; 0 <= i && i < sitNavio.length; sitNavio[i] == 0)
    @          ==> mDestruido == true;
    @*/
  public void verificarDestruido(int[] sitNavio) {
      boolean est = true;

      //@ maintaining 0 <= i && i <= sitNavio.length;
      //@ maintaining est == (\forall int k; 0 <= k && k < i; sitNavio[k] == 0);
      //@ loop_writes i, est;
      //@ decreases sitNavio.length - i;
      for (int i = 0; i < sitNavio.length; i++) {
          if (sitNavio[i] != 0) {
              est = false;
              break;
          }
      }

      if (est) {
          this.destruido = true;
      }
  }
}
