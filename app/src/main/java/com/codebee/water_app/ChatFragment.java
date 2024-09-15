package com.codebee.water_app;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.water_app.dto.AuthDTO;
import com.codebee.water_app.dto.DriverDtoChat;
import com.codebee.water_app.service.RequestService;
import com.codebee.water_app.service.WaterAppService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    NotificationManager notificationManager;
    private String channelId = "info";

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        String token = prefs.getString("accessToken", "no");
        String email = prefs.getString("email", "no");
        String ch = prefs.getString("chat", "no");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<String> cities = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        RecyclerView recyclerView = fragment.findViewById(R.id.chatRecycler);
        cities.clear();
        ids.clear();


        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(email);

        System.out.println(authDTO.getEmail());
        WaterAppService requestService = RequestService.getRequestService();
        Call<DriverDtoChat> driver = requestService.getDriver(authDTO, token);

        notificationManager = (NotificationManager) fragment.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String driver_email;
        driver.enqueue(new Callback<DriverDtoChat>() {
            @Override
            public void onResponse(Call<DriverDtoChat> call, Response<DriverDtoChat> response) {

                if (response.isSuccessful()) {


                    fragment.findViewById(R.id.sendImage).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            EditText text = fragment.findViewById(R.id.messageTxt);


                            if (text.getText().toString().isEmpty()) {

                                Toast.makeText(getActivity(), "text is Empty", Toast.LENGTH_SHORT).show();

                            } else {

                                SharedPreferences prefs = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

                                int num = prefs.getInt("num", 0);

                                String token = prefs.getString("accessToken", "no");

                                Map<String, Object> message = new HashMap<>();

                                message.put("id", String.valueOf(num));
                                message.put("driver_mobile", response.body().getEmail());
                                message.put("customer_mobile", email);
                                message.put("status", "1");
                                message.put("date", new Date());
                                message.put("message", text.getText().toString());

                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt("num", num + 1);
                                editor.apply();
                                db.collection("messages")
                                        .add(message)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                text.setText("");


                                                db.collection("messages")
                                                        .whereEqualTo("customer_mobile", email)
                                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onEvent(@Nullable QuerySnapshot value,
                                                                                @Nullable FirebaseFirestoreException e) {
                                                                if (e != null) {
                                                                    Log.w("TAG", "Listen failed.", e);
                                                                    return;
                                                                }

                                                                cities.clear();
                                                                ids.clear();


                                                                ArrayList<String> integers = new ArrayList<>();

                                                                for (QueryDocumentSnapshot doc : value) {
                                                                    integers.add((String) doc.get("id"));
//                                                                    if (doc.get("message") != null) {
//                                                                        cities.add(doc.getString("message"));
//                                                                    }
//                                                                    if (doc.get("status") != null) {
//                                                                        ids.add(doc.getString("status"));
//                                                                    }
                                                                }


                                                                Collections.sort(integers);
                                                                for (int i = 0; i < integers.size(); i++) {
                                                                    for (QueryDocumentSnapshot doc : value) {

                                                                        String integer = integers.get(i);
                                                                        String id = (String) doc.get("id");
                                                                        if (id.equals(integer)) {
                                                                            if (doc.get("message") != null) {
                                                                                cities.add(i, doc.getString("message"));

                                                                            }
                                                                            if (doc.get("status") != null) {
                                                                                ids.add(i, doc.getString("status"));
                                                                            }

                                                                        }
//
                                                                    }


                                                                }

                                                                Log.d("TAG", "Current cites in CA: " + cities);

                                                                if (cities.size() != 0) {
                                                                    RecyclerView.Adapter<ChatVH> adapter = new RecyclerView.Adapter<ChatVH>() {
                                                                        @NonNull
                                                                        @Override
                                                                        public ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                                                            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                                                                            View inflate = inflater.inflate(R.layout.chat_layout_small, parent, false);

                                                                            return new ChatVH(inflate);
                                                                        }

                                                                        @Override
                                                                        public void onBindViewHolder(@NonNull ChatVH holder, int position) {

                                                                            if ((ids.get(position)).equals("2")) {
                                                                                TextView textView = holder.getTextView();
                                                                                textView.setText(cities.get(position).toString());
                                                                            } else {
                                                                                TextView textView = holder.getTextView2();
                                                                                textView.setText(cities.get(position).toString());

                                                                            }


                                                                        }


                                                                        @Override
                                                                        public int getItemCount() {
                                                                            return cities.size();
                                                                        }
                                                                    };


                                                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                                    recyclerView.setAdapter(adapter);
                                                                } else {

                                                                }

                                                            }
                                                        });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error adding document", e);
                                            }
                                        });

                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<DriverDtoChat> call, Throwable t) {
                System.out.println(t);
            }
        });


        SharedPreferences shar = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shar.edit();
        editor.putString("chat", "yes");
        editor.apply();

        db.collection("messages")
                .whereEqualTo("customer_mobile", email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        cities.clear();
                        ids.clear();
                        ArrayList<String> integers = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : value) {
                            integers.add((String) doc.get("id"));
//                                                                    if (doc.get("message") != null) {
//                                                                        cities.add(doc.getString("message"));
//                                                                    }
//                                                                    if (doc.get("status") != null) {
//                                                                        ids.add(doc.getString("status"));
//                                                                    }
                        }


                        Collections.sort(integers);
                        for (int i = 0; i < integers.size(); i++) {
                            for (QueryDocumentSnapshot doc : value) {

                                String integer = integers.get(i);
                                String id = (String) doc.get("id");
                                if (id.equals(integer)) {
                                    if (doc.get("message") != null) {
                                        cities.add(i, doc.getString("message"));

                                    }
                                    if (doc.get("status") != null) {
                                        ids.add(i, doc.getString("status"));
                                    }

                                    if (doc.get("status").equals("2") && i == integers.size()) {


                                        Intent intent = new Intent(getActivity(), ClientHomeActivity.class);
                                        intent.putExtra("name", "ABCD");
                                        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

                                        Notification notification = new NotificationCompat.Builder(getActivity(), channelId)
                                                .setContentTitle("Pure water").setSmallIcon(R.drawable.purewater)
                                                .setContentText("new Message").setColor(Color.RED).setContentIntent(pendingIntent)
                                                .build();


                                        notificationManager.notify(1, notification);
                                    }

                                }
//
                            }

                        }


                        Log.d("TAG", "Current cites in CA: " + cities);

                        if (cities.size() != 0) {
                            RecyclerView.Adapter<ChatVH> adapter = new RecyclerView.Adapter<ChatVH>() {
                                @NonNull
                                @Override
                                public ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                                    View inflate = inflater.inflate(R.layout.chat_layout_small, parent, false);

                                    return new ChatVH(inflate);
                                }

                                @Override
                                public void onBindViewHolder(@NonNull ChatVH holder, int position) {

                                    if ((ids.get(position)).equals("2")) {
                                        TextView textView = holder.getTextView();
                                        textView.setText(cities.get(position).toString());
                                    } else {
                                        TextView textView = holder.getTextView2();
                                        textView.setText(cities.get(position).toString());

                                    }


                                }


                                @Override
                                public int getItemCount() {
                                    return cities.size();
                                }
                            };


                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(adapter);
                        } else {

                        }

                    }
                });

    }


}

class ChatVH extends RecyclerView.ViewHolder {
    public TextView getTextView2() {
        return textView2;
    }

    private TextView textView, textView2;

    public ChatVH(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.chatTextApp);
        textView2 = itemView.findViewById(R.id.chatTextApp2);

    }


    public TextView getTextView() {
        return textView;
    }
}


class SortChat implements Comparator<QueryDocumentSnapshot> {


    @Override
    public int compare(QueryDocumentSnapshot o1, QueryDocumentSnapshot o2) {

        if (o1.getString("id") == o2.getString("id"))
            return 0;
        else if (Integer.parseInt(o1.getString("id")) > Integer.parseInt(o2.getString("id")))
            return 1;
        else
            return -1;

    }
}