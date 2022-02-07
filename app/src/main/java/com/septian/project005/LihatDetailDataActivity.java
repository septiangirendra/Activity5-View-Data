package com.septian.project005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class LihatDetailDataActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edit_id, edit_nama, edit_jabatan, edit_gaji;
    Button btn_update_pegawai, btn_delete_pegawai;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_detail_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Data Pegawai");

        edit_id = findViewById(R.id.edit_id);
        edit_nama = findViewById(R.id.edit_nama);
        edit_jabatan = findViewById(R.id.edit_jabatan);
        edit_gaji = findViewById(R.id.edit_gaji);
        btn_update_pegawai = findViewById(R.id.btn_update_pegawai);
        btn_delete_pegawai = findViewById(R.id.btn_delete_pegawai);

        // menerima data inten dari class LihatDataActivity
        Intent receiveIntent = getIntent();
        id = receiveIntent.getStringExtra(Konfigurasi.PGW_ID);
        edit_id.setText(id);

        // mengambil data JSON
        getJSON();

        btn_update_pegawai.setOnClickListener(this);
        btn_delete_pegawai.setOnClickListener(this);
    }

    private void getJSON() {
        // MENGAMBIL DATA DARI ANDROID KE SERVER
        // BANTUAN DARI CLASS ASYNCtASK
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            // ctrl + o pilih OnPreExcetue
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LihatDetailDataActivity.this,
                        "Mengambil Data", "Harap Menunggu",
                        false, false);
            }

            // Saat proses ambil data terjadi
            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                String result = handler.sendGetResponse(Konfigurasi.URL_GET_DETAIL, id);
                return result;
            }

            // ctrl + o pilih OnPostExcetue
            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                displayDetailData(message);

            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void displayDetailData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JASON_ARRAY);
            JSONObject object = result.getJSONObject(0);

            String nama = object.getString(Konfigurasi.TAG_JSON_NAMA);
            String jabatan = object.getString(Konfigurasi.TAG_JSON_JABATAN);
            String gaji = object.getString(Konfigurasi.TAG_JSON_GAJI);

            edit_nama.setText(nama);
            edit_jabatan.setText(jabatan);
            edit_gaji.setText(gaji);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //    ctrl + 0
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == btn_update_pegawai){
            //method
            updateDataPegawai();
        } else if (v == btn_delete_pegawai){
            confirmDeleteDataPegawai();

        }
    }

    private void confirmDeleteDataPegawai() {
        // Confirmation Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Menghapus Data");
        builder.setMessage("Apakah anda yakin menghapus data ini?");
        builder.setIcon(getResources().getDrawable(android.R.drawable.ic_delete));
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDataPegawai();
                startActivity(new Intent(LihatDetailDataActivity.this, MainActivity.class));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteDataPegawai() {
        class DeleteEmployee extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LihatDetailDataActivity.this,
                        "Menghapus", "Data",
                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                String s = handler.sendGetResponse(Konfigurasi.URL_DELETE, id);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(LihatDetailDataActivity.this, "Hapus" + s,
                        Toast.LENGTH_SHORT).show();
            }
        }

        DeleteEmployee deleteEmployee = new DeleteEmployee();
        deleteEmployee.execute();

    }

    private void updateDataPegawai() {
        // data apa saja yang akan diubah
        final String nama = edit_nama.getText().toString().trim();
        final String jabatan = edit_jabatan.getText().toString().trim();
        final String gaji = edit_gaji.getText().toString().trim();

        class UpdateDataPegawai extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LihatDetailDataActivity.this,
                        "Mengubah Data", "Harap Menunggu",
                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Konfigurasi.KEY_PGW_ID, id);
                params.put(Konfigurasi.KEY_PGW_NAMA, nama);
                params.put(Konfigurasi.KEY_PGW_JABATAN, jabatan);
                params.put(Konfigurasi.KEY_PGW_GAJI, gaji);
                HttpHandler handler = new HttpHandler();
                // Untuk mengupdate menggunakan sendPostRequest
                String result = handler.sendPostRequest(Konfigurasi.URL_UPDATE, params);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(LihatDetailDataActivity.this, "Pesan" + s,
                        Toast.LENGTH_SHORT).show();

                // Redirect ke LihatDataActivity
                startActivity(new Intent(LihatDetailDataActivity.this, LihatDataActivity.class));
            }
        }

        UpdateDataPegawai updateDataPegawai = new UpdateDataPegawai();
        updateDataPegawai.execute();

    }
}