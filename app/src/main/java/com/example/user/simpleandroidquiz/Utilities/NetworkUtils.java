package com.example.user.simpleandroidquiz.Utilities;

import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by user on 16/10/2017.
 */

public class NetworkUtils {

    final static String FIREBASE_DATABASE_URL = "https://quiz-questions-79773.firebaseio.com/QuestionsAnswers";
    final static String FIREBASE_DATABASE_REVISION = "https://quiz-questions-79773.firebaseio.com/DatabaseRevision";

    final static String PARAM_QUERY = ".json";
    final static String PARAM_PRINT = "print";

    final static String print = "pretty";

    /* Builds URL used to query Firebase questions */
    public static URL buildQuestionsUrl() {
        Uri builtUri = Uri.parse(FIREBASE_DATABASE_URL).buildUpon()
                .appendPath(PARAM_QUERY)
                .appendQueryParameter(PARAM_PRINT, print)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /* Builds URL to read the revision of Firebase database */
    public static URL buildRevisionUrl() {
        Uri builtUri = Uri.parse(FIREBASE_DATABASE_REVISION).buildUpon()
                .appendPath(PARAM_QUERY)
                .appendQueryParameter(PARAM_PRINT, print)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /* This method returns the entire result from the HTTP response. */
    @Nullable
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
