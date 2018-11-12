package net.daw.bean;

import com.google.gson.annotations.Expose;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TipousuarioBean {
	
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
        
        public TipousuarioBean fill(ResultSet oResultSet, Connection oConnection, Integer expand) throws Exception{
           this.setId(oResultSet.getInt("id")) ;
           this.setDesc(oResultSet.getString("desc"));
            return this;
        }

}
