package br.ufrn.imd.dao;

import java.util.ArrayList;
import java.util.Random;

import br.ufrn.imd.model.Corveta;
import br.ufrn.imd.model.Destroyer;
import br.ufrn.imd.model.Fragata;
import br.ufrn.imd.model.Navio;
import br.ufrn.imd.model.Submarino;

/**
 * Classe abstrata que representa o tabuleiro de jogo para a batalha naval.
 */
public abstract class Malha {

	//@ spec_public
	protected int[][] barcos;

	//@ spec_public
	protected ArrayList<Navio> navios;

	//@ spec_public
	protected int[] naviosRestantes;

	//@ spec_public
	protected boolean vez;

	//@ spec_public nullable
	protected MalhaListener listener;

	

	//@ public invariant barcos != null;
	//@ public invariant barcos.length == 10;
	//@ public invariant (\forall int i; 0 <= i && i < barcos.length; barcos[i] != null && barcos[i].length == 10);
	//@ public invariant navios != null;
	//@ public invariant naviosRestantes != null;
	//@ public invariant naviosRestantes.length == 4;

	//@ public normal_behavior
	//@ ensures barcos != null && barcos.length == 10;
	//@ ensures (\forall int i; 0 <= i && i < 10; barcos[i] != null);
	//@ ensures navios != null && navios.isEmpty();
	//@ ensures naviosRestantes != null && naviosRestantes.length == 4;
	//@ ensures vez == false;
	//@ ensures listener == null;
	//@ pure
	public Malha() {
		this.barcos = new int[10][10];
		this.navios = new ArrayList<Navio>();
		this.naviosRestantes = new int[4];
		this.vez = false;
		this.listener = null;
	}
	
	//@ public normal_behavior
	//@ requires l != null;
	//@ assignable this.listener;
	//@ ensures this.listener == l;
	//@ also
	//@ public normal_behavior
	//@ requires l == null;
	//@ assignable this.listener;
	//@ ensures this.listener == null;
	public void setListener(/* @ nullable @ */ MalhaListener l) {
		this.listener = l;
	}


	//@ public normal_behavior
	//@ ensures \result == vez;
	public /* @ pure helper @ */ boolean isVez() {
		return vez;
	}


	//@ public normal_behavior
	//@ assignable this.vez;
	//@ ensures this.vez == vez;
	public void setVez(boolean vez) {
		this.vez = vez;
	}


	//@ public normal_behavior
	//@ ensures \result == barcos;	
	public /* @ pure helper @ */ int[][] getBarcos() {
		return barcos;
	}

	
	//@ public normal_behavior
	//@ requires barcos != null;
	//@ requires barcos.length == 10;
	//@ requires (\forall int i; 0 <= i && i < 10; barcos[i] != null && barcos[i].length == 10);
	//@ assignable this.barcos;
	//@ ensures this.barcos == barcos;
	public void setBarcos(int[][] barcos) {
		this.barcos = barcos;
	}

	//@ public normal_behavior
	//@ ensures \result == naviosRestantes;
	public /* @ pure helper @ */ int[] getNaviosRestantes() {
		return naviosRestantes;
	}

	//@ public normal_behavior
	//@ requires naviosRestantes != null;
	//@ requires naviosRestantes.length == 4;
	//@ assignable this.naviosRestantes;
	//@ ensures this.naviosRestantes == naviosRestantes;
	public void setNaviosRestantes(int[] naviosRestantes) {
		this.naviosRestantes = naviosRestantes;
	}

	
	//@ public normal_behavior
	//@ requires navios != null;
	//@ assignable this.navios;
	//@ ensures this.navios == navios;
	public void setNavios(ArrayList<Navio> navios) {
		this.navios = navios;
	}


	//@ public normal_behavior
	//@ ensures \result == navios;
	public /* @ pure helper @ */ ArrayList<Navio> getNavios() {
		return navios;
	}


	// IGNORE ESSA LINHA - ENCONTRADO ERRO NO OPENJML PARA MATRIZ assignable barcos[*][*], navios.*, naviosRestantes[*];
	
