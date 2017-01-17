package weathermonitor.com.weathermonitor;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryActivity extends Activity {

    String[] cities = new String[7];
    RequestFactory requestFactory;
    String cityParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        requestFactory = new RequestFactory();
        initCitiesList();
        ListView listView = (ListView) findViewById(R.id.historyList);
        listView.setTextFilterEnabled(true);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.history_item, cities);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String selectedCity = ((TextView) view).getText().toString();
                System.out.println(">>>>>>>>>>>>>>selectedCity " + selectedCity);
                switch(selectedCity) {
                    case "Bucuresti":
                        cityParam = "B";
                        break;
                    case "Timisoara":
                        cityParam = "TM";
                        break;
                    case "Iasi" :
                        cityParam = "IS";
                        break;
                    case "Cluj Napoca":
                        cityParam = "CJ";
                        break;
                    case "Constanta":
                        cityParam = "CT";
                        break;
                    case "Craiova":
                        cityParam = "CR";
                        break;
                    default:
                        cityParam ="GL";
                }
                System.out.println(">>>>>>>>>>>>>>city param " + cityParam);
                Intent intent = new Intent(HistoryActivity.this, CityInformationActivity.class);
                intent.putExtra("city", cityParam);
                startActivity(intent);
            }
        });
    }

    private void initCitiesList() {
        cities[0] = "Bucuresti";
        cities[1] = "Timisoara";
        cities[2] = "Cluj Napoca";
        cities[3] = "Constanta";
        cities[4] = "Galati";
        cities[5] = "Iasi";
        cities[6] = "Craiova";
    }

 }
