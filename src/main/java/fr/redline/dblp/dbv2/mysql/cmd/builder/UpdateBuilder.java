package fr.redline.dblp.dbv2.mysql.cmd.builder;

import fr.redline.dblp.dbv2.mysql.Location;
import fr.redline.dblp.dbv2.mysql.cmd.Commands;
import fr.redline.dblp.dbv2.mysql.cmd.SQLCmd;
import fr.redline.dblp.dbv2.mysql.data.LinkedData;
import fr.redline.dblp.dbv2.mysql.data.key.Keys;
import fr.redline.dblp.dbv2.mysql.inter.Command;
import fr.redline.dblp.dbv2.mysql.inter.SubCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpdateBuilder implements Command {

    final LinkedData linkData;
    final Keys keys;
    final String table;
    final Collection<SubCommand> subCommands = new ArrayList<>();
    final List<Location> acceptedLocation = new ArrayList<Location>(){{
        add(Location.BEFORE_WHERE);
        add(Location.AFTER_WHERE);
    }};

    public UpdateBuilder(String table, LinkedData linkData, Keys keys){
        this.table = table;
        this.linkData = linkData;
        this.keys = keys;
    }

    @Override
    public boolean canReturnData() {
        return false;
    }

    @Override
    public SQLCmd getSqlCmd() {
        StringBuilder cmd = new StringBuilder();
        cmd.append("UPDATE").append(" ").append(table).append(" ").append("SET").append("\n");
        cmd.append(linkData.toCmdString(Commands.UPDATE));
        List<Object> objects = new ArrayList<>(linkData.getObjectList());
        objects.addAll(appendSubCommand(cmd, Location.BEFORE_WHERE));
        if (keys != null) {
            cmd.append(keys.toWhereString());
            objects.addAll(keys.getObjectList());
        }
        objects.addAll(appendSubCommand(cmd, Location.AFTER_WHERE));
        return new SQLCmd(cmd.toString(), objects);
    }

    public List<Object> appendSubCommand(StringBuilder stringBuilder, Location location) {

        List<Object> objects = new ArrayList<>();
        boolean first = true;

        if (!subCommands.isEmpty())

            for (SubCommand subCommand : subCommands) {

                if (subCommand.acceptLocation(location)) {
                    if (subCommand.needComa(location) && !first)
                        stringBuilder.append(",\n");
                    else
                        stringBuilder.append(" ");
                    if (first)
                        first = false;
                    stringBuilder.append(subCommand.getCmdString());
                    if (subCommand.needAppendValue())
                        objects.addAll(subCommand.getValueToAppend());
                    subCommand.addedAt(location);
                }

            }

        return objects;
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
