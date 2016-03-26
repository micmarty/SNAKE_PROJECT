package content;

import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import java.awt.*;
import javafx.scene.image.Image;
import java.util.ArrayList;

/**
 * Created by Michał Martyniak and company :P on 19.03.2016.
 */

public class Snake {
    private Point head;             //coordinates of snake's head
    private ArrayList<Point> body;  //holds body segments
    private KeyCode lastKey;        //direction variable, allow to continue snake's movement in that direction constantly

    private Integer points;             //player's points
    private LifeStatus lifeStatus;  //to know more look a defined enum a few lines above this one ^
    private Image image;
    private String playerName;

    /*  create snake that has ony HEAD with given coordinates   */
    public Snake(Point startingPoint, String name, SnakeColor color) {
        head = startingPoint;                     //head always exist
        body = new ArrayList<>();                 //at the beginning body is empty
        lastKey = null;                      //no key is pressed at the beginning

        points = 0;
        playerName=name;
        lifeStatus = LifeStatus.ALIVE;            //snake is alive
        switch(color){
            case Red:
                image = new Image(getClass().getResourceAsStream("resources/red.png"));
                break; 
            case Green:
                image = new Image(getClass().getResourceAsStream("resources/green.png"));
                break; 
            case Blue:
                image = new Image(getClass().getResourceAsStream("resources/blue.png"));
                break; 
            case Yellow:
                image = new Image(getClass().getResourceAsStream("resources/yellow.png"));
                break; 
        }
    }

    public void move(BarrierType mask[][], Point translate){
        //universal statement considering all cases of movement
        if(mask[head.x + translate.x][head.y + translate.y] == BarrierType.EMPTY){
            head.translate(translate.x, translate.y);   //x one left, y stays the same
        }
        else{//if label is not empty, then crash occurs
            lifeStatus = LifeStatus.DEAD;
        }
    }

    /*  changes the coordinates of snake's head (and convert old head to new body element)  */
    //change to boolean later(for collision)
    public void considerAction(BarrierType[][] mask){
        body.add(head);                             //save head as a body now

        Point actualTranslation = new Point(0, 0);   //temporary helper that doesn't move our snake yet!!
                                                    //it says, where snake should move
        if(lastKey != null){                                            //(proper value is after switch statement
            switch (lastKey){
                case L:
                    lifeStatus = LifeStatus.RESIGNED;   //Snake gave up completely in that round
                    body.remove(body.size() - 1);       //! if adding head to body list was inappropriate
                    return;                             //EXIT whole method, no further instructions must be executed!
                case W:
                    actualTranslation.y = -1;            //one up
                    break;
                case S:
                    actualTranslation.y = 1;             //one down
                    break;
                case A:
                    actualTranslation.x = -1;            //one left
                    break;
                case D:
                    actualTranslation.x = 1;             //one right
                    break;
                default:                                 //no key - skip that method
                    return;
            }
            //actualTranslation holds always direction to which snake is following
            move(mask, actualTranslation);
        }
    }

    /*  returns only head coordinates (useful for drawing)  */
    public Point getHead(){
        return head;
    }

    public Image getImage(){
        return image;
    }
    
    public Integer getPoints(){
        return points;
    }
    
    public String getPlayerName(){
        return playerName;
    }
    
    /*  returns value of life ^^ */
    public LifeStatus getLifeStatus(){
        return lifeStatus;
    }

    /*set snake's life value. It is unused yet!*/
    public void setLife(LifeStatus value){
        lifeStatus = value;
    }

    /*  returns list of ALL coordinates that belong to that snake */
    public ArrayList<Point> wholeSnake(){
        ArrayList<Point> wholeSnakeList = body;
        wholeSnakeList.add(head);
        return wholeSnakeList;
    }

    public void setLastKey(KeyCode key){
        lastKey = key;
    }

    public void setReady(Point startingPoint) {
        head = startingPoint;                           //head always exist
        body = new ArrayList<>();
        lastKey = null;
        if(lifeStatus == LifeStatus.DEAD)   //Resigned players can't continue their game
            lifeStatus = LifeStatus.ALIVE;
    }

    
}