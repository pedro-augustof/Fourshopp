package br.com.fourshopp.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Cargo {

    ADMINISTRADOR(1,"ADMINISTRADOR"),
    CHEFE_SECAO(2,"CHEFE DE SEÇÃO"),
    GERENTE(3,"GERENTE"),
    OPERADOR(4,"OPERADOR");

    private Integer cd;
    private String cargo;

    Cargo(int cd, String cargo) {
        this.cd = cd;
        this.cargo = cargo;
    }

    public int getCd() {
        return cd;
    }

    public String getCargo() {
        return cargo;
    }

    private static final Map<Integer, Cargo> lookup = new HashMap<>();
    static {
        for(Cargo x : EnumSet.allOf(Cargo.class))
            lookup.putIfAbsent(x.getCd(), x);
    }
    public static Cargo get(int key) {
        return lookup.get(key);

    }

}
