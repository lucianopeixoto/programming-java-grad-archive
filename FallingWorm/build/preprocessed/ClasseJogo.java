
import java.io.IOException;
import java.util.Random;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
//import javax.microedition.rms.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luciano Peixoto
 */

public class ClasseJogo extends GameCanvas implements Runnable{

	private Thread thread;
	private Graphics graphics;
	private LayerManager layerManager;
	private Sprite spritePlayer;
	private TiledLayer tiledChao, tiledBackground[];
	private Image imagePlayer, imageFloor, imageBackground, imageGameOver;
//	private RecordStore rs;
//	private String txtDados;

	private Random sorteio;		// Utilizada para gerar os valores pseudo randomicos

	private int xPlayer, 
				yPlayer,
				xBuraco,
				xAux,
				yAux,
				aux1,
				aux2,
				tecla,
				estadoJogo,
				estadoPlayer,
				velocidade,
				pontos,
				vidas;

	private int andarEsquerdaSeq[] = {7,6,5,6};
	private int andarDireitaSeq [] = {0,1,2,1};
	private int paradoEsquerdaSeq[] = {4};
	private int paradoDireitaSeq[] = {3};

	// Definindo as constantes dos estados do jogador:
	private static final int andarEsquerda=1;
	private static final int andarDireita=2;
    private static final int paradoEsquerda=3;
    private static final int paradoDireita=4;

	// Definindo as constantes dos estados do jogo:
	private static final int jogoRodando=1;
	private static final int fimDeJogo=2;
    private static final int menuPrincipal=3;
    private static final int menuRecordes=4;

	public ClasseJogo (){
		super( true );

//		try {
//			rs=RecordStore.openRecordStore("ProvaMakerRS", true);
//			if (rs.getNumRecords() == 0) {
//				rs.addRecord(txtDados.getBytes(), 0, txtDados.getBytes().length);
//			}
//			txtDados = new String(rs.getRecord(1));
//		}
//		catch (RecordStoreException rse) {
//			System.out.println("CATCH!");
//			System.out.println(rse);
//		}
//
		try {
			imagePlayer = Image.createImage("player.png");
			imageFloor = Image.createImage("floor.png");
			imageBackground = Image.createImage("background.png");
			imageGameOver = Image.createImage("gameover.png");
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}

		sorteio = new Random();
		graphics = getGraphics();
		layerManager = new LayerManager();
		tiledChao = new TiledLayer(23, 1, imageFloor, 10, 10);
		tiledBackground = new TiledLayer[2];
		tiledBackground[0] = new TiledLayer(5, 8, imageBackground, 46, 46);
		tiledBackground[1] = new TiledLayer(5, 8, imageBackground, 46, 46);
		spritePlayer = new Sprite(imagePlayer, 64, 64);
		spritePlayer.defineCollisionRectangle(30, 10, 5, 54);

		this.setFullScreenMode(true);	// - Coloca o jogo em modo totalmente fullscreen.

		// Preenchendo os backgrounds:
		int celulas[][] =	{{	4,1,1,1,1,
								1,1,1,1,1,
								1,1,1,2,6,
								1,1,1,1,1,
								1,5,3,7,1,
								1,1,1,1,1,
								1,1,5,7,1,
								2,7,1,1,1},
							{	1,1,1,1,1,
								1,1,1,1,1,
								1,1,1,2,3,
								3,4,1,1,1,
								1,1,2,7,1,
								1,1,1,1,1,
								5,3,4,1,1,
								1,1,1,1,1 }};
		for (aux2=0; aux2<2; aux2++){
			aux1=0;
			for (yAux = 0; yAux < 8; yAux++) {
				for (xAux = 0; xAux<5; xAux++){
					tiledBackground[aux2].setCell(xAux, yAux, celulas[aux2][aux1]);
					aux1++;
				}
			}
		}
		// final do preenchimento

		graphics.setColor( 255, 255, 255);
		graphics.fillRect( 0, 0, getWidth(), getHeight() );
		layerManager.setViewWindow(0, 0, 230, 260);

		// Setando a Thread que atualiza os quadros de animação do Player
//		threadAnimacao = new ClasseAnimationThread (spritePlayer);
//		threadAnimacao.iniciaThread();

		// Adicionando os dois backgrounds ao layer manager
		layerManager.append(tiledChao);
		layerManager.append(spritePlayer);
		layerManager.append(tiledBackground[0]);
		layerManager.append(tiledBackground[1]);


		novoJogo();
		estadoJogo = jogoRodando;
	}

