package hkp.logimap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kryształ on 03.12.2015.
 */
public class Delivery implements Serializable{
    Integer id;

    ArrayList<Destination> destinations;

    Delivery() {
        destinations = new ArrayList<>();
        id = (new Random()).nextInt(100)+1000;
    }
}
