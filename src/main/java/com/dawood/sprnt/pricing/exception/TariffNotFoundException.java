package com.dawood.sprnt.pricing.exception;

public class TariffNotFoundException extends RuntimeException {
    public TariffNotFoundException(String message) {
        super(message);
    }

    public TariffNotFoundException(){
        super("No Tariff found");
    }
}
