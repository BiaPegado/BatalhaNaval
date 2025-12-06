package br.ufrn.imd.dao;

import java.util.ArrayList;
import br.ufrn.imd.model.Navio;

/**
 * Malha do computador (alvo do jogador).
 */
public class MalhaPC extends Malha {

    //@ public normal_behavior
    //@ assignable barcos, navios;
    //@ ensures barcos != null && barcos.length == 10;
    //@ ensures (\forall int i; 0 <= i && i < 10; barcos[i] != null && barcos[i].length == 10);
    //@ ensures navios != null && navios.isEmpty();
    //@ ensures naviosRestantes != null && naviosRestantes.length == 4;
    public MalhaPC() {
        super();
        this.navios = new ArrayList<Navio>();
        this.barcos = new int[10][10];
    }

    /**
     * Realiza um disparo na malha do PC (feito pelo jogador).
     * Retorna int[] {codigo, x, y}
     */
    //@ also
    //@ public behavior
    //@ assignable \everything;
    //@ requires 0 <= coorX && coorX < 10;
    //@ requires 0 <= coorY && coorY < 10;
    //@ ensures \result != null;
    //@ ensures \result.length == 3;
    //@ ensures \result[0] >= 0 && \result[0] <= 3;
    @Override
    public int[] atirar(int coorX, int coorY) {

        if (!(0 <= coorX && coorX < 10 && 0 <= coorY && coorY < 10)) {
            if (listener != null) listener.setStatus("Coordenada inválida!");
            return new int[] {3, coorX, coorY};
        }

        int val = barcos[coorX][coorY];

        if (val == 0) {
            
            barcos[coorX][coorY] = 3;
            
            if (listener != null) listener.atualizarCelula(coorX, coorY, "file:src/br/ufrn/imd/image/ondinhaExplodida.png", 1);
            return new int[] {0, coorX, coorY};
            
        } else if (val == 1) {
            
            barcos[coorX][coorY] = 2;
            
            if (listener != null) listener.atualizarCelula(coorX, coorY, "file:src/br/ufrn/imd/image/barcoExplodido.png", 1);
            return new int[] {1, coorX, coorY};
            
        } else if (val == 2) {
            if (listener != null) listener.setStatus("Já atirou nessa posição e acertou!");
            return new int[] {2, coorX, coorY};
            
        } else { // val == 3
            if (listener != null) listener.setStatus("Já atirou nessa posição e errou!");
            return new int[] {3, coorX, coorY};
        }
    }
}