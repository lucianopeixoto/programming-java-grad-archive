/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elevador;

import javax.swing.*;    
import java.awt.*;

/**
 *
 * @author Luciano
 */
public class Elevador {

	/**
	 * @param args the command line arguments
	 */
	
	private static void criarMostrarJanela() {
		// Variáveis:
		JFrame janela;
		JLabel label1;
		JMenuBar barraMenuPrincipal;
		JMenu menuArquivo;
		Dimension resolucao;
        
		// Criar e configurar a janela:
		janela = new JFrame();
		janela.setName ("Elevador");
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // EXEMPLO: Criando a label1 e adicionando ela ao frame1:
		label1 = new JLabel();
		label1.setName("Elevador");
		label1.setText("By Luciano Peixoto");
		janela.getContentPane().add(label1);
		
		// Criar e adicionar o menu a tela:
		barraMenuPrincipal = new JMenuBar();
		menuArquivo = new JMenu();
		barraMenuPrincipal.add(menuArquivo);
		menuArquivo.setName("Arquivo");
		menuArquivo.setText("Arquivo");
		janela.setJMenuBar (barraMenuPrincipal);

        // Mostrar a janela:
		resolucao = new Dimension();
		resolucao = Toolkit.getDefaultToolkit().getScreenSize();
		janela.setSize(800, 600);
		janela.setLocation(((resolucao.width)/2)-(janela.getWidth()/2), ((resolucao.height)/2)-(janela.getHeight()/2));
		janela.setResizable(false);
        janela.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                criarMostrarJanela();
            }
        });
    }
	
}