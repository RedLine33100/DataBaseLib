package fr.redline.dblp.dbv2.mysql.cmd.builder;

import fr.redline.dblp.dbv2.mysql.Location;
import fr.redline.dblp.dbv2.mysql.cmd.Commands;
import fr.redline.dblp.dbv2.mysql.cmd.SQLCmd;
import fr.redline.dblp.dbv2.mysql.data.LinkedData;
import fr.redline.dblp.dbv2.mysql.inter.Command;
import fr.redline.dblp.dbv2.mysql.inter.SubCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InsertBuilder implements Command {

    final Collection<SubCommand> subCommands = new ArrayList<>();
    final List<Location> acceptedLocation = new ArrayList<Location>(){{
    }};

    final LinkedData linkedData;
    final String table;

    public InsertBuilder(String table, LinkedData linkedData){
        this.table = table;
        this.linkedData = linkedData;
    }

    @Override
    public boolean canReturnData() {
        return false;
    }

    @Override
    public SQLCmd getSqlCmd() {
        StringBuilder cmd = new StringBuilder();
        List<Object> objects = linkedData.getObjectList();
        cmd.append("INSERT").append(" ").append(table).append("(").append(linkedData.toCmdString(Commands.INSERT)).append(")").append(" ").append(" VALUES ").append("(");
        StringBuilder interro = null;
        for(int i = 0; i < objects.size(); i++){
            if(interro == null) interro = new StringBuilder();
            else interro.append(",\n");
            interro.append("?");
        }
        cmd.append(interro).append(")");
        return new SQLCmd(cmd.toString(), objects);
    }

    @Override
    public boolean addSubCommand(SubCommand command) {
        for(Location location : command.getAvailableLocation()){
            if(acceptedLocation.contains(location)){
                subCommands.add(command);
                return true;
            }
        }
        return false;
    }
}
