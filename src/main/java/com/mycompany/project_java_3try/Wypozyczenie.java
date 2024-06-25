package com.mycompany.project_java_3try;

import java.time.LocalDate;
import javax.persistence.*;

@Entity
@Table(name = "Wypozyczenia")
public class Wypozyczenie  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login", nullable = false)
    private Klienci klient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rejestracja", nullable = false)
    private Egzemplarze egzemplarz;

    @Column(name = "data_wypozyczenia", nullable = false)
    private LocalDate dataWypozyczenia;

    @Column(name = "data_zwrotu", nullable = false)
    private LocalDate dataZwrotu;

    public Wypozyczenie(Klienci klient, Egzemplarze egzemplarz, LocalDate dataWypozyczenia, LocalDate dataZwrotu) {
  
        this.klient = klient;
        this.egzemplarz = egzemplarz;
        this.dataWypozyczenia = dataWypozyczenia;
        this.dataZwrotu = dataZwrotu;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Klienci getKlient() {
        return klient;
    }

    public void setKlient(Klienci klient) {
        this.klient = klient;
    }

    public Egzemplarze getEgzemplarz() {
        return egzemplarz;
    }

    public void setEgzemplarz(Egzemplarze egzemplarz) {
        this.egzemplarz = egzemplarz;
    }

    public LocalDate getDataWypozyczenia() {
        return dataWypozyczenia;
    }

    public void setDataWypozyczenia(LocalDate dataWypozyczenia) {
        this.dataWypozyczenia = dataWypozyczenia;
    }

    public LocalDate getDataZwrotu() {
        return dataZwrotu;
    }

    public void setDataZwrotu(LocalDate dataZwrotu) {
        this.dataZwrotu = dataZwrotu;
    }
     public Wypozyczenie() {}
}
