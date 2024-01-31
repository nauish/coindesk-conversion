package com.rickli.cathay.service;

import com.rickli.cathay.dto.CoindeskApiResponse;
import com.rickli.cathay.entity.Currency;
import com.rickli.cathay.repository.CurrencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CoindeskServiceImpl implements CoindeskService {

    private static final String COINDESK_API_URL = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private final CurrencyRepository currencyRepository;

    public CoindeskServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public CoindeskApiResponse consumeCoindeskAPI() {
        return new RestTemplate().getForObject(COINDESK_API_URL, CoindeskApiResponse.class);
    }

    public CoindeskApiResponse convertCoindeskApiData() {
        CoindeskApiResponse response = consumeCoindeskAPI();

        String originalUpdatedDate = response.getTime().getUpdatedISO();
        response.getTime().setUpdated(convertDateFormat(originalUpdatedDate));

        response.getBpi().forEach((currencyCode, currencyInfo) -> {
            Map<String, String> currencyNamesMap = getCurrencyNamesMap(currencyRepository.findAll());
            String currencyName = currencyNamesMap.getOrDefault(currencyCode, "無對應中文名");
            currencyInfo.setName(currencyName);
        });

        return response;
    }

    // 一次從 DB 拿資料避免取 n 次
    private Map<String, String> getCurrencyNamesMap(List<Currency> currencies) {
        return currencies.stream()
                .collect(Collectors.toMap(Currency::getCode, Currency::getName));
    }

    // 轉換 ISO 時間至符合時間格式範例時間格式：1990/01/01 00:00:00
    private String convertDateFormat(String inputDateTime) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(inputDateTime);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return offsetDateTime.format(outputFormatter);
    }
}