package weathermonitor.com.weathermonitor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{


    LatLng bucharest = new LatLng(44.426, 26.102);
    LatLng timisoara = new LatLng(45.748, 21.208);
    LatLng cluj = new LatLng(46.771, 23.623);
    LatLng iasi = new LatLng(47.158, 27.601);
    LatLng craiova = new LatLng(44.330, 23.794);
    LatLng galati = new LatLng(45.435, 28.007);
    LatLng constanta = new LatLng(44.159, 28.634);

    Button goToHistory;
    private RequestFactory requestFactory;
    private GoogleMap map;
    private int lastDataId = 0;

    private Map<String, WeatherParameters> cityWeatherInfo = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToHistory = (Button) findViewById(R.id.goToHistory);
        goToHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
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

        // list of markers
        List<Marker> markers = new ArrayList<Marker>();
        Marker newMarker = map.addMarker(new MarkerOptions().position(bucharest).title("Bucuresti"));
        markers.add(newMarker);
        newMarker = map.addMarker(new MarkerOptions().position(timisoara).title("Timisoara"));
        markers.add(newMarker);
        newMarker = map.addMarker(new MarkerOptions().position(cluj).title("Cluj Napoca"));
        markers.add(newMarker);
        newMarker = map.addMarker(new MarkerOptions().position(iasi).title("Iasi"));
        markers.add(newMarker);
        newMarker = map.addMarker(new MarkerOptions().position(craiova).title("Craiova"));
        markers.add(newMarker);
        newMarker = map.addMarker(new MarkerOptions().position(galati).title("Galati"));
        markers.add(newMarker);
        newMarker = map.addMarker(new MarkerOptions().position(constanta).title("Constanta"));
        markers.add(newMarker);
        map.setInfoWindowAdapter(new CustomInfoAdapter());
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
                    System.out.println(">>>>>>last id is: " + lastDataId);
                    lastDataId = Integer.valueOf(jsonObject.getString("id"));
                    WeatherParameters wp = new WeatherParameters();
                    wp.setCity(jsonObject.getString("city"));
                    wp.setTemperature(jsonObject.getString("temperature"));
                    wp.setWindSpeed(jsonObject.getString("wind_speed"));
                    wp.setAtmPressure(jsonObject.getString("atm_pressure"));
                    wp.setHumidity(jsonObject.getString("humidity"));
                    cityWeatherInfo.put(wp.getCity(), wp);
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

    class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            View v = getLayoutInflater().inflate(R.layout.info_window, null);
            TextView city = (TextView) v.findViewById(R.id.cityName);
            TextView temp = (TextView) v.findViewById(R.id.temperature);
            TextView windSpeed = (TextView) v.findViewById(R.id.windSpeed);
            TextView humidity = (TextView) v.findViewById(R.id.humidity);
            TextView atmPressure = (TextView) v.findViewById(R.id.atm_pressure);
            WeatherParameters wp = null;
            if (marker.getTitle().equals("Bucuresti")){
                wp = cityWeatherInfo.get("B");
            } else if (marker.getTitle().equals("Timisoara")) {
                wp = cityWeatherInfo.get("TM");
            }else if (marker.getTitle().equals("Cluj Napoca")) {
                wp = cityWeatherInfo.get("CJ");
            }else if (marker.getTitle().equals("Iasi")) {
                wp = cityWeatherInfo.get("IS");
            } else if (marker.getTitle().equals("Galati")) {
                wp = cityWeatherInfo.get("GL");
            } else if(marker.getTitle().equals("Constanta")) {
                wp = cityWeatherInfo.get("CT");
            } else if(marker.getTitle().equals("Craiova")) {
                wp = cityWeatherInfo.get("CR");
            }
            city.setText(marker.getTitle());
            if(wp != null) {
                temp.setText("Temperatura: " + wp.getTemperature() + "°C");
                windSpeed.setText("Viteza vant: " + wp.getWindSpeed() + "m/s");
                humidity.setText("Umiditate: " + wp.getHumidity() + "%");
                atmPressure.setText("Presiune: " + wp.getAtmPressure() + "mb");
            }
            return v;
        }
    }
}
