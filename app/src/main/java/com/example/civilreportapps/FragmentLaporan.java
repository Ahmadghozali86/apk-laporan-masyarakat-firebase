package com.example.civilreportapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class FragmentLaporan extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.laporan, container, false);

        TextView textViewLogout = view.findViewById(R.id.metu);

        // Mengambil referensi ke setiap CardView
        CardView cardViewKebakaran = view.findViewById(R.id.cardViewKebakaran);
        CardView cardViewKesehatan = view.findViewById(R.id.cardViewKesehatan);
        CardView cardViewBencana = view.findViewById(R.id.cardViewBencana);

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Proses logout
                FirebaseAuth.getInstance().signOut();

                // Redirect ke halaman login atau halaman awal aplikasi setelah logout
                Intent intent = new Intent(getActivity(), login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        // Menambahkan OnClickListener ke setiap CardView
        cardViewKebakaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk membuka TambahLaporanActivity saat cardViewKebakaran diklik
                Intent intent = new Intent(getActivity(), TambahLaporan.class);
                intent.putExtra("jenislaporan", "kebakaran");
                startActivity(intent);
            }
        });

        cardViewKesehatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk membuka TambahLaporanActivity saat cardViewKesehatan diklik
                Intent intent = new Intent(getActivity(), TambahLaporan1.class);
                intent.putExtra("jenislaporan", "kesehatan");

                startActivity(intent);
            }
        });

        cardViewBencana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk membuka TambahLaporanActivity saat cardViewBencana diklik
                Intent intent = new Intent(getActivity(), TambahLaporan2.class);
                intent.putExtra("jenislaporan", "bencana alam");

                startActivity(intent);
            }
        });

        return view;
    }
}