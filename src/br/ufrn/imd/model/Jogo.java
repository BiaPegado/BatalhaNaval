package br.ufrn.imd.model;

import br.ufrn.imd.dao.Malha;

/**
 * Classe que representa o jogo de batalha naval. Implementa a interface Thread
 * para permitir a execucao simultanea de jogos. Gerencia o estado do jogo,
 * incluindo a movimentacao automatica dos turnos e a verificacao de vitoria.
 */
public class Jogo extends Thread {
	private Malha player;

	private volatile boolean running = true; // Flag de controle

	/**
	 * Obtem o jogador atual.
	 *
	 * @return O objeto Malha que representa o tabuleiro jogador atual.
	 */
	public Malha getPlayer() {
		return player;
	}

	/**
	 * Define o jogador atual.
	 *
	 * @param player O objeto Malha que representa o tabuleiro jogador atual.
	 */
	public void setPlayer(Malha player) {
		this.player = player;
	}

	/**
	 * Construtor da classe Jogo. Inicializa o jogo com o jogador especificado e
	 * define o nome da thread.
	 *
	 * @param player O objeto Malha que representa o tabuleiro jogador atual.
	 * @param str    Nome da thread.
	 */
	public Jogo(Malha player, String str) {
		super(str);
		this.player = player;
	}

	/**
	 * Metodo para parar a thread de maneira segura.
	 */
	public void stopThread() {
		running = false;
	}

	/**
	 * Executa o jogo enquanto a flip 'running' estiver verdadeira. Cada turno
	 * consiste em mover automaticamente o turno para o proximo jogador, realizar um
	 * tiro automatico e verificar o estado dos navios do jogador atual. Se todos os
	 * navios do jogador estiverem destruidos, o jogo termina.
	 */
	public void run() {
		System.out.println(this.getName() + " iniciando.");
		while (running) {
			try {
				if (player.isVez() == true) {
					sleep(100);
					this.player.atirar(0, 0);
					player.setVez(false);
				}
				for (Navio navio : this.player.getNavios()) {
					int i = 0;
					int[] sitNavio = new int[navio.getTamanho()];
					for (String coordenada : navio.getPosicao()) {
						String[] xy = coordenada.split(",");
						int x = Integer.parseInt(xy[0]);
						int y = Integer.parseInt(xy[1]);
						sitNavio[i] = player.getBarcos()[x][y];
						i++;
					}
					navio.verificarDestruido(sitNavio);

					if (navio.isDestruido() == true) {
						this.player.getNaviosRestantes()[navio.getTamanho() - 2] = 0;
					}
				}
				if ((this.player.getNaviosRestantes()[0] == 0) && (this.player.getNaviosRestantes()[1] == 0)
						&& (this.player.getNaviosRestantes()[2] == 0) && (this.player.getNaviosRestantes()[3] == 0)) {
					System.out.println("Todos morreram...");
					running = false; // Parar a thread
				}
				sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				running = false;
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
				running = false;
			}
		}
		System.out.println(this.getName() + " finalizou o jogo.");
	}
}
