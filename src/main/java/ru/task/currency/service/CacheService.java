package ru.task.currency.service;

import ru.task.currency.dto.ApiResponse;
import ru.task.currency.dto.RateObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Stream;

public class CacheService {
    private String path;

    public CacheService(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        this.path = path;
    }

    //file
    //from to timestamp rate
    public Optional<ApiResponse> getActualExchangeRateFromFile(String fromCurrency, String toCurrency) {

        Optional<ApiResponse> result = Optional.empty();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();

        try (Stream<String> stream = Files.lines((Paths.get(path)))) {
            result = stream.map(line -> line.split(" "))
                    .filter(str -> {
                        LocalDate rateDate = LocalDate.parse(str[2], formatter);
                        Long daysPeriod = ChronoUnit.DAYS.between(rateDate, now);
                        return (str[0].equals(fromCurrency) && str[1].equals(toCurrency) && daysPeriod.equals(0L));
                    }).map(oneOpt -> {
                        return new ApiResponse(oneOpt[0], oneOpt[2], new RateObject(oneOpt[1], Double.parseDouble(oneOpt[3])));
                    }).findFirst();
            stream.close();
        } catch (IOException e) {
            System.out.println("Can't read file");
        }

        return result;
    }

    public void saveExchangeRateRowToFile(ApiResponse rate) {
        String stringToPersist = rate.getBase() + " " + rate.getRates().getName() +
                " " + rate.getDate() + " " + rate.getRates().getRate().toString() + "\n";
        try {
            Files.write(Paths.get(path), stringToPersist.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Writing to file failed");
        }
    }

}
