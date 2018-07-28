package com.brijir.averias.bd;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User {

    public User(){}

    @DatabaseField(generatedId = true, columnName = "Id", canBeNull = false)
    public int Id;

    @DatabaseField(columnName = "UserId", canBeNull = false)
    public int UserId;

    @DatabaseField(columnName = "Name", canBeNull = false)
    public String Name;

    @DatabaseField(columnName = "Email", canBeNull = false)
    public String Email;

    @DatabaseField(columnName = "Phone", canBeNull = false)
    public int Phone;

    @DatabaseField(columnName = "UserName", canBeNull = false)
    public String UserName;

    @DatabaseField(columnName = "Password", canBeNull = false)
    public String Password;
}
