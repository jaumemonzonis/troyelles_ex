/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.google.gson.annotations.Expose;

import net.daw.dao.LineaDao;
import net.daw.dao.UsuarioDao;
import net.daw.helper.EncodingHelper;


/**
 *
 * @author a044531896d
 */
public class CarritoBean {
	
private ProductoBean obj_producto;
private Integer cantidad;

	

public ProductoBean getObj_producto() {
		return obj_producto;
	}
public void setObj_producto(ProductoBean obj_producto) {
		this.obj_producto = obj_producto;
	}
public Integer getCantidad() {
		return cantidad;
	}
public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}


	
	
	

}