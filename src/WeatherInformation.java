public class WeatherInformation {
    private String condition;
    private String temperature;

    public WeatherInformation(String condition, String temperature) {
        this.condition = condition;
        this.temperature = temperature;
    }

    public String getCondition() {
        return condition;
    }

    public String getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return this.condition + "," + this.temperature;
    }
}
