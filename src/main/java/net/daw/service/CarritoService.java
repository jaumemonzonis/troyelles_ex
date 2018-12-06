/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.Request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.daw.bean.CarritoBean;
import net.daw.bean.FacturaBean;
import net.daw.bean.LineaBean;
import net.daw.bean.ProductoBean;
import net.daw.bean.ReplyBean;
import net.daw.bean.UsuarioBean;
import net.daw.connection.publicinterface.ConnectionInterface;
import net.daw.constant.ConnectionConstants;
import net.daw.dao.FacturaDao;
import net.daw.dao.LineaDao;
import net.daw.dao.ProductoDao;
import net.daw.dao.UsuarioDao;
import net.daw.factory.ConnectionFactory;
import net.daw.helper.EncodingHelper;

public class CarritoService implements Serializable{

	  HttpServletRequest oRequest;
	    String ob = null;
	    Gson oGson = new Gson();
	   
	ArrayList<CarritoBean> carrito = null;

    public CarritoService(HttpServletRequest oRequest) {
        super();
        this.oRequest = oRequest;
        ob = oRequest.getParameter("ob");
    }
    
    protected Boolean checkPermission(String strMethodName) {
        UsuarioBean oUsuarioBean = (UsuarioBean) oRequest.getSession().getAttribute("user");
        return oUsuarioBean != null;
}
    
    
    public ReplyBean add() throws Exception {
    	 ReplyBean oReplyBean;
            
        if (checkPermission("add")) {
        	
        ConnectionInterface oConnectionPool = null;
        HttpSession sesion = oRequest.getSession();	
        	
        try {
            Connection oConnection;

            //Si no existe la sesion creamos al carrito
            if (sesion.getAttribute("carrito") == null) {
                carrito = new ArrayList<CarritoBean>();
            } else {
                carrito = (ArrayList<CarritoBean>) sesion.getAttribute("carrito");
            }

            //Obtenemos el producto que deseamos a�adir al carrito
            Integer id = Integer.parseInt(oRequest.getParameter("prod"));
            oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
            oConnection = oConnectionPool.newConnection();
            ProductoDao oProductoDao = new ProductoDao(oConnection, "producto");
            ProductoBean oProductoBean = oProductoDao.get(id, 1);

            //Para saber si tenemos agregado el producto al carrito de compras
            int indice = -1;
            //recorremos todo el carrito de compras
            for (int i = 0; i < carrito.size(); i++) {
                if (oProductoBean.getId() == carrito.get(i).getObj_producto().getId()) {
                    //Si el producto ya esta en el carrito, obtengo el indice dentro
                    //del arreglo para actualizar al carrito de compras
                    indice = i;
                    break;
                }
            }
            CarritoBean oCarritoBean = new CarritoBean();
            if (indice == -1) {
                //Si es -1 es porque voy a registrar
                if (oProductoBean.getExistencias() > 0) {
                    oCarritoBean.setObj_producto(oProductoBean);
                    oCarritoBean.setCantidad(1);
                    carrito.add(oCarritoBean);
                }
            } else {
                //Si es otro valor es porque el producto esta en el carrito
                //y vamos actualizar la cantidad
                Integer cantidad = carrito.get(indice).getCantidad() + 1;
                if (oProductoBean.getExistencias() >= cantidad) {
                    carrito.get(indice).setCantidad(cantidad);
                }
            }
            //Actualizamos la sesion del carrito de compras
            sesion.setAttribute("carrito", carrito);

            oReplyBean = new ReplyBean(200, oGson.toJson(carrito));

        } catch (Exception ex) {
           // Logger.getLogger(CarritoService.class.getName()).log(Level.SEVERE, null, ex);
            oReplyBean = new ReplyBean(500, "Error en add carrito: " + ex.getMessage());
        }
        finally {
            oConnectionPool.disposeConnection();
        }
        
        } else {
        	oReplyBean = new ReplyBean(401, "Unauthorized");
        }	
        return oReplyBean;
    }

    public ReplyBean show() {
   ReplyBean oReplyBean;
    	if (checkPermission("reduce")) {
        HttpSession sesion = oRequest.getSession();

        if (sesion.getAttribute("carrito") == null) {
            oReplyBean = new ReplyBean(200, EncodingHelper.quotate("Carrito vacio"));
        } else {
            oReplyBean = new ReplyBean(200, oGson.toJson(sesion.getAttribute("carrito")));
        }

        } else {
            oReplyBean = new ReplyBean(401, "Unauthorized");
        }
        return oReplyBean;
}
    
