package br.ufrn.imd.dao;

/**
 * Interface leve para permitir que o modelo (Malha) notifique a interface.
 * Não contém referências a JavaFX, portanto é safe para OpenJML.
 */
public interface MalhaListener {
    /**
     * Solicita que a UI atualize a célula (x,y) com a imagem fornecida.
     * @param x coordenada x
     * @param y coordenada y
     * @param imagePath caminho da imagem a ser exibida
     * @param id id do tabuleiro (0 = jogador, 1 = PC)
     */
	//@ public behavior
    //@ assignable \nothing; 
    //@ ensures true; 
    void atualizarCelula(int x, int y, String imagePath, int id);

    /**
     * Solicita que a UI exiba um texto de status ao usuário.
     * @param texto texto de status
     */
    //@ public behavior
    //@ assignable \nothing; 
    //@ ensures true; 
    void setStatus(String texto);
}
