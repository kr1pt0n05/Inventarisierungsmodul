package com.hs_esslingen.insy.utils;

import com.hs_esslingen.insy.model.Inventory;

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


    public static String fullTextSearchString(Inventory inv) {
        String text =
                inv.getId()
                + (inv.getCostCenter() == null ? "" : inv.getCostCenter().getDescription())
                + (inv.getUser() == null ? "" : inv.getUser().getName())
                + (inv.getCompany() == null ? "" : inv.getCompany().getName())
                + (inv.getDescription() == null ? "" : inv.getDescription())
                + (inv.getSerialNumber() == null ? "" : inv.getSerialNumber())
                + (inv.getPrice() == null ? "" : inv.getPrice())
                + (inv.getLocation() == null ? "" : inv.getLocation())
                + (inv.getCreatedAt() == null ? "" : inv.getCreatedAt().getDayOfMonth() + "." + inv.getCreatedAt().getMonthValue() + "." + inv.getCreatedAt().getYear());
        return text.toLowerCase().replaceAll(" ", "");
    }

}
