package com.uqac.wesplit.adapters;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class Personelle {
    private String _id;
    private String nom;
    private String salaire;

    public Personelle(String _id, String nom, String salaire) {
        this._id = _id;
        this.nom = nom;
        this.salaire = salaire;
    }

    public Personelle() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSalaire() {
        return salaire;
    }

    public void setSalaire(String salaire) {
        this.salaire = salaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Personelle)) return false;
        Personelle that = (Personelle) o;
        return get_id().equals(that.get_id()) &&
                getNom().equals(that.getNom()) &&
                getSalaire().equals(that.getSalaire());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(get_id(), getNom(), getSalaire());
    }

    @Override
    public String toString() {
        return "Personelle{" +
                "_id='" + _id + '\'' +
                ", nom='" + nom + '\'' +
                ", salaire='" + salaire + '\'' +
                '}';
    }
}
