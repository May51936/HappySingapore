package Utils;


import io.reactivex.Observable;
import module.url.NewsRsp;
import module.url.NewsRspAll;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

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

}
