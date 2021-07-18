package Utils;


import io.reactivex.Observable;
import module.url.NewsRsp;
import module.url.NewsRspAll;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
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
    @GET("covid-19/")
    Observable<ResponseBody> getCovid();

}
