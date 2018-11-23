/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.daw.bean.FacturaBean;
import net.daw.bean.LineaBean;
import net.daw.bean.ReplyBean;
import net.daw.bean.TipousuarioBean;
import net.daw.bean.UsuarioBean;
import net.daw.connection.publicinterface.ConnectionInterface;
import net.daw.constant.ConnectionConstants;
import net.daw.dao.FacturaDao;
import net.daw.dao.LineaDao;
import net.daw.dao.TipousuarioDao;
import net.daw.factory.ConnectionFactory;
import net.daw.helper.EncodingHelper;
import net.daw.helper.ParameterCook;

public class LineaService {
    HttpServletRequest oRequest;
	String ob = null;

	public LineaService(HttpServletRequest oRequest) {
		super();
		this.oRequest = oRequest;
		ob = oRequest.getParameter("ob");
	}
    protected Boolean checkPermission(String strMethodName) {
        UsuarioBean oUsuarioBean = (UsuarioBean) oRequest.getSession().getAttribute("user");
        if (oUsuarioBean != null) {
            return true;
        } else {
            return false;
        }
}
	public ReplyBean get() throws Exception {
		ReplyBean oReplyBean;
		ConnectionInterface oConnectionPool = null;
		Connection oConnection;
		  if (this.checkPermission("get")) {
		try {
			Integer id = Integer.parseInt(oRequest.getParameter("id"));
			oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
			oConnection = oConnectionPool.newConnection();
			LineaDao oLineaDao = new LineaDao(oConnection, ob);
			LineaBean oLineaBean = oLineaDao.get(id, 2);
			Gson oGson = new Gson();
			oReplyBean = new ReplyBean(200, oGson.toJson(oLineaBean));
		} catch (Exception ex) {
			throw new Exception("ERROR: Service level: get method: " + ob + " object", ex);
        }
    } else {
        oReplyBean = new ReplyBean(401, "Unauthorized");
    }
    return oReplyBean;
}

	public ReplyBean remove() throws Exception {
		ReplyBean oReplyBean;
		ConnectionInterface oConnectionPool = null;
		Connection oConnection;
        if (this.checkPermission("remove")) {
		try {
			Integer id = Integer.parseInt(oRequest.getParameter("id"));
			oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
			oConnection = oConnectionPool.newConnection();
			LineaDao oLineaDao = new LineaDao(oConnection, ob);
			int iRes = oLineaDao.remove(id);
oReplyBean = new ReplyBean(200, Integer.toString(iRes));
		} catch (Exception ex) {
			throw new Exception("ERROR: Service level: remove method: " + ob + " object", ex);
		} finally {
			oConnectionPool.disposeConnection();
        }
    } else {
        oReplyBean = new ReplyBean(401, "Unauthorized");
    }
    return oReplyBean;
}

