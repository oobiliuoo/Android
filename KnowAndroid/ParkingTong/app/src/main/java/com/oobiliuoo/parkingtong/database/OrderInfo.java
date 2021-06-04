package com.oobiliuoo.parkingtong.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * @author biliu
 */
public class OrderInfo extends LitePalSupport {

    @Column(nullable = false)
    private String orderID;

    @Column(nullable = false)
    private String orderTel;

    @Column(nullable = false)
    private String parkName;
    @Column(nullable = false)
    private String oderTime;
    @Column(defaultValue = "0")
    private String money;
    @Column(defaultValue = "unKnow")
    private String inTime;
    @Column(defaultValue = "unKnow")
    private String outTime;
    @Column(defaultValue = "预订")
    private String state;


    public OrderInfo() {
    }


    public OrderInfo(String orderID, String parkName, String oderTime, String state) {
        this.orderID = orderID;
        this.parkName = parkName;
        this.oderTime = oderTime;
        this.state = state;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderTel() {
        return orderTel;
    }

    public void setOrderTel(String orderTel) {
        this.orderTel = orderTel;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getOderTime() {
        return oderTime;
    }

    public void setOderTime(String oderTime) {
        this.oderTime = oderTime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
