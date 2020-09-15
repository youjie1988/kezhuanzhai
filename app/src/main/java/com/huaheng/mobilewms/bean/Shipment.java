package com.huaheng.mobilewms.bean;


import java.util.List;

public class Shipment  {

    private ShipmentHeader shipmentHeader;

    private List<ShipmentDetail> shipmentDetails;

    public ShipmentHeader getShipmentHeader() {
        return shipmentHeader;
    }

    public void setShipmentHeader(ShipmentHeader shipmentHeader) {
        this.shipmentHeader = shipmentHeader;
    }

    public List <ShipmentDetail> getShipmentDetails() {
        return shipmentDetails;
    }

    public void setShipmentDetails(List <ShipmentDetail> shipmentDetails) {
        this.shipmentDetails = shipmentDetails;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "shipmentHeader=" + shipmentHeader +
                ", shipmentDetails=" + shipmentDetails +
                '}';
    }
}
