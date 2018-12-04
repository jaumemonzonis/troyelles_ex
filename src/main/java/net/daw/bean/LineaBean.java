
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.bean;

import com.google.gson.annotations.Expose;
import java.sql.Connection;
import java.sql.ResultSet;
import net.daw.dao.FacturaDao;
import net.daw.dao.ProductoDao;

public class LineaBean {

	  //@Expose
    private int id;
    //@Expose
    private int cantidad;
    // @Expose(serialize = false)
    private int id_producto;
    // @Expose(serialize = false)
    private int id_factura;
    //@Expose(deserialize = false)
    private ProductoBean obj_Producto;
    // @Expose(deserialize = false)
    private FacturaBean obj_Factura;
    // @Expose(deserialize = false)
    public ProductoBean getObj_Producto() {
        return obj_Producto;
    }

    public void setObj_Producto(ProductoBean obj_Producto) {
        this.obj_Producto = obj_Producto;
    }

    public FacturaBean getObj_Factura() {
        return obj_Factura;
    }

    public void setObj_Factura(FacturaBean obj_Factura) {
        this.obj_Factura = obj_Factura;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public LineaBean fill(ResultSet oResultSet, Connection oConnection, Integer expand) throws Exception {

        this.setId(oResultSet.getInt("id"));
        this.setCantidad(oResultSet.getInt("cantidad"));

        if (expand > 0) {
            ProductoDao oProductoDao = new ProductoDao(oConnection, "producto");
            this.setObj_Producto(oProductoDao.get(oResultSet.getInt("id_producto"), expand - 1));
        } else {
            this.setId_producto(oResultSet.getInt("id_producto"));
        }
        if (expand > 0) {
            FacturaDao oFacturaDao = new FacturaDao(oConnection, "factura");
            this.setObj_Factura(oFacturaDao.get(oResultSet.getInt("id_factura"), expand - 1));
        } else {
            this.setId_factura(oResultSet.getInt("id_factura"));
        }

        return this;
    }

}