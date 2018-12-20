package com.sglei.basemodule.net.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.orhanobut.hawk.Hawk;
import com.sglei.basemodule.BookApplication;
import com.sglei.basemodule.BuildConfig;
import com.sglei.basemodule.net.https.certification.TrustAllCerts;
import com.sglei.basemodule.util.Logger;
import com.sglei.basemodule.util.NetUtils;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @creator sglei
 * @time 2018/12/19 14:14
 */
public class RetrofitApiFactory {
    public static final long CACHE_STALE_SEC = 60 * 60 * 24;//24小时
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    private static Retrofit innerRetrofit;
    private static Retrofit outerRetrofit;

    private RetrofitApiFactory() {
        throw new AssertionError();
    }

    public static String BASE_URL = BuildConfig.HostUrl;

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor sRewriteCacheControlInterceptor = chain -> {
        Request request = chain.request().newBuilder().addHeader("token", Hawk.get("")).build();
        if (!NetUtils.isNetworkAvailable()) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            Logger.e("no network");
        }
        Response originalResponse = chain.proceed(request);
        if (NetUtils.isNetworkAvailable()) {
            //获取返回header重的版本配置和token字段，并做相应的缓存
            String cacheControl = request.cacheControl().toString();
            Map<String, List<String>> stringListMap = originalResponse.headers().toMultimap();
            List<String> tokens = stringListMap.get("token");
            if (tokens != null && tokens.size() > 0) {
                Hawk.put("token", tokens.get(0));
            }

            return originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        } else {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                    .removeHeader("Pragma")
                    .build();
        }
    };

    private static final Interceptor rewriteResponseInterceptorOffline = chain -> {
        Request request = chain.request();
        if (!NetUtils.isNetworkAvailable()) {
            request = request.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached")
                    .build();
        }
        return chain.proceed(request);
    };

    private static final Interceptor inter = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            return null;
        }
    };


    private static final HttpLoggingInterceptor mResponseLogInterceptor = new HttpLoggingInterceptor(message -> {
        if (message.contains("http") && !message.contains("ms")) {
            Logger.d("\n请求地址：" + message);
        } else if (message.contains("sign")) {
            Logger.d("\n请求报文：" + message);
        } else if (message.contains("data")) {
            Logger.d("\n响应报文：" + message);
        }
    });


    public static <T> T create(final Class<T> service) {
        return innerRetrofit.create(service);
    }


    /**
     * 初始化网络通信服务
     */
    public static void init() {
        // 指定缓存路径,缓存大小100Mb
        Cache cache = new Cache(new File(BookApplication.getInstance().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        mResponseLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache)
                .retryOnConnectionFailure(true)
                .addInterceptor(rewriteResponseInterceptorOffline)
                .addInterceptor(mResponseLogInterceptor)
                .addNetworkInterceptor(sRewriteCacheControlInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier((s, sslSession) -> true)
                .build();


        innerRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        outerRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    /**
     * 用来解决https需要证书认证的问题
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }

}
