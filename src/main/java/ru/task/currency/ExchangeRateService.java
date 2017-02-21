package ru.task.currency;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.task.currency.dto.ApiResponse;
import ru.task.currency.dto.RateObject;
import ru.task.currency.serializer.RatesDeserializer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

public class ExchangeRateService implements Callable<ApiResponse>{

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(RateObject.class, new RatesDeserializer())
            .create();

    private String fromCurrency;
    private String toCurrency;

    public ExchangeRateService(String fromCurrency, String toCurrency) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    public ApiResponse call() throws Exception {
        return getExchangeRate();
    }

    public ApiResponse getExchangeRate() throws IOException {
        URL url = getUrl();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        ApiResponse response;
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            response = gson.fromJson(br, ApiResponse.class);
            in.close();
            br.close();
        } finally {
            urlConnection.disconnect();
        }
        return response;
    }

    private URL getUrl() throws MalformedURLException {
        String urlString = "http://api.fixer.io/latest?base=" + this.fromCurrency + "&symbols=" + this.toCurrency;
        return new URL(urlString);
    }

}
