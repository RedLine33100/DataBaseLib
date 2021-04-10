package fr.redline.dblp.dbv2.mysql.cmd;

import com.sun.istack.internal.NotNull;

import java.util.List;

public class SQLCmd {

    private final List<String> colToGet;
    private final List<Object> values;
    private final String cmd;

    public SQLCmd(@NotNull final String cmd, @NotNull final List<String> colToGet, @NotNull final List<Object> values){
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
