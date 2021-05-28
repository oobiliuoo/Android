package com.oobiliuoo.parkingtong.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * 数据表 UserInfo 的构造类
 * @author biliu
 */
public class UsersInfo extends LitePalSupport {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private String tel;
    @Column(nullable = false)
    private String pwd;

    public UsersInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


}
