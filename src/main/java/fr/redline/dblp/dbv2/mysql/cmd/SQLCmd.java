package fr.redline.dblp.dbv2.mysql.cmd;

import java.util.List;

public class SQLCmd {
    private final List<Object> values;
    private final String cmd;

    public SQLCmd(final String cmd, final List<Object> values){
        this.cmd = cmd;
        this.values = values;
    }

    public final String getCmd(){
        return cmd;
    }

    public final List<Object> getValues(){
        return values;
    }

}
