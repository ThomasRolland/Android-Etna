package io.etna.intranet.Curl;

/**
 * Created by thomasrolland on 13/04/2017.
 */

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public enum NetworkService implements NetworkInterface {
    INSTANCE;

    private static final long CONNECT_TIMEOUT = 20000;   // 2 seconds
    private static final long READ_TIMEOUT = 20000;      // 2 seconds
    private static OkHttpClient okHttpClient = null;
    private static final String SEARCH_URL = "https://auth.etna-alternance.net/login";


    /**
     * Method to build and return an OkHttpClient so we can set/get
     * headers quickly and efficiently.
     * @return OkHttpClient
     */
    private OkHttpClient buildClient() {
        if (okHttpClient != null) return okHttpClient;

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);

        // Logging interceptor
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);



        // custom interceptor for adding header and NetworkMonitor sliding window
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // Add whatever we want to our request headers.
                RequestBody formBody = new FormBody.Builder()
                        .add("login", "rollan_t")
                        .add("password", "oO07Yvqu")
                        .build();

                Request request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Cookie", "authenticator=eyJpZGVudGl0eSI6ImV5SnBaQ0k2Tnprek5Dd2liRzluYVc0aU9pSmlaV1J0YVc1ZmFpSXNJbXh2WjJGeklqcG1ZV3h6WlN3aVozSnZkWEJ6SWpwYkluTjBkV1JsYm5RaVhTd2liRzluYVc1ZlpHRjBaU0k2SWpJd01UY3RNRFF0TVRrZ01UQTZNek02TVRVaWZRPT0iLCJzaWduYXR1cmUiOiJqQ1dFUnYyTFFGejBhSlwvMTBqMGNtcTZ3UXZcLzYwRkVEK1wvdXdzRlNiQ2wrRDRSUXpxUG9MaDJqQjlCcnJnWVZzSk5zMHVKckYrKzBYUkxCODJoZVF1V1pQaUtKd00xSVgyQ283a0dFUFpCVkJ6MlFOT2xGdWdON3B3cEpoQ1BjV0J1K3JaWGhZYkU5b1hFTkI5TVMwbjlNbzBSemxQV2hcL2VtNjlvNk9XR3ZyU1czQ3RYcW42QmtwYzVwV3dGcjY0NzlXeHl0dHpsZ1RLY21pZ3dBbXJKb2N1MXlwOWVFNFZBTUJkMUFxcHVIZHBOWlhqRnNiY3JHbnQ0VkVocmlUSkxucG9GVHBPWFZUeDBFcGRYZGM0d001aVhHZWFlYmYwMTA4V25vRVF1VGtlOFZqZXZva05OK2tyTFNxaVU2UzVWZDBOUEdHakdYVFc2UkppU0g5dUpaM3lpbGVXSU1NcVJ3MEE0aHFjRVhTMHRSdGNGbGxvT1RrdVkyM2JKTWordnJ3SWM1NVwvK0pxNzdmd3BURU9SbjI2aUJVQzc4UGM3V1FmYjQ5VUJjaCs0UUNzTTdBR0ozYk9lKzQ4S0dNcnZXamthN2dVcWpWNmFuU2VxclhKZVlWUTk1dVp2QUFmbVR0cTU3Rm0ydm1aVVZjaWdRWkNZaE05aXk1K0U4S05FZVBBYUdHNW5qdTRQRUZuXC92TXIrV293TklzMVgxdDBIQTd6TUlVUHB4WEIyaEpzSkFvd3hWdGtEZGlDNmZUd0k5NEVBeVlOajdMWHdSbkxSc05EenJjR0t3ckFENDJKbTlHNGhhZXF2N0crOVwvK2ZHRHJPZXJyQXVQb1Z2VHI1aEVPUmNxQ0t0TWRROWZRNW5aTkFEU25BYTRwSWhZNzM5dkFpQXE2dHFFdmM9In0")
                        .build();
                //.post(formBody)
                Response response;
                try {
                    response = chain.proceed(request);
                } catch (SocketTimeoutException | UnknownHostException e) {
                    e.printStackTrace();
                    throw new IOException(e);
                }
                return response;
            }
        });

        return  okHttpClientBuilder.build();
    }

    private Request.Builder buildRequest(URL url) {
        return new Request.Builder()
                .url(url);
    }

    private Request.Builder buildRequest(URL url, String credential) {
        return buildRequest(url).header("Authorization", credential);
    }

    private URL buildURL(Uri builtUrl) {
        if (builtUrl == null) return null;
        try {
            String urlStr = builtUrl.toString();
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private URL buildURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getData(Request request) {
        OkHttpClient client = buildClient();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public String getString(String endpoint, String username, String password) {
        Log.d("NetworkService", "getString by username and password from " + endpoint);
        String credentials = username + ":" + password;
        final String basicAuth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Request request = buildRequest(buildURL(endpoint), basicAuth).build();
        return getData(request);
    }

    @Override
    public String getString(String endpoint, String token) {
        Log.d("NetworkService", "getString by Bearer token from " + endpoint);
        String credentials = "Bearer " + token;
        Request request = buildRequest(buildURL(endpoint), credentials).build();
        return getData(request);
    }

    @Override
    public String search(String[] get, String[] query, String urle, String[] path) {
        Uri.Builder uri = Uri.parse(urle)
                .buildUpon();
        for (int i = 0; i < path.length ; i++) {
            uri.appendPath(path[i]);
        }
        for (int i = 0; i < query.length ; i++) {
           uri.appendQueryParameter(query[i], get[i]);
        }
        Uri uril  = uri.build();
        URL url = buildURL(uril);
        Log.d("NetworkService","built search url: " + url.toString());
        Request request = buildRequest(url).build();
        return getData(request);
    }

}