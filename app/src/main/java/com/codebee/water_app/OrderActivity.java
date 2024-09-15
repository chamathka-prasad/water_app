package com.codebee.water_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.codebee.water_app.dto.AuthDTO;
import com.codebee.water_app.dto.CartItemsDto;
import com.codebee.water_app.dto.LocationDto;
import com.codebee.water_app.dto.MessageDto;
import com.codebee.water_app.service.MessageService;
import com.codebee.water_app.service.RequestService;
import com.codebee.water_app.service.WaterAppService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;
    private Marker markerCurrent;

    private Location currentlocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        SharedPreferences prefs = OrderActivity.this.getSharedPreferences("data", Context.MODE_PRIVATE);

        String email = prefs.getString("email", "no");
        String token = prefs.getString("accessToken", "no");

        Button button = findViewById(R.id.getLocation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });

        findViewById(R.id.requestOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentlocation != null) {
                    WaterAppService requestService = RequestService.getRequestService();
                    LocationDto locationDto = new LocationDto();
                    locationDto.setEmail(email);
                    locationDto.setLat(String.valueOf(currentlocation.getLatitude()));
                    locationDto.setLon(String.valueOf(currentlocation.getLongitude()));

                    Call<MessageDto> messageDtoCall = requestService.addOrder(locationDto, token);
                    messageDtoCall.enqueue(new Callback<MessageDto>() {
                        @Override
                        public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {


                            Intent intent = new Intent(OrderActivity.this, CartViewActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onFailure(Call<MessageDto> call, Throwable t) {

                        }
                    });

                }


            }
        });


        findViewById(R.id.closeImageOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OrderActivity.this, ClientHomeActivity.class);
                startActivity(intent);
            }
        });


    }

    public void getAccess() {
        if (checkPermission()) {
//            map.setMyLocationEnabled(true);
            getLastLocation();

        } else {
//            map.setMyLocationEnabled(true);

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_PERMISSION_REQUEST_CODE);
        }


    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(8.0163701, 79.8417505);
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(8);

        map.moveCamera(center);
        map.animateCamera(zoom);
        getAccess();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 10 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();

            } else {

                Snackbar.make(findViewById(R.id.mapContainer), "Turn on Location", Snackbar.LENGTH_INDEFINITE).setAction("Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                    }
                }).show();
            }
        }

    }


    private boolean checkPermission() {

        boolean permission = false;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            permission = true;

        }
        return permission;
    }

    private void getLastLocation() {
        if (checkPermission()) {

//            Task<Location> task = fusedLocationProviderClient.getLastLocation();
//            task.addOnSuccessListener(new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if (location != null) {
//                        currentlocation = location;
//                        LatLng latLng = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
//                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,70));
//                        map.addMarker(new MarkerOptions().position(latLng).title("my location"));
//                    }
//                }
//            });


            LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                    .setWaitForAccurateLocation(true)
                    .setMinUpdateIntervalMillis(500)
                    .setMaxUpdateDelayMillis(1000)
                    .build();

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);


                    currentlocation = locationResult.getLastLocation();
                    LatLng latLng = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
//                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,100));
//                    map.addMarker(new MarkerOptions().position(latLng).title("my location"));
                    CameraUpdate center =
                            CameraUpdateFactory.newLatLng(latLng);
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                    map.moveCamera(center);
                    map.animateCamera(zoom);
                    if (markerCurrent == null) {
                        MarkerOptions options = new MarkerOptions()
                                .title("my location")
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                                .position(latLng);
                        markerCurrent = map.addMarker(options);


                    } else {
                        markerCurrent.setPosition(latLng);
                    }
                    System.out.println(latLng);
//                    moveCamera(latLng);
                }


            }, Looper.getMainLooper());
        }
    }
}