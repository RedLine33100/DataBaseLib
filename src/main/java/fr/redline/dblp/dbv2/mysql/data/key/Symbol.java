package fr.redline.dblp.dbv2.mysql.data.key;

public enum Symbol {

    SUP_AS(">"),
    SUP_EQUALS_AS(">="),
    EQUALS("="),
    INF_AS("<"),
    INF_EQUALS_AS("<=");

    final String symbol;

    Symbol(String symbol){
        this.symbol = symbol;
    }

    public String getString(){
        return symbol;
    }

}
