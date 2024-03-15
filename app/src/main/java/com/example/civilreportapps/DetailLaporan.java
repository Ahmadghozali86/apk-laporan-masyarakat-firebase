package com.example.civilreportapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailLaporan extends AppCompatActivity {

    TextView namaP, teleponP, tanggalP, lokasiP, isiP;
    String jenisLaporan;
    ImageView gambarP;
    String key = "";
    String imageUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan);

        namaP = findViewById(R.id.namap);
        tanggalP = findViewById(R.id.tanggalp);
        teleponP = findViewById(R.id.teleponp);
        lokasiP = findViewById(R.id.lokasip);
        isiP = findViewById(R.id.isip);
        gambarP = findViewById(R.id.gambarp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            namaP.setText(bundle.getString("nama"));
            tanggalP.setText(bundle.getString("tanggal"));
            lokasiP.setText(bundle.getString("lokasi"));
            teleponP.setText(bundle.getString("telepon"));
            isiP.setText(bundle.getString("isi"));
            key = bundle.getString("key");
            jenisLaporan=bundle.getString("jenislaporan");
            imageUrl = bundle.getString("gambar");
            Log.d("Detail Menu", "URL Gambar: " + imageUrl);
            Glide.with(this).load(bundle.getString("gambar")).into(gambarP);


        }


    }
    public void UpdateLaporan(View view) {
        Intent intent = new Intent(DetailLaporan.this, UpdateLaporan.class)
                .putExtra("nama", namaP.getText().toString())
                .putExtra("telepon", teleponP.getText().toString())
                .putExtra("tanggal", tanggalP.getText().toString())
                .putExtra("lokasi", lokasiP.getText().toString())
                .putExtra("isi", isiP.getText().toString())
                .putExtra("gambar", imageUrl)
                .putExtra("jenislaporan",jenisLaporan)
                .putExtra("Key", key);
        startActivity(intent);
    }

    public void HapusLaporan (View view) {
        DatabaseReference databaseReference;

        if (jenisLaporan.equals("kebakaran")){
            databaseReference = FirebaseDatabase.getInstance().getReference("Histori Laporan Kebakaran");
            
        } else if (jenisLaporan.equals("kesehatan")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Histori Laporan Kesehatan");

        }else if (jenisLaporan.equals("bencana alam")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Histori Laporan Bencana Alam");

        }else {
            Toast.makeText(this, "Jenis data tidak dikenali", Toast.LENGTH_SHORT).show();
            return;
        }
        databaseReference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DetailLaporan.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                deleteImageFromStorage(imageUrl);
                finish();
            }
        });


    }
    private void deleteImageFromStorage(String imageUrl) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);

        // Menghapus gambar dari Firebase Storage
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("DetailLaporan", "Gambar berhasil dihapus dari Firebase Storage");
            }
        });
    }
}