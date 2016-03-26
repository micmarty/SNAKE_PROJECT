package content;

import com.sun.javafx.geom.Rectangle;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;

import javafx.animation.AnimationTimer;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class GraphicalInterface extends Application {
    //---------------------------
    //private
    //javaFX windows and scene
    private Stage window;                   //entire window handler
    private Scene mainScene;                //scene that holds whole content

    //layouts
    private BorderPane borderPane;          //holds boardGridPane and infoGridPane within
    private GridPane boardGridPane;         //layout to store our labels(board)
    private GridPane infoGridPane;              //layout for info bar at the top of the window
    
    //Important for FPS calculations
    private long previousFrameTime;         //time in nanosecond of the latest frame
    
    //important for game
    private Integer playersCount = 4;
    private Integer playerId = 3;
    private Integer roundNumber = 1;
    private Snake[] snakes = new Snake[playersCount];
    
    
    private Integer[] rewards = new Integer[]{0, 1, 2, 3};

    //images
    private Image bg;                       //background
    private Image brick;                    //peripheral wall
    private Image infoBarBg;                //template from paint
    private Image white;  

    //layout elements(childrens)
    private Label[][] board = new Label[sizeWidth][sizeHeight];
    private BarrierType[][] mask = new BarrierType[sizeWidth][sizeHeight]; //mask containing position of snakes, walls, etc
    private Label[] names = new Label[playersCount];
    private Label[] scores = new Label[playersCount];
    private Label round;
    //-----------------------------
    //static
    private final static int infoBarHeight = 80;      //constant variable which determines InfoBar Height
    private final static int sizeWidth = 60;          //Width of our Label board
    private final static int sizeHeight = 29;         //Height our Label board

    private static String windowName = "SNAKE - alpha compilation test";
    private static int windowWidth = sizeWidth * 20;
    private static int windowHeight = (sizeHeight * 20) + infoBarHeight; //how many round have to be done until the game ends

    private static int fps = 4;             //how many frames/moves are in one second
    

    //----------------------------
    //methods
    /*  initializing just our board             */
    private void initBoard(boolean refreshOnly){
        
        for(int x = 0; x < sizeWidth; x++)
            for(int y = 0; y < sizeHeight; y++){

                if(!refreshOnly)//important(if snake dies, we dont need to reallocate memory for labels
                    board[x][y] = new Label();                  //calling constructor
                board[x][y].setGraphic(new ImageView(bg));  //always fill board with background color
                mask[x][y] = BarrierType.EMPTY;             //updating mask

            }
        
    }

    /*  initializing as method name */
    private void initLabelToGridAssignment() {
        for(int x = 0; x < sizeWidth; x++)
            for(int y = 0; y < sizeHeight; y++){
                GridPane.setConstraints(board[x][y],x,y);       //bind board tile to proper COLUMN and ROW in our grid
                boardGridPane.getChildren().add(board[x][y]);   //finally add each of them
            }
    }

    /*  initializing multiple Image references to class variables   */
    private void initImages() {
        //basic textures, we can use Colors instead of Images
        bg = new Image(getClass().getResourceAsStream("resources/bg.png"));   //bg - background
        brick = new Image(getClass().getResourceAsStream("resources/brick.png"));
        infoBarBg = new Image(getClass().getResourceAsStream("resources/infoBar.png"));
        
    }
    
    /*set the attributes for label*/
    private void setter(Label label, int position){
        infoGridPane.setConstraints(label,0,0);
        infoGridPane.setMargin(label,new Insets(0,0,0,position));
        infoGridPane.getChildren().add(label);
    
    }
    
    /*initializing names on top*/
    private void initNames(){
        for(int i=0; i<playersCount; i++){
            names[i]=new Label(snakes[i].getPlayerName());
            setter(names[i], 80+i*265);
        }
    }
    
/*initializing scores and number of tour on top*/
    private void initScoreAndRound(){
        for(int i=0; i<playersCount; i++){
            scores[i] = new Label(snakes[i].getPoints().toString());
            setter(scores[i],220+i*265);          
        }            	
        round = new Label("tura: "+roundNumber);
        setter(round,1100);
    }
    
    private void updateScores(){
        for(int i=0; i<playersCount; i++){
            scores[i].setText(snakes[i].getPoints().toString());
        }
    }
    
    private void updateRound(){
        round.setText("tura: "+roundNumber);
    }
    
    
    /*  initializing variables/resources only   */
    @Override                                //override javaFX native method
    public void init(){
        //to change, need asking about names
        snakes[0] = new Snake (new Point (1,1), "Ada", SnakeColor.Red);
        snakes[1] = new Snake (new Point (21,1), "Mateusz", SnakeColor.Blue);
        snakes[2] = new Snake (new Point (5,1), "Michal", SnakeColor.Green);
        snakes[3] = new Snake (new Point (8,1), "Ania", SnakeColor.Yellow);
        
        snakes[0].kill();
        snakes[1].kill();
        snakes[2].kill();
        
        //moved here because Hbox requires InfoBg to be initialized
        initImages();                   //call Images initialization for further use

        //TOP
        infoGridPane = new GridPane();        
        infoGridPane.setMinHeight(infoBarHeight);
        infoGridPane.getChildren().add(new ImageView(infoBarBg));
        

        //CENTER
        boardGridPane = new GridPane();
        boardGridPane.setPadding(new Insets(0,0,0,0));    //0 pixel padding on each side
        boardGridPane.setVgap(0);                         //vertical spacing between each label
        boardGridPane.setHgap(0);                         //horizontal spacing

        //MAIN LAYOUT
        borderPane = new BorderPane();
        borderPane.setCenter(boardGridPane);              //board layout is in the center of main layout
        borderPane.setTop(infoGridPane);


        initBoard(false);               //call board initialization method
                                        //'false' means - force allocating memory for labels
        initLabelToGridAssignment();    //bind board tiles to proper place in grid
        initNames();
        initScoreAndRound();
    }
    
    /* initializing walls*/
    public void initWalls(PeripheralWall peripheralWall){
        for(Point w : peripheralWall.getWall()){// 'w' means element
            board[w.x][w.y].setGraphic(new ImageView(brick)); // adding walls 
            mask[w.x][w.y] = BarrierType.WALL; //upgrading mask
        }
    }
    
    public void endRound(){
        System.out.println("Round " + roundNumber + " ended");
        
        List<Snake> sortedSnakes = new ArrayList<Snake>(Arrays.asList(snakes));     //Arrays.asList() changes array into a list
        Collections.sort(sortedSnakes, new TimeOfDeathComparator());
        
        int prevReward = 0;
        int nextReward = 0;
        for(int i = 0; i < playersCount; i++){
            if (i == 0){
                sortedSnakes.get(0).addPoints(rewards[nextReward]);
                prevReward = nextReward;
                nextReward++;
            }
            else{
                if(sortedSnakes.get(i).getTimeOfDeath().equals(sortedSnakes.get(i - 1).getTimeOfDeath())){
                    sortedSnakes.get(i).addPoints(rewards[prevReward]);
                    nextReward++;
                }
                else{
                    sortedSnakes.get(i).addPoints(rewards[nextReward]);
                    prevReward = nextReward;
                    nextReward++;
                }
            }
        }
        updateScores();
    }
    
    /*initalizing new round*/
    
    public void newRound(){
        roundNumber+=1;
        updateRound();
    }

    //IT IS TECHNICALLY OUR MAIN //(learned from documentation)
    @Override                               //override javaFX native method
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;              //must-have assignment
        window.setTitle(windowName);        //window TITLE

        mainScene = new Scene(borderPane, windowWidth,windowHeight);//10 left padding, 40*20 tiles space, 10 right padding
      
        //Snake snake = new Snake(new Point(9,9), "Ziomek");
        Snake currentSnake = snakes[playerId];
        
        
        Integer temp;
        temp= currentSnake.getPoints();
        
        PeripheralWall peripheralWall = new PeripheralWall(sizeWidth, sizeHeight);

        //display wall only once
        initWalls(peripheralWall);
        //EVENT FOR KEYBOARD
        EventHandler<KeyEvent> keyEventEventHandler = event -> {
            currentSnake.setLastKey(event.getCode());    //call snake method, to filter the input and choose further direction
            //event.consume();                 //don't allow to propagete event value further(next calls)
        };

        //add event handler constructed right above this line to WHOLE WINDOW(mainScene)^
        mainScene.addEventHandler(KeyEvent.KEY_PRESSED, keyEventEventHandler);

        window.setScene(mainScene);
        window.show();                      //display mainScene on the window
        
        
        /* GAME LOOP. we must mull this over, how we'll handle everything in here*/
        AnimationTimer timer;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                    //helpful for managing frames, second is 10^9 nanoseconds
                    long second = 1000000000;
                    long timeBetweenFrames = now - previousFrameTime;

                    //simplifying must-have statements (right side returns TRUE or FALSE)
                    boolean isAlive = (snakes[playerId].getLifeStatus() == LifeStatus.ALIVE);
                    boolean isProperFrame = (second/timeBetweenFrames <= fps || previousFrameTime == 0);

                    //FPS = 1s/timeBetweenFrames or if it's first frame!!!
                    // (because previousFrameTime is 0 before hitting the  if statement;
                    if(isProperFrame){
                        if(isAlive){
                            snakes[playerId].considerAction(mask);         //update snake's position

                            //refresh/add only head to the board, more optimal solution
                            Point h = snakes[playerId].getHead();          //h for shortcut in line below
                            board[h.x][h.y].setGraphic(new ImageView(snakes[playerId].getImage()));
                            mask[h.x][h.y] = BarrierType.SNAKE;
                        }else{//is DEAD or RESIGNED
                            if(roundNumber<10) {
                                endRound();
                                //there's no sense in rebuilding game, when it was the least
                                //if sneak is dead, this method makes him alive again
                                snakes[playerId].setReady(new Point(9, 9));

                                //resignated snake is not alive, so board is not refreshed
                                if(snakes[playerId].getLifeStatus() == LifeStatus.ALIVE){

                                    //THIS COMMENT REFERS TO initBoard(true) method!!!
                                    //overwrite all labels with starting arrangement
                                    //WITHOUT allocatin new labels for board
                                    //thanks to that, code is more optimal
                                    //!!!because we dont need to call initLabelToGridAssignment!!!!!
                                    initBoard(true);                //pass ONLY REFRESH argument, for more see method description
                                    initWalls(peripheralWall);      //in case of destroyed wall, refresh all wall labels

                                }
                                newRound();
                                
                            }else{
                                System.out.println("Last round is done. GAME OVER. Relaunch app to play again ");
                                Platform.exit();        //exit application
                            }
                        }
                        previousFrameTime = now;            //save current frame as older than next 'now' values
                    }
            }

        };
        timer.start();
    }

    public static void main(String[] args) {
        //tutorials are saying, that this main will not be useful in further project evaluation
        launch(args);//must-have call (javaFX standard)
    }
}
