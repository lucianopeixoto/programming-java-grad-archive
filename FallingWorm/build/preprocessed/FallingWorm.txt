/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;


/**
 * @author Luciano Peixoto
 */
public class FallingWorm extends MIDlet implements CommandListener{

	private Display tela;
	private ClasseJogo jogo;
	private Command comandoPausa,
					comandoNovo;
	private Form formMenu;

    public void startApp() {
		tela = Display.getDisplay(this);
		comandoPausa = new Command ("Pausa", Command.STOP, 0);
		comandoNovo = new Command ("Novo Jogo", Command.OK, 0);
		formMenu = new Form("Falling Worm");
		formMenu.setCommandListener(this);
		formMenu.addCommand(comandoNovo);
		jogo = new ClasseJogo();
		tela.setCurrent(jogo);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

	public void commandAction(Command c, Displayable d) {
		if (d == formMenu){
			if (c == comandoNovo){
				tela.setCurrent(jogo);
			}
		}
	}
}
