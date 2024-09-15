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
import com.codebee.water_app.dto.MessageDto;
import com.codebee.water_app.dto.ProductDTO;
import com.codebee.water_app.service.MessageService;
import com.codebee.water_app.service.RequestService;
import com.codebee.water_app.service.WaterAppService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductViewFragment newInstance(String param1, String param2) {
        ProductViewFragment fragment = new ProductViewFragment();
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
        return inflater.inflate(R.layout.fragment_product_view, container, false);
    }


    List<ProductDTO> productDTOS = new ArrayList<ProductDTO>();

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        TextView viewById = getActivity().findViewById(R.id.homeTitleText);
        viewById.setText("Home");


        SharedPreferences prefs = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String token = prefs.getString("accessToken", "no");
        String email = prefs.getString("email", "no");
        WaterAppService requestService = RequestService.getRequestService();
        Call<List<ProductDTO>> products = requestService.getProducts();


        products.enqueue(new Callback<List<ProductDTO>>() {


            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                if (response.isSuccessful()) {
                    List<ProductDTO> productDTOS = new ArrayList<ProductDTO>();

                    productDTOS.addAll(response.body());
                    System.out.println(productDTOS.size());
                    RecyclerView.Adapter adapter = new RecyclerView.Adapter<ViewHolder>() {
                        @NonNull
                        @Override
                        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            System.out.println("oncreateViewHolder");

                            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                            View inflate = inflater.inflate(R.layout.product_view, parent, false);
                            return new ViewHolder(inflate);
                        }

                        @Override
                        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                            System.out.println(productDTOS.get(position).getName().toString());
                            holder.getTextViewTitleVH().setText(productDTOS.get(position).getName().toString());
                            holder.getGetTextPrice2VH().setText(productDTOS.get(position).getPrice().toString());
                            holder.getGetTextdescription3VH().setText(productDTOS.get(position).getDescription().toString());

                            holder.getEditButtonVH().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    String qty = holder.getEditTextVH().getText().toString();
                                    if (qty.isEmpty()) {
                                        new MessageService(getActivity(), "please add a qty", false).setMessage();
                                    } else {

//                                        new MessageService(getActivity(), qty, false).setMessage();


                                        CartDto cartDto = new CartDto();
                                        cartDto.setProduct(productDTOS.get(position).getId());
                                        cartDto.setQty(Integer.parseInt(qty));


                                        WaterAppService service = RequestService.getRequestService();


                                        cartDto.setUserEmail(email);
                                        System.out.println(token);
                                        Call<MessageDto> messageDtoCall = service.addToCart(cartDto, token);
                                        messageDtoCall.enqueue(new Callback<MessageDto>() {
                                            @Override
                                            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                                                if (response.isSuccessful()) {

                                                    ClientHomeActivity activity = (ClientHomeActivity) getActivity();
                                                    activity.setCartCount();
                                                    new MessageService(getActivity(), "successfully added to cart", true).setMessage();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<MessageDto> call, Throwable t) {
                                                new MessageService(getActivity(), "please check your connection", false).setMessage();
                                            }
                                        });
                                    }

                                }
                            });


                            int imageSize = productDTOS.get(position).getImages().size();


                            if (imageSize != 0) {


                                if (imageSize == 1) {

                                    Picasso.get()
                                            .load(Uri.parse(RequestService.getContectPath() + productDTOS.get(position).getImages().get(0))).resize(200, 200)
                                            .into(holder.getImageView1VH());
                                } else if (imageSize == 2) {
                                    Picasso.get()
                                            .load(Uri.parse(RequestService.getContectPath() + productDTOS.get(position).getImages().get(0))).resize(200, 200)
                                            .into(holder.getImageView1VH());
                                    Picasso.get()
                                            .load(Uri.parse(RequestService.getContectPath() + productDTOS.get(position).getImages().get(1))).resize(200, 200)
                                            .into(holder.getImageView2VH());

                                } else if (imageSize == 3) {

                                    Picasso.get()
                                            .load(Uri.parse(RequestService.getContectPath() + productDTOS.get(position).getImages().get(0))).resize(200, 200)
                                            .into(holder.getImageView1VH());

                                    Picasso.get()
                                            .load(Uri.parse(RequestService.getContectPath() + productDTOS.get(position).getImages().get(1))).resize(200, 200)
                                            .into(holder.getImageView2VH());
                                    Picasso.get()
                                            .load(Uri.parse(RequestService.getContectPath() + productDTOS.get(position).getImages().get(2))).resize(200, 200)
                                            .into(holder.getImageView3VH());

                                }

                            }


                        }

                        @Override
                        public int getItemCount() {
                            return productDTOS.size();
                        }
                    };

                    RecyclerView recyclerView = fragment.findViewById(R.id.recyclerViewProduct);

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });


    }


}

class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView1VH, imageView2VH, imageView3VH;
    private TextView textViewTitleVH, getTextPrice2VH, getTextdescription3VH;

    private EditText editTextVH;
    private Button editButtonVH;

    public EditText getEditTextVH() {
        return editTextVH;
    }

    public Button getEditButtonVH() {
        return editButtonVH;
    }

    public ImageView getImageView1VH() {
        return imageView1VH;
    }

    public ImageView getImageView2VH() {
        return imageView2VH;
    }

    public ImageView getImageView3VH() {
        return imageView3VH;
    }

    public TextView getTextViewTitleVH() {
        return textViewTitleVH;
    }

    public TextView getGetTextPrice2VH() {
        return getTextPrice2VH;
    }

    public TextView getGetTextdescription3VH() {
        return getTextdescription3VH;
    }

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewTitleVH = itemView.findViewById(R.id.mainText);
        getTextPrice2VH = itemView.findViewById(R.id.price);
        getTextdescription3VH = itemView.findViewById(R.id.description);
        imageView1VH = itemView.findViewById(R.id.imageViewProduct1);
        imageView2VH = itemView.findViewById(R.id.imageViewProduct2);
        imageView3VH = itemView.findViewById(R.id.imageViewProduct3);
        editButtonVH = itemView.findViewById(R.id.productButtonItem);
        editTextVH = itemView.findViewById(R.id.productQtyField);

    }
}