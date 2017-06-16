package com.pingan.library.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class Util {
    private static final Random random;

    public static String slurp(final InputStream stream) throws IOException {
        final char[] buf = new char[8192];
        final StringBuilder sb = new StringBuilder();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        while (true) {
            final int n = reader.read(buf);
            if (n < 0) {
                break;
            }
            sb.append(buf, 0, n);
        }
        return sb.toString();
    }

    public static String sanitizeUrl(final String urlString) {
        if (urlString == null) {
            return null;
        }
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            return null;
        }
        final StringBuffer sanitizedUrl = new StringBuffer();
        sanitizedUrl.append(url.getProtocol());
        sanitizedUrl.append("://");
        sanitizedUrl.append(url.getHost());
        if (url.getPort() != -1) {
            sanitizedUrl.append(":");
            sanitizedUrl.append(url.getPort());
        }
        sanitizedUrl.append(url.getPath());
        return sanitizedUrl.toString();
    }

    public static Random getRandom() {
        return Util.random;
    }

    static {
        random = new Random();
    }
}