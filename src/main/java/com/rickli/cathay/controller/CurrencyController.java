package com.rickli.cathay.controller;

import com.rickli.cathay.entity.Currency;
import com.rickli.cathay.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/currency")
// 幣別 DB 維護功能。
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this .currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<List<Currency>> getAllCurrencies(){
        List<Currency> currencies = currencyService.getAllCurrencies();
        return ResponseEntity.ok(currencies);
    }
    @PostMapping
    public ResponseEntity<Currency> getCurrencyByCode(@RequestBody Currency currency) {
        Currency newCurrency = currencyService.addCurrency(currency);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCurrency);
    }
    @PutMapping("/{code}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable String code, @RequestBody Currency updatedCurrency) {
        Optional<Currency> updated = currencyService.updateCurrency(code, updatedCurrency);
        return updated.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{code}")
    public ResponseEntity<Object> deleteCurrency(@PathVariable String code) {
            currencyService.deleteCurrency(code);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Deleted"));
    }
}
