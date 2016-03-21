/*
 *
 */
package uk.ac.ncl.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Exchanger;

/**
 * The Class DateParser.
 * <p>
 * This is a utility class, with methods to parse strings into dates according
 * to a specific format, thus avoiding rewriting the same code over and over
 * again
 */
public class DateParser {

    private static DateFormat df = DateFormat.getInstance(); // new
    // SimpleDateFormat("dd/MM/yyyy HH:mm:ss");//DateFormat.getInstance();
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private static SimpleDateFormat dftime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private final static long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    /**
     * Parses the date.
     *
     * @param d the d
     * @return the date
     */
    public static Date parse(String d) {
        try {
            return df.parse(d);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "String cannot be parsed as a date");
        }
    }

    public static Date simpleParse(String d) {
        try {
            return sdf.parse(d);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "String cannot be parsed as a date");
        }
    }

    public static Date dateTimeParse(String d){
        try {
            return dftime.parse(d);
        } catch (Exception e){
            throw new IllegalArgumentException("String cannot be parsed as a date");
        }
    }

    /**
     * Format. dd/MM/yyyy HH:mm:ss
     *
     * @param d the date
     * @return the string
     */
    public static String format(Date d) {
        return df.format(d);
    }

    public static String simpleFormat(Date d) {
        return sdf.format(d);
    }

    public static int calculate(Date d) {
        return (int) ((new Date().getTime() - d.getTime()) / DAY_IN_MILLIS);
    }
}
