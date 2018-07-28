package com.brijir.averias.bd;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class Usuario {

    public Usuario(){}

    @DatabaseField(generatedId = true, columnName = "Id", canBeNull = false)
    public int Id;

    @DatabaseField(columnName = "cedula", canBeNull = false)
    public String cedula;

    @DatabaseField(columnName = "nombre", canBeNull = false)
    public String nombre;

    @DatabaseField(columnName = "correo", canBeNull = false)
    public String correo;

    @DatabaseField(columnName = "tel", canBeNull = false)
    public String tel;

    @DatabaseField(columnName = "UserName", canBeNull = false)
    public String UserName;

    @DatabaseField(columnName = "Password", canBeNull = false)
    public String Password;
}
