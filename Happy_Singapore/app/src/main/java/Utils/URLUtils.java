package Utils;

public class URLUtils {
    /**
     * 各种网址URL获取类
     */

    //新闻API
    public static final String news = "https://newsapi.org/v2/";
    public static final String news_addition = "top-headlines?country=sg&apiKey=";
    //data.gov.sg API URL基址
    public static final String API_base = "https://api.data.gov.sg/v1";

    //天气情况
    public static final String temperature = API_base + "/environment/air-temperature";
    public static final String rainfall = API_base + "/environment/rainfall";
    public static final String humidity = API_base + "/environment/relative-humidity";
    public static final String wind_direction = API_base + "/environment/wind-direction";
    public static final String wind_speed = API_base + "/environment/wind-speed";
}
