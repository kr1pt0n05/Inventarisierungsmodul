package com.hs_esslingen.insy.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class StringParser {


    /*
     * Safely parses a string like "8.743,58" (European number format) into a BigDecimal
     *
     * */
    public static BigDecimal parseString(String input) throws ParseException {
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        Number number = nf.parse(input);

        return new BigDecimal(number.toString());
    }

}
