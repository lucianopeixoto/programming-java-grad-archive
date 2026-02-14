/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author Luciano Carvalho Peixoto
 */
public class ProvaMaker extends MIDlet implements CommandListener {
    private Display tela;
	private Form formQuestao, formComeco, formFinal;
	private ChoiceGroup grupoRespostas;
	private TextField txtNomeAluno;
	private List listIndice;
	private Alert alertFinalizar;
	private Command proximaQuestao, questaoAnterior, indiceQuestoes, confirmaProva, comandoOk, respostaSim, respostaNao, comandoProva, comandoSair;
	private ClasseProva prova;
	private int qId = 0, rId= 0;



	private boolean showQuestao() {
		if (prova.getQuestao(qId) != null && qId >= 0 && qId <50) {
			formQuestao.deleteAll();
			grupoRespostas.deleteAll();
			formQuestao.removeCommand(questaoAnterior);
			formQuestao.removeCommand(proximaQuestao);
			if (qId != 0) formQuestao.addCommand(questaoAnterior);
			if (prova.getQuestao(qId+1) != null) formQuestao.addCommand(proximaQuestao);
			
			rId = 0;
			tela.setCurrent(formQuestao);
			formQuestao.append(new StringItem(prova.getNomeProva(),""));
			formQuestao.append(new StringItem(((qId+1)+") "), prova.getQuestao(qId).getPergunta()));

			while (prova.getQuestao(qId).getResposta(rId) != null && rId < 8){
				grupoRespostas.append(prova.getQuestao(qId).getResposta(rId).getTexto(), null);
				rId ++;
			}
			grupoRespostas.setSelectedIndex(prova.getQuestao(qId).getSelecionada(), true);
			formQuestao.append(grupoRespostas);
			return true;
		} else return false;
	}

	private void showIndice () {
		qId=0;
		listIndice.deleteAll();
		tela.setCurrent(listIndice);
		while (prova.getQuestao(qId) != null && qId < 50){
			listIndice.append(prova.getQuestao(qId).getPergunta(), null);
			qId ++;
		}
	}

	private void showFinal () {
		formFinal.append(new StringItem ("Aluno: ", prova.getNomeAluno()));
		formFinal.append(new StringItem ("Prova: ", prova.getNomeProva()));
		formFinal.append(new StringItem ("Professor: ", prova.getNomeProfessor()));
		formFinal.append(new StringItem ("Pontuação: ", prova.getPontuacao() + " Pontos"));
		tela.setCurrent(formFinal);
	}

