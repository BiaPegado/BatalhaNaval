package br.ufrn.imd.control;

import java.util.ArrayList;

import br.ufrn.imd.dao.MalhaJogador;
import br.ufrn.imd.dao.MalhaListener;
import br.ufrn.imd.dao.MalhaPC;
import br.ufrn.imd.model.Jogo;
import br.ufrn.imd.model.Navio;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Controlador responsavel pela integracao entre a interface grafica e o codigo.
 * Este controlador gerencia a interface do usuario, manipulacao de eventos e
 * logica de jogo.
 * <p>
 * Contem metodos para inicializacao do jogo, posicionamento de navios, inicio
 * do jogo, atuacao do jogador e PC, e manipulacao de eventos de mouse para
 * selecao e movimentacao de navios.
 * </p>
 */
public class Controlador implements MalhaListener{

	private MalhaJogador malha;
	private MalhaPC malhaPC;
	private boolean jogoIniciado = false;
	private boolean jogoAcabou = false;
	private boolean barcosPosicionados = false;
	private Thread jogo1Thread;
	private Thread jogo2Thread;
	private int[] selecionado = new int[4];
	private Navio navioSelecionado = null;

	@FXML
	private Rectangle corveta;
	@FXML
	private Rectangle destroyer;
	@FXML
	private Rectangle fragata;
	@FXML
	private Rectangle submarino;
	@FXML
	private Label statusLabel;
	@FXML
	private Button buttonJogar;
	@FXML
	private Button buttonIniciar;
	@FXML
	private Button buttonAtirar;
	@FXML
	private BorderPane painel;
	@FXML
	private Pane paneTabuleiroJogador;
	@FXML
	private Pane paneTabuleiroPC;
	@FXML
	private GridPane tabuleiroJogador;
	@FXML
	private GridPane tabuleiroPC;

	/**
	 * Inicializa o controlador, preparando o ambiente para o jogo.
	 */
	@FXML
	void initialize() {
		malha = new MalhaJogador();
		malhaPC = new MalhaPC();
		malha.setListener(this);
	    malhaPC.setListener(this);
		selecionado[0] = -1;
		selecionado[1] = -1;
		selecionado[2] = -1;
		selecionado[3] = -1;
	}

	/**
	 * Posiciona os navios do jogador e do PC de forma aleatoria em ambos os
	 * tabuleiros.
	 * 
	 * @param event Evento disparado pelo botao de iniciar o posicionamento
	 *              aleatorio.
	 */
	@FXML
	void posicionarAleatorio(ActionEvent event) {
		if (jogoAcabou) {
			statusLabel.setText("O jogo ja foi finalizado!");
			return;
		}
		if (jogoIniciado) {
			statusLabel.setText("O jogo ja comecou!");
			return;
		}
		if (barcosPosicionados) {
			barcosPosicionados = true;
			statusLabel.setText("Os barcos foram aleatorizados.");
			return;
		}
		tabuleiroJogador = createTabuleiro();
		tabuleiroPC = createTabuleiro();

		paneTabuleiroJogador.getChildren().add(tabuleiroJogador);
		tabuleiroJogador.layoutXProperty()
				.bind(paneTabuleiroJogador.widthProperty().subtract(tabuleiroJogador.widthProperty()).divide(2));
		tabuleiroJogador.layoutYProperty()
				.bind(paneTabuleiroJogador.heightProperty().subtract(tabuleiroJogador.heightProperty()).divide(2));

		paneTabuleiroPC.getChildren().add(tabuleiroPC);
		tabuleiroPC.layoutXProperty()
				.bind(paneTabuleiroPC.widthProperty().subtract(tabuleiroPC.widthProperty()).divide(2));
		tabuleiroPC.layoutYProperty()
				.bind(paneTabuleiroPC.heightProperty().subtract(tabuleiroPC.heightProperty()).divide(2));

		statusLabel.setText("Gerando barcos...");

		tabuleiroJogador = posicionarNavio(malha.posicionarNavio(2), tabuleiroJogador);
		tabuleiroJogador = posicionarNavio(malha.posicionarNavio(3), tabuleiroJogador);
		tabuleiroJogador = posicionarNavio(malha.posicionarNavio(4), tabuleiroJogador);
		tabuleiroJogador = posicionarNavio(malha.posicionarNavio(5), tabuleiroJogador);

		malhaPC.posicionarNavio(2);
		malhaPC.posicionarNavio(3);
		malhaPC.posicionarNavio(4);
		malhaPC.posicionarNavio(5);

		barcosPosicionados = true;
	}

