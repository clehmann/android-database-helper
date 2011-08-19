package net.chrislehmann.common.sqlhelper;

public class IntegerColumn extends Column{


    private boolean autoIncrement = false;

    public IntegerColumn(String name) {
        super(name, Type.INTEGER);
    }

    public IntegerColumn setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
        return this;
    }

    @Override
    public String getCreateString() {
        String createString = super.getCreateString();
        if( autoIncrement )
        {
            createString += " autoincrement";
        }
        return createString;
    }
}
