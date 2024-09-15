package com.codebee.water_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codebee.water_app.dto.AuthDTO;
import com.codebee.water_app.dto.MessageDto;
import com.codebee.water_app.dto.OrderDto;
import com.codebee.water_app.dto.OrderItemDto;
import com.codebee.water_app.dto.OrderReceiveDto;
import com.codebee.water_app.service.RequestService;
import com.codebee.water_app.service.WaterAppService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderItemsViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderItemsViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderItemsViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderItemsViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderItemsViewFragment newInstance(String param1, String param2) {
        OrderItemsViewFragment fragment = new OrderItemsViewFragment();
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
        return inflater.inflate(R.layout.fragment_order_items_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);


        TextView viewById = getActivity().findViewById(R.id.homeTitleText);
        viewById.setText("Orders");
        WaterAppService requestService = RequestService.getRequestService();

        SharedPreferences prefs = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String token = prefs.getString("accessToken", "no");
        String email = prefs.getString("email", "no");
        AuthDTO authDTO = new AuthDTO();

        authDTO.setEmail(email);


        Call<List<OrderDto>> orders = requestService.getOrders(authDTO, token);
        orders.enqueue(new Callback<List<OrderDto>>() {
            @Override
            public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                if (response.isSuccessful()) {
                    List<OrderDto> body = response.body();
//                    body.forEach(bo -> {
//
//                        System.out.println(bo.getId());
//                    });


                    RecyclerView.Adapter<OrderVH> adapter = new RecyclerView.Adapter<OrderVH>() {
                        @NonNull
                        @Override
                        public OrderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                            View inflate = inflater.inflate(R.layout.order_items_view, parent, false);

                            return new OrderVH(inflate);
                        }

                        @Override
                        public void onBindViewHolder(@NonNull OrderVH holder, int position) {
                            TextView orderIdVh = holder.getOrderIdVh();
                            orderIdVh.setText("ID : "+body.get(position).getId().toString());

                            TextView oderAllVh = holder.getOderAllVh();

                            List<OrderItemDto> orderItemsDtos = body.get(position).getCartItemsDtos();
                            orderItemsDtos.forEach(items -> {
                                String order = items.getName();
                                String qty = String.valueOf(items.getQty());
                                String all = order + " : " + qty+"\n";

                                oderAllVh.setText(oderAllVh.getText().toString()+all);
                            });


                        }

                        @Override
                        public int getItemCount() {
                            return body.size();
                        }
                    };

                    RecyclerView recyclerView = fragment.findViewById(R.id.orderRecyclerView);

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<OrderDto>> call, Throwable t) {

            }
        });
    }
}

class OrderVH extends RecyclerView.ViewHolder {


    private TextView orderIdVh, orderAllVh;

    public TextView getOrderIdVh() {
        return orderIdVh;
    }

    public TextView getOderAllVh() {
        return orderAllVh;
    }

    public OrderVH(@NonNull View itemView) {
        super(itemView);

        orderIdVh = itemView.findViewById(R.id.orderItemsFragId);
        orderAllVh = itemView.findViewById(R.id.ordersItemsAll);
    }
}