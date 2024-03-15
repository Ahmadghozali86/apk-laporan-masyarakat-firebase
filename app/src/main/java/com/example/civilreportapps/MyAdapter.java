package com.example.civilreportapps;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataLaporan> dataList;


    public MyAdapter(Context context, List<DataLaporan> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context).load(dataList.get(position).getImglaporan()).into(holder.gambar);
        holder.nama.setText(dataList.get(position).getNama());
        holder.tanggal.setText(dataList.get(position).getTanggal());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailLaporan.class);
                intent.putExtra("gambar", dataList.get(holder.getAdapterPosition()).getImglaporan());
                intent.putExtra("nama", dataList.get(holder.getAdapterPosition()).getNama());
                intent.putExtra("telepon", dataList.get(holder.getAdapterPosition()).getTelepon());
                intent.putExtra("lokasi", dataList.get(holder.getAdapterPosition()).getLokasi());
                intent.putExtra("tanggal", dataList.get(holder.getAdapterPosition()).getTanggal());
                intent.putExtra("key", dataList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("jenislaporan", dataList.get(holder.getAdapterPosition()).getJenisLaporan());
                intent.putExtra("isi", dataList.get(holder.getAdapterPosition()).getIsi());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();}

    public void searchDataList(ArrayList<DataLaporan> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView gambar;
    TextView nama, tanggal;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        gambar = itemView.findViewById(R.id.gambar);
        recCard = itemView.findViewById(R.id.recCard);
        tanggal = itemView.findViewById(R.id.tanggal);
        nama = itemView.findViewById(R.id.nama);

    }
}