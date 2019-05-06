package com.example.a18oscte_app;

public class Snus {

    private String name;
    private String company;
    private String mangd;
    private String kategori;
    private int styrka;
    private int pris;
    private String bild;

    public Snus(String n, String c, String m,String k,int s,int p, String b){
        name = n;
        company = c;
        mangd = m;
        kategori = k;
        styrka = s;
        pris = p;
        bild = b;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getMangd() {
        return mangd;
    }

    public String getKategori() {
        return kategori;
    }

    public int getStyrka() {
        return styrka;
    }

    public int getPris() {
        return pris;
    }

    public String getBild() {
        return bild;
    }
}
