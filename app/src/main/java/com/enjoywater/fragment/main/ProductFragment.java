package com.enjoywater.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enjoywater.R;
import com.enjoywater.activity.MyApplication;
import com.enjoywater.adapter.HomeAdapter;
import com.enjoywater.entity.News;
import com.enjoywater.entity.Product;
import com.enjoywater.entity.UserLoginInfo;
import com.enjoywater.service.BaseResponse;
import com.enjoywater.service.MainService;
import com.enjoywater.utils.Constant;
import com.enjoywater.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment implements OnMapReadyCallback {
    private static ProductFragment instance;
    private Context mContext;
    private UserLoginInfo mUserLoginInfo;
    private MainService mainService;
    private Gson gson = new Gson();
    private ArrayList<Product> mProductsList;
    private GoogleMap mMap;

    public static ProductFragment getInstance() {
        if (instance == null) {
            instance = new ProductFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mUserLoginInfo = gson.fromJson(Utils.getStringNotNull(mContext, Constant.USER_LOGIN_INFO), UserLoginInfo.class);
        mainService = MyApplication.getInstance().getMainService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        ButterKnife.bind(this, view);
        initUI();
        getTopSale();
        return view;
    }

    private void initUI() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getTopSale() {
        Call<BaseResponse> getTopSale = mainService.getTopSale(mUserLoginInfo.getToken());
        getTopSale.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null && response.body().isSuccess() && response.body().getData() != null) {
                    mProductsList = new ArrayList<>();
                    try {
                        JSONArray arrayData = new JSONArray(gson.toJson(response.body().getData()));
                        for (int i = 0, z = arrayData.length(); i<z; i++) {
                            Object o = arrayData.get(i);
                            if (o instanceof JSONObject) {
                                Product product = gson.fromJson(o.toString(), Product.class);
                                if (product != null) {
                                    mProductsList.add(product);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setData() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(21.0053787, 105.811429);
        mMap.addMarker(new MarkerOptions().position(sydney).title("My Home"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
