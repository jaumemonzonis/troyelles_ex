/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.service;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.Request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.daw.bean.CarritoBean;
import net.daw.bean.FacturaBean;
import net.daw.bean.ProductoBean;
import net.daw.bean.ReplyBean;
import net.daw.bean.UsuarioBean;
import net.daw.connection.publicinterface.ConnectionInterface;
import net.daw.constant.ConnectionConstants;
import net.daw.dao.FacturaDao;
import net.daw.dao.ProductoDao;
import net.daw.dao.UsuarioDao;
import net.daw.factory.ConnectionFactory;

public class CarritoService implements Serializable{

	 HttpServletRequest oRequest;
	 String ob = null;
   

    public CarritoService(HttpServletRequest oRequest) {
        super();
        this.oRequest = oRequest;
        ob = oRequest.getParameter("ob");
    }

    public ReplyBean add() throws Exception {
        ReplyBean oReplyBean;
        ConnectionInterface oConnectionPool = null;
        Connection oConnection;
        ArrayList<CarritoBean> alCarritoBean = new ArrayList<CarritoBean>();
        CarritoBean oCarritoBean = new CarritoBean();
        Gson oGson = new Gson();
        
            try {

                oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
                oConnection = oConnectionPool.newConnection();
                 
            	Integer prod = Integer.parseInt(oRequest.getParameter("prod"));
            	Integer cantidad = 1;
                
            	//creo producto
            	ob = "producto";
                ProductoDao oProductoDao = new ProductoDao(oConnection,ob);
                ProductoBean oProductoBean = oProductoDao.get(prod, 1);
                
                //inicio la session
                HttpSession session = oRequest.getSession();
                //creo un arraylist en la sesion
                ArrayList<CarritoBean> alProdSession = (ArrayList<CarritoBean>) session.getAttribute("prod");
                //validamos si existen existencias
                if (alProdSession != null) {
                    for (CarritoBean o : alProdSession) {
                        if (oProductoBean.getId() == o.getObj_producto().getId()) {
                        	 cantidad = o.getCantidad();
                    		 cantidad++;
                    		 
                        } else {
                            alCarritoBean.add(o);
                        }
                    }
                }
                //añadimos valores a carritobean
                oCarritoBean.setCantidad(cantidad);
                oCarritoBean.setObj_producto(oProductoBean);
                
                //añadimos el bean carrito al array
                alCarritoBean.add(oCarritoBean);

                //lo pasamos a la sesion
                oRequest.getSession().setAttribute("prod", alCarritoBean);
             
            } catch (Exception ex) {
                throw new Exception("ERROR: Service level: add method: " + ob + " object", ex);
            } finally {
                oConnectionPool.disposeConnection();
            }
       
            return new ReplyBean(200, oGson.toJson(oRequest.getSession()));
    }

    public ReplyBean show() throws Exception {
        Gson oGson = new Gson();
        return new ReplyBean(200, oGson.toJson(oRequest.getSession().getAttribute("prod")));
}
 
    public ReplyBean empty() throws Exception {
        Gson oGson = new Gson();
        oRequest.getSession().setAttribute("prod", null);
        return new ReplyBean(200, oGson.toJson(oRequest.getSession()));
}
   
    public ReplyBean reduce() throws Exception {
        ReplyBean oReplyBean;
        ArrayList<CarritoBean> alCarritoBean = new ArrayList<CarritoBean>();
        Gson oGson = new Gson();
        Integer prod = Integer.parseInt(oRequest.getParameter("prod"));
            	
        //me traigo el arraylist en la sesion
        ArrayList<CarritoBean> alProdSession = (ArrayList<CarritoBean>) oRequest.getSession().getAttribute("prod");
                
        //validamos si existen existencias
                if (alProdSession != null) {
                    for (CarritoBean o : alProdSession) {
                        if (prod == o.getObj_producto().getId()) {
                        		 Integer cantidad = o.getCantidad();
                        		 cantidad--;
                        		 o.setCantidad(cantidad);
                        		 alCarritoBean.add(o); 
                        } else {
                        	 alCarritoBean.add(o);
                        }
                    }
                }

                //lo pasamos a la sesion
                oRequest.getSession().setAttribute("prod", alCarritoBean);

            return new ReplyBean(200, oGson.toJson(oRequest.getSession()));
    } 
    
    
    public ReplyBean buy() throws Exception {
    	   ReplyBean oReplyBean;
           ConnectionInterface oConnectionPool = null;
           Connection oConnection;
           ArrayList<CarritoBean> alCarritoBean = new ArrayList<CarritoBean>();
           CarritoBean oCarritoBean = new CarritoBean();
           Gson oGson = new Gson();
    	
           try {

               oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
               oConnection = oConnectionPool.newConnection();
  
           	//creo factura
           	   ob = "factura";
           	   UsuarioBean oUsuarioBean = (UsuarioBean) oRequest.getSession().getAttribute("user");
           	   Date fecha = new Date();
           	   FacturaBean oFacturaBean= new FacturaBean();
           	   oFacturaBean.setFecha(fecha);
           	   oFacturaBean.setIva(21);
           	   oFacturaBean.setObj_usuario(oUsuarioBean);

               FacturaDao oFacturaDao = new FacturaDao(oConnection,ob);
               oFacturaDao.create(oFacturaBean);
               
              // obtener productos del array
               //compruebo si existen
               		//meto lineas
               		// resto existencias
               //creo factura
               
              
               

            
           } catch (Exception ex) {
               throw new Exception("ERROR: Service level: buy method: " + ob + " object", ex);
           } finally {
               oConnectionPool.disposeConnection();
           }
      
           return new ReplyBean(200, oGson.toJson(oRequest.getSession()));
    }
}
