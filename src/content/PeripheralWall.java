package content;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Michał Martyniak on 20.03.2016.
 */
public class PeripheralWall {
    private ArrayList<Point> segments; //holds Wall segments

    /*  create Wall with segments on specific position */
    public PeripheralWall(int size) {
        segments = new ArrayList<>();
        for(int x = 0; x < size; x++)
            for(int y = 0; y < size; y++){
                if(x==0 || y==0 || x == size-1 || y == size-1)
                    segments.add(new Point(x,y));
            }
    }
    /*  Get list of wall segments   */
    public ArrayList<Point> getWall(){
        return segments;
    }

}
