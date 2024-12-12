package com.publicissapient.cryptohistory.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Util {

    public static final String GET_HISTORY_URL = "https://api.coindesk.com/v1/bpi/historical/close.json";
    public static final String GET_CURRENCIES = "https://api.coindesk.com/v1/bpi/supported-currencies.json";
    public static final String GET_CONVERSION_RATES = "https://v6.exchangerate-api.com/v6/{apiKey}/latest/USD";
    public static final String HIGH_VALUE_TEXT = " (high)";
    public static final String LOW_VALUE_TEXT = " (low)";
    public static final String NO_DATA_ERROR = "No data found for specified dates!";
    public static final String DATE_ERROR = "Please validate your input dates.";
    public static final String INTERNAL_SERVER_ERROR = "Some error occured, Please try again later!";
    public static final String SUCCESS = "Success";
    public static final String USD_CURRENCY = "USD";

    public static LocalDate getLocalDateFromString(String dateString){
        return LocalDate.parse(dateString);
    }

    public static String formatDate(String inputDate) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = inputFormat.parse(inputDate);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        return outputFormat.format(date);
    }
}
