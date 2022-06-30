package br.com.fourshopp.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Setor {

    MERCEARIA(1,"MERCEARIA"),
    BAZAR(2,"BAZAR"),
    ELETRONICOS(3,"ELETRONICOS"),
    COMERCIAL(4,"COMERCIAL");


    private Integer cd;
    private String setor;

    Setor(int cd, String setor) {
        this.cd = cd;
        this.setor = setor;
    }

    public int getCd() {
        return cd;
    }

    public String getSetor() {
        return setor;
    }

    private static final Map<Integer, Setor> lookup = new HashMap<>();
    static {
        for(Setor x : EnumSet.allOf(Setor.class))
            lookup.putIfAbsent(x.getCd(), x);
    }
    public static Setor get(int key) {
        return lookup.get(key);

    }
}
