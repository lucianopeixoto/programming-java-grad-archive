/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Item;
import javax.microedition.rms.*;


public class Midlet extends MIDlet implements CommandListener {
   private int x=1, j=0, qId, rId;
   private Form mainForm, Form2, Form3;
   private Display d;
   private Command ComandoSair;
   private Command ComandoCont, ComandoCont2, ComandoOutra;
   private Command ComandoSobre, ComandoGravar;
   private StringItem strItem1;
   private TextField enunciado, resposta1, resposta2, resposta3;
   private StringItem enunciado1;
   private Alert AlertSobre, AlertGra;
   private ChoiceGroup resposta;
   private RecordStore rs;
   private ClasseProva Prova;
   private String provaStringada;

   public Midlet () {
      Prova=new ClasseProva("Prova");
      AlertSobre=  new Alert("Prova Maker","Este Programa Foi desenvolvido por Alexandre Lopes e Luciano Peixoto",null,AlertType.INFO);
      AlertSobre.setTimeout(3000);
      AlertGra= new Alert(null,"Obrigrado pela preferencia e para aplicar a prova utilize o Prova Maker 2",null,AlertType.INFO);
      AlertGra.setTimeout(Alert.FOREVER);
      mainForm=new Form("Programa Maker");

      Form2= new Form("Questão "+x+"");
      Form3= new Form("Questão "+x+"");

      ComandoSair = new Command("Sair",Command.EXIT,0);
      ComandoCont = new Command("Continua",Command.OK,0);
      ComandoCont2 = new Command("Continua",Command.OK,0);
      ComandoSobre = new Command("Sobre",Command.HELP,0);
      ComandoGravar= new Command("Finalizar",Command.OK,0);
      ComandoOutra= new Command("Adicionar Mais",Command.BACK,0);
      mainForm.addCommand(ComandoSair);
      mainForm.addCommand(ComandoCont);
      mainForm.addCommand(ComandoSobre);
      Form2.addCommand(ComandoSair);
      Form2.addCommand(ComandoCont2);
      Form3.addCommand(ComandoSair);
      Form3.addCommand(ComandoGravar);
      Form3.addCommand(ComandoOutra);
      AlertGra.addCommand(ComandoSair);
      mainForm.setCommandListener(this);
      Form2.setCommandListener(this);
      Form3.setCommandListener(this);

      AlertGra.setCommandListener(this);
      strItem1=new StringItem("Intruções","\n Coloque o enunciado da questão e as opções de resposta. Depois, confira o enunciado e escolha a resposta correta. Se desejar mais questões, clique em (Adicionar Mais) e grave.",StringItem.PLAIN);
      strItem1.setLayout(Item.LAYOUT_2);
      enunciado=new TextField("Enunciado","Digite o Enunciado Aqui \n \n",200,TextField.ANY);
      enunciado.setLayout(Item.LAYOUT_2);
      enunciado.setPreferredSize(-1, 80);
      enunciado1= new StringItem(null,""+enunciado.getString()+"",StringItem.PLAIN);
      enunciado1.setLayout(Item.LAYOUT_2);
      enunciado1.setPreferredSize(-1, 80);
       resposta1=new TextField("Respostas:","RESPOSTA 1",20,TextField.ANY);
       resposta2=new TextField(null,"RESPOSTA 2",20,TextField.ANY);
       resposta3=new TextField(null,"RESPOSTA 3",20,TextField.ANY);




      resposta=new ChoiceGroup(null,Choice.EXCLUSIVE);
      resposta.setLayout(Item.LAYOUT_2);

      mainForm.append(strItem1);
      Form2.append(enunciado);
      Form2.append(resposta1);
      Form2.append(resposta2);
      Form2.append(resposta3);
      Form3.append(enunciado1);
      Form3.append(resposta);


      }
      public void startApp() {



         d=Display.getDisplay(this);
         d.setCurrent(mainForm);
      }
      public void pauseApp() { }
      public void destroyApp(boolean unconditional) { }
      public void commandAction(Command c, Displayable s) {
	   if (c== ComandoSair) {
               notifyDestroyed();
              try {
        rs.closeRecordStore();
      } catch (Exception e) {
      }

           }
           if (c== ComandoCont) d.setCurrent(Form2);
           if (c== ComandoSobre) d.setCurrent(AlertSobre);
           if (c== ComandoCont2) {
           Prova.addQuestao(""+enunciado.getString()+"");
           Prova.getQuestao(j).addResposta(""+resposta1.getString()+"", 0);
           Prova.getQuestao(j).addResposta(""+resposta2.getString()+"", 0);
           Prova.getQuestao(j).addResposta(""+resposta3.getString()+"", 0);
           resposta.deleteAll();
           resposta.append(""+resposta1.getString()+"", null);
           resposta.append(""+resposta2.getString()+"", null);
           resposta.append(""+resposta3.getString()+"", null);
           enunciado1.setText(enunciado.getString());
           d.setCurrent(Form3);
           }
           if (c==ComandoOutra){

             Prova.getQuestao(j).getResposta(resposta.getSelectedIndex()).setPontos(10);


               d.setCurrent(Form2);
               x=x+1;
               j=j+1;
               Form2.setTitle("Questão "+x+"");
               Form3.setTitle("Questão "+x+"");
           }
           if (c==ComandoGravar){


            Prova.getQuestao(j).getResposta(resposta.getSelectedIndex()).setPontos(10);




             d.setCurrent(AlertGra);
             try {
             rs=RecordStore.openRecordStore("ProvaMakerRS", true, RecordStore.AUTHMODE_ANY, true);
            
            provaStringada=provaToString(Prova);
            rs.addRecord(provaStringada.getBytes(),0,provaStringada.getBytes().length);


              }
         catch (RecordStoreException rse) {
            System.out.println(rse);
         }




   }
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
}