	/**
	 * Inicia o jogo apos a posicao dos navios ser definida.
	 * 
	 * @param event Evento disparado pelo botao de iniciar o jogo.
	 */
	@FXML
	void comecarJogo(ActionEvent event) {
		if (!barcosPosicionados) {
			statusLabel.setText("As malhas ainda nao foram criadas");
			return;
		}
		if (jogoAcabou) {
			statusLabel.setText("O jogo ja foi finalizado!");
			return;
		}
		if (jogoIniciado) {
			statusLabel.setText("O jogo ja comecou!");
		}
		jogoIniciado = true;
		statusLabel.setText("O jogo comecou!");

		Jogo jogo1 = new Jogo(malha, "Jogador");
		Jogo jogo2 = new Jogo(malhaPC, "PC");

		jogo1.start();
		jogo2.start();

		jogo1Thread = new Thread(() -> {
			try {
				jogo1.join(); // Espera o termino do jogo1
				if (jogo2Thread != null)
					jogo2Thread.interrupt();
				if (jogo2 != null)
					jogo2.stopThread();
				Platform.runLater(() -> statusLabel.setText("O jogo acabou!")); // Atualiza a interface
				jogoAcabou = true;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		jogo2Thread = new Thread(() -> {
			try {
				jogo2.join(); // Espera o termino do jogo2
				if (jogo1Thread != null)
					jogo1Thread.interrupt();
				if (jogo1 != null)
					jogo1.stopThread();
				Platform.runLater(() -> statusLabel.setText("O jogo acabou!")); // Atualiza a interface
				jogoAcabou = true;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		jogo1Thread.start();
		jogo2Thread.start();

	}

	/**
	 * Seleciona uma celula do tabuleiro quando clicada.
	 * 
	 * @param event Evento de clique do mouse.
	 * @param x     Coordenada X da celula clicada.
	 * @param y     Coordenada Y da celula clicada.
	 * @param cell  Regiao da celula clicada.
	 */
	private void SelecionarCelula(MouseEvent event, int x, int y, Region cell) {
		String caminho = "file:src/br/ufrn/imd/image/mira.png";

		if (jogoAcabou) { // Impede que o jogador clique se o jogo ja tiver terminado.
			statusLabel.setText("O jogo ja foi finalizado!");
			return;
		}

		if (navioSelecionado != null) {
			String[] xyNavio = navioSelecionado.getPosicao().get(0).split(",");
			int xNavio = Integer.parseInt(xyNavio[0]);
			int yNavio = Integer.parseInt(xyNavio[1]);

			if ((xNavio == x) && (yNavio == y)) {
				girarBarco(event, x, y, navioSelecionado);
				return;
			} else {
				moverBarco(event, x, y, navioSelecionado);
				navioSelecionado = null;
				return;
			}
		}

		if ((selecionado[0] == 1)) {
			if ((selecionado[3] == 0) || (selecionado[3] == 1)) {
				caminho = "file:src/br/ufrn/imd/image/ondinha.png";
			} else if (selecionado[3] == 2) {
				caminho = "file:src/br/ufrn/imd/image/barcoExplodido.png";
			} else if (selecionado[3] == 3) {
				caminho = "file:src/br/ufrn/imd/image/ondinhaExplodida.png";
			}
			modificarTabuleiro(selecionado[1], selecionado[2], caminho, 1);
			malhaPC.getBarcos()[selecionado[1]][selecionado[2]] = selecionado[3];
			System.out.println("selecionou outro, malha mudada para " + selecionado[3]);
		}

		selecionado[0] = 1;
		selecionado[1] = x;
		selecionado[2] = y;
		selecionado[3] = malhaPC.getBarcos()[x][y];

		if ((jogoIniciado) && (!jogoAcabou)) {
			caminho = "file:src/br/ufrn/imd/image/mira.png";
			System.out.println(x + ", " + y);
			modificarTabuleiro(x, y, caminho, 1);
			buttonAtirar.setOnMouseClicked(event2 -> atirarClicado(event, x, y));
		} else {
			System.out.println("\njogo nao iniciado, selecione o navio");
			String coordenadas = (x + "," + y);
			System.out.println("coordenadas: " + x + "," + y);
			// Pega coordenadas e acha o navio
			for (Navio navio : malha.getNavios()) {
				for (String cord : navio.getPosicao()) {
					if (cord.equals(coordenadas)) {

						navioSelecionado = navio;
						break;
					}
				}
				if (navioSelecionado != null)
					break;
			}
			System.out.println("navio: " + (navioSelecionado != null ? navioSelecionado.toString() : "nenhum"));

		}
	}

	/**
	 * Mova um navio selecionado para uma nova posicao no tabuleiro.
	 * 
	 * @param event Evento de clique do mouse.
	 * @param x     Nova coordenada X onde o navio sera movido.
	 * @param y     Nova coordenada Y onde o navio sera movido.
	 * @param navio Navio a ser movido.
	 */
	private void moverBarco(MouseEvent event, int x, int y, Navio navio) {
		if (jogoIniciado) {
			return;
		}
		System.out.println("Mover barco para: " + x + "," + y);

		ArrayList<String> coordenadas = new ArrayList<>(navio.getPosicao());
		// Limpa posicoes antigas
		if (malha.verificarEspacoDisponivel(x, y, navio.getTamanho(), navio.getDirecao())) {
			for (String coord : coordenadas) {
				String[] xy = coord.split(",");
				int oldX = Integer.parseInt(xy[0]);
				int oldY = Integer.parseInt(xy[1]);
				malha.getBarcos()[oldX][oldY] = 0;
				modificarTabuleiro(oldX, oldY, "file:src/br/ufrn/imd/image/ondinha.png", 0);
			}

			// Atualiza posicoes novas
			int tamanho = coordenadas.size();
			ArrayList<String> novaPosicao = new ArrayList<String>();
			for (int i = 0; i < tamanho; i++) {
				int newX = x;
				int newY = y;
				String novaCoordenada = newX + "," + newY;
				if (navio.getDirecao() == 0) {
					newY += i;
					novaCoordenada = newX + "," + (newY);
					malha.getBarcos()[newX][newY] = 1; // 1 representa a presenca de um navio
					novaPosicao.add(novaCoordenada);
					modificarTabuleiro(newX, newY, "file:src/br/ufrn/imd/image/barco.png", 0);
					statusLabel.setText("Mudando navio para (" + newX + ", " + newY + ")");
				} else {
					newX += i;
					novaCoordenada = (newX) + "," + newY;
					malha.getBarcos()[newX][newY] = 1; // 1 representa a presenca de um navio
					novaPosicao.add(novaCoordenada);
					modificarTabuleiro(newX, newY, "file:src/br/ufrn/imd/image/barco.png", 0);
					statusLabel.setText("Mudando navio para (" + newX + ", " + newY + ")");
				}
				navio.setPosicao(novaPosicao);
			}
		} else {
			statusLabel.setText("Tentou mover barco para local invalido!");
		}

	}

	/**
	 * Gira um navio selecionado em sua posicao atual.
	 * 
	 * @param event Evento de clique do mouse.
	 * @param x     Coordenada X da celula onde o navio esta selecionado.
	 * @param y     Coordenada Y da celula onde o navio esta selecionado.
	 * @param navio Navio a ser girado.
	 */
	private void girarBarco(MouseEvent event, int x, int y, Navio navio) {
		if (jogoIniciado) {
			return;
		}
		// Inverte a direcao do barco
		navio.setDirecao(navio.getDirecao() == 0 ? 1 : 0);
		// Limpa as coordenadas antigas
		for (String coord : navio.getPosicao()) {
			String[] xy = coord.split(",");
			int oldX = Integer.parseInt(xy[0]);
			int oldY = Integer.parseInt(xy[1]);
			malha.getBarcos()[oldX][oldY] = 0;
			modificarTabuleiro(oldX, oldY, "file:src/br/ufrn/imd/image/ondinha.png", 0);
		}

		// Atualiza as novas coordenadas
		ArrayList<String> novaPosicao = new ArrayList<>();
		boolean espacoDisponivel = true;
		for (int i = 0; i < navio.getTamanho(); i++) {
			int newX = x + (navio.getDirecao() == 0 ? 0 : i);
			int newY = y + (navio.getDirecao() == 0 ? i : 0);
			if (newX >= 10 || newY >= 10 || malha.getBarcos()[newX][newY] != 0) {
				espacoDisponivel = false;
				break;
			}
			novaPosicao.add(newX + "," + newY);
		}

		if (espacoDisponivel) {
			for (String coord : novaPosicao) {
				String[] xy = coord.split(",");
				int newX = Integer.parseInt(xy[0]);
				int newY = Integer.parseInt(xy[1]);
				malha.getBarcos()[newX][newY] = 1;
				modificarTabuleiro(newX, newY, "file:src/br/ufrn/imd/image/barco.png", 0);
			}
			navio.setPosicao(novaPosicao);
		} else { // Se tentar girar para um local invalido ele reseta tudo.
			navio.setDirecao(navio.getDirecao() == 0 ? 1 : 0);
			for (String coord : navio.getPosicao()) {
				String[] xy = coord.split(",");
				int oldX = Integer.parseInt(xy[0]);
				int oldY = Integer.parseInt(xy[1]);
				malha.getBarcos()[oldX][oldY] = 1;
				modificarTabuleiro(oldX, oldY, "file:src/br/ufrn/imd/image/barco.png", 0);
			}
			statusLabel.setText("Tentou girar barco para local invalido!");
		}
	}

	/**
	 * Realiza um tiro no tabuleiro do PC.
	 * 
	 * @param event Evento de clique do mouse.
	 * @param x     Coordenada X onde o tiro sera disparado.
	 * @param y     Coordenada Y onde o tiro sera disparado.
	 */
	private void atirarClicado(MouseEvent event, int x, int y) {
	    int[] resultado = malhaPC.atirar(x, y);
	    int codigo = resultado[0];
	    // opcional: int rx = resultado[1], ry = resultado[2];

	    // alterar interface conforme codigo:
	    switch (codigo) {
	        case 0: // água
	            // a própria malha já pediu a atualização via listener.atualizarCelula
	            statusLabel.setText("Você errou!");
	            break;
	        case 1:
	            statusLabel.setText("Você acertou!");
	            break;
	        case 2:
	            statusLabel.setText("Posição já foi acertada anteriormente.");
	            break;
	        case 3:
	            statusLabel.setText("Posição inválida ou já foi errada.");
	            break;
	    }

	    malha.setVez(true);
	    selecionado[0] = 0;
	    selecionado[1] = -1;
	    selecionado[2] = -1;
	    selecionado[3] = -1;
	}


	/**
	 * Cria um novo tabuleiro para o jogador ou PC.
	 * 
	 * @return GridPane representando o tabuleiro.
	 */
	private GridPane createTabuleiro() {

		GridPane tabuleiro = new GridPane();
		Image image = new Image("file:src/br/ufrn/imd/image/ondinha.png");

		// Criar o BackgroundImage
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, false));

		// Adicionar calulas com a imagem de fundo
		for (int row = 0; row < 10; row++) { // Ajuste o tamanho do tabuleiro conforme necessario
			for (int col = 0; col < 10; col++) {
				Region cell = new Region();
				cell.setMinSize(42, 40); // Defina o tamanho da calula
				cell.setBackground(new Background(backgroundImage));

				// Adicionar evento de clique na calula
				int finalRow = row;
				int finalCol = col;
				cell.setOnMousePressed(event -> SelecionarCelula(event, finalRow, finalCol, cell));

				tabuleiro.add(cell, col, row);
			}
		}
		return tabuleiro;
	}

	/**
	 * Posiciona um navio no tabuleiro.
	 * 
	 * @param posicoes  Lista de coordenadas onde o navio sera posicionado.
	 * @param tabuleiro Tabuleiro onde o navio sera posicionado.
	 * @return GridPane atualizado com o navio posicionado.
	 */
	private GridPane posicionarNavio(ArrayList<String> posicoes, GridPane tabuleiro) {
		for (String coordenada : posicoes) {
			String[] coordenadaInt = coordenada.split(",");
			int x = Integer.parseInt(coordenadaInt[0]);
			int y = Integer.parseInt(coordenadaInt[1]);

			Image image = new Image("file:src/br/ufrn/imd/image/barco.png");

			BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
					new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, false));

			Region cell = new Region();
			cell.setMinSize(42, 40);
			cell.setBackground(new Background(backgroundImage));
			cell.setOnMousePressed(event -> SelecionarCelula(event, x, y, cell));

			tabuleiro.add(cell, y, x);
		}
		return tabuleiro;
	}

	/**
	 * Modifica a aparencia de uma celula especifica no tabuleiro.
	 * 
	 * @param x         Coordenada X da celula a ser modificada.
	 * @param y         Coordenada Y da celula a ser modificada.
	 * @param imagePath Caminho para a imagem a ser usada.
	 * @param id        Identificador da celula (0 para jogador, 1 para PC).
	 */
	public void modificarTabuleiro(int x, int y, String imagePath, int id) { // id -> Jogador = 0, PC = 1;
		// Cadigo para modificar a celula
		Platform.runLater(() -> {
			Image image = new Image(imagePath);
			BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
					new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, false));

			Region cell = new Region();
			cell.setMinSize(42, 40);
			cell.setBackground(new Background(backgroundImage));
			cell.setOnMousePressed(event -> SelecionarCelula(event, x, y, cell));

			if (id == 0) {
				tabuleiroJogador.add(cell, y, x);
			} else {
				tabuleiroPC.add(cell, y, x);
			}
		});

	}

