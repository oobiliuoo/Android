package com.oobiliuoo.parkingtong.object;

public class OrderInfo {
    private String orderID;
    private String parkName;
    private String oderTime;
    private String money;
    private String inTime;
    private String outTime;
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
