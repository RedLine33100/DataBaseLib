package fr.redline.dblp.dbv2.mysql.data;

import fr.redline.dblp.dbv2.mysql.cmd.Commands;
import fr.redline.dblp.dbv2.mysql.inter.ColonCommand;

import java.util.ArrayList;
import java.util.List;

public class ColonList {

    public ColonList(ColonCommand colonCommand){
        addColon(colonCommand);
    }

    public ColonList(String colonCommand){
        addColon(colonCommand);
    }

    List<Object> colonDataCollection = new ArrayList<>();

    public void addColon(ColonCommand colonCommand){
        if(colonCommand.needValueName())
            colonDataCollection.add(colonCommand.getValueName());
        else
            colonDataCollection.add(colonCommand);
    }

    public void addColon(String colon){
        colonDataCollection.add(colon);
    }

    public String toCmdString(Commands commands){

        StringBuilder stringBuild = null;

        if(!colonDataCollection.isEmpty())

        switch(commands.name){
            case "SELECT":{
                for(Object colon : colonDataCollection){

                    if(stringBuild == null)
                        stringBuild = new StringBuilder();
                    else
                        stringBuild.append(",\n");

                    if(colon instanceof ColonCommand){
                        ColonCommand colonCommand = (ColonCommand) colon;
                        stringBuild.append(colonCommand.getCmdString()).append("(").append(colonCommand.getColon());
                        if(colonCommand.needValueName())
                            stringBuild.append(" AS ").append(colonCommand.getValueName());
                    }else stringBuild.append(colon);
                }
                break;
            }
        }

        if(stringBuild == null) stringBuild = new StringBuilder();
        return stringBuild.toString();

    }

    public List<String> getColList(){
        List<String> col = new ArrayList<>();
        if(!colonDataCollection.isEmpty())
            for(Object colon : colonDataCollection){
                if(colon instanceof ColonCommand) {
                    ColonCommand colonCommand = (ColonCommand) colon;
                    if(colonCommand.needValueName())
                        col.add(colonCommand.getValueName());
                    else
                        col.add(colonCommand.getColon());
                }else
                    col.add((String) colon);
            }
        return col;
    }

}
