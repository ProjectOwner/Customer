package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.SearchResultApotek;
import mobi.garden.bottomnavigationtest.CONFIG;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class SearchResultApotekAdapter extends RecyclerView.Adapter<SearchResultApotekAdapter.SearchResultApotekAdapterViewHolder> {
    List<ModelPromo> modelPromo;
    List<ModelPromo> allproductlist;//ini 20
    List<ModelPromo> allproduct; // ini semua
    Context context;
    String tempurl;
    static DecimalFormat df;
    public static String add_url = "http://pharmanet.apodoc.id/customer/addCartCustomer.php";

    //login
    SessionManagement session;
    HashMap<String, String> login;
    public static String CustomerID,memberID, userName;

    public SearchResultApotekAdapter(Context context) {
        this.context = context;
    }

    public SearchResultApotekAdapter(List<ModelPromo> modelPromo, Context context) {
        this.modelPromo = modelPromo;
        this.context = context;
    }

    public void setProductList(List<ModelPromo> allProductList) {
        this.allproductlist = allProductList;
        notifyDataSetChanged();
    }
    public void setProductListFull(List<ModelPromo> allProduct) {
        this.allproduct = allProduct;
    }

    @NonNull
    @Override
    public SearchResultApotekAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cv_promo_selengkapnya, parent, false);
        return new SearchResultApotekAdapter.SearchResultApotekAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultApotekAdapterViewHolder holder, int position) {
        if(modelPromo != null) {
            final ModelPromo mp = modelPromo.get(position);


            session = new SessionManagement(context);
            login = session.getMemberDetails();
            userName = login.get(SessionManagement.USERNAME);
            memberID = login.get(SessionManagement.KEY_KODEMEMBER);


            df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setCurrencySymbol("Rp. ");
            dfs.setMonetaryDecimalSeparator('.');
            dfs.setGroupingSeparator('.');
            df.setDecimalFormatSymbols(dfs);
            df.setMaximumFractionDigits(0);


            holder.tvNamaProdukPromo.setText(mp.getPromoNameProduct());
            holder.tvHargaCoret.setText(String.valueOf(df.format(mp.getPriceProduct())));
//        holder.tvharga.setText("Rp. " + String.valueOf(mp.getProductPriceAfterDC()));
            holder.tvharga.setText(String.valueOf(df.format(mp.getProductPriceAfterDC())));

            if (mp.getPriceProduct() != mp.getProductPriceAfterDC()) {
                holder.tvHargaCoret.setPaintFlags(holder.tvHargaCoret.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (mp.getProductPriceAfterDC() == 0) {
                holder.tvHargaCoret.setText(" ");
                holder.tvharga.setText(String.valueOf(df.format(mp.getPriceProduct())));
            }

            tempurl = mp.getProductNameUrl();
            Log.d("onBindViewHolder: ", tempurl);
            if (tempurl.contains(" ")) {
                tempurl = tempurl.replace(" ", "%20");
            }
            Picasso.with(context).load(tempurl).into(holder.imgProduct, new Callback() {
                @Override
                public void onSuccess() {
                    Picasso.with(context).load(tempurl).into(holder.imgProduct);
                }

                @Override
                public void onError() {
                    holder.imgProduct.setImageResource(R.drawable.nopicture);
                    //Picasso.with(context).load("http://www.pharmanet.co.id/images/logo.png").into(holder.imgProduct);
                }
            });

            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mp.getProductPriceAfterDC() == 0) {
                        add(mp.ProductID, mp.getPriceProduct(), 1, memberID);
                    } else {
                        add(mp.ProductID, mp.getProductPriceAfterDC(), 1, memberID);
                    }
                    holder.btnAdd.setEnabled(false);
                    holder.btnAdd.setBackgroundResource(R.drawable.add_button_set_enabled);
//                Toast.makeText(context, ""+mp.ProductID, Toast.LENGTH_SHORT).show();

                    Intent data = new Intent();
                    String text = "test123123";
                    data.putExtra(CONFIG.PREV_PAGE, text);
                }
            });

            holder.ll_obat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Intent i = new Intent(context, DetailObatHome.class);
                 //   i.putExtra("ProductName", mp.getPromoNameProduct());
                 //   context.startActivity(i);
                }
            });
            for (int j = 0; j < SearchResultApotek.cartList.size(); j++) {
                if (mp.ProductID.equals(SearchResultApotek.cartList.get(j).productID)) {
                    holder.btnAdd.setEnabled(false);
                    holder.btnAdd.setBackgroundResource(R.drawable.add_button_set_enabled);
                    break;
                }
            }
//        for(int j = 0; j<PromoSelengkapnyaActivity.cartList.size(); j++){
//            if(mp.ProductID.equals(PromoSelengkapnyaActivity.cartList.get(j).productID)){
//                holder.btnAdd.setEnabled(false);
//                holder.btnAdd.setBackgroundResource(R.drawable.add_button_set_enabled);
//                Toast.makeText(context, "searchpromo", Toast.LENGTH_SHORT).show();
//                break;
//            }
//        }
        }
    }

    @Override
    public int getItemCount() {
//        if(modelPromo.isEmpty()) {
            return modelPromo.size();
//        }else{
//            return allproductlist.size();
//        }
    }

    public class SearchResultApotekAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaProdukPromo, tvHargaCoret, tvharga;
        ImageView imgProduct;
        LinearLayout ll_obat;
        Button btnAdd;

        public SearchResultApotekAdapterViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNamaProdukPromo = itemView.findViewById(R.id.tvNamaProdukPromo);
            tvHargaCoret = itemView.findViewById(R.id.tvHargaCoret);
            tvharga = itemView.findViewById(R.id.tvHarga);
            btnAdd = itemView.findViewById(R.id.btn_add_obat);
            ll_obat = itemView.findViewById(R.id.llproduk);
        }
    }
    public void addkeranjang(String productname, String outletID){
        String url = "http://pharmanet.apodoc.id/customer/AddProductToCart.php?ProductName="+productname+"&OutletID="+outletID;
        Log.d("addkeranjangasd: ",url);

        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(req);
    }
    public void add(String product_id, int product_price, int qty, String memberID) {
        Log.d("dsa", memberID);
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("ProductID", product_id);
            objDetail.put("outletProductPrice", product_price);
            objDetail.put("Qty", qty);
            objDetail.put("CustomerID", memberID);
            objDetail.put("UpdatedBy",memberID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Log.d("cartpromo", objAdd.toString());
//        Toast.makeText(context, "poipoi", Toast.LENGTH_SHORT).show();
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, add_url, objAdd,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(context, "asdqwe", Toast.LENGTH_SHORT).show();
                        try {
                            if (response.getString("status").equals("OK")) {
                                //CartApotekActivity.initiateBelowAdapter();
                                SearchResultApotek.show_cart(SearchResultApotek.urlbawahs,memberID);
                                SearchResultApotek.showView(SearchResultApotek.rvObatPromo,SearchResultApotek.urlPromo+SearchResultApotek.apotekk);
                                SearchResultApotek.showViewFav();
                                SearchResultApotek.showViewAll();


                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
//searchresultapotekadapter