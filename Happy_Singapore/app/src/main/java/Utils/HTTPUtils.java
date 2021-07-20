package Utils;


import io.reactivex.Observable;
import module.url.NewsRsp;
import module.url.NewsRspAll;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface HTTPUtils {
    /**
     * 网络请求帮助类
     */

    /**
     * 获取新闻
     * @return
     */
    @GET(URLUtils.news_addition + APIkeys.news_API_key)
    Observable<NewsRspAll> getNews();

    /**
     * 获取图片
     */
    @GET
    Observable<ResponseBody> getPic(@Url String url);

    /**
     * 获取疫情资讯
     */
    @GET
    Observable<ResponseBody> getCovid(@Url String url);

    /**
     * 获取疫情总结
     */
    @GET
    Observable<ResponseBody> getCovidSummary(@Url String url);

    /**
     * 测试用GET接口
     */
    @GET
    Observable<ResponseBody> test(@Url String url);

    /**
     * 获取公交信息
     */
    @Headers("AccountKey:"+ APIkeys.LTA_API_key)
    @GET("ltaodataservice/BusArrivalv2")
    Observable<ResponseBody> getBusArrivals();


}
