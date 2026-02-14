/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luciano
 */
public class ClasseResposta {
	// Atributos
	private String texto;	// Texto da resposta
	private int pontos;		// Quanto o cara ganha se escolher aquela

	// Métodos Construtores
	public ClasseResposta (){
		texto = "Isso é uma resposta!";
		pontos = 0;
	}
	public ClasseResposta (String txt){
		texto = txt;
		pontos = 0;
	}
	public ClasseResposta (String txt, int pnt){
		texto = txt;
		pontos = pnt;
	}

	// Métods de Acesso/Modificação
	public void setTexto (String txt){
		texto = txt;
	}
	public String getTexto (){
		return texto;
	}
	public void setPontos (int pnt){
		pontos = pnt;
	}
	public int getPontos (){
		return pontos;
	}
}
