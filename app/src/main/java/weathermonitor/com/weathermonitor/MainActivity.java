package weathermonitor.com.weathermonitor;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

    private RequestFactory requestFactory;
    private GoogleMap map;
    private int lastDataId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestFactory = new RequestFactory();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            public void run() {
                Log.i("test", "thread");
                new GetRecentData().execute();
                handler.postDelayed(this, 20000);
            }
        };
        runnable.run();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng bucharest = new LatLng(44.426, 26.102);
        LatLng timisoara = new LatLng(45.748, 21.208);

        // list of markers
        List<Marker> markers = new ArrayList<Marker>();
        Marker newMarker = map.addMarker(new MarkerOptions().position(bucharest).title("Marker for Bucharest"));
        markers.add(newMarker);
        newMarker = map.addMarker(new MarkerOptions().position(timisoara).title("Marker for Bucharest"));
        markers.add(newMarker);

        // calculate bounds for the markers on the map
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker m : markers) {
            builder.include(m.getPosition());
        }
        LatLngBounds bounds = builder.build();
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,250,250, 0));
    }

    private class GetRecentData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String query = "?action=recentdata&lastId=" + lastDataId;

            InputStream is = requestFactory.createGetRequest(query);
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(is));
            try {
                String dataJsonArray = inputStream.readLine();
                if (dataJsonArray != null) {
                    System.out.println(">>>>>>>>" + dataJsonArray);
                } else {
                    System.out.println(">>>>>>NULL");
                }

                JSONArray jsonArray = new JSONArray(dataJsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    System.out.println(">>>>>>>>" + jsonArray.get(i).toString());
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    // TODO - prelucreaza datele
                    lastDataId = Integer.valueOf(jsonObject.getString("id"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
