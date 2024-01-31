package com.rickli.cathay.controller;

import com.rickli.cathay.dto.CoindeskApiResponse;
import com.rickli.cathay.service.CoindeskService;
import com.rickli.cathay.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class CoindeskController {

    private final CoindeskService coindeskService;

    public CoindeskController(CoindeskService coindeskService, CurrencyService currencyService) {
        this.coindeskService = coindeskService;
    }

    @GetMapping("/v1/coindesk")
    // 呼叫 Coindesk 的 API。
    public ResponseEntity<CoindeskApiResponse> getCurrentCoindeskPrice() {
        CoindeskApiResponse coindeskApiResponse = this.coindeskService.consumeCoindeskAPI();
        return new ResponseEntity<>(coindeskApiResponse, HttpStatus.OK);
    }

    @GetMapping("/v2/coindesk")
    // 呼叫 Coindesk 的 API，並進行資料轉換，組成新 API。
    public ResponseEntity<CoindeskApiResponse> convertCoindeskPrice() {
        CoindeskApiResponse coindeskApiResponse = this.coindeskService.convertCoindeskApiData();
        return new ResponseEntity<>(coindeskApiResponse, HttpStatus.OK);
    }
}
