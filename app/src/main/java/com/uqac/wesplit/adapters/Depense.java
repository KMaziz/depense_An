package com.uqac.wesplit.adapters;

import java.io.Serializable;
import java.util.Map;

/**
 Représente une dépense
 */

public class Depense implements Serializable {

    private String _id;
    private String categorie;
    private String montant;
    private String quantite;
    private String titre;
    private String timestamp;

    public Depense() {
    }

    public Depense(String _id, String categorie, String montant, String quantite, String titre, String timestamp, Map<String, String> users) {
        this._id = _id;
        this.categorie = categorie;
        this.montant = montant;
        this.quantite = quantite;
        this.titre = titre;
        this.timestamp = timestamp;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCategorie() { return categorie; }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getQuantite() {
        return quantite;
    }

    public void setQuantite(String payeparid) {
        this.quantite = payeparid;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Depense depense = (Depense) o;

        if (_id != null ? !_id.equals(depense._id) : depense._id != null) return false;
        if (categorie != null ? !categorie.equals(depense.categorie) : depense.categorie != null)
            return false;
        if (montant != null ? !montant.equals(depense.montant) : depense.montant != null)
            return false;
        if (quantite != null ? !quantite.equals(depense.quantite) : depense.quantite != null)
            return false;

        if (titre != null ? !titre.equals(depense.titre) : depense.titre != null) return false;
        if (timestamp != null ? !timestamp.equals(depense.timestamp) : depense.timestamp != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (categorie != null ? categorie.hashCode() : 0);
        result = 31 * result + (montant != null ? montant.hashCode() : 0);
        result = 31 * result + (quantite != null ? quantite.hashCode() : 0);
        result = 31 * result + (titre != null ? titre.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Depense{" +
                "_id='" + _id + '\'' +
                ", categorie='" + categorie + '\'' +
                ", montant='" + montant + '\'' +
                ", payeparid='" + quantite + '\'' +
                ", titre='" + titre + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
