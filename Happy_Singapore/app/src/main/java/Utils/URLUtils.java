package Utils;

public class URLUtils {
    /**
     * 各种网址URL获取类
     */

    //新闻API
    public static final String news = "https://newsapi.org/";
    public static final String news_addition = "v2/top-headlines?country=sg&apiKey=";
    //疫情资讯
    public static final String COVID_19 = "https://www.moh.gov.sg/news-highlights/";
    //疫情详细信息
    public static final String moh = "https://www.moh.gov.sg/covid-19/";
    //data.gov.sg API URL基址
    public static final String API_base = "https://api.data.gov.sg/v1";

    //公交到站信息
    public static final String bus_arrival = "http://datamall2.mytransport.sg/";

    //天气情况
    public static final String temperature = API_base + "/environment/air-temperature";
    public static final String rainfall = API_base + "/environment/rainfall";
    public static final String humidity = API_base + "/environment/relative-humidity";
    public static final String wind_direction = API_base + "/environment/wind-direction";
    public static final String wind_speed = API_base + "/environment/wind-speed";
}
