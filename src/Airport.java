import java.util.Queue;

public class Airport {
    private String code;
    private String name;
    private Queue<WeatherInformation> weatherTempList;
    private int delay;

    public Airport(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Queue<WeatherInformation> getWeatherTempList() {
        return weatherTempList;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void addWeatherToList(WeatherInformation weatherInformation) {
        weatherTempList.add(weatherInformation);
    }

    private String displayCurrentWeather() {
        WeatherInformation temp = weatherTempList.poll();
        weatherTempList.add(temp);
        return temp.toString();
    }

    public String displayAirportInfo() {
        return "airport," + name + ',' + displayCurrentWeather();
    }
}