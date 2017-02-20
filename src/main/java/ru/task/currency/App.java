package ru.task.currency;

import ru.task.currency.dto.ApiResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {

    public enum Currency {
        AUD, GBP, KRW, SEK, BGN, HKD, MXN, SGD, BRL, HRK, MYR, THB,
        CAD, HUF, NOK, TRY, CHF, IDR, HZD, USD, CNY, ILS, PHP, ZAR,
        CZK, INR, PLN, EUR, DKK, JPY, RON, RUB
    }

    public static void main(String[] args) throws IOException {

        ExchangeRateService webService = createWebServiceInstanceWithConsole();
        ApiResponse response = webService.getExchangeRate();

        System.out.println(response);

    }

    public static ExchangeRateService createWebServiceInstanceWithConsole() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter from currency");
        String from = in.readLine();

        Currency currencyFrom = Currency.valueOf(from);
        //handle invalid string
        //обернуть в try и терминэйт если поймал эксепшн?

        System.out.println("Enter to currency");
        String to = in.readLine();
        return new ExchangeRateService(from, to);
    }


}