    public ReplyBean empty() {
    	  ReplyBean oReplyBean;
    	if (checkPermission("empty")) {
        HttpSession sesion = oRequest.getSession();

        if (sesion.getAttribute("carrito") == null) {
            oReplyBean = new ReplyBean(200, EncodingHelper.quotate("El carrito ya esta vacio"));
        } else {
            sesion.setAttribute("carrito", null);
            oReplyBean = new ReplyBean(200, EncodingHelper.quotate("Carrito vacio"));
        }

        } else {
            oReplyBean = new ReplyBean(401, "Unauthorized");
        }
        return oReplyBean;
}

    public ReplyBean reduce() {
    	ReplyBean oReplyBean;
    	if (checkPermission("reduce")) {
 
        HttpSession sesion = oRequest.getSession();

        if (sesion.getAttribute("carrito") == null) {
            oReplyBean = new ReplyBean(200, EncodingHelper.quotate("No hay carrito"));
        } else {
            carrito = (ArrayList<CarritoBean>) sesion.getAttribute("carrito");
            Integer id = Integer.parseInt(oRequest.getParameter("prod"));
           
            
            int indice=-1;
            
            for (int i = 0; i < carrito.size(); i++) {
                if (id == carrito.get(i).getObj_producto().getId()) {
                    indice = i;
                    break;
                }
            }
            
            if (indice == -1) {
                oReplyBean = new ReplyBean(200, EncodingHelper.quotate("El producto no esta en el carrito"));
            } else {
                int cantidad = carrito.get(indice).getCantidad();
                if (carrito.get(indice).getCantidad()>1 ) {
                    carrito.get(indice).setCantidad(cantidad-1);
                    sesion.setAttribute("carrito", carrito);
                    oReplyBean = new ReplyBean(200, oGson.toJson(carrito));
                }else{
                   carrito.remove(indice);
                }
                   
                
                if (carrito.size()<1) {
                   	sesion.setAttribute("carrito", null);
                       oReplyBean = new ReplyBean(200, EncodingHelper.quotate("Carrito vacio"));
                       
                   } else {
                	   sesion.setAttribute("carrito", carrito);
                       oReplyBean = new ReplyBean(200, oGson.toJson(carrito));
                   }
                
            }
        }
        } else {
            oReplyBean = new ReplyBean(401, "Unauthorized");
        }
        return oReplyBean;
}   
    
    public ReplyBean buy() throws Exception {
    	 ReplyBean oReplyBean;
        ConnectionInterface oConnectionPool = null;
        //Obtenemos la sesion actual
        HttpSession sesion = oRequest.getSession();
        Connection oConnection = null;
        try {

            oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
            oConnection = oConnectionPool.newConnection();
            oConnection.setAutoCommit(false);
            int id = ((UsuarioBean) sesion.getAttribute("user")).getId();
            carrito = (ArrayList<CarritoBean>) sesion.getAttribute("carrito");

            FacturaBean oFacturaBean = new FacturaBean();
            Date fechaHoraAhora = new Date();
            oFacturaBean.setId_usuario(id);
            oFacturaBean.setFecha(fechaHoraAhora);
            oFacturaBean.setIva(21.0F);


            FacturaDao oFacturaDao = new FacturaDao(oConnection, "factura");

            FacturaBean oFacturaBeanCreada = oFacturaDao.create(oFacturaBean);
            int id_factura = oFacturaBeanCreada.getId();
      
            LineaDao oLineaDao;
            LineaBean oLineaBean;
            ProductoDao oProductoDao = new ProductoDao(oConnection, "producto");
            oLineaDao = new LineaDao(oConnection, "linea");
            ProductoBean oProductoBean;

            for (CarritoBean ib : carrito) {

             
                int cant = ib.getCantidad();

                oLineaBean = new LineaBean();

                oLineaBean.setId_factura(id_factura);
                oLineaBean.setId_producto(ib.getObj_producto().getId());
                oLineaBean.setCantidad(cant);

                oLineaDao.create(oLineaBean);

             
                oProductoBean = new ProductoBean();
                oProductoBean.setId(ib.getObj_producto().getId());
                oProductoBean = ib.getObj_producto();
                oProductoBean.setExistencias(oProductoBean.getExistencias() - cant);
                oProductoDao.update(oProductoBean);

            }

            oConnection.commit();

            carrito.clear();
            sesion.setAttribute("carrito", carrito);

            oReplyBean = new ReplyBean(200, EncodingHelper.quotate("Factura n� " + id_factura + " creada con �xito"));

        } catch (Exception e) {

            try {
                oConnection.rollback();
            } catch (SQLException excep) {

            }

            oReplyBean = new ReplyBean(500, "Error en buy carritoService: " + e.getMessage());
        } finally {
            oConnectionPool.disposeConnection();
        }

        return oReplyBean;

    }

}