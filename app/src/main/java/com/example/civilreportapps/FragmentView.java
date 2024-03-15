package com.example.civilreportapps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentView extends Fragment {
    RecyclerView recyclerView, recyclerView1,recyclerView2;
    List<DataLaporan> dataList, dataList1, dataList2;
    ValueEventListener eventListener, eventListener1, eventListener2;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;

    DatabaseReference databaseReference2;

    public FragmentView(){

    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView2 = view.findViewById(R.id.recyclerView2);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(requireContext(), 1);
        recyclerView1.setLayoutManager(gridLayoutManager1);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(requireContext(), 1);
        recyclerView2.setLayoutManager(gridLayoutManager2);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        dataList1 = new ArrayList<>();
        dataList2 = new ArrayList<>();

        MyAdapter adapter = new MyAdapter(requireContext(), dataList);
        recyclerView.setAdapter(adapter);
        MyAdapter adapter1 = new MyAdapter(requireContext(), dataList1);
        recyclerView1.setAdapter(adapter1);
        MyAdapter adapter2 = new MyAdapter(requireContext(), dataList2);
        recyclerView2.setAdapter(adapter2);


        databaseReference = FirebaseDatabase.getInstance().getReference("Histori Laporan Kebakaran");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Histori Laporan Kesehatan");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Histori Laporan Bencana Alam");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataLaporan dataLaporan = itemSnapshot.getValue(DataLaporan.class);
                    dataLaporan.setKey(itemSnapshot.getKey());
                    dataList.add(dataLaporan);
                }

                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                dialog.dismiss();
            }

        });
        eventListener1 = databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList1.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataLaporan dataLaporan = itemSnapshot.getValue(DataLaporan.class);
                    dataLaporan.setKey(itemSnapshot.getKey());
                    dataList1.add(dataLaporan);
                }

                adapter1.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                dialog.dismiss();
            }

        });
        eventListener2 = databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList2.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataLaporan dataLaporan = itemSnapshot.getValue(DataLaporan.class);
                    dataLaporan.setKey(itemSnapshot.getKey());
                    dataList2.add(dataLaporan);
                }

                adapter2.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                dialog.dismiss();
            }

        });

        return view;
    }

}