package br.com.valdir.desafiolistafrutas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/* {
          "page":1,
          "results": [],
          "total_results": 19640,
          "total_pages": 982
} */

public class JsonUtil {

    public static List<ItemFruta> fromJsonToList(String json) {
        List<ItemFruta> list = new ArrayList<>();
        try {

            JSONArray results = new JSONArray(json);

            for (int i = 0; i < results.length(); i++) {
                JSONObject frutaObject = results.getJSONObject(i);
                ItemFruta itemFruta = new ItemFruta(i+1, frutaObject);
                list.add(itemFruta);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<ItemFruta> fromJsonArrayToList(JSONArray jsonArray) {
        List<ItemFruta> list = new ArrayList<>();
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject frutaObject = jsonArray.getJSONObject(i);
                ItemFruta itemFruta = new ItemFruta(i+1, frutaObject);
                list.add(itemFruta);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
