/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hello;

/**
 *
 * @author pliniobs
 */
 import javax.microedition.midlet.MIDlet;
 import javax.microedition.lcdui.game.*;
 import javax.microedition.lcdui.*;
 import java.util.Random;
 import javax.microedition.rms.RecordStore;
 
 public class GamePlinio extends GameCanvas implements Runnable {
     private RecordStore recordStore = null;

     /* Constantes gerais */
     int DIREITA = 1, ESQUERDA = 2, ACELERA = 3, DESACELERA = 4;
     /* */
     /* Atributos referente a nave principal */
     Sprite Nave_Princ, Power;
     int Power_cont = 5; // Não é permitido que mais de 5 aeronaves inimigas escapem
     Sprite Tiro;
     int Tiro_Y,Tiro_X, Tiro_Step = 0;
     Image Aviao_Princ, Power_bar;
     Image Tiro_Img;
     int Nave_Princ_X = 100, Nave_Princ_Y = 200; // Definem a posição da nave na tela
     boolean Flag_Atira = false;
     /* Fim atributos Nave principal*/

     /* Atributo referentes as naves inimigas*/
     int Inim_x, Inim_y = -40; // Posição x e y dos inimigos
     int Inimigo_Step = 0;
     Sprite Inim[] = new Sprite[5];
     Image Inim_Img[] = new Image[5];
     int Inimigo;   // numero randomico a ser gerado
     int Ini_Dificuldade = 3; // nivel de dificuldade do jogo controlado automaticamente com o passar do tempo
     /* Fim atributos naves inimigas*/

     /* Atributos referentes as explosoes*/
     Sprite Explosao1;
     Image Exp1;
     Sprite Explosao2;
     Image Exp2;
     int Exp_Cont = 0;
     /* Fim atributos referentes as explosoes*/


     /* atributos referentes ao back ground*/
     Image  Backg, Logo;
     TiledLayer BackGnd;
     int Tile_Anim1, Tile_Anim2, Cont_Anim;
     /* fim atributos referentes ao back ground*/

     Image Game_Over;
     Sprite GameOver;
     Thread Principal = new Thread(this);
     boolean Fim_Jogo = true;
     int Pontuacao = 0, Game_Speed = 10;

     LayerManager lm =new LayerManager();
     Graphics GraficoJogo = this.getGraphics();
     
     public GamePlinio(){

         super(false);

         Init_Records();

         try{
             // Inicialização dos graficos referentes a nave princiapal
             Aviao_Princ = Aviao_Princ.createImage("/Aviao_Principal.png");
             Nave_Princ =  new Sprite(Aviao_Princ,60,46);
             Nave_Princ.setRefPixelPosition(0,0);
             Nave_Princ.setVisible(true);
             Power_bar = Power_bar.createImage("/power.png");
             Power = new Sprite(Power_bar,53,12);
             Power.setVisible(true);
             Power.setPosition(180, 3);

             Tiro_Img = Tiro_Img.createImage("/Tiro.png");
             Tiro = new Sprite(Tiro_Img,17,16);
             Tiro.setRefPixelPosition(0,0);
             Tiro.setVisible(false);
             // Fim inicialização dos graficos da nave principal

             // Inicialização dos graficos referentes as naves inimigas;
             Inim_Img[0] = Inim_Img[0].createImage("/Inim_1.png");
             Inim[0] = new Sprite(Inim_Img[0],32,33);
             Inim[0].setRefPixelPosition(0,0);

             Inim_Img[1] = Inim_Img[1].createImage("/Inim_2.png");
             Inim[1] = new Sprite(Inim_Img[1],32,33);
             Inim[1].setRefPixelPosition(0,0);

             Inim_Img[2] = Inim_Img[2].createImage("/Inim_3.png");
             Inim[2] = new Sprite(Inim_Img[2],32,33);
             Inim[2].setRefPixelPosition(0,0);

             Inim_Img[3] = Inim_Img[3].createImage("/Inim_4.png");
             Inim[3] = new Sprite(Inim_Img[3],32,33);
             Inim[3].setRefPixelPosition(0,0);

             Inim_Img[4] = Inim_Img[4].createImage("/Inim_5.png");
             Inim[4] = new Sprite(Inim_Img[4],32,33);
             Inim[4].setRefPixelPosition(0,0);

             for(int i=0;i<5;i++){
                Inim[i].setVisible(false);
                Inim[i].setTransform(Sprite.TRANS_ROT270);
             }
             // Fim inicialização dos graficos das naves inimigas

             // inicialização das explosoes e inimação game over
             Exp1 = Exp1.createImage("/Explosao.png");
             Explosao1 = new Sprite(Exp1,33,27);
             Explosao1.setVisible(false);

             Exp2 = Exp2.createImage("/Explosao2.png");
             Explosao2 = new Sprite(Exp2,64,52);
             Explosao2.setVisible(false);

             //Logo_Game = Logo_Game.createImage("/tiro.png");
             Game_Over = Game_Over.createImage("/Game_Over.png");
             GameOver = new Sprite(Game_Over,96,13);
             GameOver.setVisible(false);

             // Inicialização do background
             Logo = Logo.createImage("/Logo.png");
             Backg = Backg.createImage("/BackGround.png");
             BackGnd = new TiledLayer(8,9,Backg,32,32);
             Tile_Anim1 = BackGnd.createAnimatedTile(1);
             Tile_Anim2 = BackGnd.createAnimatedTile(2);
             boolean Tile_Flag = false;
             for(int C = 0; C < 8; C++)
                 for(int L = 0; L < 9; L++){
                     if(Tile_Flag){
                        BackGnd.setCell(C, L, Tile_Anim1);
                        Tile_Flag = false;
                     }
                     else{
                        BackGnd.setCell(C, L, Tile_Anim2);
                        Tile_Flag = true;
                     }
                 }
             //BackGnd.setVisible(true);
             
             BackGnd.setPosition(0, 0);

             
         }catch(Exception ex){
             ex.printStackTrace();}

         //Principal = new Thread(this);
         //Principal.start();
         Mostra_Splash();
     }
     public void Inicia_Jogo(){
         Finaliza_Jogo();
         Nave_Princ_X = 100;
         Nave_Princ_Y = 250;
         Pontuacao = 0;
         Game_Speed = 10;
         Ini_Dificuldade = 3;
         Power_cont = 5;
         Power.setFrame(0);
         Fim_Jogo = false;
         Principal = new Thread(this);
         Principal.start();
         Explosao1.setVisible(false);
         Explosao2.setVisible(false);
         Nave_Princ.setVisible(true);
         GameOver.setVisible(false);
         for(int i=0;i<5;i++)
             Inim[i].setVisible(false);
     }
     public void Finaliza_Jogo(){
         Fim_Jogo = true;
     }
     public void run(){
         int Tecla;
         while(Fim_Jogo == false){
             Tecla = getKeyStates();
             switch(Tecla){
                 case UP_PRESSED:
                     Movimenta_Nave(ACELERA);
                     break;
                 case DOWN_PRESSED:
                     Movimenta_Nave(DESACELERA);
                     break;
                 case LEFT_PRESSED:
                     Movimenta_Nave(ESQUERDA);
                     break;
                 case RIGHT_PRESSED:
                     Movimenta_Nave(DIREITA);
                     break;
                 case FIRE_PRESSED:
                     Flag_Atira = true;
                     break;
             }
             try{
                 Limpa_Tela();           
                 Atualiza_Graficos();
                 Thread.sleep(Game_Speed);
             }catch (Exception minhaExcessao){
                 minhaExcessao.printStackTrace();
             }finally{
                 flushGraphics();
             }
         }
     }

     public void Movimenta_Nave(int Direcao){
         switch (Direcao){
             case 1:    //Movimenta para a direita
                 if((Nave_Princ_X + 60)< this.getWidth())
                     Nave_Princ_X += 5;
                 break;
             case 2:    //Movimenta para a esquerda
                 if(Nave_Princ_X > 0)
                    Nave_Princ_X -= 5;
                 break;
             case 3:    //Acelera!
                 if(Nave_Princ_Y > 0)
                     Nave_Princ_Y -= 5;
                 break;
             case 4:    //Desacelera
                 if((Nave_Princ_Y + 46)< this.getHeight())
                     Nave_Princ_Y += 5;
                 break;
         }

         // Tentar mudar posição do sprite aqui...
     }
    public void Limpa_Tela(){
        GraficoJogo.setColor(255, 255, 255);   // Ajusta o Fundo da tela para cor branca
        GraficoJogo.fillRect(0, 0, this.getWidth(), this.getHeight()); // Desenha um retangulo tomando a tela toda
        GraficoJogo.setColor(0, 0, 0); //Ajusta a cor padrão preto
     }
    public void Atualiza_Graficos(){
        
        GraficoJogo.drawString("Score: " + Pontuacao, 0, 0, Graphics.TOP|Graphics.LEFT);
        
        // Atualiza graficos do background
        Atualiza_BackGround();


        // Atualiza os graficos da nave principal
        
        Nave_Princ.setPosition(Nave_Princ_X,Nave_Princ_Y);
        Nave_Princ.nextFrame();

        
        //Atualiza os graficos referentes ao disparo da nave
        Atira();
        Tiro.setPosition(Tiro_X,Tiro_Y);
        

        //Atualiza os graficos referentes aos inimigos;
        Maquina_Inimigos();

        //Posiciona todos os graficos no Layer Manager

        //lm.append(BackGnd);
        lm.append(Nave_Princ);
        lm.append(Power);
        lm.append(Tiro);
        lm.append(Inim[Inimigo]);
        lm.append(Explosao1);
        lm.append(Explosao2);
        lm.append(GameOver);
        BackGnd.setPosition(0, 17);
        BackGnd.paint(GraficoJogo);
        lm.setViewWindow(0, 0, this.getWidth(), this.getHeight());
        lm.paint(GraficoJogo, 0, 0);
        

    }
    public void Atira(){
        if (Flag_Atira ==  true){
            switch(Tiro_Step){ // Maquina de estado para a execução do tiro da nave
                case 0: // Calcula a posição do Sprite do tiro
                    
                    Tiro_X = Nave_Princ_X + 21; // Centraliza na frente da do sprite da nave principal
                    Tiro_Y = Nave_Princ_Y; // Posiciona o sprite na frente da Nave

                    System.out.println(Tiro_Y);
                    Tiro.setVisible(true);
                    Tiro_Step++;

                    break;
                case 1: // Efetua o movimento do sprite
                    if(Tiro_Y > 0)
                        Tiro_Y -= 10;
                    else
                        Tiro_Step++;
                    break;

                case 2: // Final da animação do tiro
                    Tiro.setVisible(false);
                    Tiro_Step = 0;
                    Flag_Atira = false;
                    break;
            }
        }
        else{
            Tiro_Step = 0;
            Tiro.setVisible(false);
        }
    }
    public void Maquina_Inimigos(){ // Maquina para gerenciamento dos sprites dos inimigos
        Random generator = new Random();
        generator.setSeed(System.currentTimeMillis());

        switch(Inimigo_Step){
            case 0: // Verifica qual sprite deve ser exibido e sua pos. X na tela
                Inim_y = 0;
                Inimigo = generator.nextInt()%4;
                if(Inimigo < 0) Inimigo *= -1;
                Inim_x = generator.nextInt()%180;
                if(Inim_x < 0) Inim_x *= -1;

                System.out.println(Inimigo);
                System.out.println(Inim_x);

                Inim[Inimigo].setPosition(Inim_x,Inim_y);
                Inim[Inimigo].setVisible(true);
                Inimigo_Step ++;
                break;
            case 1:
                Inim[Inimigo].setPosition(Inim_x,Inim_y);
                Inim_y += Ini_Dificuldade;
                if(Inim_y > 340){
                    Inimigo_Step =  0;
                    Inim_y = 0;
                    
                    Power_cont --;
                    if(Power_cont == 0)
                        Inimigo_Step = 10;
                    else
                        Power.nextFrame();


                }
                else if(Inim[Inimigo].collidesWith(Nave_Princ, false)){
                   System.out.println("Colidiu com a nave princip");
                   Inimigo_Step = 10; // Informa Fim de jogo!!!!
                }
                else if(Inim[Inimigo].collidesWith(Tiro, false)){
                   System.out.println("Colidiu com tiro");
                   Inimigo_Step = 20; // Destroi nave inimiga
                }
                break;
            case 10: // Fim de jogo;
                Nave_Princ.setVisible(false);
                Explosao2.setVisible(true);
                Explosao2.setPosition(Nave_Princ_X,Nave_Princ_Y);
                Explosao2.nextFrame();
                Exp_Cont++;
                GameOver.setVisible(true);
                GameOver.setPosition(70,130);
                GameOver.nextFrame();
                Game_Speed = 100;
                if(Exp_Cont == 20){
                    Exp_Cont = 0;
                    Finaliza_Jogo();
                    Verifica_Record(Pontuacao);
                    Inimigo_Step = 0;
                }


                break;
            case 20: // Destroi nave inimiga e gera uma animação de explosao
                Flag_Atira = false;
                Inim[Inimigo].setVisible(false);
                Explosao1.setVisible(true);
                Explosao1.setPosition(Inim_x,Inim_y);
                Explosao1.nextFrame();
                Exp_Cont ++;
                if(Exp_Cont == 5){
                   Exp_Cont = 0;
                   Explosao1.setVisible(false);
                   Inimigo_Step = 0;
                   Pontuacao += 10;
                   if((Pontuacao % 50) == 0) // a dificauldade do game aumenta a cada 50 Pontos
                       Ini_Dificuldade ++;
                }
                break;
        }
    }
    public void Mostra_Records(){
            int Y = 55;
            Finaliza_Jogo();            
             //try{
                 Limpa_Tela();
             //}catch (Exception minhaExcessao){
               //  minhaExcessao.printStackTrace();
             //}finally{
                 GraficoJogo.drawString("Maiores Pontuações", 50, Y, Graphics.TOP|Graphics.LEFT);
                 Y+=15;
                 for(int i = 1; i != 6; i++){
                     GraficoJogo.drawString(i + " - " + readStuff(i), 50, Y, Graphics.TOP|Graphics.LEFT);
                     //GraficoJogo.drawString(null, i, i, Inim_x)
                     Y+=15;
                 }
                 //GraficoJogo.drawImage(Logo, 0, 0, GraficoJogo.TOP|GraficoJogo.LEFT);
                 lm.setViewWindow(0, 0, this.getWidth(), this.getHeight());
                 //lm.paint(GraficoJogo, 0, 0);
                 flushGraphics();
             //}
    }
    public void Mostra_Splash(){
            Finaliza_Jogo();
             try{
                 Limpa_Tela();
             }catch (Exception minhaExcessao){
                 minhaExcessao.printStackTrace();
             }finally{
                 GraficoJogo.drawImage(Logo, 0, 0, Graphics.TOP|Graphics.LEFT);
                 lm.setViewWindow(0, 0, this.getWidth(), this.getHeight());
                 lm.paint(GraficoJogo, 0, 0);
                 flushGraphics();
             }
    }
    public void Atualiza_BackGround(){ // Atualiza o background e anima os Tiles
        Cont_Anim++;
        if(Cont_Anim > 10){
            if(BackGnd.getAnimatedTile(Tile_Anim1) == 1){
                BackGnd.setAnimatedTile(Tile_Anim1, 2);
                BackGnd.setAnimatedTile(Tile_Anim2, 1);
            }
            else{
                BackGnd.setAnimatedTile(Tile_Anim1, 1);
                BackGnd.setAnimatedTile(Tile_Anim2, 2);
            }
            Cont_Anim = 0;
        }
    }
    public void Verifica_Record(int Record_){ // Verifica se existe um novo record e posiciona na lista de records
        int[] Reco = new int[5];
        for(int i=1; i<6;i++){
            if(Get_Record(i)<Record_){
                Set_Record(Record_,i);
                break;
            }
        }
            listStuff();
    }
    public void Set_Record(int Record_, int index){ // Ajusta o valor do record indicado
        saveStuff(Integer.toString(Record_),index);
    }
    public void Init_Records(){
        int Num_Records;
        Num_Records = getnumStuff();
        if(Num_Records == 0){
            int i;
            for(i = 1;i != 6;i++){
                saveStuff("0");
            }
            saveStuff("Inicializada",i);
        }
        listStuff();
    }
    public int Get_Record(int id){  // Faz a leitura dos record stores e retorna o valor como inteiro
        return Integer.valueOf(readStuff(id)).intValue();
    }

    private int getnumStuff(){
        int numero_rec = 0;
        try {
            recordStore = RecordStore.openRecordStore("REC_STORE", true);
            numero_rec = recordStore.getNumRecords();
            System.out.println("Total de dados: " + Integer.toString(numero_rec));
        }
        catch (Exception e) {
            System.out.println("Exception occured in readStuff: " + e.getMessage());
        }
        finally {
            if (recordStore != null) {
                try {
                    recordStore.closeRecordStore();
                }
                catch (Exception e) {
                    // ignore
                }
            }
        }
        return numero_rec;
    }
    private String readStuff(int endereco){ // Efetua a leitura de um valor indicado pelo parametro "endereco" no record store
        String stuff = "Ã± existe";
        try {
            recordStore = RecordStore.openRecordStore("REC_STORE", true);
            int n = recordStore.getNumRecords();
            //String stuff;

            if (n > 0 && endereco <= n) {
                stuff = new String(recordStore.getRecord(endereco));
            }
            else {
                stuff = "nexiste";
            }

            System.out.println(stuff);
        }
        catch (Exception e) {
            System.out.println("Exception occured in readStuff: " + e.getMessage());
        }
        finally {
            if (recordStore != null) {
                try {
                    recordStore.closeRecordStore();
                }
                catch (Exception e) {
                    // ignore
                }
            }
        }
        return stuff;
    }
    private void saveStuff(String Data){ // Salva no record store uma string na ultima posição livre
        try {
            recordStore = RecordStore.openRecordStore("REC_STORE", true);
            //int n = recordStore.getNumRecords();
            byte[] stuff = Data.getBytes();
            recordStore.addRecord(stuff, 0, stuff.length);
        } catch (Exception e) {
            System.out.println("Exception occured in saveStuff: " + e.getMessage());
        } finally {
            if (recordStore != null) {
                try {
                    recordStore.closeRecordStore();
                } catch (Exception e){
                }
            }
        }
    }
    private void saveStuff(String Data, int id) { // Atualiza um dado no record store num posição ja existente indicada por "id"
        try {
            recordStore = RecordStore.openRecordStore("REC_STORE", true);
            int n = recordStore.getNumRecords();
            byte[] stuff = Data.getBytes();
            recordStore.setRecord(id, stuff, 0, stuff.length);
        } catch (Exception e) {
            System.out.println("Exception occured in saveStuff: " + e.getMessage());
        } finally {
            if (recordStore != null) {
                try {
                    recordStore.closeRecordStore();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        }
    private void listStuff(){// Função utilizada apenas para debug. lista todos os record stores existentes
        int Numero_Records;
        Numero_Records = getnumStuff();
        for(int i = 1; i<= Numero_Records; i++){
            System.out.println("Record: " + Integer.toString(i) + " Valor: " + readStuff(i));
        }
    }
 }
