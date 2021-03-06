package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.CartApotekActivity;
import mobi.garden.bottomnavigationtest.Activity.InformasiObatAs;
import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Model.obat;

public class obat_adapter_as extends RecyclerView.Adapter<obat_adapter_as.obatViewHolder> {

    List<obat> obatlist;
    List<obat> cartlist;
    Context context;
    String CustomerID;
    static DecimalFormat df;

    UserLocalStore userLocalStore;
    public obat_adapter_as(Context c, List<obat> obatlist, List<obat> cartlist) {
        this.obatlist = obatlist;
        this.context = c;
        this.cartlist = cartlist;
    }

    public obat_adapter_as(Context c, List<obat> obatlist) {
        this.obatlist = obatlist;
        this.context = c;
    }


    @Override
    public obat_adapter_as.obatViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.cv_obat_as, viewGroup, false);

        return new obat_adapter_as.obatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(obat_adapter_as.obatViewHolder holder, int position) {

        final obat pr = obatlist.get(position);

        userLocalStore  = new UserLocalStore(context);
        User currUser = userLocalStore.getLoggedInUser();
        CustomerID = currUser.getUserID();

        df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);


        Picasso.with(context).load(pr.productPhoto).into(holder.iv_picture_obat_as);
        holder.tv_nama_obat_as.setText(pr.productName);
        holder.tv_qty_obat_as.setText(String.valueOf(pr.outletProductStockQty));
        holder.tv_price_obat_as.setText(String.valueOf(df.format(pr.outletProductPrice)));

        holder.iv_picture_obat_as.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,InformasiObatAs.class);
                i.putExtra(InformasiObatAs.EXTRA_OBAT, pr);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });

        for(int j=0;j<cartlist.size();j++){
            if(pr.productID.equals(cartlist.get(j).productID)){
                holder.btn_add_obat.setEnabled(false);
                holder.btn_add_obat.setBackgroundResource(R.drawable.add_button_set_enabled);
                break;
            }
        }

        holder.btn_add_obat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add(pr.productID,pr.productName, pr.outletProductPrice,1, Integer.parseInt(CustomerID));
                holder.btn_add_obat.setEnabled(false);
                holder.btn_add_obat.setBackgroundResource(R.drawable.add_button_set_enabled);
            }
        });

    }

    @Override
    public int getItemCount() {
        return obatlist.size();
    }

    public static class obatViewHolder extends RecyclerView.ViewHolder {

        int i=0;
        ImageView iv_picture_obat_as;
        LinearLayout ll_cv_obat_as;
        TextView tv_nama_obat_as, tv_qty_obat_as, tv_price_obat_as;
        Button btn_add_obat;

        public obatViewHolder(View v) {
            super(v);

            iv_picture_obat_as=(ImageView) v.findViewById(R.id.iv_picture_obat_as);
            ll_cv_obat_as = (LinearLayout) v.findViewById(R.id.ll_cv_obat_as);
            tv_nama_obat_as= (TextView) v.findViewById(R.id.tv_nama_obat_as);
            btn_add_obat=(Button)v.findViewById(R.id.btn_add_obat);
            tv_qty_obat_as = (TextView) v.findViewById(R.id.tv_qty_obat_as);
            tv_price_obat_as =(TextView) v.findViewById(R.id.tv_price_obat_as);
        }
    }


    public void add(String product_id, String product_name, int product_price, int qty, int CustomerID) {
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("ProductName", product_name);
            objDetail.put("Price", product_price);
            objDetail.put("Qty", qty);
            objDetail.put("CustomerID", CustomerID);
            objDetail.put("UpdatedBy",CustomerID);
            objDetail.put("ProductID", product_id);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/addCartCustomer.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {
                                CartApotekActivity.initiateBelowAdapter();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
