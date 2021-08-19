package fr.redline.dblp.dbv2.mysql;

import fr.redline.dblp.dbv2.mysql.data.LinkedData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommandUtils {

    public static String columnNameToString(List<String> columnName) {
        StringBuilder columnString= new StringBuilder();
        for(int i=0; i<=columnName.size()-1; i++) {
            if(columnString.length() == 0) columnString.append(columnName.get(i));
            else columnString.append(",\n").append(columnName.get(i));
        }
        return columnString.toString();
    }

    public static void setValues(PreparedStatement statement, List<Object> donneeList) {

        if(donneeList == null || donneeList.isEmpty()) return;

        for(int i=0; i<donneeList.size(); i++) {
            Object donnee = donneeList.get(i);
            try {
                statement.setObject(i, donnee);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    public static String getUpdateString(List<String> updateColumn) {
        StringBuilder columnString = null;

        for (String s : updateColumn) {

            if (columnString == null) columnString = new StringBuilder(s+" = ?");
            else columnString.append(", ").append(s).append(" = ?");

        }

        if(columnString == null) return null;

        return columnString.toString();
    }

    public static String interrogationString(List<Object> donnee) {

        StringBuilder interrogation= null;
        for(int i=0; i<=donnee.size()-1; i++) {
            if(interrogation == null) interrogation = new StringBuilder("?");
            else interrogation.append(",?");
        }

        if(interrogation == null) return null;
        return interrogation.toString();
    }

    public static List<LinkedData> convertResultToObjects(ResultSet result){
        List<LinkedData> list = new ArrayList<>();

        try {
            while(result.next())
                list.add(convertResultToObjectWN(result));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static LinkedData convertResultToObject(ResultSet result){
        try {
            result.next();
            return convertResultToObjectWN(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static LinkedData convertResultToObjectWN(ResultSet result){
        LinkedData list = new LinkedData();

        try {
            for(int i = 0; i<result.getMetaData().getColumnCount(); i++) {
                String colonne = result.getMetaData().getColumnName(i);
                list.addObject(colonne, result.getObject(colonne));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
