package in.gohelper.rest;

import in.gohelper.urls.Urls;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {

//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Interceptor.Chain chain) throws IOException {
//                Request original = chain.request();
//
//                Request request = original.newBuilder()
//                        .header("User-Agent", "Your-App-Name")
//                        .header("Accept", "application/vnd.yourapi.v1.full+json")
//                        .method(original.method(), original.body())
//                        .build();
//
//                return chain.proceed(request);
//            }
//        });

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Urls.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
