package ma.ensaj.edugame.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.TimeUnit;

import ma.ensaj.edugame.EduGameApp;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.1.149:8088";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            Context context = EduGameApp.getAppContext();

            // Interceptor to attach JWT token
            Interceptor jwtInterceptor = chain -> {
                SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                String token = prefs.getString("jwt_token", null);

                Request.Builder requestBuilder = chain.request().newBuilder();
                if (token != null) {
                    requestBuilder.addHeader("Authorization", "Bearer " + token);
                }

                return chain.proceed(requestBuilder.build());
            };

            // OkHttpClient with interceptor
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(jwtInterceptor)
                    .build();

            // Initialize Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
