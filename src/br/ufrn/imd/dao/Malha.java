package br.ufrn.imd.dao;

import java.util.ArrayList;
import java.util.Random;
import br.ufrn.imd.control.Controlador;
import br.ufrn.imd.model.Corveta;
import br.ufrn.imd.model.Destroyer;
import br.ufrn.imd.model.Fragata;
import br.ufrn.imd.model.Navio;
import br.ufrn.imd.model.Submarino;

/**
 * Classe abstrata que representa o tabuleiro de jogo para a batalha naval.
 * Gerencia a colocacao e o estado dos navios no tabuleiro.
 * <p>
 * Esta classe e estendida por {@link MalhaJogador} e {@link MalhaPC} para
 * representar o tabuleiro do jogador e o tabuleiro do computador,
 * respectivamente.
 * <p>
 * Cada instancia de {@code Malha} contem uma matriz bidimensional representando
 * o tabuleiro, uma lista de objetos {@link Navio} representando os navios, e
 * arrays para rastrear os navios restantes e quem tem a vez.
 * <p>
 * Metodos permitem posicionamento de navios, verificacao de espacos disponiveis
 * e disparos de tiros.
 * 
 */
public abstract class Malha {
	protected int[][] barcos;
	protected ArrayList<Navio> navios;
	/**
	 * Array que rastreia quantos navios restantes existem no tabuleiro.
	 */
	protected int[] naviosRestantes;
	protected boolean vez;

	/**
	 * Referencia ao objeto {@link Controlador} responsavel pela logica do jogo.
	 */
	protected Controlador control;

	/**
	 * Retorna o objeto {@link Controlador} associado a este tabuleiro.
	 * 
	 * @return O objeto {@link Controlador}.
	 */
	public Controlador getControl() {
		return control;
	}

	/**
	 * Define o objeto {@link Controlador} associado a este tabuleiro.
	 * 
	 * @param control O novo objeto {@link Controlador}.
	 */
	public void setControl(Controlador control) {
		this.control = control;
	}

	/**
	 * Retorna a matriz bidimensional que representa o tabuleiro de jogo.
	 * 
	 * @return A matriz bidimensional {@code barcos}.
	 */
	public int[][] getBarcos() {
		return barcos;
	}

	/**
	 * Define a matriz bidimensional que representa o tabuleiro de jogo.
	 * 
	 * @param barcos A nova matriz bidimensional para o tabuleiro de jogo.
	 */
	public void setBarcos(int[][] barcos) {
		this.barcos = barcos;
	}

	/**
	 * Retorna o array que rastreia quantos navios restantes existem no tabuleiro.
	 * 
	 * @return O array {@code naviosRestantes}.
	 */
	public int[] getNaviosRestantes() {
		return naviosRestantes;
	}

	public boolean isVez() {
		return vez;
	}

	public void setVez(boolean vez) {
		this.vez = vez;
	}

	/**
	 * Define o array que rastreia quantos navios restantes existem no tabuleiro.
	 * 
	 * @param naviosRestantes O novo array para rastrear os navios restantes.
	 */
	public void setNaviosRestantes(int[] naviosRestantes) {
		this.naviosRestantes = naviosRestantes;
	}

	/**
	 * Define a lista de objetos {@link Navio} que representam os navios
	 * posicionados no tabuleiro.
	 * 
	 * @param navios A nova lista de objetos {@link Navio}.
	 */
	public void setNavios(ArrayList<Navio> navios) {
		this.navios = navios;
	}

	/**
	 * Construtor da classe Malha. Inicializa os atributos barcos, navios, navios
	 * restantes e vez.
	 */
	public Malha() {
		this.barcos = new int[10][10];
		this.navios = new ArrayList<Navio>();
		this.naviosRestantes = new int[4];
		this.vez = false;
	}

	public ArrayList<Navio> getNavios() {
		return navios;
	}

	/**
	 * Imprime o estado atual do tabuleiro de jogo.
	 */
	public void imprimirBarcos() {
		for (int b = 0; b < 10; b++) {
			for (int a = 0; a < 10; a++) {
				System.out.print(barcos[b][a] + " ");
			}
			System.out.println("");
		}
		System.out.println("");
	}

