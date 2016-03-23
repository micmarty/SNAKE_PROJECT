package content;

import javafx.application.Application;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;

import javafx.animation.AnimationTimer;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

import java.awt.*;


public class GraphicalInterface extends Application {
    //---------------------------
    //private
    private Stage window;                   //entire window handler
    private Scene mainScene;                //scene that holds whole content
    private GridPane grid;                  //layout to store our labels

    //Important for FPS calculations
    private long previousFrameTime;         //time in nanosecond of the latest frame

    private Image bg;                       //background
    private Image brick;                    //peripheral wall
    //-----------------------------
    //static
    private final static int size = 40;     //in our board of labels width=height
    private static String windowName = "SNAKE - alpha compilation test";
    private static int windowWidth = size * 20;
    private static int windowHeight = size * 20;
    private static int fps = 4;             //how many frames/moves are in one second
    private static int roundsToPlay = 3;    //how many round have to be done until the game ends

    private Label[][] board = new Label[size][size];
    private BarrierType[][] mask = new BarrierType[size][size]; //mask containing position of snakes, walls, etc


    //----------------------------
    //methods
    /*  initializing just our board             */
    private void initBoard(boolean refreshOnly){
        for(int x = 0; x < size; x++)
            for(int y = 0; y < size; y++){

                if(!refreshOnly)//important(if snake dies, we dont need to reallocate memory for labels
                    board[x][y] = new Label();                  //calling constructor
                board[x][y].setGraphic(new ImageView(bg));  //always fill board with background color
                mask[x][y] = BarrierType.EMPTY;             //updating mask

            }
    }

    /*  initializing as method name */
    private void initLabelToGridAssignment() {
        for(int x = 0; x < size; x++)
            for(int y = 0; y < size; y++){
                GridPane.setConstraints(board[x][y],x,y);   //bind board tile to proper COLUMN and ROW in our grid
                grid.getChildren().add(board[x][y]);        //finally add each of them
            }
    }

    /*  initializing multiple Image references to class variables   */
    private void initImages() {
        //basic textures, we can use Colors instead of Images
        bg = new Image(getClass().getResourceAsStream("resources/bg.png"));   //bg - background
        brick = new Image(getClass().getResourceAsStream("resources/brick.png"));
    }

    /*  initializing variables/resources only   */
    @Override                                //override javaFX native method
    public void init(){
        grid = new GridPane();
        grid.setPadding(new Insets(0,0,0,0));    //0 pixel padding on each side
        grid.setVgap(0);                         //vertical spacing between each label
        grid.setHgap(0);                         //horizontal spacing

        initImages();               //call Images initialization for further use
        initBoard(false);            //call board initialization method
                                    //'false' means - force allocating memory for labels
        initLabelToGridAssignment();//bind board tiles to proper place in grid
    }
    
    /* initializing walls*/
    public void initWalls(PeripheralWall peripheralWall){
        for(Point w : peripheralWall.getWall()){// 'w' means element
            board[w.x][w.y].setGraphic(new ImageView(brick)); // adding walls 
            mask[w.x][w.y] = BarrierType.WALL; //upgrading mask
        }
    }


    //IT IS TECHNICALLY OUR MAIN //(learned from documentation)
    @Override                               //override javaFX native method
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;              //must-have assignment
        window.setTitle(windowName);   //window TITLE

        mainScene = new Scene(grid,windowWidth,windowHeight);//10 left padding, 40*20 tiles space, 10 right padding

        Snake snake = new Snake(new Point(9,9));
        PeripheralWall peripheralWall = new PeripheralWall(size);

        //display wall only once
        initWalls(peripheralWall);
        //EVENT FOR KEYBOARD
        EventHandler<KeyEvent> keyEventEventHandler = event -> {
            snake.setHead(event.getCode());    //call snake method, to filter the input and choose further direction
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
                    boolean isAlive = (snake.getLifeStatus() == LifeStatus.ALIVE);
                    boolean isProperFrame = (second/timeBetweenFrames <= fps || previousFrameTime == 0);

                    //FPS = 1s/timeBetweenFrames or if it's first frame!!!
                    // (because previousFrameTime is 0 before hitting the  if statement;
                    if(isProperFrame){
                        if(isAlive){
                            snake.considerAction(mask);         //update snake's position

                            //refresh/add only head to the board, more optimal solution
                            Point h = snake.getHead();          //h for shortcut in line below
                            board[h.x][h.y].setGraphic(new ImageView(snake.getImage()));
                            mask[h.x][h.y] = BarrierType.BLUE_SNAKE;
                        }else{//is DEAD or RESIGNED
                            if(roundsToPlay > 1) {              //there's no sense in rebuilding game, when it was the least
                                //if sneak is dead, this method makes him alive again
                                snake.setReady(new Point(9, 9));

                                //resignated snake is not alive, so board is not refreshed
                                if(snake.getLifeStatus() == LifeStatus.ALIVE){

                                    //THIS COMMENT REFERS TO initBoard(true) method!!!
                                    //overwrite all labels with starting arrangement
                                    //WITHOUT allocatin new labels for board
                                    //thanks to that, code is more optimal
                                    //!!!because we dont need to call initLabelToGridAssignment!!!!!
                                    initBoard(true);                //pass ONLY REFRESH argument, for more see method description
                                    initWalls(peripheralWall);      //in case of destroyed wall, refresh all wall labels

                                }
                                roundsToPlay -= 1;                  //decrease
                                System.out.println("Round " + (3 - roundsToPlay) + " ended");
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
