package hkp.logimap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public interface AsyncListener {
  public void processFinish(ArrayList<LatLng> output,boolean route);
}
