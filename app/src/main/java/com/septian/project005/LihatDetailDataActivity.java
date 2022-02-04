package com.septian.project005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

public class LihatDetailDataActivity extends AppCompatActivity {

    EditText edit_id, edit_nama, edit_jabatan, edit_gaji;
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


        // menerima inten dari class LihatDataActivity
        Intent receiveIntent = getIntent();
        id = receiveIntent.getStringExtra(Konfigurasi.PGW_ID);
        edit_id.setText(id);

        // mengambil data JSON
        getJSON();
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

}