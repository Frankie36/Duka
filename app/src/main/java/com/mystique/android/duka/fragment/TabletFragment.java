package com.mystique.android.duka.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mystique.android.duka.R;
import com.mystique.android.duka.Util.JSONParser;
import com.mystique.android.duka.adapter.ProductsAdapter;
import com.mystique.android.duka.model.Product;
import com.mystique.android.duka.model.ProductJson;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabletFragment extends Fragment {

    private View rootView;
    private RecyclerView rvProducts;
    private List<Product> productsList;

    public TabletFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_phone, container, false);
        rvProducts = rootView.findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.products_grid_columns)));
        loadProducts();
        rvProducts.setAdapter(new ProductsAdapter(getActivity(), productsList));
        return rootView;
    }

    private void loadProducts() {
        // Open and parse file
        try {
            ProductJson productJson = new Gson().fromJson(new JSONParser().loadJSONFromAsset(getActivity(), R.raw.phone_shop), ProductJson.class);
            productsList = productJson.getProducts();

       /*     JSONObject obj = new JSONObject(new JSONParser().loadJSONFromAsset(getActivity(), R.raw.phone_shop));
            JSONArray m_jArry = obj.getJSONArray("products");

            for (int i = 0; i < m_jArry.length(); i++) {
                // Parse the JSON
                JSONObject jsonproduct = m_jArry.getJSONObject(i);
                String id = jsonproduct.getString("original_id");
                String name = jsonproduct.getString("name");
                String color = jsonproduct.getString("color");
                int value = jsonproduct.getInt("value");
                String logo = jsonproduct.getString("logo");


                Product selectedProduct = new Product(id, name, logo, value);
                // Add the object to the list
                products.add(selectedProduct);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
