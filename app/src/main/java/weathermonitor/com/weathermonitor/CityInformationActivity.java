package weathermonitor.com.weathermonitor;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CityInformationActivity extends AppCompatActivity {

    RequestFactory requestFactory;
    String cityParam;
    List<WeatherParameters> weatherHistory = new ArrayList<>();
    List<String> historyInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_information);
        requestFactory = new RequestFactory();
        Intent intent = getIntent();
        cityParam = intent.getStringExtra("city");
        GetCityData task = new GetCityData();
        task.execute();
        new GetCityData().execute();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fillHistoryInfo();
    }

    private void fillHistoryInfo() {
        historyInfo = new ArrayList<>();
        String item = "";
        for(WeatherParameters wp : weatherHistory) {
            item = "Timp: " + wp.getTimestamp() + "; " + wp.getTemperature() + "°C; " +
                    wp.getWindSpeed() + "m/s; " + wp.getHumidity() + "%; " +
                    wp.getAtmPressure() + "mb";
            historyInfo.add(item);
        }
        String[] historyInfoArr = new String[historyInfo.size()];
        for(int i = 0; i < historyInfo.size(); i++) {
            historyInfoArr[i] = historyInfo.get(i);
        }
        ListView listView = (ListView) findViewById(R.id.cityHistoryList);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.city_info_item, historyInfoArr);
        listView.setAdapter(listAdapter);
    }

    private class GetCityData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String query = "?action=citydata&city=" + cityParam;

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
                weatherHistory = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    System.out.println(">>>>>>>>" + jsonArray.get(i).toString());
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    WeatherParameters wp = new WeatherParameters();
                    wp.setCity(jsonObject.getString("city"));
                    wp.setTemperature(jsonObject.getString("temperature"));
                    wp.setWindSpeed(jsonObject.getString("wind_speed"));
                    wp.setAtmPressure(jsonObject.getString("atm_pressure"));
                    wp.setHumidity(jsonObject.getString("humidity"));
                    wp.setTimestamp(jsonObject.getString("timestamp"));
                    weatherHistory.add(wp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
