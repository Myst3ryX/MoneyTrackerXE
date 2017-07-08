package com.myst3ry.moneytrackerxe;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myst3ry.moneytrackerxe.api.LSApi;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LSApp extends Application {

    private static final String KEY_AUTH_TOKEN = "auth-token";
    private static final String PREFERENSES_SESSION = "private_auth_session";

    private LSApi api;

    @Override
    public void onCreate() {
        super.onCreate();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.HEADERS : HttpLoggingInterceptor.Level.NONE))
                .addInterceptor(new AuthInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://android.loftschool.com/basic/v1/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        api = retrofit.create(LSApi.class);

    }

    public LSApi getApi() {
        return api;
    }

    public void editAuthToken(String authToken) {
        getSharedPreferences(PREFERENSES_SESSION, MODE_PRIVATE).edit().putString(KEY_AUTH_TOKEN, authToken).apply();
    }

    public String readAuthToken() {
        return getSharedPreferences(PREFERENSES_SESSION, MODE_PRIVATE).getString(KEY_AUTH_TOKEN, "");
    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(readAuthToken());
    }

    private class AuthInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request originalRequest = chain.request();
            HttpUrl url = originalRequest.url().newBuilder().addQueryParameter("auth-token", readAuthToken()).build();
            return chain.proceed(originalRequest.newBuilder().url(url).build());
        }
    }
}
