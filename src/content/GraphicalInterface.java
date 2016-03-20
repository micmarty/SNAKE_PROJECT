package content;

import javafx.application.Application;

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
    private Image red;
    private Image yellow;
    private Image blue;
    private Image green;
    private Image brick;
    //-----------------------------
    //static
    private final static int size = 40;     //in our board of labels width=height
    private static String windowName = "SNAKE - alpha compilation test";
    private static int windowWidth = size * 20;
    private static int windowHeight = size * 20;
    private static int fps = 4;             //how many frames/moves are in one second


    Label[][] board = new Label[size][size];


    //----------------------------
    //methods
    /*  initializing just our board             */
    private void initBoard(){
        for(int x = 0; x < size; x++)
            for(int y = 0; y < size; y++){
                board[x][y] = new Label();          //calling constructor

                //must-have variables for lambda expression
                final int finalX = x;
                final int finalY = y;

                board[x][y].setOnMousePressed(e->{              //lambda expression for event handling
                    System.out.println(finalX + "," + finalY);  //display coordinates in console
                    Image newTileImage=bg;                      //another constant that holds reference for assigning new color of a tile

                    //doesn't work while event is mouseEntered.
                    //In order to change the newTileImage use setOnMouseClicked

                    if(e.isPrimaryButtonDown())
                        newTileImage = red;
                    else if(e.isSecondaryButtonDown())
                        newTileImage = yellow;
                    else if(e.isMiddleButtonDown())
                        newTileImage = blue;
                    else if(e.isSecondaryButtonDown() && e.isPrimaryButtonDown())
                        newTileImage = green;


                    board[finalX][finalY].setGraphic(new ImageView(newTileImage));   //assigning new Image for event-caller
                });

                board[x][y].setGraphic(new ImageView(bg));                           //always fill board with background color
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
        bg = new Image(getClass().getResourceAsStream("resources/grey.png"));   //bg - background
        red = new Image(getClass().getResourceAsStream("resources/red.png"));
        yellow = new Image(getClass().getResourceAsStream("resources/yellow.png"));
        blue = new Image(getClass().getResourceAsStream("resources/blue.png"));
        green = new Image(getClass().getResourceAsStream("resources/green.png"));
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
        initBoard();                //call board initialization method
        initLabelToGridAssignment();//bind board tiles to proper place in grid
    }


    //IT IS TECHNICALLY OUR MAIN //(learned from documentation)
    @Override                               //override javaFX native method
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;              //must-have assignment
        window.setTitle(windowName);   //window TITLE

        mainScene = new Scene(grid,windowWidth,windowHeight);//10 left padding, 40*20 tiles space, 10 right padding

        Snake snake = new Snake(4,5);
        PeripheralWall peripheralWall = new PeripheralWall(size);

        //display wall only once
        for(Point w : peripheralWall.getWall()){// 'e' means element
            board[w.x][w.y].setGraphic(new ImageView(brick));
        }

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
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //helpful for managing frames, second is 10^9 nanoseconds
                long second = 1000000000;
                long timeBetweenFrames = now - previousFrameTime;
                System.out.print("TO ma byc!");
                //FPS = 1s/timeBetweenFrames or if it's first frame!!!
                // (because previousFrameTime is 0 before hitting the  if statement;
                if(second/timeBetweenFrames <= fps || previousFrameTime == 0){
                    snake.move();               //update snake's position

                    /*  refresh/add only head to the board, more optimal solution  */
                    Point h = snake.getHead();  //h for shortcut in line below
                    board[h.x][h.y].setGraphic(new ImageView(blue));
                    previousFrameTime = now;    //save current frame as older than next 'now' values
                }
                //comments below are partly done above
                //render
                //sync
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        //tutorials are saying, that this main will not be useful in further project evaluation
        launch(args);//must-have call (javaFX standard)
    }
}
