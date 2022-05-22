package com.example.tpo5.models;

public class Car {
    private int id;
    private String make;
    private String model;
    private String type;
    private int productionYear;
    private int fuelConsumption;

    public Car(int id, String make, String model, String type, int productionYear, int fuelConsumption) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.type = type;
        this.productionYear = productionYear;
        this.fuelConsumption = fuelConsumption;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(int fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }
}
