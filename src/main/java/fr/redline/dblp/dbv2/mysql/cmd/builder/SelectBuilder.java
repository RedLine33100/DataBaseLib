package fr.redline.dblp.dbv2.mysql.cmd.builder;

import fr.redline.dblp.dbv2.mysql.cmd.Commands;
import fr.redline.dblp.dbv2.mysql.cmd.SQLCmd;
import fr.redline.dblp.dbv2.mysql.data.ColonList;
import fr.redline.dblp.dbv2.mysql.Location;
import fr.redline.dblp.dbv2.mysql.inter.Command;
import fr.redline.dblp.dbv2.mysql.inter.SubCommand;
import fr.redline.dblp.dbv2.mysql.data.key.Keys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SelectBuilder implements Command {

    final ColonList colonList;
    final String table;
    final Keys keys;
    final Collection<SubCommand> subCommands = new ArrayList<>();
    final List<Location> acceptedLocation = new ArrayList<Location>(){{
        add(Location.AFTER_SELECT_COLUMNS);
        add(Location.BEFORE_FROM_WORD);
        add(Location.BEFORE_FROM_TABLE);
        add(Location.AFTER_FROM_TABLE);
        add(Location.BEFORE_WHERE);
        add(Location.AFTER_WHERE);
        add(Location.CMD_END);
    }};

    public SelectBuilder(String table, ColonList colonList){
        this.table = table;
        this.colonList = colonList;
        this.keys = null;
    }

    public SelectBuilder(String table, ColonList colonList, Keys keys){
        this.table = table;
        this.colonList = colonList;
        this.keys = keys;
        this.acceptedLocation.add(Location.AFTER_WHERE);
    }

    @Override
    public boolean canReturnData() {
        return true;
    }

    @Override
    public SQLCmd getSqlCmd() {

        List<String> colSelect = new ArrayList<>(colonList.getColList());

        StringBuilder cmd = new StringBuilder();
        cmd.append("SELECT");

        List<Object> objects = new ArrayList<>(appendSubCommand(cmd, Location.AFTER_CMD));

        cmd.append(" ").append(this.colonList.toCmdString(Commands.SELECT));

        objects.addAll(appendSubCommand(cmd, Location.AFTER_SELECT_COLUMNS));

        objects.addAll(appendSubCommand(cmd, Location.BEFORE_FROM_WORD));

        cmd.append("\n").append("FROM");

        objects.addAll(appendSubCommand(cmd, Location.BEFORE_FROM_TABLE));

        cmd.append(" ").append(table);

        objects.addAll(appendSubCommand(cmd, Location.AFTER_FROM_TABLE));

        objects.addAll(appendSubCommand(cmd, Location.BEFORE_WHERE));

        if(keys != null){
            cmd.append(keys.toWhereString());
            objects.addAll(keys.getObjectList());
        }

        objects.addAll(appendSubCommand(cmd, Location.AFTER_WHERE));

        objects.addAll(appendSubCommand(cmd, Location.CMD_END));

        return new SQLCmd(cmd.toString(), colSelect, objects);

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
