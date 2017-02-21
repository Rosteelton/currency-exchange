package ru.task.currency.dto;

public class ApiResponse {
    private String base;
    private String date;
    private RateObject rates;


    public ApiResponse(String base, String date, RateObject rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public RateObject getRates() {
        return rates;
    }

    @Override
    public String toString() {
        return base + " => " + rates;
    }
}
