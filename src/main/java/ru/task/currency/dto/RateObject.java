package ru.task.currency.dto;

public class RateObject {
    private String name;
    private Double rate;

    public RateObject(String name, double rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public Double getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return name + " : " + rate;
    }
}