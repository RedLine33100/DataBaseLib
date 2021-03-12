package fr.redline.dblp.dbv2.mysql.cmd;

public enum Commands {

    SELECT("SELECT"),
    DELETE("DELETE"),
    INSERT("INSERT"),
    DROP("DROP"),
    UPDATE("UPDATE");

    public final String name;
    Commands(String name){
        this.name = name;
    }

}
