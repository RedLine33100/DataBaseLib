package fr.redline.dblp.dbv2.mysql.data.key;

public class KeyData {

    final String colon;
    final Object value;
    final Separator separator;
    final Symbol symbol;
    final String name;

    public KeyData(String name, Separator separator, String colon, Symbol symbol, Object value){
        this.colon = colon;
        this.value = value;
        this.symbol = symbol;
        this.separator = separator;
        this.name = name;
    }

    protected KeyData(String name, String colon, Symbol symbol, Object value){
        this.colon = colon;
        this.value = value;
        this.symbol = symbol;
        this.separator = null;
        this.name = name;
    }

    public Separator getSeparator(){
        return separator;
    }

    public Symbol getSymbol(){
        return symbol;
    }

    public Object getValue(){
        if(value != null)
        return value;
        else return "null";
    }

    public String getColon() {
        return colon;
    }

    public String getKeyName(){
        return name;
    }

}
