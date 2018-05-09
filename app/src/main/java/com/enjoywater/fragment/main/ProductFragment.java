package com.enjoywater.fragment.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.activity.MyApplication;
import com.enjoywater.adapter.ProductAdapter;
import com.enjoywater.entity.Product;
import com.enjoywater.entity.UserInfo;
import com.enjoywater.service.BaseResponse;
import com.enjoywater.service.MainService;
import com.enjoywater.utils.Constant;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.DialogConfirmInfo;
import com.enjoywater.view.DialogConfirmOrder;
import com.enjoywater.view.DialogConfirmProduct;
import com.enjoywater.view.TvSegoeuiRegular;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {
    private static final int REQUEST_PERMISSION_LOCATION = 111;
    private static ProductFragment instance;
    @BindView(R.id.rv_list_product)
    RecyclerView rvListProduct;
    @BindView(R.id.tv_address)
    TvSegoeuiRegular tvAddress;
    @BindView(R.id.layout_address)
    LinearLayout layoutAddress;
    @BindView(R.id.btn_order)
    ImageView btnOrder;
    private Context mContext;
    private UserInfo mUserInfo, mOrderInfo;
    private MainService mainService;
    private Gson gson = new Gson();
    private ArrayList<Product> mProductsList;
    private ArrayList<Product> mConfirmedProductsList;
    private GoogleMap mMap;
    private LinearLayoutManager mLayoutManager;
    private ProductAdapter mProductAdapter;
    private LocationManager mLocationManager;
    private Location mLocation;
    private LatLng defaultLatLng = new LatLng(21.0279211, 105.831051);
    private String mAddressLine;
    private boolean waitingGPS = false;

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
        mUserInfo = gson.fromJson(Utils.getStringNotNull(mContext, Constant.USER_LOGIN_INFO), UserInfo.class);
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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rvListProduct.setLayoutManager(mLayoutManager);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> selectedProducts = new ArrayList<>();
                for (Product product : mProductsList) {
                    if (product.isSelected()) selectedProducts.add(product);
                }
                if (selectedProducts.isEmpty()) {
                    Toast.makeText(mContext, "Bạn chưa chọn sản phẩm nào.", Toast.LENGTH_SHORT).show();
                } else {
                    confirmProduct(selectedProducts);
                }
            }
        });
    }

    private void getTopSale() {
        Call<BaseResponse> getTopSale = mainService.getTopSale(mUserInfo.getToken());
        getTopSale.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null && response.body().isSuccess() && response.body().getData() != null) {
                    mProductsList = new ArrayList<>();
                    try {
                        JSONArray arrayData = new JSONArray(gson.toJson(response.body().getData()));
                        for (int i = 0, z = arrayData.length(); i < z; i++) {
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
                    setData();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setData() {
        mProductAdapter = new ProductAdapter(mContext, mProductsList, itemClickListener);
        rvListProduct.setAdapter(mProductAdapter);
    }

    public interface ItemClickListener {
        void productSelect(int position, boolean isSelect);
    }

    ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void productSelect(int position, boolean isSelect) {
            mProductsList.get(position).setSelected(isSelect);
        }
    };

    private void confirmProduct(ArrayList<Product> selectedProducts) {
        @SuppressLint("HandlerLeak") Handler confirmProductHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == DialogConfirmProduct.ACTION_SEND) {
                    mConfirmedProductsList = (ArrayList<Product>) msg.obj;
                    confirmInfo();
                }
            }
        };
        new DialogConfirmProduct(mContext, confirmProductHandler, selectedProducts);
    }

    private void confirmInfo() {
        @SuppressLint("HandlerLeak") Handler confirmInfoHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == DialogConfirmInfo.ACTION_SEND) {
                    mOrderInfo = (UserInfo) msg.obj;
                    mOrderInfo.setLatitude(mLocation.getLatitude());
                    mOrderInfo.setLongitude(mLocation.getLongitude());
                    mOrderInfo.setAddress(mAddressLine);
                    confirmOrder();
                } else if (msg.what == DialogConfirmInfo.ACTION_ClOSE) {
                    confirmProduct(mConfirmedProductsList);
                }
            }
        };
        new DialogConfirmInfo(mContext, confirmInfoHandler);
    }

    private void confirmOrder() {
        @SuppressLint("HandlerLeak") Handler confirmOrderHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == DialogConfirmOrder.ACTION_SEND) {

                } else if (msg.what == DialogConfirmInfo.ACTION_ClOSE) {
                    confirmInfo();
                }
            }
        };
        new DialogConfirmOrder(mContext, confirmOrderHandler, mConfirmedProductsList, mOrderInfo);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(((Activity) mContext).findViewById(android.R.id.content), "Vui lòng cấp quyền cho App", Snackbar.LENGTH_INDEFINITE).setAction("Cho Phép",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            }
        } else {
            setupMap();
        }
    }

    @SuppressLint("MissingPermission")
    private void setupMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setMinZoomPreference(12.0f);
        mMap.setMaxZoomPreference(17.0f);
        UiSettings uiSettings = mMap.getUiSettings();
        //uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setMyLocationButtonEnabled(true);
        checkGPS();
    }

    @SuppressLint("MissingPermission")
    private void checkGPS() {
        mLocation = null;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager != null) {
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.e("mLocationManager", "GPS_PROVIDER ON");
                mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        mLocation = location;
                        Log.e("GPS_PROVIDER", "onLocationChanged: " + mLocation.getLatitude() + ", " + mLocation.getLongitude());
                        LatLng currentLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                        showLocation(currentLatLng, true);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Log.e("GPS_PROVIDER", "onStatusChanged");
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Log.e("GPS_PROVIDER", "onProviderEnabled");
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Log.e("GPS_PROVIDER", "onProviderDisabled");
                    }
                }, null);
            }
            if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Log.e("mLocationManager", "NETWORK_PROVIDER ON");
                mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if (mLocation == null) {
                            mLocation = location;
                            Log.e("NETWORK_PROVIDER", "onLocationChanged: " + mLocation.getLatitude() + ", " + mLocation.getLongitude());
                            LatLng currentLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                            showLocation(currentLatLng, true);
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Log.e("NETWORK_PROVIDER", "onStatusChanged");
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Log.e("NETWORK_PROVIDER", "onProviderEnabled");
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Log.e("NETWORK_PROVIDER", "onProviderDisabled");
                    }
                }, null);
            }
            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showLocation(defaultLatLng, false);
                //
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Thông báo");
                alertDialog.setMessage("Không thể lấy vị trí hiện tại của bạn");
                alertDialog.setPositiveButton("Cài đặt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        waitingGPS = true;
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        } else {
            showLocation(defaultLatLng, false);
            //
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Thông báo");
            alertDialog.setMessage("Không thể lấy vị trí hiện tại của bạn");
            alertDialog.setPositiveButton("Cài đặt", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    waitingGPS = true;
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }

    private void showLocation(LatLng currentLatLng, boolean getAddress) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14.0f));
        if (getAddress) {
            Geocoder geocoder;
            geocoder = new Geocoder(mContext, Locale.getDefault());
            try {
                Address address = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1).get(0);
                String addressLine = address.getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                if (addressLine != null && !addressLine.isEmpty()) {
                    Log.e("addressLine", addressLine);
                    mAddressLine = addressLine;
                    tvAddress.setText(addressLine);
                    btnOrder.setVisibility(View.VISIBLE);
                } else {
                    mAddressLine = "";
                    tvAddress.setText("");
                    btnOrder.setVisibility(View.GONE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mAddressLine = "";
            tvAddress.setText("");
            btnOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        //Toast.makeText(mContext, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(mContext, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("onRequestPermissions", "onRequestPermissionsResult");
        if (requestCode == REQUEST_PERMISSION_LOCATION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupMap();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (waitingGPS) {
            checkGPS();
            waitingGPS = false;
        }
    }
}
