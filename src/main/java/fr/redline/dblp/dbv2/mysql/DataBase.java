package fr.redline.dblp.dbv2.mysql;

import fr.redline.dblp.dbv2.mysql.cmd.SQLCmd;
import fr.redline.dblp.dbv2.mysql.cmd.builder.InsertBuilder;
import fr.redline.dblp.dbv2.mysql.cmd.builder.SelectBuilder;
import fr.redline.dblp.dbv2.mysql.data.LinkedData;
import fr.redline.dblp.dbv2.mysql.data.key.Keys;
import fr.redline.dblp.dbv2.mysql.inter.Command;
import fr.redline.dblp.global.AsyncGestion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class DataBase {

    private AsyncGestion asyncGestion = new AsyncGestion();
    private Connection connection;
    private String baseAddress = "jdbc:mysql://", ip, baseName, username, mdp;
    private final static ArrayList<DataBase> dataBaseFile = new ArrayList<>();

    public DataBase(String host, String database, String username, String mdp) {
        this.ip = host;
        this.baseName = database;
        this.username = username;
        this.mdp = mdp;
        dataBaseFile.add(this);
    }

    public String getDataBase() { return baseName; }
    public String getUserName() { return username; }
    public Connection getConnection() {return connection;}

    /// Connecting Part

    public static void disconnectAll() {
        if(!dataBaseFile.isEmpty()) {
            for(DataBase base : new ArrayList<>(dataBaseFile)) {
                try {
                    base.disconnect();
                } catch (SQLException ignored) {
                }

                dataBaseFile.remove(base);
            }
        }
    }

    public boolean connect() throws SQLException{
        if(isConnected()) return false;
        connection = DriverManager.getConnection(this.baseAddress+this.ip+"/"+this.baseName+"?user="+this.username+"&password="+this.mdp); /// removed: "&useUnicode=true&characterEncoding=UTF-8"
        return true;
    }

    public void disconnect()throws SQLException{
        if(!isConnected()) return;
        connection.close();
        connection = null;
    }

    ///Connecter ou non
    public boolean isConnected() {
        return connection != null;
    }


    ///Changing connection information part

    public void setIp(String ip) {

        try {
            disconnect();
        } catch (SQLException ignored) {
        }

        this.ip = ip;

    }

    public void setAddress(String baseAddress) {

        try {
            disconnect();
        } catch (SQLException ignored) {
        }

        this.baseAddress = baseAddress;

    }

    public void setBaseName(String baseName) {

        try {
            disconnect();
        } catch (SQLException ignored) {
        }

        this.baseName = baseName;

    }

    public void setUser(String user) {

        try {
            disconnect();
        } catch (SQLException ignored) {
        }

        this.username = user;

    }

    public void setMdp(String mdp) {

        try {
            disconnect();
        } catch (SQLException ignored) {
        }

        this.mdp = mdp;

    }

    /// Cmd part

    public boolean tryQuery(Command command, boolean async){
        if(!isConnected()) return false;

        AtomicBoolean result = new AtomicBoolean(false);

        Runnable runnable = () -> {

            try {

                SQLCmd sqlCmd = command.getSqlCmd();

                PreparedStatement statement = connection.prepareStatement(sqlCmd.getCmd());
                CommandUtils.setValues(statement, sqlCmd.getValues());
                statement.close();

            } catch (SQLException ignored) {
            }

        };

        if(!async) runnable.run();
        else asyncGestion.execute(runnable);

        return result.get();
    }

    public boolean tryQuery(String command, List<Object> values, boolean async){
        if(!isConnected()) return false;

        AtomicBoolean result = new AtomicBoolean(false);

        Runnable runnable = () -> {

            try {

                PreparedStatement statement = connection.prepareStatement(command);
                CommandUtils.setValues(statement, values);
                result.set(statement.execute());
                statement.close();

            }catch (SQLException ignored) {

            }

        };

        if(async) asyncGestion.execute(runnable);
        else runnable.run();

        return result.get();
    }

    public List<LinkedData> getDatas(Command command, boolean async){

        if(!isConnected()) return null;

        if(!command.canReturnData()) return null;

        AtomicReference<List<LinkedData>> objectList = new AtomicReference<>();

        Runnable runnable = () -> {

            try {

                SQLCmd sqlCmd = command.getSqlCmd();

                PreparedStatement state = getConnection().prepareStatement(sqlCmd.getCmd());
                CommandUtils.setValues(state, sqlCmd.getValues());
                objectList.set(CommandUtils.convertResultToObjects(state.executeQuery(), sqlCmd.getColList()));

                state.close();

            } catch (SQLException ignored) {

            }

        };

        if(async) asyncGestion.execute(runnable);
        else runnable.run();

        return objectList.get();
    }

    public List<LinkedData> getDatas(String command, List<Object> values, List<String> col, boolean async){

        if(!isConnected()) return null;

        AtomicReference<List<LinkedData>> objectList = new AtomicReference<>();

        Runnable runnable = () -> {

            try {

                PreparedStatement state = getConnection().prepareStatement(command);
                CommandUtils.setValues(state, values);
                objectList.set(CommandUtils.convertResultToObjects(state.executeQuery(), col));

                state.close();

            } catch (SQLException ignored) {

            }

        };

        if(async) asyncGestion.execute(runnable);
        else runnable.run();

        return objectList.get();

    }

    public LinkedData getData(Command command, boolean async){

        if(!isConnected()) return null;

        if(!command.canReturnData()) return null;

        AtomicReference<LinkedData> objectList = new AtomicReference<>();

        Runnable runnable = () -> {

            try {

                SQLCmd sqlCmd = command.getSqlCmd();

                PreparedStatement state = getConnection().prepareStatement(sqlCmd.getCmd());
                CommandUtils.setValues(state, sqlCmd.getValues());
                objectList.set(CommandUtils.convertResultToObject(state.executeQuery(), sqlCmd.getColList()));

                state.close();

            } catch (SQLException ignored) {

            }

        };

        if(async) asyncGestion.execute(runnable);
        else runnable.run();

        return objectList.get();

    }

    public LinkedData getData(String command, List<Object> values, List<String> col, boolean async) throws SQLException {

        if(!isConnected()) return null;

        AtomicReference<LinkedData> objectList = new AtomicReference<>();

        Runnable runnable = () -> {

            try {
                PreparedStatement state = getConnection().prepareStatement(command);
                CommandUtils.setValues(state, values);
                objectList.set(CommandUtils.convertResultToObject(state.executeQuery(), col));

                state.close();
            } catch (SQLException ignored) {

            }

        };

        if(async) asyncGestion.execute(runnable);
        else runnable.run();

        return objectList.get();

    }

    public Object getOrInsert(String table, String colon, Object data, Keys keys, boolean async) throws SQLException {

        AtomicReference<Object> object = new AtomicReference<>();

        if(!isConnected()) return null;

        Runnable runnable = () -> {

            try {

                PreparedStatement state = getConnection().prepareStatement("SELECT " + colon + " FROM " + table + keys.toWhereString());
                ResultSet result = state.executeQuery();

                if (result.next()) {
                    object.set(result.getObject(colon));
                }

                state.close();

            } catch (SQLException ignored) {

            }

        };

        if(async) asyncGestion.execute(runnable);
        else runnable.run();

        if(object.get() != null)
            return object.get();

        if(tryQuery( new InsertBuilder(table, new LinkedData().addObject(colon, data)), async)){
            return data;
        }

        return null;
    }

    public boolean containsData(SelectBuilder command, boolean async) throws SQLException {

        if(!isConnected()) return false;

        if(!command.canReturnData()) return false;

        AtomicBoolean resultBoolean = new AtomicBoolean(false);

        Runnable runnable = () -> {

            try {

                SQLCmd sqlCmd = command.getSqlCmd();

                PreparedStatement state = getConnection().prepareStatement(sqlCmd.getCmd());
                CommandUtils.setValues(state, sqlCmd.getValues());
                ResultSet result = state.executeQuery();
                resultBoolean.set(result.next());
                state.close();

            }catch(SQLException ignored){

            }

        };

        if(async) asyncGestion.execute(runnable);
        else runnable.run();

        return resultBoolean.get();

    }

}

