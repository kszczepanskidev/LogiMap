package hkp.logimap;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by kryształ on 03.12.2015.
 */
public class Item implements Serializable {
    Integer id;
    String state;

    Item() {
        id = (new Random()).nextInt(50) + 100;
        state = "dżast paked";
    }
}
