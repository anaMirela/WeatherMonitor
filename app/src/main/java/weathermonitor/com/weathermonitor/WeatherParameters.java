package weathermonitor.com.weathermonitor;

/**
 * Created by Mi on 1/17/2017.
 */

public class WeatherParameters {

    private String city;
    private String temperature;
    private String windSpeed;
    private String atmPressure;
    private String humidity;
    private String timestamp;

    public WeatherParameters () {
        super();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getAtmPressure() {
        return atmPressure;
    }

    public void setAtmPressure(String atmPressure) {
        this.atmPressure = atmPressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
