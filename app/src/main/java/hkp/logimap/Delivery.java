package hkp.logimap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kryształ on 03.12.2015.
 */
public class Delivery implements Serializable{
    Integer id;
    String state;
    ArrayList<String> destinations = new ArrayList<>();
    ArrayList<Package> load = new ArrayList<>();

    Delivery() {
        id = (new Random()).nextInt(100)+1000;

        for(int i=0;i<5;++i)
            load.add(new Package());
    }
}
