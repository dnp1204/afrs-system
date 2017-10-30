package AFRS.Model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The Airport model - each instance represents a real world airport
 */
public class Airport {
    private String code;
    private String name;
    private Queue<WeatherInformation> weatherTempList;
    private int delay;
    private int connection;

    /**
     * constructor for airports
     * @param code - string: three digit code representation of the airport
     * @param name - string: the full name of  the airport
     */
    public Airport(String code, String name) {
        this.code = code;
        this.name = name;
        weatherTempList = new LinkedList<>();
    }

    /**
     * Created a getter for the airport code
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * getter for the name of the airport
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * getter for the WeatherInformation list
     * @return weatherTempList
     */
    public Queue<WeatherInformation> getWeatherTempList() {
        return weatherTempList;
    }

    /**
     * getter for the delay time of an airport
     * @return delay
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Function to add a time delay to an airport
     * only used when parsing the from the fight delay textfile
     * @param delay - time as an int of a delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getConnection() { return connection; }

    public void setConnection(int connection) { this.connection = connection; }

    /**
     * a function to add the a WeatherInformation object to the the weatherTempList
     * used only when parsing the WeatherInformation info from a text file
     * @param weatherInformation - a WeatherInformation object created in the AirportRequest
     */
    public void addWeatherToList(WeatherInformation weatherInformation) {
        weatherTempList.add(weatherInformation);
    }

    /**
     * the helper method to display the condition and temperature of
     * an airport
     * @return temp.toString()
     */
    private String displayCurrentWeather() {
        WeatherInformation temp = weatherTempList.remove();
        weatherTempList.add(temp);
        return temp.toString();
    }

    /**
     * display the airport info included weather information and delay time
     * @return string
     */
    public String displayAirportInfo() {
        return "airport," + name + ',' + displayCurrentWeather() + ',' + delay;
    }
}