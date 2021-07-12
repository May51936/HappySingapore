package module.url;

import com.google.gson.annotations.SerializedName;

public class BaseRsp<T> {
    /**
     * 请求返回基类
     */
    @SerializedName(value = "error", alternate = {"errormsg", "message", "msg"})
    private String error;
    @SerializedName(value = "errorcode", alternate = {"errorCode", "code", "statusCode"})
    private int error_code;

    public void set_error(String error){
        this.error = error;
    }

    public String get_error(){
        return this.error;
    }

    public void set_error_code(int error_code){
        this.error_code = error_code;
    }

    public int get_error_code(){
        return this.error_code;
    }
}