	/**
	 * Abre uma janela com as instrucoes do jogo.
	 * 
	 * @param event Evento disparado pelo botao 'About'.
	 */
	@FXML
	void mostrarAjuda(ActionEvent event) {
		// Cria um novo Stage (janela)
		Stage ajudaStage = new Stage();
		ajudaStage.setTitle("Ajuda");

		// Cria um TextArea para mostrar o texto de ajuda
		TextArea ajudaTexto = new TextArea();
		ajudaTexto.setText("Instrucoes do jogo:\n\n" + "1. Posicionamento de navios:\n"
				+ "   - Clique em 'Iniciar' para posicionar seus navios aleatoriamente.\n"
				+ "   - Apos posicionar, voce pode clicar em um navio no tabuleiro para seleciona-lo.\n"
				+ "   - Clique novamente em um navio selecionado para gira-lo.\n"
				+ "   - Clique em outra celula para mover o navio selecionado para a posicao escolhida.\n\n"
				+ "2. Iniciando o jogo:\n"
				+ "   - Apos reposicionar seus navios, clique em 'Jogar' para comecar a partida.\n"
				+ "   - Voce jogara em turnos contra o computador.\n\n" + "3. Jogando:\n"
				+ "   - Clique em uma celula do tabuleiro do computador para seleciona-la e quando estiver pronto clique em 'Atirar'.\n"
				+ "   - Se voce acertar um navio, ele sera marcado como atingido.\n"
				+ "   - Continue atirando ate que todos os navios do computador sejam destruidos.\n\n"
				+ "4. Vencendo:\n" + "   - O jogo termina quando todos os navios de um jogador sao destruidos.\n"
				+ "   - O vencedor e o jogador que ainda tiver navios restantes.\n\n" + "Boa sorte e divirta-se! :)");
		ajudaTexto.setWrapText(true);
		ajudaTexto.setEditable(false); // Torna o TextArea somente leitura

		// Define o tamanho do TextArea
		ajudaTexto.setPrefSize(400, 400);

		// Cria uma Scene com o TextArea
		Scene ajudaScene = new Scene(ajudaTexto);

		// Define a Scene no Stage
		ajudaStage.setScene(ajudaScene);

		// Mostra a janela de ajuda
		ajudaStage.show();
	}

	/**
	 * Fecha o aplicativo.
	 * 
	 * @param event Evento disparado pelo botao de fechar ou o botao close.
	 */
	@FXML
	void fechar(ActionEvent event) {
		Platform.exit();
		System.exit(0);
	}

	/**
	 * Define o texto exibido no label de status.
	 * 
	 * @param text Texto a ser exibido.
	 */
	public void setStatusLabel(String text) {
		statusLabel.setText(text);
	}

	/**
	 * Verifica se o jogo acabou.
	 * 
	 * @return Verdadeiro se o jogo acabou, falso caso contrario.
	 */
	public boolean isJogoAcabou() {
		return jogoAcabou;
	}
	
	@Override
	public void atualizarCelula(int x, int y, String imagePath, int id) {
	    // Garantir execução na thread JavaFX
	    Platform.runLater(() -> modificarTabuleiro(x, y, imagePath, id));
	}

	@Override
	public void setStatus(String texto) {
	    Platform.runLater(() -> statusLabel.setText(texto));
	}

}
