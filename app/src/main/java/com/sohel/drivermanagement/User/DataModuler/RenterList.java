package com.sohel.drivermanagement.User.DataModuler;

import com.sohel.drivermanagement.User.Adapter.RenterListAdapter;

public class RenterList {
    String advanced;
    String dueAmount;
    String floorId;
    String homeId;
    String name;
    String phone;
    String renterId;
    String floorName;
    String homeName;

    public RenterList(){}

    public RenterList(String advanced, String dueAmount, String floorId, String homeId, String name, String phone, String renterId,String floorName,String homeName) {
        this.advanced = advanced;
        this.dueAmount = dueAmount;
        this.floorId = floorId;
        this.homeId = homeId;
        this.name = name;
        this.phone = phone;
        this.renterId = renterId;
        this.floorName=floorName;
        this.homeName=homeName;
    }


    public String getAdvanced() {
        return advanced;
    }

    public String getDueAmount() {
        return dueAmount;
    }

    public String getFloorId() {
        return floorId;
    }

    public String getHomeId() {
        return homeId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRenterId() {
        return renterId;
    }

    public String getFloorName() {
        return floorName;
    }

    public String getHomeName() {
        return homeName;
    }
}