	void novoJogo(){
		// Inicializando o que for preciso:
		tiledBackground[0].setPosition(0,0);
		tiledBackground[1].setPosition(0, tiledBackground[0].getHeight());
		tiledChao.setPosition (0, 260);
		xPlayer = 230/2 - spritePlayer.getWidth()/2;
		yPlayer = 0;
		estadoPlayer = paradoEsquerda;
		sorteiaBuraco ();
		velocidade = 1;
		pontos = 0;
		vidas = 3;

	}

	void sorteiaBuraco (){
		xBuraco = sorteio.nextInt(18);
		aux1 = 0;
		while (aux1 < 23){
			if (aux1 == xBuraco){
				if (aux1>0) {
					tiledChao.setCell(aux1-1, 0, 2);
				}
				tiledChao.setCell(aux1, 0, 0);
				aux1++;
				tiledChao.setCell(aux1, 0, 0);
				aux1++;
				tiledChao.setCell(aux1, 0, 0);
				aux1++;
				tiledChao.setCell(aux1, 0, 0);
				aux1++;
				tiledChao.setCell(aux1, 0, 0);
				aux1++;
				tiledChao.setCell(aux1, 0, 0);
				aux1++;
				if (aux1<23) {
					tiledChao.setCell(aux1, 0, 3);
					aux1++;
				}
			}
			else{
				tiledChao.setCell(aux1, 0, 1);
				aux1++;
			}
		}
	}

	// Quando o canvas for escondido (HIDE) parar a thread:
	protected void hideNotify(){
		thread = null;
	}