	/**
	 * Posiciona um navio no tabuleiro de acordo com o tamanho especificado e
	 * retorna as posicoes ocupadas pelo navio.
	 * 
	 * @param tamanho O tamanho do navio a ser posicionado.
	 * @return Uma lista de strings representando as posicoes ocupadas pelo navio.
	 */
	public ArrayList<String> posicionarNavio(int tamanho) {
		Random random = new Random();
		int direcao = gerarDirecao(); // 0 para horizontal, 1 para vertical

		int posX, posY;
		do {
			posX = random.nextInt(10);
			posY = random.nextInt(10);
		} while (!verificarEspacoDisponivel(posX, posY, tamanho, direcao));

		ArrayList<String> posicoes = new ArrayList<String>();
		for (int i = 0; i < tamanho; i++) {
			if (direcao == 0) {
				this.barcos[posX][posY + i] = 1; // 1 representa a presenca de um navio
				posicoes.add(posX + "," + (posY + i));
			} else {
				this.barcos[posX + i][posY] = 1; // 1 representa a presenca de um navio
				posicoes.add((posX + i) + "," + posY);
			}
		}
		adicionarNavio(tamanho, posicoes, direcao);
		return posicoes;
	}

	/**
	 * Adiciona um navio ao tabuleiro de jogo com base no tamanho e nas posicoes
	 * fornecidas.
	 * 
	 * @param tamanho  O tamanho do navio a ser adicionado.
	 * @param posicoes As posicoes ocupadas pelo navio.
	 * @param direcao  A direcao em que o navio deve ser colocado (horizontal ou
	 *                 vertical).
	 */
	public void adicionarNavio(int tamanho, ArrayList<String> posicoes, int direcao) {
		switch (tamanho) {
		case 2:
			navios.add(new Corveta(posicoes, direcao));
			naviosRestantes[0] = 1;
			break;
		case 3:
			navios.add(new Submarino(posicoes, direcao));
			naviosRestantes[1] = 1;
			break;
		case 4:
			navios.add(new Fragata(posicoes, direcao));
			naviosRestantes[2] = 1;
			break;
		case 5:
			navios.add(new Destroyer(posicoes, direcao));
			naviosRestantes[3] = 1;
			break;

		}

	}

	/**
	 * Verifica se ha espaco suficiente no tabuleiro para posicionar um navio de
	 * determinado tamanho e direcao.
	 * 
	 * @param posX    A coordenada X inicial onde o navio sera posicionado.
	 * @param posY    A coordenada Y inicial onde o navio sera posicionado.
	 * @param tamanho O tamanho do navio a ser posicionado.
	 * @param direcao A direcao em que o navio deve ser colocado (horizontal ou
	 *                vertical).
	 * @return {@code true} se houver espaco suficiente, {@code false} caso
	 *         contrario.
	 */
	public boolean verificarEspacoDisponivel(int posX, int posY, int tamanho, int direcao) {
		if (direcao == 0) {
			if (posY + tamanho > 10) {
				return false;
			}
			for (int i = 0; i < tamanho; i++) {
				if (barcos[posX][posY + i] == 1) {
					return false;
				}
			}
		} else {
			if (posX + tamanho > 10) {
				return false;
			}
			for (int i = 0; i < tamanho; i++) {
				if (barcos[posX + i][posY] == 1) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Gera uma direcao aleatoria para posicionar um navio (horizontal ou vertical).
	 * 
	 * @return 0 para horizontal, 1 para vertical.
	 */
	public int gerarDirecao() {
		Random random = new Random();
		return random.nextInt(2);
	}

	/**
	 * Metodo abstrato que deve ser implementado pelas subclasses para realizar um
	 * disparo no tabuleiro.
	 * 
	 * @param coorX A coordenada X do tiro.
	 * @param coorY A coordenada Y do tiro.
	 */
	public abstract void atirar(int coorX, int coorY);

}
