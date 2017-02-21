package ru.task.currency;

import ru.task.currency.dto.ApiResponse;
import ru.task.currency.service.CacheService;
import ru.task.currency.service.LoaderBarService;
import ru.task.currency.service.WebService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App {

    public static void main(String[] args) {
        ExchangeRateService exchangeRateService;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Timer timer = new Timer();
        LoaderBarService loaderBar = new LoaderBarService();
        try {
            exchangeRateService = createExchangeRateServiceInstanceWithIO();
            Future<Optional<ApiResponse>> response = executorService.submit(exchangeRateService);

            if (!response.isDone()) {
                timer.scheduleAtFixedRate(loaderBar, 0, 20);
            }

            Optional<ApiResponse> desiredRate = response.get();

            if (desiredRate.isPresent()) {
                System.out.println("\n" + desiredRate.get());
            } else {
                System.out.println("Sorry desired exchange rate hasn't  been found.\nPlease try again later");
            }
        } catch (Exception e) {
            System.out.println("Oops! Something wrong!");
        } finally {
            timer.cancel();
            executorService.shutdown();
        }
    }

    public static ExchangeRateService createExchangeRateServiceInstanceWithIO() throws IOException {
        ArrayList<String> dictionary = initDictionary();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter FROM currency");
        String from = in.readLine();

        while (!dictionary.contains(from)) {
            System.out.println("Sorry, this currency is not supported!");
            System.out.println("Enter FROM currency");
            from = in.readLine();
        }

        System.out.println("Enter TO currency");
        String to = in.readLine();

        while (!dictionary.contains(to)) {
            System.out.println("Sorry, this currency is not supported!");
            System.out.println("Enter TO currency");
            to = in.readLine();
        }

        WebService webService = new WebService();
        CacheService cacheService = new CacheService("SavedExchangeRates.txt");

        return new ExchangeRateService(webService, cacheService, from, to);
    }

    public static ArrayList<String> initDictionary() {
        return new ArrayList<>(
                Arrays.asList("AUD", "GBP", "KRW", "SEK", "BGN", "HKD", "MXN", "SGD", "BRL", "HRK",
                        "MYR", "THB", "CAD", "HUF", "NOK", "TRY", "CHF", "IDR", "HZD", "USD", "CNY",
                        "ILS", "PHP", "ZAR", "CZK", "INR", "PLN", "EUR", "DKK", "JPY", "RON", "RUB")
        );
    }
}
