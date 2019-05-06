package com.example.a18oscte_app;

import android.provider.BaseColumns;



public class SnusReaderContract {

    private SnusReaderContract() {}

    public static class Entry implements BaseColumns {
        public static final String TABLE_NAME = "Snus";
        public static final String COLUMN_NAME_NAME = "namn";
        public static final String COLUMN_NAME_PRIS = "pris";
        public static final String COLUMN_NAME_FORETAG = "foretag";
        public static final String COLUMN_NAME_MANGD = "mangd";
        public static final String COLUMN_NAME_KATEGORI = "kategori";
        public static final String COLUMN_NAME_NIKOTINHALT = "nikotinhalt";
        public static final String COLUMN_NAME_BILD = "bild";
    }


}