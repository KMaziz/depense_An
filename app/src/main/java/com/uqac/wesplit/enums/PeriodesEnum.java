package com.uqac.wesplit.enums;

/**
 Enum représentant les périodes de recherche possibles
 */

public enum PeriodesEnum {

    SEPTJOURS ("7 derniers jours "),
    QUINZEJOURS ("15 derniers jours"),
    TRENTEJOURS ("30 derniers jours"),
    TOUT ("Tout prendre en compte"),
    OneJour("aujourd'hui");

    private String periode = "";

    PeriodesEnum(String periode){
        this.periode = periode;
    }

    public String toString(){
        return periode;
    }

}
