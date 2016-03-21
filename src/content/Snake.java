package content;

import javafx.scene.input.KeyCode;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Michał Martyniak and company :P on 19.03.2016.
 */
public class Snake {
    private Point head;             //coordinates of snake's head
    private ArrayList<Point> body;  //holds body segments
    private KeyCode lastKey;        //direction variable, allow to continue snake's movement in that direction constantly
    private int life;               //1==snakie is alive, 0== snake is dead, -1== player resigned
    private int points;             //player's points
    
    /*  create snake that has ony HEAD with given coordinates   */
    public Snake(int staryAtX, int startAtY) {
        head = new Point(staryAtX,startAtY);      //head always exist
        body = new ArrayList<>();                 //at the begining body is empty
        lastKey = KeyCode.K;                      //no key is pressed at the beginning
        life=1;                                   //snake is alive
    }



    /*  changes the coordinates of snake's head (and convert old head to new body element)  */
    //change to boolean later(for collision)
    public void move(int[][] mask){
       // body.add(head);                 //save head as a body now
        switch (lastKey){
            case L:
                life=-1;
                break;
            case W:
                if(mask[this.head.x][this.head.y-1] == BarrierType.EMPTY.value){        //check if              
                    body.add(head);
                    head.translate(0,-1);   //x stays the same, y one upper
                }
                else{
                    this.setLife(0);
                }
                break;
            case S:
                if(mask[this.head.x][this.head.y+1] == BarrierType.EMPTY.value){
                    body.add(head);
                    head.translate(0,1);    //x stays the same, y one down
                }
                else{
                    this.setLife(0);
                }
                break;
            case A:
                if(mask[this.head.x-1][this.head.y] == BarrierType.EMPTY.value){
                    body.add(head);
                    head.translate(-1,0);   //x one left, y stays the same              
                }
                else{
                    this.setLife(0);
                }
                break;
            case D:
                if(mask[this.head.x+1][this.head.y] == BarrierType.EMPTY.value){
                    body.add(head);
                    head.translate(1,0);    //x one right, y stays the same
                }
                else{
                    this.setLife(0);
                }
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
    
    /*  returns value of life ^^ */
    public int getLife(){
        return life;
    }
    
    /*set snake's life value)*/
    public void setLife(int value){
        if(value==1||value==0||value==-1)
            life=value;
    }
    
    
    /*  returns list of ALL coordinates that belong to that snake */
    public ArrayList<Point> wholeSnake(){
        ArrayList<Point> wholeSnakeList = body;
        wholeSnakeList.add(head);
        return wholeSnakeList;
    }
    
    public void SnakeReady(int staryAtX, int startAtY) {
        head = new Point(staryAtX,startAtY);      //head always exist
        body = new ArrayList<>(); 
        if(life==0)                               //no when player resigned
            life=1;
    }

    
}