	public void startApp() {

		// Setando o display:
		tela = Display.getDisplay(this);

		// Criando o formQuestao e seus comandos:
		formQuestao = new Form ("PM - Respondendo");
		formQuestao.setCommandListener(this);
		grupoRespostas = new ChoiceGroup ("Respostas:", Choice.EXCLUSIVE);
		proximaQuestao = new Command ("Proxima", Command.OK, 0);
		questaoAnterior = new Command ("Anterior", Command.OK, 1);
		comandoSair = new Command ("Sair", Command.EXIT, 5);
		confirmaProva = new Command ("Finalizar", Command.OK, 2);
		indiceQuestoes = new Command ("Indice", Command.OK, 2);
		formQuestao.addCommand(confirmaProva);
		formQuestao.addCommand(indiceQuestoes);
		formQuestao.addCommand(comandoSair);

		// Criando o formFinal e seus comandos:
		formFinal = new Form ("PM - Resumo");
		formFinal.addCommand(comandoSair);
		formFinal.setCommandListener(this);
		


		// Criando o listIndice e seus comandos:
		listIndice = new List ("PM - Indice", List.IMPLICIT);
		listIndice.setCommandListener(this);
		comandoOk = new Command ("Ok", Command.OK, 0);
		comandoProva = new Command ("Prova", Command.ITEM, 2);
		listIndice.addCommand(comandoOk);
		listIndice.addCommand(confirmaProva);
		listIndice.addCommand(comandoProva);
		listIndice.addCommand(comandoSair);

		// Criando o alertFinalizar e seus comandos:
		alertFinalizar = new Alert ("Confirmar Prova?", "Você gostaria de finalizar a prova agora?", null, AlertType.CONFIRMATION);
		alertFinalizar.setCommandListener(this);
		respostaSim = new Command ("Sim", Command.OK, 1);
		respostaNao = new Command ("Não", Command.CANCEL, 0);
		alertFinalizar.addCommand (respostaSim);
		alertFinalizar.addCommand (respostaNao);

		// Criando o formComeco e seus comandos:
		formComeco = new Form ("Prova Maker");
		formComeco.setCommandListener(this);
		formComeco.addCommand (comandoOk);
		formComeco.addCommand(comandoSair);
		
		// Parte reservada para se criar uma prova de exemplo (Furutamente ser substituida pela funcionalidade de carregar uma prova):
		// COMEÇO DA PARTE RESERVADA -----
		prova = new ClasseProva("Prova de S.E.");
		prova.setNomeProfessor("Jorge Campos");
		prova.addQuestao("1 + 1:");
		prova.getQuestao(0).addResposta("4", 0);
		prova.getQuestao(0).addResposta("3", 0);
		prova.getQuestao(0).addResposta("1", 0);
		prova.getQuestao(0).addResposta("2", 10);
		prova.addQuestao("1 - 1:");
		prova.getQuestao(1).addResposta("10", 0);
		prova.getQuestao(0).addResposta("0", 10);
		prova.addQuestao("Isso é uma pergunta?");
		prova.getQuestao(2).addResposta("Não", 0);
		prova.getQuestao(2).addResposta("Sim", 10);
		prova.getQuestao(2).addResposta("Talvez", 5);
		prova.getQuestao(2).addResposta("Sim! E isso é uma resposta.", 15);
		// FIM -----

		// Adiciondo itens ao formComeco e mostrando-o
		txtNomeAluno = new TextField ("Aluno:", prova.getNomeAluno(), 100, TextField.ANY&TextField.SENSITIVE&TextField.NON_PREDICTIVE&TextField.INITIAL_CAPS_WORD);
		formComeco.append (new StringItem("Prova: ",prova.getNomeProva()));
		formComeco.append (new StringItem("Professor: ",prova.getNomeProfessor()));
		formComeco.append (txtNomeAluno);

		tela.setCurrent(formComeco);
		
	}

    public void pauseApp() {
		
    }

    public void destroyApp(boolean unconditional) {

    }

    public void commandAction(Command c, Displayable d) {
		if (d == formComeco) {
			if (c == comandoOk)	{
				prova.setNomeAluno(txtNomeAluno.getString());
				showIndice();
			}
			if (c == comandoSair){
				 notifyDestroyed();
			}
		}
		if (d == formFinal) {
			if (c == comandoSair){
				 notifyDestroyed();
			}
		}
		if (d == formQuestao){
			if (c == proximaQuestao){
				prova.getQuestao(qId).setSelecionada(grupoRespostas.getSelectedIndex());
				if (prova.getQuestao(qId+1)!= null) qId ++;
				showQuestao ();
			}
			if (c == questaoAnterior){
				prova.getQuestao(qId).setSelecionada(grupoRespostas.getSelectedIndex());
				if (qId > 0) qId --;
				showQuestao ();
			}
			if (c == indiceQuestoes){
				prova.getQuestao(qId).setSelecionada(grupoRespostas.getSelectedIndex());
				showIndice ();
			}
			if (c == confirmaProva){
				prova.getQuestao(qId).setSelecionada(grupoRespostas.getSelectedIndex());
				tela.setCurrent(alertFinalizar);
			}
			if (c == comandoSair){
				 notifyDestroyed();
			}
		}
		if (d == listIndice) {
			if (c == comandoOk)	{
				qId = listIndice.getSelectedIndex();
				showQuestao();
			}
			if (c == confirmaProva){
				tela.setCurrent(alertFinalizar);
			}
			if (c == comandoProva){
				tela.setCurrent(formComeco);
			}
			if (c == comandoSair){
				 notifyDestroyed();
			}
		}
		if (d == alertFinalizar) {
			if (c == respostaSim)	{
				qId = 0;
				while (prova.getQuestao(qId) != null && qId < 50){
				prova.setPontuacao(prova.getPontuacao() + prova.getQuestao(qId).getResposta(prova.getQuestao(qId).getSelecionada()).getPontos());
				qId ++;
				}
			showFinal();
			}
			if (c == respostaNao)	{
				showIndice ();
			}
		}
    }
}