package cn.smartDietician.backEnd.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by wangshuai on 2016/4/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalerIdPassword {
    private String ID;
    private String PassWord;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }
}
