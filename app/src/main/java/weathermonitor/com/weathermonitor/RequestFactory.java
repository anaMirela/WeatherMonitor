package weathermonitor.com.weathermonitor;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mi on 12/6/2016.
 */

public class RequestFactory {

    public static final String SERVER_URL ="http://172.19.10.6/server_weather_app/index.php";//"http://192.168.0.100/server_weather_app/index.php";

    public InputStream createGetRequest(String query) {
        System.out.println(">>>>>>>>>>>>>>request is " +SERVER_URL + query);
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(SERVER_URL + query).openConnection();
            Log.i("tag", con.getResponseCode() + "");
            return con.getInputStream();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
