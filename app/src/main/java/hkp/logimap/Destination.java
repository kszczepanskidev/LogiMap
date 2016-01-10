package hkp.logimap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by krysztal on 21.12.15.
 */
public class Destination implements Serializable{
    String name;
    String state, date;
    ArrayList<Package> packages;

    Destination() {
        packages = new ArrayList<>();

        /*for(int i=0;i<5;++i)
            packages.add(new Package());*/

        state = "On da road";
        date = "Yesterday";
    }
}
