package module.url;

import module.url.BaseRsp;

public class NewsRsp extends BaseRsp {
    /**
     * 新闻请求返回内容
     */

    private String title;
    private String name;
    private String date;
    private String pic;
    private String url;

    public void set_title(String title){
        this.title = title;
    }

    public String get_title(){
        return this.title;
    }

    public void set_name(String name){
        this.name = name;
    }

    public String get_name(){
        return this.name;
    }

    public void set_date(String date){
        this.date = date;
    }

    public String get_date(){
        return this.date;
    }

    public void set_pic(String pic){
        this.pic = pic;
    }

    public String get_pic(){
        return this.pic;
    }

    public void set_url(String url){
        this.url = url;
    }

    public String get_url(){
        return this.url;
    }

}
