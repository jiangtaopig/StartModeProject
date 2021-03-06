package com.zjt.startmodepro;

import java.util.Formatter;
import java.util.IllegalFormatException;
import java.util.Locale;

public class StringFormatter {
    private static ThreadLocal<StringBuilder> sLocalFormatBuilder = new ThreadLocal<StringBuilder>(){
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder();
        }
    };
    private static ThreadLocal<Formatter> sLocalFormatter = new ThreadLocal<Formatter>(){
        @Override
        protected Formatter initialValue() {
            return new Formatter(sLocalFormatBuilder.get(), Locale.getDefault());
        }
    };

    /**
     * @see Formatter#format(String, Object...)
     * @throws IllegalFormatException
     */
    public static String format(String format, Object... args) {
        return format(Locale.getDefault(), format, args);
    }

    /**
     * @see Formatter#format(Locale, String, Object...)
     * @throws IllegalFormatException
     */
    public synchronized static String format(Locale l, String format, Object... args) {
        for (;;) {
            sLocalFormatBuilder.get().setLength(0);
            return sLocalFormatter.get().format(l, format, args).toString();
        }
    }
}
