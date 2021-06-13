package com.oobiliuoo.parkingtong.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * @author biliu
 */
public class CarModelTable extends LitePalSupport {

    /**
     *  车名
     * */
    @Column(nullable = false)
    private String carName ;


    /**
     *  车型
     * */
    @Column(nullable = false)
    private String carModel ;

    public CarModelTable() {
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
