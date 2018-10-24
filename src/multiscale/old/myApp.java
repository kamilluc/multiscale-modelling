import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by kamil on 04.05.2017.
 */
public class myApp extends Application {

    private static int appHeight=100*4;
    private static int appWidth=150*4;
    private static final int pixelSize=1;
    private static int logicNeighbourhood=0;
    private static boolean logicPeriodic=false;
    private static int numberOfFirstSeeds=150;
    private static int seedRule=0;
    private static int newSeeds=0;
    private static boolean startValue=false;
    private static int radiusValue=1;
    private static boolean gameState=true;
    private static boolean recrystalization=false;
    private static int kValue=1000;
    private static int mcIterations=10;
    private static int numberOfStates=50;
    private static int newMc=1;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("NRZnR");
        Group root = new Group();
        Canvas canvas = new Canvas(appWidth*pixelSize, appHeight*pixelSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();
//        drawShapes(gc);
        //test
        //radio
        Label neighLabel=new Label("Neighbourhood:");
        neighLabel.setLayoutX(appWidth*pixelSize+5);
        neighLabel.setLayoutY(0);
        final ToggleGroup group = new ToggleGroup();

        RadioButton rb0=new RadioButton("Moore");
        rb0.setToggleGroup(group);
        rb0.setSelected(true);

        RadioButton rb1=new RadioButton("Neumann");
        rb1.setToggleGroup(group);

        RadioButton rb2=new RadioButton("Hexagonal Left");
        rb2.setToggleGroup(group);

        RadioButton rb3=new RadioButton("Hexagonal Right");
        rb3.setToggleGroup(group);

        RadioButton rb4=new RadioButton("Hexagonal Random");
        rb4.setToggleGroup(group);

        RadioButton rb5=new RadioButton("Pentagonal Random");
        rb5.setToggleGroup(group);
        int buttonSpace=20;
        rb0.setLayoutX(appWidth*pixelSize+5);
        rb0.setLayoutY(buttonSpace);
        rb1.setLayoutX(appWidth*pixelSize+5);
        rb1.setLayoutY(rb0.getLayoutY()+buttonSpace);
        rb2.setLayoutX(appWidth*pixelSize+5);
        rb2.setLayoutY(rb1.getLayoutY()+buttonSpace);
        rb3.setLayoutX(appWidth*pixelSize+5);
        rb3.setLayoutY(rb2.getLayoutY()+buttonSpace);
        rb4.setLayoutX(appWidth*pixelSize+5);
        rb4.setLayoutY(rb3.getLayoutY()+buttonSpace);
        rb5.setLayoutX(appWidth*pixelSize+5);
        rb5.setLayoutY(rb4.getLayoutY()+buttonSpace);
        //koniec radio

        //periodic
        Label periodicLabel=new Label("Borders:");
        periodicLabel.setLayoutX(appWidth*pixelSize+5);
        periodicLabel.setLayoutY(rb5.getLayoutY()+2*buttonSpace);

        final ToggleGroup group2 = new ToggleGroup();

        RadioButton rb6=new RadioButton("Non-periodic");
        rb6.setToggleGroup(group2);
        rb6.setSelected(true);

        RadioButton rb7=new RadioButton("Periodic");
        rb7.setToggleGroup(group2);

        rb6.setLayoutX(appWidth*pixelSize+5);
        rb6.setLayoutY(periodicLabel.getLayoutY()+buttonSpace);
        rb7.setLayoutX(appWidth*pixelSize+5);
        rb7.setLayoutY(rb6.getLayoutY()+buttonSpace);
        //end periodic

        //seeds
        Label seedLabel=new Label("Seeds:");
        seedLabel.setLayoutX(appWidth*pixelSize+5);
        seedLabel.setLayoutY(rb7.getLayoutY()+2*buttonSpace);

        final ToggleGroup group3 = new ToggleGroup();

        RadioButton rb8=new RadioButton("Random");
        rb8.setToggleGroup(group3);
        rb8.setSelected(true);
        RadioButton rb9=new RadioButton("Even");
        rb9.setToggleGroup(group3);
        RadioButton rb10=new RadioButton("Random with Radius");
        rb10.setToggleGroup(group3);
        RadioButton rb11=new RadioButton("Clicked");
        rb11.setToggleGroup(group3);


        rb8.setLayoutX(appWidth*pixelSize+5);
        rb8.setLayoutY(seedLabel.getLayoutY()+buttonSpace);
        rb9.setLayoutX(appWidth*pixelSize+5);
        rb9.setLayoutY(rb8.getLayoutY()+buttonSpace);
        rb10.setLayoutX(appWidth*pixelSize+5);
        rb10.setLayoutY(rb9.getLayoutY()+buttonSpace);
        rb11.setLayoutX(appWidth*pixelSize+5);
        rb11.setLayoutY(rb10.getLayoutY()+buttonSpace);
        //seeds end


        //textfieldy
        Label tf1Label=new Label("Seeds count:");
        TextField textField1=new TextField("150");
        Label tf2Label=new Label("Radius:");
        TextField textField2=new TextField("4");
        Label tf3Label=new Label("Height:");
        TextField textField3=new TextField("150");
        Label tf4Label=new Label("Width:");
        TextField textField4=new TextField("150");
        Label tf5Label=new Label("Add new seeds:");
        TextField textField5=new TextField("0");

        tf1Label.setLayoutX(appWidth*pixelSize+5);
        tf1Label.setLayoutY(rb11.getLayoutY()+buttonSpace+10);
        textField1.setLayoutX(appWidth*pixelSize+5);
        textField1.setLayoutY(tf1Label.getLayoutY()+buttonSpace);

        tf2Label.setLayoutX(appWidth*pixelSize+5);
        tf2Label.setLayoutY(textField1.getLayoutY()+buttonSpace+10);
        textField2.setLayoutX(appWidth*pixelSize+5);
        textField2.setLayoutY(tf2Label.getLayoutY()+buttonSpace);

        tf3Label.setLayoutX(appWidth*pixelSize+5);
        tf3Label.setLayoutY(textField2.getLayoutY()+buttonSpace+10);
        textField3.setLayoutX(appWidth*pixelSize+5);
        textField3.setLayoutY(tf3Label.getLayoutY()+buttonSpace);

        tf4Label.setLayoutX(appWidth*pixelSize+5);
        tf4Label.setLayoutY(textField3.getLayoutY()+buttonSpace+10);
        textField4.setLayoutX(appWidth*pixelSize+5);
        textField4.setLayoutY(tf4Label.getLayoutY()+buttonSpace);


        //koniec text field
        Button btn4=new Button();
        btn4.setText("START");
        btn4.setLayoutX(appWidth*pixelSize+5);
        btn4.setLayoutY(textField4.getLayoutY()+5*buttonSpace);

        CheckBox checkBox=new CheckBox("Recrystallization");
        checkBox.setLayoutX(btn4.getLayoutX());
        checkBox.setLayoutY(btn4.getLayoutY()-buttonSpace-5);
checkBox.setSelected(false);



        Button btn3=new Button();
        btn3.setText("RESET");
        btn3.setLayoutX(appWidth*pixelSize+5);
        btn3.setLayoutY(textField4.getLayoutY()+2*buttonSpace);


        Button btn2=new Button();
        btn2.setText("PAUSE");
        btn2.setLayoutX(btn3.getLayoutX()+3*buttonSpace-5);
        btn2.setLayoutY(textField4.getLayoutY()+2*buttonSpace);

        Button btn=new Button();
        btn.setText("SET");
        btn.setLayoutX(btn2.getLayoutX()+3*buttonSpace-5);
        btn.setLayoutY(textField4.getLayoutY()+2*buttonSpace);

        tf5Label.setLayoutX(appWidth*pixelSize+5);
        tf5Label.setLayoutY(btn4.getLayoutY()+buttonSpace+10);
        textField5.setLayoutX(appWidth*pixelSize+5);
        textField5.setLayoutY(tf5Label.getLayoutY()+buttonSpace);


        Label tf6Label=new Label("K const:");
        TextField textField6=new TextField("1000");
        textField6.setLayoutX(appWidth*pixelSize+5);
        textField6.setLayoutY(tf5Label.getLayoutY()+3*buttonSpace+10);
        tf6Label.setLayoutX(appWidth*pixelSize+5);
        tf6Label.setLayoutY(tf5Label.getLayoutY()+2*buttonSpace+10);



        //monte carlo
//        n stanow text box
//        1 opcja losowanie planszy rand z n stanow
//        2 opcja dzialanie na planszy juz po rozroscie ziaren
//        zrob liste komorek; rand ele z lissty; sprawdz sasiadow (moore); sumuj energie (energia++ za kazda inna komorke w sasiedztwie);
//         RAZ podmien te komorke; licz energie jak mniejsza zostaw jak nie zostaw stara; usun komrke z listy; losuj kolejna itd; to wszystko to 1 iteracja mc;
//        text box iteracje mc
        Label tf7Label=new Label("Number of states:");
        TextField textField7=new TextField("10");

        tf7Label.setLayoutX(appWidth*pixelSize+5+200);
        tf7Label.setLayoutY(0);
        textField7.setLayoutX(appWidth*pixelSize+5+200);
        textField7.setLayoutY( tf7Label.getLayoutY()+buttonSpace);

        Label tf8Label=new Label("MC Iterations:");
        TextField textField8=new TextField("200");

        tf8Label.setLayoutX(appWidth*pixelSize+5+200);
        tf8Label.setLayoutY(textField7.getLayoutY()+2*buttonSpace);
        textField8.setLayoutX(appWidth*pixelSize+5+200);
        textField8.setLayoutY(tf8Label.getLayoutY()+buttonSpace);

        final ToggleGroup group4 = new ToggleGroup();
        RadioButton rb20=new RadioButton("New MC");
        rb20.setToggleGroup(group4);
        rb20.setSelected(true);

        RadioButton rb21=new RadioButton("Old MC");
        rb21.setToggleGroup(group4);
        RadioButton rb22=new RadioButton("MC Off");
        rb22.setToggleGroup(group4);

        rb20.setLayoutX(appWidth*pixelSize+5+200);
        rb20.setLayoutY(textField8.getLayoutY()+2*buttonSpace);
        rb21.setLayoutX(appWidth*pixelSize+5+200);
        rb21.setLayoutY(rb20.getLayoutY()+buttonSpace);
        rb22.setLayoutX(appWidth*pixelSize+5+200);
        rb22.setLayoutY(rb21.getLayoutY()+buttonSpace);
        //koniec mc

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Hello World!");
                if(rb0.isSelected()) logicNeighbourhood=0;
                else if(rb1.isSelected()) logicNeighbourhood=1;
                else if(rb2.isSelected()) logicNeighbourhood=2;
                else if(rb3.isSelected()) logicNeighbourhood=3;
                else if(rb4.isSelected()) logicNeighbourhood=4;
                else if(rb5.isSelected()) logicNeighbourhood=5;

                if(rb6.isSelected()) logicPeriodic=false;
                else if(rb7.isSelected()) logicPeriodic=true;

                appHeight=Integer.parseInt(textField3.getText());
                appWidth=Integer.parseInt(textField4.getText());
                canvas.setHeight(appHeight*pixelSize);
                canvas.setWidth(appWidth*pixelSize);
                numberOfFirstSeeds=Integer.parseInt(textField1.getText());
                kValue=Integer.parseInt(textField6.getText());
                //mc
                mcIterations=Integer.parseInt(textField8.getText());
                numberOfStates=Integer.parseInt(textField7.getText());
                if(rb20.isSelected())newMc=1;
                else if(rb21.isSelected()) newMc=2;
                else if(rb22.isSelected()) newMc=0;
                        //=Boolean.parseBoolean(rb20.getText());
               // System.out.println(newMc);
                //end mc
                if(rb8.isSelected()) seedRule=0;
                else if(rb9.isSelected()) seedRule=1;
                else if(rb10.isSelected()) seedRule=2;
                else if(rb11.isSelected()) seedRule=3;
        radiusValue=Integer.parseInt(textField2.getText());
                newSeeds=Integer.parseInt(textField5.getText());
               // drawShapes(gc,root,rb0,rb1,btn2,btn3);

                if(checkBox.isSelected())
                    recrystalization=true;
                            else
                                recrystalization=false;
            }
        });

        btn4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                 drawShapes(gc,root,rb0,rb1,btn2,btn3);
            }
        });

        //end
        root.getChildren().add(canvas);
        root.getChildren().add(btn);
        root.getChildren().add(rb0);
        root.getChildren().add(rb1);
        root.getChildren().add(rb2);
        root.getChildren().add(rb3);
        root.getChildren().add(rb4);
        root.getChildren().add(rb5);
        root.getChildren().add(neighLabel);
        root.getChildren().addAll(periodicLabel, rb6, rb7);
        root.getChildren().addAll(seedLabel, rb8,rb9,rb10,rb11, btn2);
        root.getChildren().addAll(tf1Label,tf2Label, checkBox, tf3Label,tf4Label,textField1,textField2,textField3,textField4,btn3,btn4,textField5,tf5Label,textField6,tf6Label);
        root.getChildren().addAll(textField7,textField8,tf7Label,tf8Label,rb20,rb21,rb22);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


    }

    private void drawShapes(GraphicsContext gc, Group root, RadioButton rb0,RadioButton rb1, Button btn2, Button btn3) {
        Logic logic = new Logic(appWidth, appHeight, numberOfFirstSeeds);
        logic.setNeigh(logicNeighbourhood);
        logic.setPeriodic(logicPeriodic);
//        setNeigh: 0 moore
//                  1 neumann
//                  2 hexaLeft
//                  3 hexaRight
//                  4 hexaRand
//                  5 pentaRand
        //logic.setPeriodic(logicPeriodic);
        logic.setSeedRule(seedRule);
        logic.setRadius(radiusValue);
        logic.addNewSeeds(newSeeds);
        logic.setK((double)kValue);
        logic.setRecrystalization(recrystalization);
        logic.setNewMc(newMc);
        logic.setNumberOfStates(numberOfStates);
        logic.setMcIterations(mcIterations);
        logic.start();
//
//        while (logic.emptyFields() > 0){
//            //System.out.println(logic.emptyFields());
//            logic.nextStep();
//        }
//
//        for(int i=0;i<appHeight;i++){
//            for(int j=0;j<appWidth;j++){
//                gc.setFill(logic.map[i+1][j+1].getColor());
//                gc.fillRect(j*pixelSize,i*pixelSize,pixelSize,pixelSize);
//            }
//        }

//new


        //klikanie to ziarno start
        root.setOnMouseClicked(
                new EventHandler<MouseEvent>()
                {
                    public void handle(MouseEvent e)
                    {
                        int x=(int)(e.getX());
                        int y=(int)(e.getY());
                        //System.out.println(x+" "+y);
                        logic.newSeed(x,y);

//                        if ( targetData.containsPoint( e.getX(), e.getY() ) )
//                        {
//                            double x = 50 + 400 * Math.random();
//                            double y = 50 + 400 * Math.random();
//                            targetData.setCenter(x,y);
//                            points.value++;
//                        }
//                        else
//                            points.value = 0;
                    }
                });
        //clicked ziarno koniec

        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount( Timeline.INDEFINITE );

        final long timeStart = System.currentTimeMillis();

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.1),                // 60 FPS
                new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae) {
                        //if (logic.emptyFields() > 0) {
                            for (int i = 0; i < appHeight; i++) {
                                for (int j = 0; j < appWidth; j++) {
                                    gc.setFill(logic.map[i + 1][j + 1].getColor());
                                    gc.fillRect(j * pixelSize, i * pixelSize, pixelSize, pixelSize);
                                }
                            }
                       //logic.setSeedRule(seedRule);
                        if(rb0.isSelected()) logicNeighbourhood=0;
                        else if(rb1.isSelected()) logicNeighbourhood=1;
//                        else if(rb2.isSelected()) logicNeighbourhood=2;
//                        else if(rb3.isSelected()) logicNeighbourhood=3;
//                        else if(rb4.isSelected()) logicNeighbourhood=4;
//                        else if(rb5.isSelected()) logicNeighbourhood=5;
                            logic.setNeigh(logicNeighbourhood);
                            logic.nextStep();


                            //new

                        //end

                        //}
                    }
                });

        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Hello World!");

                gameState = !gameState;
                if (!gameState) gameLoop.pause();
                else
                    gameLoop.play();
                logic.addNewSeeds(newSeeds);
                //logic.start();
            }});


        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Hello World!");

//                gameState = !gameState;
//                if (!gameState) gameLoop.pause();
//                else
//                    gameLoop.play();








logic.saveToFile();
//                logic.start();
                //logic.setPeriodic(logicPeriodic);
            }});

                gameLoop.getKeyFrames().add( kf );
        gameLoop.play();

        //koniec new

    }
}
