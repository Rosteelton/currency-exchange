package ru.task.currency;

import ru.task.currency.dto.ApiResponse;
import ru.task.currency.service.CacheService;
import ru.task.currency.service.WebService;

import java.util.Optional;
import java.util.concurrent.Callable;

public class ExchangeRateService implements Callable<Optional<ApiResponse>>{

    private WebService webService;
    private CacheService cacheService;
    private String fromCurrency;
    private String toCurrency;

    public ExchangeRateService(WebService webService, CacheService cacheService, String fromCurrency, String toCurrency) {
        this.webService = webService;
        this.cacheService = cacheService;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    public Optional<ApiResponse> getExchangeRate() {
        Optional<ApiResponse> rateFromFile = cacheService.getActualExchangeRateFromFile(fromCurrency, toCurrency);
        if (rateFromFile.isPresent()) {
            System.out.println("\nReturned from file");
            return rateFromFile;
        } else {
            Optional<ApiResponse> rateFromWeb = webService.fetchExchangeRateFromWeb(fromCurrency, toCurrency);
            rateFromWeb.ifPresent(some -> cacheService.saveExchangeRateRowToFile(some));
            System.out.println("\nReturned from web");
            return rateFromWeb;
        }
    }

    @Override
    public Optional<ApiResponse> call() throws Exception {
        return getExchangeRate();
    }
}
