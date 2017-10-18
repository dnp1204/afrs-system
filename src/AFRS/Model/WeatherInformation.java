package AFRS.Model;

/**
 * Representation of a weather that an airport has
 */
public class WeatherInformation {
    private String condition;
    private String temperature;

    /**
     * the constructor for weather information given all the information
     * @param condition - string:  the weather condition at the airport
     * @param temperature - string: temperature at the airport
     */
    public WeatherInformation(String condition, String temperature) {
        this.condition = condition;
        this.temperature = temperature;
    }

    /**
     * getter condition for the WeatherInformation object
     *
     * @return condition - returns the condition at the airport
     */
    public String getCondition() {
        return condition;
    }

    /**
     * getter temperature for the WeatherInformation object
     *
     * @return temperature - returns the temperature at the airport
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * weather to string
     * @return string of the weather information
     */
    @Override
    public String toString() {
        return this.condition + "," + this.temperature;
    }
}
