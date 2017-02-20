package ru.task.currency.dto;

public class ApiResponse {
    private String base;
    private RateObject rates;

    public String getBase() {
        return base;
    }

    public RateObject getRates() {
        return rates;
    }

    @Override
    public String toString() {
        return base + " => " + rates;
    }
}
