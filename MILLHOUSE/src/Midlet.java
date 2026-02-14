
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Item;
import javax.microedition.rms.*;


public class Midlet extends MIDlet implements CommandListener, ItemStateListener {
   private int x=1;
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
   private byte[] Enunc, Resp1, Resp2, Resp3, Opca;

   public Midlet () {

      AlertSobre=  new Alert("Prova Maker","Este Programa Foi desenvolvido por Alexandre Lopes e Luciano Peixoto",null,AlertType.INFO);
      AlertSobre.setTimeout(3000);
      AlertGra= new Alert(null,"Gravado Com Sucesso. Deseja Gravar outro?",null,AlertType.INFO);
      AlertGra.setTimeout(Alert.FOREVER);
      mainForm=new Form("Programa Maker");
      Form2= new Form("Questão "+x+"");
      Form3= new Form("Questão "+x+"");
      ComandoSair = new Command("Sair",Command.EXIT,0);
      ComandoCont = new Command("Continua",Command.OK,0);
      ComandoCont2 = new Command("Continua",Command.OK,0);
      ComandoSobre = new Command("Sobre",Command.HELP,0);
      ComandoGravar= new Command("Gravar",Command.OK,0);
      ComandoOutra= new Command("Adicionar Mais",Command.BACK,0);
      mainForm.addCommand(ComandoSair);
      mainForm.addCommand(ComandoCont);
      mainForm.addCommand(ComandoSobre);
      Form2.addCommand(ComandoSair);
      Form2.addCommand(ComandoCont2);
      Form3.addCommand(ComandoSair);
      Form3.addCommand(ComandoGravar);
      AlertGra.addCommand(ComandoOutra);
      AlertGra.addCommand(ComandoSair);
      mainForm.setCommandListener(this);
      Form2.setCommandListener(this);
      Form3.setCommandListener(this);
      Form2.setItemStateListener(this);
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
          //try{
         //     RecordStore.deleteRecordStore("QUESTAO");
         //     rs=RecordStore.openRecordStore("QUESTAO", true, 1, false);
    //      }
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
           resposta.deleteAll();
           resposta.append(""+resposta1.getString()+"", null);
           resposta.append(""+resposta2.getString()+"", null);
           resposta.append(""+resposta3.getString()+"", null);
           enunciado1.setText(enunciado.getString());
           d.setCurrent(Form3);
           }
           if (c==ComandoOutra){
               d.setCurrent(Form2);
               x=x+1;
               Form2.setTitle("Questão "+x+"");
               Form3.setTitle("Questão "+x+"");
           }
           if (c==ComandoGravar){
               try{
             String STR;
             STR=""+enunciado1.getText()+"";
               Enunc=STR.getBytes();
               rs.addRecord(Enunc, 0, Enunc.length);
               Resp1=resposta.getString(1).getBytes();
               rs.addRecord(Resp1, 0, Resp1.length);
               Resp2=resposta.getString(2).getBytes();
               rs.addRecord(Resp2, 0, Resp2.length);
               Resp3=resposta.getString(3).getBytes();
               rs.addRecord(Resp3, 0, Resp3.length);
               Opca=Integer.toString(resposta.getSelectedIndex()).getBytes();
               rs.addRecord(Opca, 0, Opca.length);


               d.setCurrent(AlertGra);
               }catch (RecordStoreException rse) {
          System.out.println(rse);
      }
           }
           }
         public void itemStateChanged(Item i) {
     if (i== resposta1){



     }
   }
}
