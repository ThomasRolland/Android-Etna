package io.etna.intranet.Curl;

/**
 * Created by thomasrolland on 13/04/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.etna.intranet.Storage.TinyDB;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.JavaNetCookieJar;
import okhttp3.logging.HttpLoggingInterceptor;

public enum NetworkService implements NetworkInterface {
    INSTANCE;

    private static final long CONNECT_TIMEOUT = 20000;   // 2 seconds
    private static final long READ_TIMEOUT = 20000;      // 2 seconds
    private static OkHttpClient okHttpClient = null;
    private static final String SEARCH_URL = "https://auth.etna-alternance.net/login";

    CookieJar cookieJar = new CookieJar() {
        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put("etna", cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            Log.d("cookie used", String.valueOf(cookieStore.get("etna")));
            Log.d("cookie url", url.host());
            List<Cookie> cookies = cookieStore.get("etna");
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    };

    /**
     * Method to build and return an OkHttpClient so we can set/get
     * headers quickly and efficiently.
     * @return OkHttpClient
     */
    OkHttpClient buildClient_login(final String login, final String password) {
        if (okHttpClient != null) return okHttpClient;
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // Add whatever we want to our request headers.
                RequestBody formBody = new FormBody.Builder()
                        .add("login", login)
                        .add("password", password)
                        .build();

                Request request = chain.request().newBuilder()
                        .post(formBody)
                        .addHeader("Accept", "application/json")
                        .build();
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
        // init okhttp 3 logger

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        return  okHttpClientBuilder
                .cookieJar(cookieJar)
                .build();
    }

    private Request.Builder buildRequest(URL url) {
        return new Request.Builder()
                .url(url);
    }

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
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        //.addHeader("Cookie", )
                        .build();
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

        return  okHttpClientBuilder
                .cookieJar(cookieJar)
                .build();
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

    private String getData_login(Request request,String login,String password) {
        OkHttpClient client = buildClient_login(login, password);
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap getData_image(Request request) {
        OkHttpClient client = buildClient();
        try {
            Response response = client.newCall(request).execute();
            return BitmapFactory.decodeStream(response.body().byteStream());
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


    public Bitmap search_image(String[] get, String[] query, String urle, String[] path) {
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
        return getData_image(request);
    }

    public String search_login(String login, String password, String urle) {
        Uri.Builder uri = Uri.parse(urle)
                .buildUpon();
        Uri uril  = uri.build();
        URL url = buildURL(uril);
        Log.d("NetworkService","built search url: " + url.toString());
        Request request = buildRequest(url).build();
        return getData_login(request, login, password);
    }

}