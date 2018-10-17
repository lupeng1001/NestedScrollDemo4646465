package cn.tsou.bean;

/**
 * 所有实体类的基类
 * @auther lupeng
 * @date 2018-10-17
 */

public class BaseInfo {
    protected String code;
    protected String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
