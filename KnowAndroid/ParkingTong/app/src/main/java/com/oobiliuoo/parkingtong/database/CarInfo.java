package com.oobiliuoo.parkingtong.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * @author biliu
 */
public class CarInfo extends LitePalSupport {

    /**
     * 帐号
     * */
    @Column(nullable = false)
    private String tel;
    /**
     * 车牌号
     * */
    @Column(nullable = false)
    private String carNum;

    /**
     * 车名
     * */
    private String carName;

    /**
     * 车型
     * */
    private String carModel;

    public CarInfo() {
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
}
