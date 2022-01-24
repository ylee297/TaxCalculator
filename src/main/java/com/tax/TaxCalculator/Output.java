package com.tax.TaxCalculator;

import java.math.BigDecimal;

public class Output {

    String preTaxAmount;
    String taxAmount;
    String grandTotal;
    BigDecimal exchangeRate;

    //getter
    public String getPreTaxAmount() {
        return preTaxAmount;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public String getGroudTotal() {
        return grandTotal;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    // setter
    public void setPreTaxAmount(String preTaxAmount) {
        this.preTaxAmount = preTaxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public void setGroudTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