	public ReplyBean getcount() throws Exception {
		ReplyBean oReplyBean;
		ConnectionInterface oConnectionPool = null;
		Connection oConnection;
	       if (this.checkPermission("getcount")) {
		try {
			oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
			oConnection = oConnectionPool.newConnection();
			LineaDao oLineaDao = new LineaDao(oConnection, ob);
			int registros = oLineaDao.getcount();
			Gson oGson = new Gson();
oReplyBean = new ReplyBean(200, oGson.toJson(registros));
		} catch (Exception ex) {
			throw new Exception("ERROR: Service level: getcount method: " + ob + " object", ex);
		} finally {
			oConnectionPool.disposeConnection();
        }
    } else {
        oReplyBean = new ReplyBean(401, "Unauthorized");
    }
    return oReplyBean;
}
    public ReplyBean getcountxusuario() throws Exception {
        ReplyBean oReplyBean;
        ConnectionInterface oConnectionPool = null;
        Connection oConnection;
        if (this.checkPermission("getcountxusuario")) {
            try {
            	Integer id_factura = Integer.parseInt(oRequest.getParameter("idfactura"));
            	oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
    			oConnection = oConnectionPool.newConnection();
    			LineaDao oLineaDao = new LineaDao(oConnection, ob);
    			int registros = oLineaDao.getcountxlinea(id_factura);
    			Gson oGson = new Gson();
    			oReplyBean = new ReplyBean(200, oGson.toJson(registros));
            } catch (Exception ex) {
                throw new Exception("ERROR: Service level: getcount method: " + ob + " object", ex);
            } finally {
                oConnectionPool.disposeConnection();
            }

        } else {
            oReplyBean = new ReplyBean(401, "Unauthorized");
        }
        return oReplyBean;
    }
	public ReplyBean create() throws Exception {
		ReplyBean oReplyBean;
		ConnectionInterface oConnectionPool = null;
		Connection oConnection;
        if (this.checkPermission("create")) {
		try {
			String strJsonFromClient = oRequest.getParameter("json");
			Gson oGson = new Gson();
			LineaBean oLineaBean = new LineaBean();
			oLineaBean = oGson.fromJson(strJsonFromClient, LineaBean.class);
			oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
			oConnection = oConnectionPool.newConnection();
			LineaDao oLineaDao = new LineaDao(oConnection, ob);
			oLineaBean = oLineaDao.create(oLineaBean);
			oReplyBean = new ReplyBean(200, oGson.toJson(oLineaBean));
		} catch (Exception ex) {
			throw new Exception("ERROR: Service level: create method: " + ob + " object", ex);
		} finally {
			oConnectionPool.disposeConnection();
        }
    } else {
        oReplyBean = new ReplyBean(401, "Unauthorized");
    }
    return oReplyBean;
}
	public ReplyBean update() throws Exception {
		int iRes = 0;
		ReplyBean oReplyBean = null;
		ConnectionInterface oConnectionPool = null;
		Connection oConnection;
	       if (this.checkPermission("update")) {
		try {
			String strJsonFromClient = oRequest.getParameter("json");
			Gson oGson = new Gson();
			LineaBean oLineaBean = new LineaBean();
			oLineaBean = oGson.fromJson(strJsonFromClient, LineaBean.class);
			oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
			oConnection = oConnectionPool.newConnection();
			LineaDao oLineaDao = new LineaDao(oConnection, ob);
			iRes = oLineaDao.update(oLineaBean);
			oReplyBean = new ReplyBean(200, Integer.toString(iRes));
		} catch (Exception ex) {
			throw new Exception("ERROR: Service level: update method: " + ob + " object", ex);
		} finally {
			oConnectionPool.disposeConnection();
        }
    } else {
        oReplyBean = new ReplyBean(401, "Unauthorized");
    }
    return oReplyBean;
}

	public ReplyBean getpage() throws Exception {
		ReplyBean oReplyBean;
		ConnectionInterface oConnectionPool = null;
		Connection oConnection;
        if (this.checkPermission("getpage")) {
		try {
			Integer iRpp = Integer.parseInt(oRequest.getParameter("rpp"));
			Integer iPage = Integer.parseInt(oRequest.getParameter("page"));
            HashMap<String, String> hmOrder = ParameterCook.getOrderParams(oRequest.getParameter("order"));
			oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
			oConnection = oConnectionPool.newConnection();
			LineaDao oLineaDao = new LineaDao(oConnection, ob);
			ArrayList<LineaBean> alLineaBean = oLineaDao.getpage(iRpp, iPage,hmOrder,2);
			Gson oGson = new Gson();
			oReplyBean = new ReplyBean(200, oGson.toJson(alLineaBean));
		} catch (Exception ex) {
			throw new Exception("ERROR: Service level: getpage method: " + ob + " object", ex);
		} finally {
			oConnectionPool.disposeConnection();
        }
    } else {
        oReplyBean = new ReplyBean(401, "Unauthorized");
    }
    return oReplyBean;
}
    public ReplyBean getpagexusuario() throws Exception {
        ReplyBean oReplyBean;
        ConnectionInterface oConnectionPool = null;
        Connection oConnection;
        if (this.checkPermission("getpagexusuario")) {
            try {
            	Integer iRpp = Integer.parseInt(oRequest.getParameter("rpp"));
    			Integer iPage = Integer.parseInt(oRequest.getParameter("page"));
    			Integer id_factura = Integer.parseInt(oRequest.getParameter("idfactura"));
                HashMap<String, String> hmOrder = ParameterCook.getOrderParams(oRequest.getParameter("order"));
    			oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
    			oConnection = oConnectionPool.newConnection();
    			LineaDao oLineaDao = new LineaDao(oConnection, ob);
    			ArrayList<LineaBean> alLineaBean = oLineaDao.getpagexlinea(iRpp, iPage, hmOrder,1,id_factura);
    			Gson oGson = new Gson();
    			oReplyBean = new ReplyBean(200, oGson.toJson(alLineaBean));
            } catch (Exception ex) {
                throw new Exception("ERROR: Service level: get page: " + ob + " object", ex);
            } finally {
                oConnectionPool.disposeConnection();
            }

        } else {
            oReplyBean = new ReplyBean(401, "Unauthorized");
        }
        return oReplyBean;
    }
}
