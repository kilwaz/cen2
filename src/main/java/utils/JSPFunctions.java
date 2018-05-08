package utils;

import requests.annotations.RequestName;

public class JSPFunctions {
    public static String resolveURL(String classReference) {
        String url = "https://kilwaz.me/";

        try {
            Class<?> classToResolve = Class.forName("requests." + classReference);

            RequestName requestNameAnnotation = classToResolve.getAnnotation(RequestName.class);
            url += requestNameAnnotation.value();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return url;
    }
}
