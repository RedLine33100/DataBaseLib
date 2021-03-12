package fr.redline.dblp.dbv2.mysql.cmd;

import java.util.List;

public class SQLCmd {

    private final List<String> colToGet;
    private final List<Object> values;
    private final String cmd;

    public SQLCmd(String cmd, List<String> colToGet, List<Object> values){
        this.cmd = cmd;
        this.colToGet = colToGet;
        this.values = values;
    }

    public final String getCmd(){
        return cmd;
    }

    public final List<Object> getValues(){
        return values;
    }

    public final List<String> getColList(){
        return colToGet;
    }

}
