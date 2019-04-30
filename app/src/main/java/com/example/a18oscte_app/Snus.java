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
        tmp ="Location: " + company + "\nHeight: " + mangd + " meters\nÖppna i Wikipedia:";

        return tmp;
    }

    public String namn(){
        return name;
    }

    public String com(){
        return company;
    }

    public String hei(){
        String he = mangd + " Meters";
        return he;
    }

    public String img(){
        return bild;
    }
}
