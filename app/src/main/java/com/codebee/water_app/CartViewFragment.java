package com.codebee.water_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codebee.water_app.dto.AuthDTO;
import com.codebee.water_app.dto.CartDto;
import com.codebee.water_app.dto.CartItemsDto;
import com.codebee.water_app.service.RequestService;
import com.codebee.water_app.service.WaterAppService;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartViewFragment newInstance(String param1, String param2) {
        CartViewFragment fragment = new CartViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart_view, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String token = prefs.getString("accessToken", "no");
        String email = prefs.getString("email", "no");
        WaterAppService requestService = RequestService.getRequestService();
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(email);
        Call<List<CartItemsDto>> cartItem = requestService.getCartItem(authDTO, token);
        cartItem.enqueue(new Callback<List<CartItemsDto>>() {
            @Override
            public void onResponse(Call<List<CartItemsDto>> call, Response<List<CartItemsDto>> response) {
                if (response.isSuccessful()) {
                    System.out.println("success");
                    List<CartItemsDto> body = response.body();
                    CartViewActivity activity = (CartViewActivity) getActivity();

                    RecyclerView.Adapter viewHolderCartAdapter = new RecyclerView.Adapter<ViewHolderCart>() {
                        @NonNull
                        @Override
                        public ViewHolderCart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                            View inflate = inflater.inflate(R.layout.cart_item_view_layout, parent, false);
                            return new ViewHolderCart(inflate);

                        }

                        double totalall = 0.0;

                        @Override
                        public void onBindViewHolder(@NonNull ViewHolderCart holder, int position) {
                            CartItemsDto cartItemsDto = body.get(position);
                            holder.getTextViewTitleVH().setText(cartItemsDto.getProductName().toString());
                            holder.getGetTextQtyVH().setText(String.valueOf(cartItemsDto.getCartItemQty()));
                            activity.cartItemsDtos.add(cartItemsDto);
                            double total = cartItemsDto.getCartItemQty() * cartItemsDto.getProductPrice();
                            totalall = totalall + total;
                            System.out.println(total);
                            holder.getGetTextTotalVH().setText(String.valueOf(total) + "" + "(" + String.valueOf(cartItemsDto.getProductPrice()) + ")");
                            if (cartItemsDto.getImages().size() != 0) {
                                Picasso.get()
                                        .load(Uri.parse(RequestService.getContectPath() + cartItemsDto.getImages().get(0))).resize(200, 200)
                                        .into(holder.getImageView1VH());
                            }

                            CartViewActivity activity = (CartViewActivity) getActivity();
                            TextView viewById = activity.findViewById(R.id.activityTotal);
                            viewById.setText(String.valueOf(totalall));


                        }

                        @Override
                        public int getItemCount() {
                            return body.size();
                        }
                    };


                    RecyclerView recyclerView = fragment.findViewById(R.id.recyclerViewCart);

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


                    recyclerView.setAdapter(viewHolderCartAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<CartItemsDto>> call, Throwable t) {
                System.out.printf(t.toString());
            }
        });


    }
}


class ViewHolderCart extends RecyclerView.ViewHolder {
    private ImageView imageView1VH;
    private TextView textViewTitleVH, getTextQtyVH, getTextTotalVH;


    private Button deleteButtonVH;

    public ViewHolderCart(@NonNull View itemView) {
        super(itemView);


        textViewTitleVH = itemView.findViewById(R.id.cartViewTitle);
        getTextQtyVH = itemView.findViewById(R.id.cartViewQty);

        getTextTotalVH = itemView.findViewById(R.id.cartTotalViewNew);
        imageView1VH = itemView.findViewById(R.id.cartItemImageView);

    }

    public ImageView getImageView1VH() {
        return imageView1VH;
    }

    public TextView getTextViewTitleVH() {
        return textViewTitleVH;
    }

    public TextView getGetTextQtyVH() {
        return getTextQtyVH;
    }

    public TextView getGetTextTotalVH() {
        return getTextTotalVH;
    }

    public Button getDeleteButtonVH() {
        return deleteButtonVH;
    }


}