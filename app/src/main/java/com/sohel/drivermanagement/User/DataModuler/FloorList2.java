package com.sohel.drivermanagement.User.DataModuler;

public class FloorList2 {
    String aloted;
    String alotedPerson;
    String alotedRenterId;
    String dueAmount;
    String electricityBill;
    String electricityIncluded;
    String floorId;
    String floorName;
    String gasBill;
    String gasIncluded;
    String homeId;
    String rentAmount;
    String utilitiesBill;
    String utilitsIncluded;
    String waterBill;
    String waterIncluded;
    String advanced;

    public  FloorList2(){}

    public FloorList2(String aloted, String alotedPerson, String alotedRenterId, String dueAmount, String electricityBill, String electricityIncluded, String floorId, String floorName, String gasBill, String gasIncluded, String homeId, String rentAmount, String utilitiesBill, String utilitsIncluded, String waterBill, String waterIncluded,String advanced) {
        this.aloted = aloted;
        this.alotedPerson = alotedPerson;
        this.alotedRenterId = alotedRenterId;
        this.dueAmount = dueAmount;
        this.electricityBill = electricityBill;
        this.electricityIncluded = electricityIncluded;
        this.floorId = floorId;
        this.floorName = floorName;
        this.gasBill = gasBill;
        this.gasIncluded = gasIncluded;
        this.homeId = homeId;
        this.rentAmount = rentAmount;
        this.utilitiesBill = utilitiesBill;
        this.utilitsIncluded = utilitsIncluded;
        this.waterBill = waterBill;
        this.waterIncluded = waterIncluded;
        this.advanced=advanced;
    }


    public String getAloted() {
        return aloted;
    }

    public String getAlotedPerson() {
        return alotedPerson;
    }

    public String getAlotedRenterId() {
        return alotedRenterId;
    }

    public String getDueAmount() {
        return dueAmount;
    }

    public String getElectricityBill() {
        return electricityBill;
    }

    public String getElectricityIncluded() {
        return electricityIncluded;
    }

    public String getFloorId() {
        return floorId;
    }

    public String getFloorName() {
        return floorName;
    }

    public String getGasBill() {
        return gasBill;
    }

    public String getGasIncluded() {
        return gasIncluded;
    }

    public String getHomeId() {
        return homeId;
    }

    public String getRentAmount() {
        return rentAmount;
    }

    public String getUtilitiesBill() {
        return utilitiesBill;
    }

    public String getUtilitsIncluded() {
        return utilitsIncluded;
    }

    public String getWaterBill() {
        return waterBill;
    }

    public String getWaterIncluded() {
        return waterIncluded;
    }

    public String getAdvanced() {
        return advanced;
    }
}
