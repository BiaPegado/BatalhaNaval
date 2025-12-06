package br.ufrn.imd.model;

import br.ufrn.imd.dao.Malha;

/**
 * Classe que representa o jogo de batalha naval. Implementa a interface Thread
 * para permitir a execução simultânea de jogos.
 */

public class Jogo extends Thread {

    //@ spec_public
    private Malha player;

    //@ spec_public
    private volatile boolean running = true;

    //@ public invariant running == true || running == false;
    //@ public invariant player != null ==> player.navios != null;
    //@ public invariant player != null ==> player.naviosRestantes != null;

    
    /*@ public normal_behavior
      @ ensures \result == player;
      @*/
    public /*@ pure @*/ Malha getPlayer() {
        return player;
    }


    /*@ public normal_behavior
      @ requires p != null;
      @ assignable this.player;
      @ ensures this.player == p;
      @*/
    public void setPlayer(Malha p) {
        this.player = p;
    }


    //@ public normal_behavior
    //@ requires p != null;
    //@ requires str != null;
    //@ ensures this.player == p;
    public Jogo(Malha p, String str) {
        super(str);
        this.player = p;
    }


    //@ public normal_behavior
    //@ assignable running;
    //@ ensures running == false;
    public void stopThread() {
        running = false;
    }


    /**
     * Loop principal da thread do jogo.
     */
    //@ also
    //@ public behavior
    //@ requires player != null;
    //@ assignable \everything;
    public void run() {
    	
        while (running) {
            try {

                // turno do jogador
                if (player.isVez() == true) {
                    safeSleep(100);
                    player.atirar(0, 0);   // coordenada arbitrária controlada pela IA
                    player.setVez(false);
                }

                // atualizar estado dos navios
                for (Navio navio : this.player.getNavios()) {

                    int i = 0;
                    int[] situacao = new int[navio.getTamanho()];

                    for (String pos : navio.getPosicao()) {
                        String[] xy = pos.split(",");
                        int x = Integer.parseInt(xy[0]);
                        int y = Integer.parseInt(xy[1]);

                        situacao[i] = player.getBarcos()[x][y];
                        i++;
                    }

                    navio.verificarDestruido(situacao);

                    if (navio.isDestruido()) {
                        this.player.getNaviosRestantes()[navio.getTamanho() - 2] = 0;
                    }
                }

                if (player.getNaviosRestantes()[0] == 0 &&
                    player.getNaviosRestantes()[1] == 0 &&
                    player.getNaviosRestantes()[2] == 0 &&
                    player.getNaviosRestantes()[3] == 0) {

                    running = false;
                }

                safeSleep(100);

            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                running = false;
            }
        }

    }
  
    private void safeSleep(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
