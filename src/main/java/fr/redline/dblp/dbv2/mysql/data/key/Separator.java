package fr.redline.dblp.dbv2.mysql.data.key;

public enum Separator {

    AND("AND"),
    OR("OR");

    final String sep;

    Separator(String sep){
        this.sep = sep;
    }

    public String getString(){
        return sep;
    }

}
