package com.tax.TaxCalculator;

public class Input {
    private String invoiceDate;
    private String invoiceTaxAmount;
    private String paymentCurrency;

    //getter
    public String getInvoiceDate() {
        return invoiceDate;
    }

    public String getInvoiceTaxAmount() {
        return invoiceTaxAmount;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }


    //setter
    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public void setInvoiceTaxAmount(String invoiceTaxAmount) {
        this.invoiceTaxAmount = invoiceTaxAmount;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }
}
