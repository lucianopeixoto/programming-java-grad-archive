
import javax.microedition.lcdui.game.Sprite;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luciano
 */
public class ClasseAnimationThread implements Runnable {
	private Sprite sprite;
	private Thread thread;
	private int modo;
	
	private int andarEsquerdaSeq[] = {7,6,5,6};
	private int andarDireitaSeq [] = {0,1,2,1};
	private int paradoEsquerdaSeq[] = {4};
	private int paradoDireitaSeq[] = {3};
	
	// Definindo as constantes dos estados do jogador:
	private static final int andarEsquerda=1;
	private static final int andarDireita=2;
    private static final int paradoEsquerda=3;
    private static final int paradoDireita=4;

	public ClasseAnimationThread (Sprite s){
		sprite = s;
	}
	
	public void iniciaThread (){
		thread = new Thread(this);
		thread.start();
	}
	
	public void paraThread (){
		thread = null;
	}
	
	public void run() {
		while (thread == Thread.currentThread()){
			sprite.nextFrame();
			switch (modo){
				case andarEsquerda:
					sprite.setFrameSequence(andarEsquerdaSeq);
					break;
				case andarDireita:
					sprite.setFrameSequence(andarDireitaSeq);
					break;
				case paradoEsquerda:
					sprite.setFrameSequence(paradoEsquerdaSeq);
					break;
				case paradoDireita:
					sprite.setFrameSequence(paradoDireitaSeq);
					break;
				default:
					break;
			}
			try {
				Thread.sleep(50);
			}
			catch( InterruptedException e ){
			}
		}
	}
	
	public void setModo (int m){
		modo = m;
	}
}
