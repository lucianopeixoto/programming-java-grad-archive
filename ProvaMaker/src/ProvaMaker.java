/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;

/**
 * @author Luciano Carvalho Peixoto e Alexandre Ribeiro Lopes
 */
public class ProvaMaker extends MIDlet implements CommandListener {
    private Display tela;
	private Form	formMenuPrincipal,
					formQuestao,
					formComeco,
					formFinal,
					formProvaInfo,
					formQuestaoEdit;
	private Alert	alertFinalizar,
					alertSobre,
					alertQuestaoSemResposta,
					alertNaoLocalizada,
					alertAjudaGeral;
	private ChoiceGroup		grupoRespostas;
	private TextField	txtNomeAluno,
						txtNomeProfessor,
						txtNomeProva,
						txtPergunta,
						txtResposta[],
						txtValor[];
	private List	listIndice,			// indice no modo de resolução
					listIndiceEdit;		// indice no modo de edição
	private Command comandoProxima,
					comandoAnterior,
					comandoIndice,
					comandoConfirmaProva,
					comandoOk,
					comandoRespostaSim,
					comandoRespostaNao,
					comandoProva,
					comandoSair,
					comandoCriarProva,
					comandoResolverProva,
					comandoEditarProva,
					comandoAjuda,
					comandoSobre,
					comandoCancela,
					comandoAddQuestao;
	private ClasseProva prova;
	private int qId = 0, rId= 0;
	private RecordStore rs;
	private boolean questaoSemResposta;
	private Image	imageFeita,
					imageBranco,
					imageProvaMaker,
					imageItemMenu,
					imageSair,
					imageInfo,
					imageAjuda;
	private String provaStringada;
	private byte[] data;




	private boolean showQuestao() {
		if (prova.getQuestao(qId) != null && qId >= 0 && qId <50) {
			formQuestao.deleteAll();
			grupoRespostas.deleteAll();
			formQuestao.removeCommand(comandoAnterior);
			formQuestao.removeCommand(comandoProxima);
			if (qId != 0) formQuestao.addCommand(comandoAnterior);
			if (prova.getQuestao(qId+1) != null) formQuestao.addCommand(comandoProxima);

			rId = 0;
			tela.setCurrent(formQuestao);
			formQuestao.setTitle(prova.getNomeProva());
			formQuestao.append(new StringItem(((qId+1)+") "), prova.getQuestao(qId).getPergunta()));

			while (prova.getQuestao(qId).getResposta(rId) != null && rId < 8){
				grupoRespostas.append(prova.getQuestao(qId).getResposta(rId).getTexto(), null);
				rId ++;
			}
			if (prova.getQuestao(qId).getSelecionada() != -1){
				grupoRespostas.setSelectedIndex(prova.getQuestao(qId).getSelecionada(), true);
			}
			formQuestao.append(grupoRespostas);
			return true;
		} else return false;
	}

