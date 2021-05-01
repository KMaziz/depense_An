package com.uqac.wesplit.enums;

/**
 Enum représentant les catégories possibles
 */

public enum CategoriesEnum {

    Stock ("Stock"),
    Chicha ("Chicha"),
    Contoire ("Contoire"),
    AUTRE ("Autre");

    private String categorie = "";

    CategoriesEnum(String categorie){
        this.categorie = categorie;
    }

    public String toString(){
        return categorie;
    }

    public static CategoriesEnum getEnum(String s) {
        switch (s) {
            case "Stock":
                return CategoriesEnum.Stock;
            case "Chicha":
                return CategoriesEnum.Chicha;
            case "Contoire":
                return CategoriesEnum.Contoire;
            case "Autre":
                return CategoriesEnum.AUTRE;
            default:
                return null;
        }
    }
}
