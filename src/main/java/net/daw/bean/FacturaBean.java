/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Date;

import net.daw.dao.UsuarioDao;


/**
 *
 * @author a044531896d
 */
public class FacturaBean {

    private int id;
    private Date fecha;
    private double iva;
    private UsuarioBean obj_usuario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public UsuarioBean getObj_usuario() {
        return obj_usuario;
    }

    public void setObj_usuario(UsuarioBean obj_usuario) {
        this.obj_usuario = obj_usuario;
    }

    public FacturaBean fill(ResultSet oResultSet, Connection oConnection, Integer expand) throws SQLException, Exception{
        this.setId(oResultSet.getInt("id"));
        this.setFecha(oResultSet.getDate("fecha"));
        this.setIva(oResultSet.getDouble("iva"));
        if(expand > 0){
            UsuarioDao oUsuarioDao = new UsuarioDao(oConnection, "usuario");
            this.setObj_usuario(oUsuarioDao.get(oResultSet.getInt("id_usuario"), expand));
            System.out.println(obj_usuario.getId());
        }
        return this;
    }
    
    public String getColumns(){
        String strColumns = "";
        strColumns += "id,";
        strColumns += "fecha,";
        strColumns += "iva,";
        strColumns += "id_usuario";
        return strColumns;
    }
    
    public String getValues(){
        String strColumns = "";
        strColumns += "null,";
        strColumns += fecha + ",";
        strColumns += iva + ",";
        strColumns += getObj_usuario().getId() + ",";
        return strColumns;
    }
    
    public String getPairs(){
        String strPairs ="";
        strPairs += "id=" + id + ",";
        strPairs += "fecha=" + fecha + ",";
        strPairs += "iva=" + iva + ",";
        strPairs += "id_usuario=" + obj_usuario.getId();
        strPairs += " WHERE id=" + id;
        return strPairs;
    }
    

}