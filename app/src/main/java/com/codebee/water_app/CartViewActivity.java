package com.codebee.water_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.codebee.water_app.dto.CartItemsDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartViewActivity extends AppCompatActivity {


    List<CartItemsDto> cartItemsDtos = new ArrayList<CartItemsDto>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);


        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.activityCartCpntainer, CartViewFragment.class, null)
                .commit();


        findViewById(R.id.cartButtonProcces).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cartItemsDtos != null&&cartItemsDtos.size()!=0) {

                    Intent intent = new Intent(CartViewActivity.this, OrderActivity.class);
                    startActivity(intent);
                }

            }
        });

        findViewById(R.id.closeImageCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartViewActivity.this, ClientHomeActivity.class);
                startActivity(intent);
            }
        });
    }
}