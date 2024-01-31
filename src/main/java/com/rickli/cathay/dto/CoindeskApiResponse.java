package com.rickli.cathay.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class CoindeskApiResponse {

    private TimeInfo time;
    private String disclaimer;
    private String chartName;

    public TimeInfo getTime() {
        return time;
    }

    public void setTime(TimeInfo time) {
        this.time = time;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public Map<String, CurrencyInfo> getBpi() {
        return bpi;
    }

    public void setBpi(Map<String, CurrencyInfo> bpi) {
        this.bpi = bpi;
    }

    @JsonProperty("bpi")
    private Map<String, CurrencyInfo> bpi;

    public static class TimeInfo {
        private String updated;
        private String updatedISO;
        private String updateduk;

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getUpdatedISO() {
            return updatedISO;
        }

        public void setUpdatedISO(String updatedISO) {
            this.updatedISO = updatedISO;
        }

        public String getUpdateduk() {
            return updateduk;
        }

        public void setUpdateduk(String updateduk) {
            this.updateduk = updateduk;
        }
    }


    public static class CurrencyInfo {
        private String code;
        private String symbol;
        private String rate;
        private String description;
        private double rate_float;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getRate_float() {
            return rate_float;
        }

        public void setRate_float(double rate_float) {
            this.rate_float = rate_float;
        }

        public String getName(){
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
