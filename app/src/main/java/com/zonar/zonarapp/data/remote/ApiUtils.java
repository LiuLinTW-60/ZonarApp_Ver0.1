package com.zonar.zonarapp.data.remote;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://18.223.175.205:8000/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
