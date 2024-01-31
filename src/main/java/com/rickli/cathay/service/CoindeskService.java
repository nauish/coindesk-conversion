package com.rickli.cathay.service;

import com.rickli.cathay.dto.CoindeskApiResponse;

public interface CoindeskService {

    CoindeskApiResponse consumeCoindeskAPI();

    CoindeskApiResponse convertCoindeskApiData();
}