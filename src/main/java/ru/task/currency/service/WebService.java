package ru.task.currency.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.task.currency.dto.ApiResponse;
import ru.task.currency.dto.RateObject;
import ru.task.currency.serializer.RatesDeserializer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class WebService {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(RateObject.class, new RatesDeserializer())
            .create();

    public Optional<ApiResponse> fetchExchangeRateFromWeb(String fromCurrency, String toCurrency) {
        HttpURLConnection urlConnection = null;
        Optional<ApiResponse> response = Optional.empty();
        try {
            URL url = getUrl(fromCurrency, toCurrency);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            response = Optional.of(gson.fromJson(br, ApiResponse.class));
            in.close();
            br.close();
        } catch (IOException e) {
            System.out.println("Something went wrong while fetching info from web");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return response;
    }

    private URL getUrl(String fromCurrency, String toCurrency) throws MalformedURLException {
        String urlString = "http://api.fixer.io/latest?base=" + fromCurrency + "&symbols=" + toCurrency;
        return new URL(urlString);
    }
}

