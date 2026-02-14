/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luciano
 */
public class ClasseQuestao {
	// Atributos
	private String pergunta;			// Texto da pergunta
	private ClasseResposta[] resposta;	// Vetor de opçõesde resposta
	private int selecionada;			// INT representando a resposta que esta selecionada atualmente

	// Métodos Construtores
	public ClasseQuestao (){
		int n;

		selecionada = -1;
		pergunta = "Seria isso uma pergunta?";
		resposta = new ClasseResposta[8];
		for (n = 0; n<8; n++){
			resposta[n] = new ClasseResposta();
		}
	}

	public ClasseQuestao (String txt){
		selecionada = -1;
		pergunta = txt;
		resposta = new ClasseResposta[8];
		resposta[0] = null;
	}

	
	// Métodos de Acesso/Modificação
	public void setPergunta (String txt){
		pergunta = txt;
	}
	public String getPergunta (){
		return pergunta;
	}

	public boolean addResposta (String txt, int pnt){
		int n = 0;
		while (resposta [n] != null && n < 8) n++;
		if (n < 8) {
			resposta[n] = new ClasseResposta (txt, pnt);
			return true;
		} else return false;
	}
	public ClasseResposta getResposta(int r){
		if (r < 8) return resposta [r];
		else return resposta [0];
	}
	public boolean dltResposta (int rId) {
		if (rId >= 0 && rId <=7) {
			while (rId < 7) {
				resposta [rId] = resposta [rId+1];
				rId ++;
			}
			resposta [rId] = null;
			return true;
		} else return false;
	}
	public void setSelecionada(int sel){
		selecionada = sel;
	}
	public int getSelecionada (){
		return selecionada;
	}
}
