package com.septian.project005;

public class Konfigurasi {
    public static final String URL_GET_ALL = "http://192.168.31.103/pegawai/tampilSemuaPgw.php";
    public static final String URL_GET_DETAIL = "http://192.168.31.103/pegawai/tampilPgw.php?id=";
    public static final String URL_GET_ADD = "http://192.168.31.103/pegawai/tambahPgw.php";
    public static final String URL_UPDATE = "http://192.168.31.103/pegawai/updatePgw.php";
    public static final String URL_DELETE = "http://192.168.31.103/pegawai/hapusPgw.php";

    // key adn value JSON yang muncul di browser
    public static final String KEY_PGW_ID = "id";
    public static final String KEY_PGW_NAMA = "name";
    public static final String KEY_PGW_JABATAN ="desg";
    public static final String KEY_PGW_GAJI ="salary";

    // flag JSON
    public static final String TAG_JASON_ARRAY = "result";
    public static final String TAG_JSON_ID = "id";
    public static final String TAG_JSON_NAMA ="name";
    public static final String TAG_JSON_JABATAN ="desg";
    public static final String TAG_JSON_GAJI ="salary";

    // variabel alias ID Pegawai
    public static final String PGW_ID = "emp_id"; // Memberikan Alias


}
