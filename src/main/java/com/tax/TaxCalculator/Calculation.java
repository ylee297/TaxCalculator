package com.tax.TaxCalculator;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@RestController
public class Calculation {

    final BigDecimal TAX_USD = new BigDecimal(0.1);
    final BigDecimal TAX_EUR = new BigDecimal(0.09);
    final BigDecimal TAX_CAD = new BigDecimal(0.11);
    final String fixerUrl = "http://data.fixer.io/api/";
    final String fixerAccessKey = "15899b525643159263b22055d855d71f";

    @RequestMapping(method = RequestMethod.POST, path="/calculation")
    public Output calculation(@RequestBody Input input) {

        // Refining date format to match date format of Fixer.io api call
        String originalDateString = input.getInvoiceDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        LocalDate localDate = LocalDate.parse(originalDateString, formatter);
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        String formattedDateString = localDate.getYear() + "-" + localDate.format(monthFormatter) + "-" + localDate.getDayOfMonth();

        // Obtaining exchange rates of the invoice date
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(fixerUrl + formattedDateString + "?access_key=" + fixerAccessKey + "&symbols=" + input.getPaymentCurrency(), String.class);
        JSONParser parser = new JSONParser();

        // Declaring output data. Used BigDecimal instead of float or doubles as floats and doubles cannot accurately represent the base 10 multiples
        BigDecimal beforeConvert = new BigDecimal(input.getInvoiceTaxAmount());
        BigDecimal exchangeRate;
        BigDecimal preTaxAmount;
        BigDecimal taxAmount;
        BigDecimal grandTotal;
        Output output = new Output();


        if(!input.getPaymentCurrency().equals("EUR")) {
            // when api call is needed
            try {
                JSONObject json = (JSONObject) parser.parse(result);
                JSONObject nestedObject = (JSONObject) json.get("rates");

                // getting a data in a nested json objet
                exchangeRate = (new BigDecimal((Double) nestedObject.get(input.getPaymentCurrency()))).setScale(6, RoundingMode.HALF_UP);

                preTaxAmount = beforeConvert.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);

                if (input.getPaymentCurrency().equals("USD")) {
                        taxAmount = preTaxAmount.multiply(TAX_USD);
                } else {
                    taxAmount = preTaxAmount.multiply(TAX_CAD);
                }


                taxAmount = taxAmount.setScale(2, RoundingMode.HALF_UP);
                grandTotal = preTaxAmount.add(taxAmount);


                output.setExchangeRate(exchangeRate);
                output.setPreTaxAmount(preTaxAmount.toString());
                output.setTaxAmount(taxAmount.toString());
                output.setGroudTotal(grandTotal.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            // when api call is not needed
            exchangeRate = new BigDecimal(1.0);
            preTaxAmount = new BigDecimal(input.getInvoiceTaxAmount());
            taxAmount = preTaxAmount.multiply(TAX_EUR).setScale(2, RoundingMode.HALF_UP);
            grandTotal = preTaxAmount.add(taxAmount);

            output.setExchangeRate(exchangeRate);
            output.setPreTaxAmount(preTaxAmount.toString());
            output.setTaxAmount(taxAmount.toString());
            output.setGroudTotal(grandTotal.toString());
        }


        return output;
    }
}
