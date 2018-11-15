/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import net.daw.helper.EncodingHelper;

/**
 *
 * @author Jesus
 */
public class TipoproductoBean {
	@Expose
    private int id;
	@Expose
    private String desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public TipoproductoBean fill(ResultSet oResultSet, Connection connection) throws SQLException {
        this.setId(oResultSet.getInt("id"));
        this.setDesc(oResultSet.getString("desc"));
        return this;
    }

    public String getColumns() {
        String strColumns = "";
        strColumns += "id,";
        strColumns += "tipoproducto.desc";
        return strColumns;
    }
    
    public String getValues(){
        String strColumns="";
        strColumns += "null,";
        strColumns += EncodingHelper.quotate(desc);
        return strColumns;
    }
    
    public String getPairs(){
        String strPairs = "";
        strPairs += "id=" + id +",";
        strPairs += "tipoproducto.desc='" + desc + "'";
        strPairs += " WHERE id=" + id;
        return strPairs;
    }

}