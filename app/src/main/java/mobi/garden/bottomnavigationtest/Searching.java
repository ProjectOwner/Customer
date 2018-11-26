package mobi.garden.bottomnavigationtest;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.PromoActivity;
import mobi.garden.bottomnavigationtest.Adapter.PromoAdapter;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;

public class Searching {
    String input,url;
    Context context;
    RecyclerView recyclerView;
    PromoAdapter promoAdapter;
    List<ModelPromo> PromoList = new ArrayList<>();
    RequestQueue queue;
    String promoname, producturl;
    int priceproduk,priceprodukafterdc;

    public Searching(Context context,String input,  RecyclerView recyclerView) {
        this.input = input;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public void search(){
        PromoList.clear();
        queue = Volley.newRequestQueue(context);
//        LinearLayoutManager llm  = new LinearLayoutManager(context);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(llm);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        showallpromo();
    }

    public void showallpromo(){
        url="http://pharmanet.apodoc.id/customer/ProductPromoAll.php?input="+input;
        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    //Toast.makeText(DetailKategori.this, "aaar", Toast.LENGTH_SHORT).show();
                    result = response.getJSONArray("result");
                    PromoList.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int i=0; i< result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
//                        ModelPromo mp = new ModelPromo();
//                        mp.setPromoNameProduct(object.getString("ProductName"));
//                        mp.setProductNameUrl(object.getString("ProductImage"));
//                        mp.setPriceProduct(object.getInt("ProductPrice"));
                        promoname = object.getString("ProductName");
                        producturl = object.getString("ProductImage");
                        priceproduk = object.getInt("ProductPrice");
                        priceprodukafterdc = object.getInt("ProductPriceAfterDiscount");
                        PromoList.add(new ModelPromo(promoname,producturl,priceproduk,priceprodukafterdc));
                        //Toast.makeText(context, "pjg:"+result.length(), Toast.LENGTH_SHORT).show();
                        Log.d("rwar", object.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //tvNamaObat.setText(tempNamekategoriname);
                }
                promoAdapter = new PromoAdapter(PromoList,context);
                recyclerView.setAdapter(promoAdapter);
                //sizeproduct+=20;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sedang Gangguan", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(req);
    }
}