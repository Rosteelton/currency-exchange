package ru.task.currency;

import ru.task.currency.dto.ApiResponse;
import ru.task.currency.service.LoaderBarService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App {

    public enum Currency {
        AUD, GBP, KRW, SEK, BGN, HKD, MXN, SGD, BRL, HRK, MYR, THB,
        CAD, HUF, NOK, TRY, CHF, IDR, HZD, USD, CNY, ILS, PHP, ZAR,
        CZK, INR, PLN, EUR, DKK, JPY, RON, RUB
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        ExchangeRateService webService = createWebServiceInstanceWithIO();
        Timer timer = new Timer();
        LoaderBarService loaderBar = new LoaderBarService();
        Future<ApiResponse> response = executorService.submit(webService);

        if (!response.isDone()) {
            timer.scheduleAtFixedRate(loaderBar, 0, 20);
        }

        System.out.println("\n" + response.get());
        timer.cancel();
        executorService.shutdown();
    }

    public static ExchangeRateService createWebServiceInstanceWithIO() throws IOException {
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
