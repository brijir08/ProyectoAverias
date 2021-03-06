package com.brijir.averias.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyServiceManager {
    private static FaultService serviceFault;

    public static FaultService getServiceFault() {
        if(serviceFault == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://fn3arhnwsg.execute-api.us-west-2.amazonaws.com/produccion/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            serviceFault = retrofit.create(FaultService.class);
        }
        return serviceFault;
    }
}
