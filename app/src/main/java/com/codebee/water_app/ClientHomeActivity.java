package com.codebee.water_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codebee.water_app.dto.AuthDTO;
import com.codebee.water_app.dto.MessageDto;
import com.codebee.water_app.service.RequestService;
import com.codebee.water_app.service.WaterAppService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientHomeActivity extends AppCompatActivity {

    public BottomNavigationView navigationView;
    NotificationManager notificationManager;
    private String channelId = "info";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.productFragmentContainer, ProductViewFragment.class, null)
                .commit();


        ImageView imageView = findViewById(R.id.imageViewnaviconhome);
        Picasso.get().load(R.drawable.purewater)

                .into(imageView);


        notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


//            orio version 1ta wada wadi version walata witharai use karanna one

            NotificationChannel channel = new NotificationChannel(channelId, "INFO", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            channel.setDescription("This is information message");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.setVibrationPattern(new long[]{0, 1000, 1000, 1000});
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);


        }

        setCartCount();
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navHome) {

                System.out.println(itemId);
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.productFragmentContainer, ProductViewFragment.class, null)
                        .commit();

            } else if (itemId == R.id.navorder) {

                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.productFragmentContainer, OrderItemsViewFragment.class, null)
                        .commit();
            }
            else if (itemId == R.id.navchat) {

                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.productFragmentContainer, ChatFragment.class, null)
                        .commit();
            }


            return true;
        });


        findViewById(R.id.cartImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientHomeActivity.this, CartViewActivity.class);
                startActivity(intent);
            }
        });
    }


    public void setCartCount() {

        SharedPreferences prefs = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        String token = prefs.getString("accessToken", "no");
        String email = prefs.getString("email", "no");
        WaterAppService requestService = RequestService.getRequestService();
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(email);
        Call<MessageDto> itemCounts = requestService.getItemCounts(authDTO, token);
        itemCounts.enqueue(new Callback<MessageDto>() {
            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {

                MessageDto body = response.body();
                String message = body.getMessage();

                TextView text = findViewById(R.id.textViewCartCount);
                text.setText(message);

            }


            @Override
            public void onFailure(Call<MessageDto> call, Throwable t) {
                System.out.println(t);
            }
        });

    }


}