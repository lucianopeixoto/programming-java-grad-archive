/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;


/**
 * @author Luciano Peixoto
 */
public class FallingWorm extends MIDlet {

	private Display tela;
	private ClasseJogo jogo;


	public void startApp() {
		tela = Display.getDisplay(this);
		jogo = new ClasseJogo();
		tela.setCurrent(jogo);
    }

	public void pauseApp() {
    }

	public void destroyApp(boolean unconditional) {
    }
}
