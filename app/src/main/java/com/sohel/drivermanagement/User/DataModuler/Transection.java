package com.sohel.drivermanagement.User.DataModuler;

public class Transection {
    String PersonName,date,floorId,payment,personId,transactionId,time;


    public Transection(){

    }

    public Transection(String personName, String date, String floorId, String payment, String personId, String transactionId, String time) {
        PersonName = personName;
        this.date = date;
        this.floorId = floorId;
        this.payment = payment;
        this.personId = personId;
        this.transactionId = transactionId;
        this.time = time;
    }

    public String getPersonName() {
        return PersonName;
    }

    public String getDate() {
        return date;
    }

    public String getFloorId() {
        return floorId;
    }

    public String getPayment() {
        return payment;
    }

    public String getPersonId() {
        return personId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTime() {
        return time;
    }
}
