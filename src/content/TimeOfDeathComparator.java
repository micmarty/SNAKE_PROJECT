/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package content;

import java.util.Comparator;

/**
 *
 * @author Ania
 */
public class TimeOfDeathComparator implements Comparator<Snake>{
    @Override
    public int compare(Snake o1, Snake o2) {
        return o1.getTimeOfDeath() - o2.getTimeOfDeath();       //returns positive number if o1 is bigger, 0 if o1==o2, negative if o2 is smaller
    }
    
}
