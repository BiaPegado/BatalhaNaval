package br.ufrn.imd.dao;

import java.util.Random;
import java.util.ArrayList;
import br.ufrn.imd.model.Navio;

/**
 * Malha do jogador (é alvo do PC). atirar(...) se coorX<0 -> PC escolhe aleatório.
 */
public class MalhaJogador extends Malha {

    /*@ public normal_behavior
      @ assignable barcos, navios;
      @ ensures barcos != null && barcos.length == 10;
      @ ensures (\forall int i; 0 <= i && i < 10; barcos[i] != null && barcos[i].length == 10);
      @ ensures navios != null && navios.isEmpty();
      @ ensures naviosRestantes != null && naviosRestantes.length == 4; // Inicializado pelo super()
      @*/
    public MalhaJogador() {
        super(); 
        this.navios = new ArrayList<Navio>();
        this.barcos = new int[10][10];
    }

    /**
     * Realiza um disparo na malha do jogador.
     * Se coorX < 0, escolhe coordenadas aleatórias (jogada do PC).
     *
     * retorna int[] {codigo, x, y}
     */
    //@ also
    //@ public behavior
    //@ assignable \everything;
    //@ ensures \result != null;
    //@ ensures \result.length == 3;
    //@ ensures 0 <= \result[1] && \result[1] < 10;
    //@ ensures 0 <= \result[2] && \result[2] < 10;
    //@ ensures \result[0] == 0 || \result[0] == 1;
    @Override
    public int[] atirar(int coorX, int coorY) {
        int x = 0; 
        int y = 0;

        //@ loop_invariant 0 <= x && x < 10;
        //@ loop_invariant 0 <= y && y < 10;
        //@ loop_writes x, y;
        while (true) {
            x = gerarCoordenada();
            y = gerarCoordenada();

            //@ assume 0 <= x && x < 10;
            //@ assume 0 <= y && y < 10;
            int val = barcos[x][y];

            if (val == 2 || val == 3) {
                continue; 
            }

            if (val == 0) {
                barcos[x][y] = 3;
                if (listener != null)
                    listener.atualizarCelula(x, y,
                        "file:src/br/ufrn/imd/image/ondinhaExplodida.png",
                        0);
                return new int[]{0, x, y};
            } else if (val == 1) {
                barcos[x][y] = 2;
                if (listener != null)
                    listener.atualizarCelula(x, y,
                        "file:src/br/ufrn/imd/image/barcoExplodido.png",
                        0);
                return new int[]{1, x, y};
            }
        }
    }
}