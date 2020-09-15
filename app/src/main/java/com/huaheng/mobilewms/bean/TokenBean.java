package com.huaheng.mobilewms.bean;

/**
 * Created by youjie on 2020/6/17
 */
public class TokenBean {

    private String token;
    private String expireTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "TokenBean{" +
                "token='" + token + '\'' +
                ", expireTime='" + expireTime + '\'' +
                '}';
    }
}
