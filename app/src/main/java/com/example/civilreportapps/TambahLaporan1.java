package com.example.civilreportapps;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class TambahLaporan1 extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_GALLERY = 102;
    private static final int REQUEST_PERMISSION_CODE = 103;




    Uri selectedImageUri;

    ActivityResultLauncher<Intent> imagePickLauncher;
    DataLaporan currentDate;
    ImageView gambar;



    private Button btnClear;
    String imageURL;
    private Uri imageUri;
    private String imageFilePath = "";
    Uri uri;
    TextInputEditText editnama, edittelepon, editlokasi, edittanggal, editisi;
    Button buttonsimpan;
    ProgressBar progressBar;
    String jenisLaporan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_laporan);

        editnama = findViewById(R.id.namaku);
        edittelepon = findViewById(R.id.teleponku);
        editlokasi = findViewById(R.id.lokasiku);
        edittanggal = findViewById(R.id.tanggalku);
        editisi = findViewById(R.id.isiku);
        gambar = findViewById(R.id.fotoLaporan);

        buttonsimpan = findViewById(R.id.btn_simpan);
        progressBar = findViewById(R.id.progressBar);


        Intent intent = getIntent();
        if (intent != null){
            jenisLaporan = intent.getStringExtra("jenislaporan");
        }
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null){
                            selectedImageUri = data.getData();
                            // Set ImageView dengan gambar yang dipilih
                            gambar.setImageURI(selectedImageUri);
                        }
                    }
                });
        gambar.setOnClickListener((v)->{
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            selectedImageUri = data.getData();
                            gambar.setImageURI(selectedImageUri);
                        }else {
                            Toast.makeText(TambahLaporan1.this, "Tidak Ada Gambar Dipilih", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        buttonsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }
    public void saveData(){
        if (selectedImageUri == null) {
            Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Foto Laporan Kesehatan").child(selectedImageUri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(TambahLaporan1.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();

                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }
    public void uploadData(){
        String NamaKu = editnama.getText().toString();
        String Teleponku = edittelepon.getText().toString();
        String LokasiKu = editlokasi.getText().toString();
        String TanggalKu = edittanggal.getText().toString();
        String IsiKu = editisi.getText().toString();
        String jenisLaporanku = jenisLaporan;
        DataLaporan dataLaporan = new DataLaporan(NamaKu, Teleponku, LokasiKu, TanggalKu, IsiKu,jenisLaporanku, imageURL);

        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        currentDate = currentDate.replace(".", ""); // Menghapus titik dari format tanggal dan waktu
        currentDate = currentDate.replace(" ", "_"); // Mengganti spasi dengan garis bawah
        currentDate = currentDate.replace(":", "-"); // Mengganti titik dua dengan tanda hubung

        FirebaseDatabase.getInstance().getReference("Histori Laporan Kesehatan").child(currentDate)
                .setValue(dataLaporan).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(TambahLaporan1.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TambahLaporan1.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

}