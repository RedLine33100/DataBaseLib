package fr.redline.dblp.dbv2.mysql.inter;

public interface ColonCommand {
    String getCmdString();
    String getValueName();
    String getColon();
    boolean needValueName();
}