	//@ public normal_behavior
	//@ requires 2 <= tamanho && tamanho <= 5;
	//@ assignable \everything;
	//@ ensures \result != null;
	//@ ensures \result.size() == tamanho;
	//@ ensures navios.size() == \old(navios.size()) + 1;
	public ArrayList<String> posicionarNavio(int tamanho) {
		Random random = new Random();
		int direcao = gerarDirecao();

		int posX = gerarCoordenada();
        int posY = gerarCoordenada();

        //@ loop_invariant 0 <= posX && posX < 10;
        //@ loop_invariant 0 <= posY && posY < 10;
        //@ loop_writes posX, posY;
        while (!verificarEspacoDisponivel(posX, posY, tamanho, direcao)) {
            posX = gerarCoordenada();
            posY = gerarCoordenada();
        }


		//@ assume (direcao == 0) ==> (0 <= posY + tamanho < 10);
        //@ assume (direcao == 1) ==> (0 <= posX + tamanho < 10);
		
		ArrayList<String> posicoes = new ArrayList<String>();

	
		//@ loop_invariant 0 <= i && i <= tamanho;		
		//@ loop_invariant posicoes.size() == i;		
		//@ loop_invariant direcao == 0 || direcao == 1;		 
		//@ loop_invariant (direcao == 0 ==> posY + i <= 10);		 
		//@ loop_invariant (direcao == 1 ==> posX + i <= 10);		
		//@ decreases tamanho - i;
		for (int i = 0; i < tamanho; i++) {
			if (direcao == 0) {
				// Horizontal: varia Y
				barcos[posX][posY + i] = 1;
				posicoes.add(posX + "," + (posY + i));
			} else {
				// Vertical: varia X
				barcos[posX + i][posY] = 1;
				posicoes.add((posX + i) + "," + posY);
			}
		}

		adicionarNavio(tamanho, posicoes, direcao);

		return posicoes;
	}

	
  //@ private normal_behavior
  //@ assignable \nothing;
  //@ ensures 0 <= \result && \result < 10;
  //@ spec_public
  protected /*@ pure helper @*/ int gerarCoordenada() {
      Random random = new Random();
      int valor = random.nextInt(10);
      //@ assume 0 <= valor && valor < 10;
      return valor;
  }
	
	//@ public behavior
	//@ requires posicoes != null;
	//@ requires tamanho == posicoes.size();
	//@ requires direcao == 0 || direcao == 1;
	//@ requires 2 <= tamanho && tamanho <= 5;
	//@ assignable navios, navios.*, naviosRestantes[*];
	//@ ensures navios.size() == \old(navios.size()) + 1;
	public void adicionarNavio(int tamanho, ArrayList<String> posicoes, int direcao) {
		Navio novo = criarNavio(tamanho, posicoes, direcao);
		navios.add(novo);

		switch (tamanho) {
		case 2:
			naviosRestantes[0] = 1;
			break;
		case 3:
			naviosRestantes[1] = 1;
			break;
		case 4:
			naviosRestantes[2] = 1;
			break;
		case 5:
			naviosRestantes[3] = 1;
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	//@ requires posicoes != null;
	//@ requires tamanho == posicoes.size();
	//@ requires 2 <= tamanho && tamanho <= 5;
	//@ requires direcao == 0 || direcao == 1;
	//@ ensures \result != null;
	//@ pure
	private /* @ helper @ */ Navio criarNavio(int tamanho, ArrayList<String> posicoes, int direcao) {

		if (tamanho == Corveta.TAMANHO) {
			return new Corveta(posicoes, direcao);

		} else if (tamanho == Submarino.TAMANHO) {
			return new Submarino(posicoes, direcao);

		} else if (tamanho == Fragata.TAMANHO) {
			return new Fragata(posicoes, direcao);

		} else if (tamanho == Destroyer.TAMANHO) {
			return new Destroyer(posicoes, direcao);

		} else {
			throw new IllegalArgumentException();
		}
	}

	//@ public normal_behavior
	//@ requires 0 <= posX && posX < 10;
	//@ requires 0 <= posY && posY < 10;
	//@ requires 2 <= tamanho && tamanho <= 5;
	//@ requires direcao == 0 || direcao == 1;
	//@ assignable \nothing;
	//@ ensures \result == true || \result == false;
	public /* @ pure helper @ */ boolean verificarEspacoDisponivel(int posX, int posY, int tamanho, int direcao) {
		if (direcao == 0) {
			if (posY + tamanho > 10)
				return false;

			
			//@ loop_invariant 0 <= i && i <= tamanho;			
			//@ decreases tamanho - i;			
			for (int i = 0; i < tamanho; i++) {
				if (barcos[posX][posY + i] == 1)
					return false;
			}
		} else {
			if (posX + tamanho > 10)
				return false;

			//@ loop_invariant 0 <= i && i <= tamanho;
			//@ decreases tamanho - i;
			for (int i = 0; i < tamanho; i++) {
				if (barcos[posX + i][posY] == 1)
					return false;
			}
		}
		return true;
	}

	//@ public normal_behavior
	//@ assignable \nothing;
	//@ ensures \result == 0 || \result == 1;
	public /*@ pure helper @*/ int gerarDirecao() {
		Random random = new Random();
		return random.nextInt(2);
	}

	
	//@ public behavior
	//@ requires (coorX < 0) || (0 <= coorX && coorX < 10);
	//@ requires (coorY < 0) || (0 <= coorY && coorY < 10);
	//@ assignable \everything;
	public abstract int[] atirar(int coorX, int coorY);
}