package com.septian.project005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LihatDataActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView list_view;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list_view = findViewById(R.id.list_view);
        list_view.setOnItemClickListener(this); // klik + o pada this pilih implement

        //methode untuk ambil data JSON
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
                loading = ProgressDialog.show(LihatDataActivity.this,
                        "Mengambil Data", "Harap Menunggu",
                        false, false);
            }

            // Saat proses ambil data terjadi
            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                String result = handler.sendGetResponse(Konfigurasi.URL_GET_ALL);
                return result;
            }

            // ctrl + o pilih OnPostExcetue
            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                JSON_STRING = message;
                Log.d("DATA JSON:", JSON_STRING);

                // menampilkan data dalam bentuk list view
                displayAllData();

            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    // method penyusuian file JSON dengan android
    private void displayAllData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        // KONVERSI JSON TO ARRAY TO ARRAY LIST
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JASON_ARRAY);
            Log.d("DATA_JSON:", JSON_STRING);

            for (int i = 0; 1 < result.length(); i++) {
                JSONObject object = result.getJSONObject(i);
                // menampilkan id dan nama
                String id = object.getString(Konfigurasi.TAG_JSON_ID);
                String name = object.getString(Konfigurasi.TAG_JSON_NAMA);

                HashMap<String, String> pegawai = new HashMap<>();
                pegawai.put(Konfigurasi.TAG_JSON_ID, id);
                pegawai.put(Konfigurasi.TAG_JSON_NAMA, name);

                // ubah format json menjadi array list
                list.add(pegawai);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        // adapter untuk meletakan array list kedalam list view
        ListAdapter adapter = new SimpleAdapter(
                getApplicationContext(), list,
                R.layout.activity_list_item,
                new String[]{Konfigurasi.TAG_JSON_ID, Konfigurasi.TAG_JSON_NAMA},
                new int[]{R.id.txt_id, R.id.txt_name}
        );
        list_view.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Ketika salah satu list dipilih
        // detail : id, name, Desg, Salary
        Intent myIntent = new Intent(LihatDataActivity.this, LihatDetailDataActivity.class);
        HashMap<String, String> map = (HashMap) adapterView.getItemAtPosition(i);
        String pgwId = map.get(Konfigurasi.TAG_JSON_ID).toString();
        myIntent.putExtra(Konfigurasi.PGW_ID, pgwId);
        startActivity(myIntent);
    }

    // AGAR BACK BUTTON BISA BEKERJA
    // alt + enter > onOptionsItemSelected

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}