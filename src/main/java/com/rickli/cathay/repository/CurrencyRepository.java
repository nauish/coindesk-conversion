package com.rickli.cathay.repository;

import com.rickli.cathay.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCode(String code);

}
