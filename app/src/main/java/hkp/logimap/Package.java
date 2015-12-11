package hkp.logimap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by kryształ on 03.12.2015.
 */
public class Package implements Serializable{
    Integer id;
    String date, state;
//    ArrayList<Item> items = new ArrayList<>();

    Package() {
        id = (new Random()).nextInt(100);
        state = "frysha glynca";
        date = "1337 12 24";

//        for(int i=0;i<3;++i)
//            items.add(new Item());
    }
}
