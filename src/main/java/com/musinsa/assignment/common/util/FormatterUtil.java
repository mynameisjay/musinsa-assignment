package com.musinsa.assignment.common.util;

import java.text.NumberFormat;

public class FormatterUtil {

    public static String toPriceFormat(Integer price) {
        return NumberFormat.getInstance().format(price);
    }

}
