package com.example.sananismayilov.myprojectsale.İntentAcivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sananismayilov.myprojectsale.Adapters.AdapterToOrders;
import com.example.sananismayilov.myprojectsale.Adapters.ConteynerToOrders;
import com.example.sananismayilov.myprojectsale.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserOrder extends AppCompatActivity {
RecyclerView recyclerView;
ArrayList<ConteynerToOrders> ordersArrayList;
SharedPreferences preferences;
    AdapterToOrders adapterToOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        recyclerView = findViewById(R.id.recylerviewtoorders);

        preferences = this.getSharedPreferences("com.example.sananismayilov.myprojectsale.İntentAcivity",MODE_PRIVATE);
        getOrders();
    }
    public void getOrders(){
        ordersArrayList = new ArrayList<>();

        String token = preferences.getString("user-token","");

            String url = "https://senan2.000webhostapp.com/SaleProject/ProductSaleProject/getselectorder.php";
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("Products");
                        for (int  i = 0;i<array.length();i++){
                            JSONObject object1 = array.getJSONObject(i);
                            String productname= object1.getString("product_name");
                            String productmodel = object1.getString("product_model");
                            String status = object1.getString("status");
                            ConteynerToOrders conteyner = new ConteynerToOrders(productname,productmodel,status);
                            ordersArrayList.add(conteyner);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    adapterToOrders = new AdapterToOrders(ordersArrayList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                    recyclerView.setAdapter(adapterToOrders);

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UserOrder.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> stringMap = new HashMap<>();
                    stringMap.put("token",token);
                    return stringMap;
                }
            };
            Volley.newRequestQueue(this).add(request);

    }
}