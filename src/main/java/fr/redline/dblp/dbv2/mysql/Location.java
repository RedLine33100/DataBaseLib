package fr.redline.dblp.dbv2.mysql;

public enum Location {

    AFTER_CMD("AFTER_CMD"),
    AFTER_SELECT_COLUMNS("AFTER_SELECT_COLUMNLIST"),
    BEFORE_FROM_WORD("BEFORE_FROM_WORD"),
    BEFORE_FROM_TABLE("BEFORE_FROM_TABLE"),
    AFTER_FROM_TABLE("AFTER_FROM_TABLE"),
    BEFORE_WHERE("BEFORE_WHERE"),
    AFTER_WHERE("AFTER_WHERE"),
    CMD_END("CMD_END");

    final String name;
    Location(String name){
        this.name = name;
    }

}
