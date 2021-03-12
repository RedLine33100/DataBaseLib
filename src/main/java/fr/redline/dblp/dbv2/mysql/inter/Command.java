package fr.redline.dblp.dbv2.mysql.inter;

import fr.redline.dblp.dbv2.mysql.cmd.SQLCmd;

public interface Command {
    SQLCmd getSqlCmd();
    boolean addSubCommand(SubCommand command);
    boolean canReturnData();
}
