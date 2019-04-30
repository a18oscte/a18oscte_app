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

    public String info(){
        String tmp;
        tmp ="Pris per dosa: "+ pris+" Kr\nFöretag: " + company + "\nMängd i dosan: " + mangd+"g\nKategori: " + kategori + "\nNikotinhalt: " + styrka+ " mg/g";

        return tmp;
    }

    public String namn(){
        return name;
    }

    public String com(){
        return company;
    }

    public String cost(){
        String he = pris + " Kr/st";
        return he;
    }

    public String img(){
        return bild;
    }
}