	// Quando o canvas for exibido iniciar a thread:
	protected void showNotify(){
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		while (thread == Thread.currentThread()) {
//			while (estadoJogo == menuPrincipal){
//				graphics.setColor(0, 0, 0);
//				graphics.fillRect(0, 0, getWidth(), getHeight());
//				// Atualiza a tela:
//				flushGraphics();
//				// depois espera um pouco:
//				try {
//					Thread.sleep(4000);
//				}
//				catch( InterruptedException e ){
//				}
//				novoJogo();
//				estadoJogo = jogoRodando;
//			}
			while (estadoJogo == fimDeJogo){
				graphics.drawImage(imageGameOver, 0, 0, 0);
				graphics.drawString(""+pontos, 95, 90, 0);
				// Atualiza a tela:
				flushGraphics();
				// depois espera um pouco:
				try {
					Thread.sleep(4000);
				}
				catch( InterruptedException e ){
				}
				novoJogo();
				estadoJogo = jogoRodando;
			}
			while (estadoJogo == jogoRodando) {

				pontos += velocidade;
				if (yPlayer < -20) {
					vidas--;
					xPlayer = 230/2 - spritePlayer.getWidth()/2;
					yPlayer = 50;
					try {
						Thread.sleep(1000);
					}
					catch( InterruptedException e ){
					}
				

				}
				if (pontos > 300) velocidade = 2;
				if (pontos > 1000) velocidade = 3;
				if (pontos > 2000) velocidade = 4;
				if (pontos > 3500) velocidade = 5;
				if (pontos > 6000) velocidade = 6;
				if (pontos > 10000) velocidade = 7;
				if (pontos > 15000) velocidade = 8;
				if (pontos > 22000) velocidade = 9;
				if (pontos > 30000) velocidade = 10;
				if (pontos > 45000) velocidade = 11;
				if (pontos > 70000) velocidade = 12;
				if (pontos > 100000) velocidade = 13;
				if (pontos > 150000) velocidade = 14;
				if (pontos > 220000) velocidade = 15;
				if (pontos > 300000) velocidade = 16;




				// Lê as teclas e executa suas ações:
				tecla = getKeyStates();
				if( ( tecla & UP_PRESSED ) != 0 ){
					pontos += 1000;
				}
				if ((tecla & DOWN_PRESSED) != 0){
					vidas ++;
				}
				if( ( tecla & LEFT_PRESSED ) != 0 ){
					if (estadoPlayer != andarEsquerda) {
						estadoPlayer=andarEsquerda;
						spritePlayer.setFrameSequence(andarEsquerdaSeq);
					}
					xPlayer -= 5+(velocidade/2);
					if (xPlayer+13 < 0) xPlayer = -13;
				}
				if( ( tecla & RIGHT_PRESSED ) != 0 ){
					if (estadoPlayer != andarDireita){
						estadoPlayer=andarDireita;
						spritePlayer.setFrameSequence(andarDireitaSeq);
					}
					xPlayer += 5+(velocidade/2);
					if (xPlayer+50 > 230) xPlayer = 230-50;
				}
				if ((tecla & LEFT_PRESSED) == (tecla & RIGHT_PRESSED)){
					if (estadoPlayer != andarEsquerda) {
						estadoPlayer=paradoEsquerda;
						spritePlayer.setFrameSequence(paradoEsquerdaSeq);
					}
					if (estadoPlayer != andarDireita) {
						estadoPlayer=paradoDireita;
						spritePlayer.setFrameSequence(paradoDireitaSeq);
					}
				}

				aux2++;
				if (aux2 == 3){
					spritePlayer.nextFrame();
					aux2 = 0;
				}


				// Pinta o fundo de preto:
				graphics.setColor( 0, 20, 100);
				graphics.fillRect( 0, 0, getWidth(), getHeight());

				// Posicionando os backgrounds:
				tiledBackground[0].setPosition(0, tiledBackground[0].getY()-1);
				tiledBackground[1].setPosition(0, tiledBackground[1].getY()-1);
				if (tiledBackground[0].getY() < 1-tiledBackground[0].getHeight()){
					tiledBackground[0].setPosition(0, tiledBackground[1].getHeight());
				}
				if (tiledBackground[1].getY() < 1-tiledBackground[1].getHeight()){
					tiledBackground[1].setPosition(0, tiledBackground[0].getHeight());
				}

				// Posicionando o chão:
				if (tiledChao.getY() < -10) {
					sorteiaBuraco();
					tiledChao.setPosition(0, 260);
				}
				tiledChao.setPosition(0, tiledChao.getY() - velocidade);

				// Posicionando o Jogador:
				if (spritePlayer.collidesWith(tiledChao, false)) {
					yPlayer -= velocidade;
				}
				else {
					yPlayer += 10;
					if (yPlayer>260-64) yPlayer = 260-64;
				}
				spritePlayer.setPosition(xPlayer, yPlayer);


				// Posicionando as Informações
				graphics.setFont(null);
				graphics.setColor(255,255,255);
				graphics.drawString("Pontos: "+pontos+"\nVidas: "+vidas+" Velocidade: " + velocidade, 10, 270, Graphics.TOP|Graphics.LEFT);

				layerManager.paint(graphics, 5, 5);
				if (vidas < 0) {
					estadoJogo = fimDeJogo;
				}
				// Atualiza a tela:
				flushGraphics();
				// depois espera um pouco:
				try {
					Thread.sleep(20);
				}
				catch( InterruptedException e ){
				}
			}
		}
	}
}
