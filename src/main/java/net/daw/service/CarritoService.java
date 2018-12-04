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
            ProductoBean oProductoBean = oProductoDao.get(id, 2);

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
    	if (checkPermission("buy")) {
           ConnectionInterface oConnectionPool = null;
           Connection oConnection;
           
          
           Gson oGson = new Gson();
           HttpSession sesion = oRequest.getSession();
           
           boolean validarExistencias = true;
           
          //UsuarioBean oUsuarioBean = (UsuarioBean) oRequest.getSession().getAttribute("user");
           try {

               oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
               oConnection = oConnectionPool.newConnection(); 
              
               //Iniciar transaccion abierta
               oConnection.setAutoCommit(false);
               	carrito = (ArrayList<CarritoBean>) oRequest.getSession().getAttribute("carrito");
               int id = ((UsuarioBean) sesion.getAttribute("user")).getId();
           		//creo factura

           	   FacturaDao oFacturaDao = new FacturaDao(oConnection,"factura");
           	   FacturaBean oFacturaBean= new FacturaBean();
           	   int id_factura = oFacturaBean.getId();
           	   Date fecha = new Date();
           	   oFacturaBean.setId(id_factura);
           	   oFacturaBean.setFecha(fecha);
           	   oFacturaBean.setIva(21);
           	   oFacturaBean.setId_usuario(id);
           	   //oFacturaBean.setId_usuario(oUsuarioBean.getId());
               oFacturaDao.create(oFacturaBean);
               
              // obtener productos del array
              
               LineaBean oLineaBean = new LineaBean();
               LineaDao oLineaDao = new LineaDao(oConnection, "linea");
               ProductoBean oProductoBean;
               ProductoDao oProductoDao = new ProductoDao(oConnection, "producto");
               
              //compruebo existencias

               for (CarritoBean o : carrito) {
            	   if (o.getCantidad() <= o.getObj_producto().getExistencias()) {
            		   
            		   int cant=o.getCantidad();
      
            		   oLineaBean.setId_factura(oFacturaBean.getId());
                       oLineaBean.setCantidad(o.getCantidad());
                       oProductoBean = oProductoDao.get(o.getObj_producto().getId(), 1);
                       oProductoBean.setId(o.getObj_producto().getId());
                       oProductoBean.setExistencias(o.getObj_producto().getExistencias()-cant);
                       oProductoDao.update(oProductoBean);
                       oLineaBean.setId_producto(o.getObj_producto().getId());
                       oLineaDao.create(oLineaBean);
                       
                   } else {
                       validarExistencias = false;
                       break;
                   }
               	}
               if (validarExistencias==true) {
                   oConnection.commit();
                   oReplyBean = new ReplyBean(200, oGson.toJson(oRequest.getSession().getAttribute("carrito")));
               } else {
                   oConnection.rollback();
                   oReplyBean = new ReplyBean(400, "No hay stock.");
}
                      
           } catch (Exception ex) {
               throw new Exception("ERROR: Service level: buy method: " + ob + " object", ex);
           } finally {
               oConnectionPool.disposeConnection();
           }
        
        } else {
            oReplyBean = new ReplyBean(401, "Unauthorized");
        }
    	   return new ReplyBean(200, oGson.toJson(oRequest.getSession()));
}
}