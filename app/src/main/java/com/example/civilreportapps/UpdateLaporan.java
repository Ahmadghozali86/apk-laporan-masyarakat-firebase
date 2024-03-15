package com.example.civilreportapps;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateLaporan extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_GALLERY = 102;
    private static final int REQUEST_PERMISSION_CODE = 103;


    ImageView gambar;
    private Button btnClear;
    String imageUrl;
    String key, oldImageURL;
    private Uri imageUri;
    private String imageFilePath = "";
    Uri uri;

    String namamu, teleponmu, lokasimu, tanggalmu, isimu;
    TextInputEditText updateeditnama, updateedittelepon, updateeditlokasi, updateedittanggal, updateeditisi;
    Button buttonupdate;
    ProgressBar progressBar;
    String jenisLaporan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_laporan);

        updateeditnama = findViewById(R.id.updatenamaku);
        updateedittelepon = findViewById(R.id.updateteleponku);
        updateeditlokasi = findViewById(R.id.updatelokasiku);
        updateedittanggal = findViewById(R.id.updatetanggalku);
        updateeditisi = findViewById(R.id.updateisiku);
        gambar = findViewById(R.id.updatefotoLaporan);

        buttonupdate = findViewById(R.id.btn_update);
        progressBar = findViewById(R.id.progressBar);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            gambar.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateLaporan.this, "Tidak ada gambar dipilih", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(UpdateLaporan.this).load(bundle.getString("gambar")).into(gambar);
            updateeditnama.setText(bundle.getString("nama"));
            updateedittelepon.setText(bundle.getString("telepon"));
            updateeditlokasi.setText(bundle.getString("lokasi"));
            updateedittanggal.setText(bundle.getString("tanggal"));
            jenisLaporan=bundle.getString("jenislaporan");
            updateeditisi.setText(bundle.getString("isi"));
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("gambar");
        }
        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        buttonupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri == null) {
                    Toast.makeText(UpdateLaporan.this, "Gambar belum diubah", Toast.LENGTH_SHORT).show();
                } else {
                    saveData();
                    Intent intent = new Intent(UpdateLaporan.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void saveData(){

        StorageReference storageReference;
        if (jenisLaporan.equals("kebakaran")){
            storageReference = FirebaseStorage.getInstance().getReference().child("Foto Laporan Kebakaran").child(uri.getLastPathSegment());

        } else if (jenisLaporan.equals("kesehatan")) {
            storageReference = FirebaseStorage.getInstance().getReference().child("Foto Laporan Kesehatan").child(uri.getLastPathSegment());

        }else if (jenisLaporan.equals("bencana alam")) {
            storageReference = FirebaseStorage.getInstance().getReference().child("Foto Laporan Bencana Alam").child(uri.getLastPathSegment());

        }else {
            Toast.makeText(this, "Jenis data tidak dikenali", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateLaporan.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                updateData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }
    public void updateData(){
        namamu = updateeditnama.getText().toString().trim();
        teleponmu = updateedittelepon.getText().toString().trim();
        lokasimu = updateeditlokasi.getText().toString().trim();
        tanggalmu = updateedittanggal.getText().toString().trim();
        isimu = updateeditisi.getText().toString().trim();
        String JENISLAPORAN = jenisLaporan;

        DatabaseReference databaseReference;
        if (JENISLAPORAN.equals("kebakaran")){
            databaseReference = FirebaseDatabase.getInstance().getReference("Histori Laporan Kebakaran").child(key);

        } else if (JENISLAPORAN.equals("kesehatan")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Histori Laporan Kesehatan").child(key);

        }else if (JENISLAPORAN.equals("bencana alam")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Histori Laporan Bencana Alam").child(key);

        }else {
            Toast.makeText(this, "Jenis data tidak dikenali", Toast.LENGTH_SHORT).show();
            return;
        }

        DataLaporan dataLaporan = new DataLaporan(namamu,teleponmu,lokasimu, tanggalmu, isimu, JENISLAPORAN, imageUrl);

        databaseReference.setValue(dataLaporan).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    reference.delete();
                    Toast.makeText(UpdateLaporan.this, "Telah di Update", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateLaporan.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}