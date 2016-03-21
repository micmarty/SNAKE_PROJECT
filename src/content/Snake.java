package content;

import javafx.scene.input.KeyCode;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Michał Martyniak on 19.03.2016.
 */
public class Snake {
    private Point head;             //coordinates of snake's head
    private ArrayList<Point> body;  //holds body segments
    private KeyCode lastKey;        //direction variable, allow to continue snake's movement in that direction constantly

    /*  create snake that has ony HEAD with given coordinates   */
    public Snake(int staryAtX, int startAtY) {
        head = new Point(staryAtX,startAtY);      //head always exist
        body = new ArrayList<>();                 //at the begining body is empty
        lastKey = KeyCode.K;                      //no key is pressed at the beginning
    }



    /*  changes the coordinates of snake's head (and convert old head to new body element)  */
    //change to boolean later(for collision)
    public void move(int[][] mask){
       // body.add(head);                 //save head as a body now
        switch (lastKey){
            case W:
                if(mask[this.head.x][this.head.y-1] == BarrierType.EMPTY.value){        //check if              
                    body.add(head);
                    head.translate(0,-1);   //x stays the same, y one upper
                }
                else
                    System.out.println("boom");
                break;
            case S:
                if(mask[this.head.x][this.head.y+1] == BarrierType.EMPTY.value){
                    body.add(head);
                    head.translate(0,1);    //x stays the same, y one down
                }
                else
                    System.out.println("boom");
                break;
            case A:
                if(mask[this.head.x-1][this.head.y] == BarrierType.EMPTY.value){
                    body.add(head);
                    head.translate(-1,0);   //x one left, y stays the same              
                }
                else
                    System.out.println("boom");
                break;
            case D:
                if(mask[this.head.x+1][this.head.y] == BarrierType.EMPTY.value){
                    body.add(head);
                    head.translate(1,0);    //x one right, y stays the same
                }
                else
                    System.out.println("boom");
                break;
            default:                    //no other key is allowed currently(we can add something in here later)
                break;
        }
    }

    /*  returns only head coordinates (useful for drawing)  */
    public Point getHead(){
        return head;
    }

    /*  gets key from event and holds it as further direction   */
    public void setHead(KeyCode key){
        lastKey = key;
    }

    /*  returns list of ALL coordinates that belong to that snake */
    public ArrayList<Point> wholeSnake(){
        ArrayList<Point> wholeSnakeList = body;
        wholeSnakeList.add(head);
        return wholeSnakeList;
    }

    //TODO collision method(firstly with borders)
    //TODO collison with it's body( when snake make a closed circle)
    
}