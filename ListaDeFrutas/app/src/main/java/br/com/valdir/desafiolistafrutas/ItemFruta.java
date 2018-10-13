package br.com.valdir.desafiolistafrutas;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Random;

/* {
      "name": "Apple",
      "image": "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/Red_Apple.jpg/265px-Red_Apple.jpg",
      "price": 35
    } */

public class ItemFruta implements Serializable {

    private long id;
    private String name;
    private String image;
    private Double price;

    public ItemFruta(long id, String name, String image, Double price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public ItemFruta(JSONObject jsonObject) throws JSONException {
        Random r = new Random();

        // TODO Procurar um gerador de ID mais confiável
        this.id = r.nextInt(10000+1);

        this.name = jsonObject.getString("name");
        this.image = jsonObject.getString("image");
        this.price = jsonObject.getDouble("price");
    }

    public ItemFruta(int seedId, JSONObject jsonObject) throws JSONException {

        this.id = seedId;

        this.name = jsonObject.getString("name");
        this.image = jsonObject.getString("image");
        this.price = jsonObject.getDouble("price");
    }

    private String buildPath(String width, String path) {
        StringBuilder builder = new StringBuilder();
        builder.append("http://")
               .append(width)
               .append(path);

        return builder.toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
