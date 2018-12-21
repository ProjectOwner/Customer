package mobi.garden.bottomnavigationtest.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mobi.garden.bottomnavigationtest.Adapter.PromoAdapter;
import mobi.garden.bottomnavigationtest.Adapter.PromoSelengkapnyaAdapter;
import mobi.garden.bottomnavigationtest.Adapter.cart_adapter;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class PromoSelengkapnyaActivity extends AppCompatActivity {
    RecyclerView rvSelengkapnya,rvObatFavorite;
    String apotekk,geturl;
    String urlPromo="";
    String urlFavorite="";
    int diskon;

    PromoSelengkapnyaAdapter promoAdapter;
    PromoSelengkapnyaAdapter favAdapter;
    PromoAdapter FavAdapter;
    int total_rating,outletProductPrice;

    List<ModelPromo> PromoList = new ArrayList<>();
    List<ModelPromo> FavList = new ArrayList<>();

    //login
    SessionManagement session;
    HashMap<String, String> login;
    public static String CustomerID,memberID, userName;

    private int dotscount;
    private ImageView[] dots;
    public static Context context;
    public static DecimalFormat df;
    private static RecyclerView recyclerViewCartList;
    private static cart_adapter adapterRvBelow;
    static RecyclerView rvProdukAll;
    private static TextView tvApotekName,tvTotalPrice;
    private static NotificationBadge mBadge;
    private static int totalPrice = 0;
    private static int count = 0;

    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_selengkapnya);

        rvSelengkapnya = findViewById(R.id.rvActivitySelengkapnya);
        rvSelengkapnya.setHasFixedSize(true);
        LinearLayoutManager llFavorite = new LinearLayoutManager(this);
        llFavorite.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvSelengkapnya.setLayoutManager(llFavorite);

        Intent i = getIntent();
        geturl = i.getStringExtra("link");
        Log.d("URL", geturl);//

        showViewPromo(rvSelengkapnya,geturl);
        showViewFav(rvSelengkapnya, geturl);

        session = new SessionManagement(getApplicationContext());
        login = session.getMemberDetails();
        userName= login.get(SessionManagement.USERNAME);
        memberID = login.get(SessionManagement.KEY_KODEMEMBER);
        context = getApplicationContext();
    }

    public void showViewPromo(final RecyclerView cardlist, String url) {
        JsonObjectRequest rec1= new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("pertama", response.toString());
                JSONArray Obats = null;
                try {
                    Obats = response.getJSONArray("result");
                    PromoList.clear();
                    Toast.makeText(PromoSelengkapnyaActivity.this, "sss"+Obats.length(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < Obats.length(); j++) {
                    try {
                        cardlist.setVisibility(View.VISIBLE);
                        JSONObject obj = Obats.getJSONObject(j);
                        PromoList.add(new ModelPromo(obj.getString("ProductID")
                                ,obj.getString("ProductName")
                                ,obj.getString("ProductImage")
                                ,obj.getInt("OutletID"),
                                obj.getInt("OutletProductPrice"),
                                obj.getInt("ProductPriceAfterDiscount")));
                        Log.d("masuk", obj.toString());
                        Toast.makeText(PromoSelengkapnyaActivity.this, "woiiii", Toast.LENGTH_SHORT).show();
                        Toast.makeText(PromoSelengkapnyaActivity.this, ""+obj.getString("productName"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                promoAdapter = new PromoSelengkapnyaAdapter(PromoList,PromoSelengkapnyaActivity.this);
                cardlist.setAdapter(promoAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PromoSelengkapnyaActivity.this, "error loading obatttt", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue req = Volley.newRequestQueue(this);
        req.add(rec1);
    }

    public void showViewFav(final RecyclerView cardlist, String url) {
        JsonObjectRequest rec1= new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("pertamaa", response.toString());
                JSONArray Obats = null;
                try {
                    Obats = response.getJSONArray("result");
                    FavList.clear();
                    Toast.makeText(PromoSelengkapnyaActivity.this, "sss"+Obats.length(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < Obats.length(); j++) {
                    try {
                        cardlist.setVisibility(View.VISIBLE);
                        JSONObject obj = Obats.getJSONObject(j);
                        if(obj.getString("ProductPriceAfterDiscount").equals("null")){
                            diskon =  0; }else{ diskon =  obj.getInt("ProductPriceAfterDiscount"); }
                        FavList.add(new ModelPromo(obj.getString("ProductID")
                                ,obj.getString("ProductName")
                                ,obj.getString("ProductImage")
                                ,obj.getInt("OutletID"),
                                obj.getInt("OutletProductPrice"),
                                diskon));
//                        FavList.add(new ModelPromo(obj.getString("ProductID")
//                                ,obj.getString("ProductName")
//                                ,obj.getString("ProductImage")
//                                ,obj.getInt("OutletID"),
//                                obj.getInt("OutletProductPrice"),
//                                obj.getInt("ProductPriceAfterDiscount")));
                        Log.d("masuk", obj.toString());
                        Toast.makeText(PromoSelengkapnyaActivity.this, "woiiii", Toast.LENGTH_SHORT).show();
                        Toast.makeText(PromoSelengkapnyaActivity.this, ""+obj.getString("productName"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                favAdapter = new PromoSelengkapnyaAdapter(FavList,PromoSelengkapnyaActivity.this);
                cardlist.setAdapter(favAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PromoSelengkapnyaActivity.this, "error loading obatttt", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue req = Volley.newRequestQueue(this);
        req.add(rec1);
    }

//    public static void show_cart(String urlbawahs, String memberID) {
//        Log.d("testurl", urlbawahs+ memberID);
//        JsonObjectRequest rec = new JsonObjectRequest(urlbawahs + memberID, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                JSONArray products = null;
//                try {
//                    products = response.getJSONArray("result");
//                    cartList.clear();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                count=0;
//                totalPrice=0;
//                for (int i = 0; i < products.length(); i++) {
////                    Toast.makeText(CartApotekActivity.context, "masuk sini", Toast.LENGTH_SHORT).show();
//                    try {
//                        recyclerViewCartList.setVisibility(View.VISIBLE);
//                        JSONObject obj = products.getJSONObject(i);
//                        cartList.add(new obat(
//                                obj.getString("ProductName"),
//                                obj.getString("ProductID"),
//                                obj.getInt("CartProductQty"),
//                                obj.getInt("OutletProductStockQty"),
//                                obj.getInt("CartProductPrice"),
//                                obj.getInt("CartProductPriceAfterDiscount")));
//
//                        temp = obj.getString("ProductName");
////                        Toast.makeText(context, ""+obj.getString("ProductName"), Toast.LENGTH_SHORT).show();
//                        totalPrice += obj.getInt("CartProductQty")*obj.getInt("CartProductPrice");
//                        count += obj.getInt("CartProductQty");
//                        Log.d("rwarqwe", obj.toString());
//                    } catch (JSONException e1) {
//                        e1.printStackTrace();
//                        Toast.makeText(context, e1.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//                tvTotalPrice.setText(df.format(totalPrice)+"");
//                mBadge.setNumber(cartList.size());
//                adapterRvBelow = new cart_adapter(context,cartList);
//                adapterRvBelow.setCartList(cartList);
//                recyclerViewCartList.setAdapter(adapterRvBelow);
//
////                tvTotalPrice.setText(df.format(totalPrice)+"");
////                mBadge.setNumber(count);
//////                Toast.makeText(context, cartList.size()+"sudah", Toast.LENGTH_SHORT).show();
////                adapterRvBelow = new cart_adapter(context,cartList);
////                recyclerViewCartList.setAdapter(adapterRvBelow);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //           Toast.makeText(Main2Activity.this, "error", Toast.LENGTH_SHORT).show();
//                Log.d("ini errors", "onErrorResponse: ");
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(rec);
//    }

    private void initBottomSheet() {
        //BOTTOM SHEET STATE
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    public static void refresh_cart(List<obat>cartList){
        count=0;
        totalPrice=0;

        for (int i = 0; i < cartList.size(); i++) {
            totalPrice+= cartList.get(i).cartProductPrice*cartList.get(i).cartProductQty;
            count += cartList.get(i).cartProductQty;
        }
        tvTotalPrice.setText(df.format(totalPrice)+"");
        mBadge.setNumber(count);

        adapterRvBelow = new cart_adapter(context,cartList);
        adapterRvBelow.setCartList(cartList);
        recyclerViewCartList.setAdapter(adapterRvBelow);
    }

    public static void refresh_total_cart(List<obat> cartList){
        count=0;
        totalPrice=0;
        for (int i = 0; i < cartList.size(); i++) {
            totalPrice+= cartList.get(i).cartProductPrice*cartList.get(i).cartProductQty;
            count += cartList.get(i).cartProductQty;
        }
        tvTotalPrice.setText(df.format(totalPrice)+"");
        mBadge.setNumber(count);
    }



    protected void onResume() {
        super.onResume();
        if(session.getUserLoggedIn()){
//            not_empty.setVisibility(not_empty.VISIBLE);
            //show_cart(urlbawah, memberID);
//            show_cart(urlbawah,Integer.parseInt(CustomerID), Outlet_ID);
        }



    }
}

