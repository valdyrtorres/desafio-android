package br.com.valdir.desafiolistafrutas.api;

/**
 * Created by valdir on 30/01/18.
 */

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIServiceGet {

    @GET("/muxidev/desafio-android/master/fruits.json")
    Call<ResponseBody> getListaFrutas();
}