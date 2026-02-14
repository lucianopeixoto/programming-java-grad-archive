/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luciano
 */
public class ClasseProva {
	private String nomeProva, nomeAluno, nomeProfessor;
	private ClasseQuestao[] questao;
	private int pontuacao;

	public ClasseProva () {
		nomeProva = "Nova Prova";
		nomeAluno = "";
		nomeProfessor = "";
		pontuacao = 0;
		questao = new ClasseQuestao [50];
		questao[0] = new ClasseQuestao();
		questao[0] = null;
	}

	public ClasseProva (String txt) {
		nomeProva = txt;
		nomeAluno = "";
		nomeProfessor = "";
		pontuacao = 0;
		questao = new ClasseQuestao [50];
		questao[0] = new ClasseQuestao();
		questao[0] = null;
	}

	public ClasseProva (String txt, String prof) {
		nomeProva = txt;
		nomeAluno = "";
		nomeProfessor = prof;
		pontuacao = 0;
		questao = new ClasseQuestao [50];
		questao[0] = new ClasseQuestao();
		questao[0] = null;
	}

	public boolean addQuestao (String txt){
		int n = 0;
		while (questao[n] != null && n < 50) n++;
		if (n < 50) {
			questao[n] = new ClasseQuestao (txt);
			return true;
		} else return false;
	}

	public ClasseQuestao getQuestao (int n){
		return questao[n];
	}

	public String getNomeProva (){
		return nomeProva;
	}
	public String getNomeProfessor (){
		return nomeProfessor;
	}
	public void setNomeProfessor (String nome){
		nomeProfessor = nome;
	}
	public String getNomeAluno (){
		return nomeAluno;
	}
	public void setNomeAluno (String nome){
		nomeAluno = nome;
	}
	public int getPontuacao(){
		return pontuacao;
	}
	public void setPontuacao(int pnt){
		pontuacao = pnt;
	}
}