	private void showIndice () {
		qId=0;
		listIndice.deleteAll();
		tela.setCurrent(listIndice);
		while (prova.getQuestao(qId) != null && qId < 50){
			if (prova.getQuestao(qId).getSelecionada() == -1){
				listIndice.append(qId+1 + ") " + prova.getQuestao(qId).getPergunta(), imageBranco);
			}
			else {
				listIndice.append(qId+1 + ") " + prova.getQuestao(qId).getPergunta(), imageFeita);
			}

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

	private void showIndiceEdit () {
		qId=0;
		listIndiceEdit.deleteAll();
		tela.setCurrent(listIndiceEdit);
		while (prova.getQuestao(qId) != null && qId < 50){
			listIndiceEdit.append(qId+1 + ") " + prova.getQuestao(qId).getPergunta(), null);
			qId ++;
		}
	}

	private boolean showQuestaoEdit() {
		if (prova.getQuestao(qId) != null && qId >= 0 && qId <50) {
			formQuestaoEdit.deleteAll();
			rId = 0;
			tela.setCurrent(formQuestaoEdit);
			formQuestaoEdit.setTitle("Questão " + (qId+1));
			formQuestaoEdit.append(txtPergunta);
			txtPergunta.setString(prova.getQuestao(qId).getPergunta());

			while (prova.getQuestao(qId).getResposta(rId) != null && rId < 8){
				txtResposta[rId].setString(prova.getQuestao(qId).getResposta(rId).getTexto());
				formQuestaoEdit.append(txtResposta[rId]);
				txtValor[rId].setString(prova.getQuestao(qId).getResposta(rId).getPontos() + "");
				formQuestaoEdit.append(txtValor[rId]);
				rId ++;
			}
			return true;
		} else return false;
	}

	private String provaToString (ClasseProva provaOrigem) {
		// Convertendo uma prova em uma string grandona:
		// Instruções para a conversão:
		// stringResultante é a string resultante, provaOrigem é uma ClasseProva
		// qId é uma variavel auxiliar pra representar uma questao e rId
		// para representar uma resposta... Substitui aí pra ficar igual
		// as coisas no seu prog...:

		String stringResultante;

		stringResultante = new String("");
		stringResultante = stringResultante.concat("_prva_");
		stringResultante = stringResultante.concat(provaOrigem.getNomeProva());
		stringResultante = stringResultante.concat("_prof_");
		stringResultante = stringResultante.concat(provaOrigem.getNomeProfessor());
		qId = 0;
		while (provaOrigem.getQuestao(qId) != null && qId < 50) {
			rId=0;
			stringResultante = stringResultante.concat("_ques_" + provaOrigem.getQuestao(qId).getPergunta());
			while (provaOrigem.getQuestao(qId).getResposta(rId) != null && rId < 8) {
				stringResultante = stringResultante.concat("_resp_" + provaOrigem.getQuestao(qId).getResposta(rId).getTexto());
				stringResultante = stringResultante.concat("_pont_" + provaOrigem.getQuestao(qId).getResposta(rId).getPontos());
				rId ++;
			}
			qId ++;
		}
		stringResultante = stringResultante.concat("_fim__");
		return stringResultante;
	}

	public ClasseProva stringToProva (String stringOrigem) {
		ClasseProva provaResultante;
		String textoResposta;
		int pontosResposta = 0;

		System.out.println(stringOrigem+"\n");
		stringOrigem = stringOrigem.substring(6); // Corta logo após o _prva_
		System.out.println(stringOrigem+"\n");
		provaResultante = new ClasseProva (stringOrigem.substring(0, stringOrigem.indexOf('_')));
		stringOrigem = stringOrigem.substring(stringOrigem.indexOf('_')); // Corta apos o nome da prova
		System.out.println(stringOrigem+"\n");

		stringOrigem = stringOrigem.substring(6); // Corta logo após o _prof_
		System.out.println(stringOrigem+"\n");
		provaResultante.setNomeProfessor(stringOrigem.substring(0, stringOrigem.indexOf('_')));
		stringOrigem = stringOrigem.substring(stringOrigem.indexOf('_')); // Corta apos o nome do professor
		System.out.println(stringOrigem+"\n");
		qId = 0;

		while (!stringOrigem.equals("_fim__")){
			rId = 0;
			System.out.println("-> LOOP QUESTAO\n");
			stringOrigem = stringOrigem.substring(6); // Corta logo apos um _ques_
			System.out.println(stringOrigem+"\n");
			provaResultante.addQuestao(stringOrigem.substring(0, stringOrigem.indexOf('_')));
			stringOrigem = stringOrigem.substring(stringOrigem.indexOf('_')); // Corta apos a pergunta
			System.out.println(stringOrigem+"\n");

			while (stringOrigem.substring(0, 6).equals("_resp_")){
					System.out.println("-> LOOP RESPOSTA\n");
					stringOrigem = stringOrigem.substring(6); // Corta apos _resp_
					System.out.println(stringOrigem+"\n");
					textoResposta = stringOrigem.substring(0, stringOrigem.indexOf('_'));
					stringOrigem = stringOrigem.substring(stringOrigem.indexOf('_')); // Corta apos a Resposta
					System.out.println(stringOrigem+"\n");

					stringOrigem = stringOrigem.substring(6); // Corta apos _pont_
					System.out.println(stringOrigem+"\n");
					pontosResposta = Integer.parseInt(stringOrigem.substring(0, stringOrigem.indexOf('_')));
					stringOrigem = stringOrigem.substring(stringOrigem.indexOf('_')); // Corta apos os pontos
					System.out.println(stringOrigem+"\n");

					provaResultante.getQuestao(qId).addResposta(textoResposta, pontosResposta);
					rId ++;
					System.out.println("FIM LOOP RESPOSTA\n");
				}
			qId ++;
			System.out.println("FIM LOOP QUESTAO\n");
		}

		return provaResultante;
	}

	public void startApp() {

		//// Abrindo o Rercord Store (FINALMENTE!)
		//try {
		//	pmRecordStore = RecordStore.openRecordStore("ProvaMakerRS", "Vendor", "Midlet");
		//	}
		//catch (RecordStoreException ex) {
		//	ex.printStackTrace();
		//	}

		// Setando o display:
		tela = Display.getDisplay(this);

		System.out.println("Imagens...");
		// Setando as imagens
		try {
			imageFeita = Image.createImage("/Feita.png");
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		try {
			imageBranco = Image.createImage("/Branco.png");
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		try {
			imageProvaMaker = Image.createImage("/provamaker.png");
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		try {
			imageItemMenu = Image.createImage("/itemmenu.png");
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		try {
			imageSair = Image.createImage("/sair.png");
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		try {
			imageInfo = Image.createImage("/info.png");
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		try {
			imageAjuda = Image.createImage("/ajuda.png");
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		System.out.println("OK\n");

		System.out.println("Comandos...");
		// Inicializando os comandos:
		comandoRespostaSim = new Command ("Sim", Command.OK, 1);
		comandoRespostaNao = new Command ("Não", Command.CANCEL, 0);
		comandoOk = new Command ("Ok", Command.OK, 0);
		comandoProva = new Command ("Prova", Command.ITEM, 2);
		comandoSair = new Command ("Sair", Command.EXIT, 5);
		comandoProxima = new Command ("Proxima", Command.OK, 0);
		comandoAnterior = new Command ("Anterior", Command.OK, 1);
		comandoConfirmaProva = new Command ("Finalizar", Command.OK, 2);
		comandoIndice = new Command ("Indice", Command.OK, 2);
		comandoCriarProva = new Command ("Criar Prova", Command.ITEM, 2);
		comandoResolverProva = new Command ("Resolver Prova", Command.ITEM, 1);
		comandoEditarProva = new Command ("Remover Prova", Command.ITEM, 2);   // EDITAR PROVA, POR ENQUANTO REMOVE A PROVA APENAS
		comandoAjuda = new Command ("Ajuda", Command.HELP, 2);
		comandoSobre = new Command ("Sobre", Command.HELP, 2);
		comandoCancela = new Command ("Cancelar", Command.CANCEL, 2);
		comandoAddQuestao = new Command ("Adicionar Questão", Command.ITEM, 2);
		System.out.println("OK\n");

		// Inicializando os TextFields
		txtNomeAluno = new TextField ("Aluno:", "", 100,	TextField.ANY|
															TextField.SENSITIVE|
															TextField.INITIAL_CAPS_WORD);

		txtNomeProfessor = new TextField ("Professor:", "", 100,	TextField.ANY|
																	TextField.SENSITIVE|
																	TextField.INITIAL_CAPS_WORD);

		txtNomeProva = new TextField ("Título da prova:", "", 100,	TextField.ANY|
																	TextField.SENSITIVE|
																	TextField.INITIAL_CAPS_WORD);

		txtPergunta = new TextField ("Pergunta:", "", 500,	TextField.ANY|
															TextField.SENSITIVE|
															TextField.INITIAL_CAPS_SENTENCE);

		txtResposta = new TextField[8];

		for (rId = 0; rId <8; rId++) {
			txtResposta[rId]= new TextField ("Resposta" + (rId+1) + ":" , "", 500,	TextField.ANY|
																					TextField.SENSITIVE|
																					TextField.NON_PREDICTIVE|
																					TextField.INITIAL_CAPS_WORD);
		}

		txtValor = new TextField[8];

		for (rId = 0; rId <8; rId++) {
			txtValor[rId]= new TextField ("Valor:", "", 4,	TextField.NUMERIC);
		}




		System.out.println("formMenuPrincipal...");
		// Criando o formMenuPrincipal
		formMenuPrincipal = new Form("Prova Maker");
		formMenuPrincipal.setCommandListener(this);
		formMenuPrincipal.append(imageProvaMaker);
		formMenuPrincipal.addCommand (comandoCriarProva);
		formMenuPrincipal.addCommand (comandoEditarProva);
		formMenuPrincipal.addCommand (comandoResolverProva);
		formMenuPrincipal.addCommand (comandoAjuda);
		formMenuPrincipal.addCommand (comandoSobre);
		formMenuPrincipal.addCommand (comandoSair);
		System.out.println("OK\n");

		// Criando o formProvaInfo
		formProvaInfo = new Form ("Informações da prova");
		formProvaInfo.setCommandListener(this);
		formProvaInfo.addCommand(comandoCancela);
		formProvaInfo.addCommand(comandoOk);
		formProvaInfo.append(txtNomeProva);
		formProvaInfo.append(txtNomeProfessor);

		// Criando o formQuestao e seus comandos:
		formQuestao = new Form ("Prova Maker");
		formQuestao.setCommandListener(this);
		formQuestao.addCommand(comandoConfirmaProva);
		formQuestao.addCommand(comandoSair);

		// Criando o grupoRespostas:
		grupoRespostas = new ChoiceGroup ("Respostas:", Choice.EXCLUSIVE);

		// Criando o formFinal e seus comandos:
		formFinal = new Form ("PM - Resumo");
		formFinal.setCommandListener(this);
		formFinal.addCommand(comandoSair);

		// Criando o listIndice e seus comandos:
		listIndice = new List ("ProvaMaker - Indice", List.IMPLICIT);
		listIndice.setCommandListener(this);
		listIndice.setSelectCommand(comandoOk);
		listIndice.addCommand(comandoConfirmaProva);
		listIndice.addCommand(comandoProva);
		listIndice.addCommand(comandoSair);

		// Criando o listIndiceEdit e seus comandos:
		listIndiceEdit = new List ("Lista de questões", List.IMPLICIT);
		listIndiceEdit.setCommandListener(this);
		listIndiceEdit.setSelectCommand(comandoOk);
		listIndiceEdit.addCommand(comandoConfirmaProva);
		listIndiceEdit.addCommand(comandoProva);
		listIndiceEdit.addCommand(comandoSair);
		listIndiceEdit.addCommand(comandoAddQuestao);

		// Criando o formQuestaoEdit e seus comandos:
		formQuestaoEdit = new Form ("");
		formQuestaoEdit.setCommandListener(this);
		formQuestaoEdit.addCommand(comandoOk);
		formQuestaoEdit.addCommand(comandoCancela);

		// Criando o alertFinalizar e seus comandos:
		alertFinalizar = new Alert ("Confirmar Prova?",
									"Você gostaria de finalizar a prova agora?",
									null,
									AlertType.CONFIRMATION);
		alertFinalizar.setTimeout(Alert.FOREVER);
		alertFinalizar.setCommandListener(this);
		alertFinalizar.addCommand (comandoRespostaSim);
		alertFinalizar.addCommand (comandoRespostaNao);

		// Criando o alertNaoLocalizada
		alertNaoLocalizada = new Alert ("Prova não localizada!",
										"Nenhuma prova foi localizada no banco de dados.",
										null,
										AlertType.ERROR);
		alertFinalizar.setTimeout(6000);

		// Criando o formComeco e seus comandos:
		formComeco = new Form ("Prova Maker");
		formComeco.setCommandListener(this);
		formComeco.addCommand (comandoOk);
		formComeco.addCommand(comandoSair);

		// Criando o alertSobre e seus comandos:
		alertSobre = new Alert(	"Prova Maker 1.0",
								"Este Programa Foi desenvolvido por Alexandre Lopes e Luciano Peixoto",
								null,
								AlertType.INFO);
		alertSobre.setTimeout(Alert.FOREVER);
		alertSobre.setCommandListener(this);
		alertSobre.addCommand(comandoOk);

		// Criando o alertAjuda e seus comandos:
		alertAjudaGeral = new Alert(	"Ajuda",
										"Para criar uma prova você deve escolher a opção CRIAR PROVA e " +
										"adicionar as perguntas que desejar.\nPara responder a prova " +
										"basta escolher RESPONDER PROVA, inserir seu nome e responder" +
										"as perguntas",
										null,
										AlertType.INFO);
		alertAjudaGeral.setTimeout(Alert.FOREVER);
		alertAjudaGeral.setCommandListener(this);
		alertAjudaGeral.addCommand(comandoOk);

		// Criando o alertQuestaoSemResposta e seus comandos:
		alertQuestaoSemResposta = new Alert("Tem certeza?",
											"Alguma(as) questão(ões) ainda não foram respondidas, deseja realmente finalizar?",
											null,
											AlertType.CONFIRMATION);
		alertQuestaoSemResposta.setTimeout (Alert.FOREVER);
		alertQuestaoSemResposta.setCommandListener(this);
		alertQuestaoSemResposta.addCommand(comandoRespostaSim);
		alertQuestaoSemResposta.addCommand(comandoRespostaNao);


		tela.setCurrent (formMenuPrincipal);
	}

    public void pauseApp() {

    }

    public void destroyApp(boolean unconditional) {
		try {
			rs.closeRecordStore();
		}
		catch (RecordStoreException ex){
		}
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
			if (c == comandoProxima){
				prova.getQuestao(qId).setSelecionada(grupoRespostas.getSelectedIndex());
				if (prova.getQuestao(qId+1)!= null) qId ++;
				showQuestao ();
			}
			if (c == comandoAnterior){
				prova.getQuestao(qId).setSelecionada(grupoRespostas.getSelectedIndex());
				if (qId > 0) qId --;
				showQuestao ();
			}
			if (c == comandoIndice){
				prova.getQuestao(qId).setSelecionada(grupoRespostas.getSelectedIndex());
				showIndice ();
			}
			if (c == comandoConfirmaProva){
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
			if (c == comandoConfirmaProva){
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
			if (c == comandoRespostaSim)	{
				// Zerando qId e zerando a pontuação:
				qId = 0;
				prova.setPontuacao(0);
				questaoSemResposta = false;
				// Loop para somar os pontos:
				while (prova.getQuestao(qId) != null && qId < 50){
					if (prova.getQuestao(qId).getSelecionada() != -1){
						prova.setPontuacao(prova.getPontuacao() + prova.getQuestao(qId).getResposta(prova.getQuestao(qId).getSelecionada()).getPontos());
					}
					else {
						questaoSemResposta = true; // se alguma questao nao tiver sido respondida seta como TRUE
					}
					qId ++;
				}
				// Verifica se alguma questão ficou sem resposta ou não,
				// caso tem ficado, avisa ao usuario e pede outra confirmação:
				if (questaoSemResposta == true) tela.setCurrent(alertQuestaoSemResposta);
				else showFinal();
			}
			if (c == comandoRespostaNao)	{
				showIndice ();
			}
		}

		if (d == alertQuestaoSemResposta) {
			if (c == comandoRespostaNao) {
				showIndice ();
			}
			if (c == comandoRespostaSim) {
				showFinal ();
			}
		}

		if (d == formMenuPrincipal){
			if (c == comandoAjuda) {
				tela.setCurrent(alertAjudaGeral);
			}
			if (c == comandoCriarProva){
				prova = new ClasseProva("Nova Prova", "Professor");
				showIndiceEdit();
			}
			if (c == comandoResolverProva){
				rs = null;
				try {
					System.out.println("try");
					rs = RecordStore.openRecordStore("ProvaMakerRS", false);
					System.out.println("rs = ....");
					data = rs.getRecord(1);
					System.out.println("data = ...");
				}
				catch (RecordStoreException ex){
					System.out.println("CATCH!!!");
				}
				if (rs != null) {
					System.out.println("TESTE:  " + new String(data));
					System.out.println("Entrei no if");
					prova = stringToProva (new String (data));

					txtNomeAluno = new TextField ("Aluno:", prova.getNomeAluno(), 100,	TextField.ANY|
																						TextField.SENSITIVE|
																						TextField.NON_PREDICTIVE|
																						TextField.INITIAL_CAPS_WORD);
					formComeco.append (new StringItem("Prova: ",prova.getNomeProva()));
					formComeco.append (new StringItem("Professor: ",prova.getNomeProfessor()));
					formComeco.append (txtNomeAluno);

					tela.setCurrent(formComeco);
				}
				else tela.setCurrent(alertNaoLocalizada);
			}
			if (c == comandoEditarProva) {
				try {
					RecordStore.deleteRecordStore("ProvaMakerRS");
				}
				catch (RecordStoreException ex){
				}
			}
			if (c == comandoSobre){
				tela.setCurrent(alertSobre);
			}
			if (c == comandoSair) {
				notifyDestroyed();
			}
		}
		if (d == alertSobre) {
			if (c == comandoOk) {
				tela.setCurrent(formMenuPrincipal);
			}
		}
		if (d == listIndiceEdit) {
			if (c == comandoOk){
				qId = listIndiceEdit.getSelectedIndex();
				showQuestaoEdit();
			}
			if (c == comandoAddQuestao){
				prova.addQuestao("Texto da nova questão");
				qId=0;
				while(prova.getQuestao(qId+1) != null) qId ++;
				prova.getQuestao(qId).addResposta("Resposta 1", 0);
				prova.getQuestao(qId).addResposta("Resposta 2", 0);
				prova.getQuestao(qId).addResposta("Resposta 3", 0);
				prova.getQuestao(qId).addResposta("Resposta 4", 0);
				showQuestaoEdit();
			}
			if (c == comandoProva){
				txtNomeProva.setString(prova.getNomeProva());
				txtNomeProfessor.setString(prova.getNomeProfessor());
				tela.setCurrent(formProvaInfo);
			}
			if (c == comandoSair) {
				notifyDestroyed();
			}
			if (c == comandoConfirmaProva){
				try {
					System.out.println("try");
					rs=RecordStore.openRecordStore("ProvaMakerRS", true);
					System.out.println("rs= Reco....");
					provaStringada = provaToString(prova);
					rs.addRecord(provaStringada.getBytes(),0,provaStringada.getBytes().length);
					System.out.println("rs.addRec");
				}
				catch (RecordStoreException rse) {
					System.out.println("CATCH!");
					System.out.println(rse);
				}
				System.out.println("de boa?");
				tela.setCurrent(formMenuPrincipal);
			}
		}
		if (d == formProvaInfo) {
			if (c == comandoOk) {
				prova.setNomeProfessor(txtNomeProfessor.getString());
				prova.setNomeProva(txtNomeProva.getString());
				tela.setCurrent(listIndiceEdit);
			}
			if (c == comandoCancela){
				tela.setCurrent(listIndiceEdit);
			}
		}
		if (d == alertAjudaGeral){
			if (c == comandoOk){
				tela.setCurrent (formMenuPrincipal);
			}
		}
		if (d == formQuestaoEdit) {
			if (c == comandoOk) {
				prova.getQuestao(qId).setPergunta(txtPergunta.getString());
				rId=0;
				while (prova.getQuestao(qId).getResposta(rId) != null && rId < 8){
					prova.getQuestao(qId).getResposta(rId).setTexto(txtResposta[rId].getString());
					prova.getQuestao(qId).getResposta(rId).setPontos(Integer.parseInt(txtValor[rId].getString()));
					rId ++;
				}
				showIndiceEdit();
			}
			if (c == comandoCancela){
				showIndiceEdit();
			}
		}
    }
}