package com.septian.project005;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class TambahDataActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_tambah_nama, edit_tambah_jabatan, edit_tambah_gaji;
    private Button btn_tambah_pegawai, btn_lihat_pegawai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);

        edit_tambah_nama = findViewById(R.id.edit_tambah_nama);
        edit_tambah_jabatan = findViewById(R.id.edit_tambah_jabatan);
        edit_tambah_gaji = findViewById(R.id.edit_tambah_gaji);
        btn_tambah_pegawai = findViewById(R.id.btn_tambah_pegawai);
        btn_lihat_pegawai = findViewById(R.id.btn_lihat_pegawai);

        btn_tambah_pegawai.setOnClickListener(this);
        btn_lihat_pegawai.setOnClickListener(this);
    }


    // Implements untuk button tambah pegawai & button lihat pegawai
    // View v ==> v untuk perwakilan button ptambah & lihat
    // bisa pakai switch case atau if else
    // implement untuk list > onSetItemListener()
    @Override
    public void onClick(View v) {
        if (v == btn_lihat_pegawai) {
            startActivity(new Intent(TambahDataActivity.this, LihatDataActivity.class));
        } else if (v == btn_tambah_pegawai) {
            simpanDataPegawai();
        }
    }

    private void simpanDataPegawai() {
        // field apa saja yang akan disimpan
        final String nama = edit_tambah_nama.getText().toString().trim();
        final String jabatan = edit_tambah_jabatan.getText().toString().trim();
        final String gaji = edit_tambah_gaji.getText().toString().trim();

        class SimpanDataPegawai extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TambahDataActivity.this,
                        "Menyimpan Data", "Harap Tunggu ...",
                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                // params digunakan untuk meyimpan ke HttpHandler
                HashMap<String, String> params = new HashMap<>();
                params.put(Konfigurasi.KEY_PGW_NAMA, nama);
                params.put(Konfigurasi.KEY_PGW_JABATAN, jabatan);
                params.put(Konfigurasi.KEY_PGW_GAJI, gaji);
                HttpHandler handler = new HttpHandler();
                // HttpHandler untuk kirim data pakai sendPostRequest
                String result = handler.sendPostRequest(Konfigurasi.URL_GET_ADD, params);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TambahDataActivity.this, "pesan:" + s,
                        Toast.LENGTH_SHORT).show();
                // method untuk clear setelah data ditambah di form
                clearText();
            }
        }

        SimpanDataPegawai simpanDataPegawai = new SimpanDataPegawai();
        simpanDataPegawai.execute();

    }

    private void clearText() {
        edit_tambah_nama.setText("");
        edit_tambah_jabatan.setText("");
        edit_tambah_gaji.setText("");
        // untuk pointer langsung menuju kolom nama di layout
        edit_tambah_nama.requestFocus();
    }


}