package net.chrislehmann.common.sqlhelper;

public class Column {

    private String name;
    private Type type;
    private boolean nullable = true;
    private boolean primaryKey = false;

    public Column(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getCreateString() {
        String createText = name + " " + type.typeString;
        if (!nullable) {
            createText += " not null";
        }
        if( primaryKey )
        {
            createText += " primary key";
        }
        return createText;
    }

    public Column setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public Column setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    public String getName() {
        return name;
    }

    public enum Type {
        INTEGER("integer"), STRING("text");
        private String typeString;

        Type(String typeString) {
            this.typeString = typeString;
        }
    }

}
