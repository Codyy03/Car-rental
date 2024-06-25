package com.mycompany.project_java_3try;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Egzemplarze")
public class Egzemplarze {

    @Column(name = "marka")
    private String marka;

    @Column(name = "model", nullable = false)
    private String model;
    
    @Id
    @Column(name = "rejestracja", nullable = false)
    private String rejestracja;

    @Column(name = "data_produkcji", nullable = false)
    private Date dataProdukcji;

    
    

    @Column(name = "kolor", nullable = false)
    private String kolor;

    @Column(name = "przebieg", nullable = false)
    private Integer przebieg;

    @Column(name = "cena", nullable = false)
    private Double cena;

    // Getters and setters
    public String getRejestracja() {
        return rejestracja;
    }

    public void setRejestracja(String rejestracja) {
        this.rejestracja = rejestracja;
    }

    public Date getDataProdukcji() {
        return  dataProdukcji;
    }

    public void setDataProdukcji(Date dataProdukcji) {
        this.dataProdukcji = dataProdukcji;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMarka() {
        return marka;
    }

    public String getModel() {
        return model;
    }


    public String getKolor() {
        return kolor;
    }

    public void setKolor(String kolor) {
        this.kolor = kolor;
    }

    public Integer getPrzebieg() {
        return przebieg;
    }

    public void setPrzebieg(Integer przebieg) {
        this.przebieg = przebieg;
    }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }
}