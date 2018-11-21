/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.service;

import com.google.gson.Gson;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import net.daw.bean.ReplyBean;
import net.daw.bean.TipoproductoBean;
import net.daw.bean.UsuarioBean;
import net.daw.connection.publicinterface.ConnectionInterface;
import net.daw.constant.ConnectionConstants;
import net.daw.dao.TipoproductoDao;
import net.daw.factory.ConnectionFactory;
import net.daw.helper.EncodingHelper;
import net.daw.helper.ParameterCook;

/**
 *
 * @author a044531896d
 */
public class TipoproductoService {
    HttpServletRequest oRequest;
	String ob = null;

	public TipoproductoService(HttpServletRequest oRequest) {
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
			TipoproductoDao oTipoproductoDao = new TipoproductoDao(oConnection, ob);
			TipoproductoBean oTipoproductoBean = oTipoproductoDao.get(id,1);
			Gson oGson = new Gson();
			oReplyBean = new ReplyBean(200, oGson.toJson(oTipoproductoBean));
		} catch (Exception ex) {
			throw new Exception("ERROR: Service level: get method: " + ob + " object", ex);
		} finally {
			oConnectionPool.disposeConnection();
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
			TipoproductoDao oTipoproductoDao = new TipoproductoDao(oConnection, ob);
			int iRes = oTipoproductoDao.remove(id);
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
			TipoproductoDao oTipoproductoDao = new TipoproductoDao(oConnection, ob);
			int registros = oTipoproductoDao.getcount();
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
			TipoproductoBean oTipoproductoBean = new TipoproductoBean();
			oTipoproductoBean = oGson.fromJson(strJsonFromClient, TipoproductoBean.class);
			oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
			oConnection = oConnectionPool.newConnection();
			TipoproductoDao oTipoproductoDao = new TipoproductoDao(oConnection, ob);
			oTipoproductoBean = oTipoproductoDao.create(oTipoproductoBean);
			oReplyBean = new ReplyBean(200, oGson.toJson(oTipoproductoBean));
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
		ReplyBean oReplyBean;
		ConnectionInterface oConnectionPool = null;
		Connection oConnection;
	       if (this.checkPermission("update")) {
		try {
			String strJsonFromClient = oRequest.getParameter("json");
			Gson oGson = new Gson();
			TipoproductoBean oTipoproductoBean = new TipoproductoBean();
			oTipoproductoBean = oGson.fromJson(strJsonFromClient, TipoproductoBean.class);
			oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
			oConnection = oConnectionPool.newConnection();
			TipoproductoDao oTipoproductoDao = new TipoproductoDao(oConnection, ob);
			iRes = oTipoproductoDao.update(oTipoproductoBean);
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
			TipoproductoDao oTipoproductoDao = new TipoproductoDao(oConnection, ob);
			ArrayList<TipoproductoBean> alTipoproductoBean = oTipoproductoDao.getpage(iRpp, iPage, hmOrder, 1);
			Gson oGson = new Gson();
			oReplyBean = new ReplyBean(200, oGson.toJson(alTipoproductoBean));
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
}