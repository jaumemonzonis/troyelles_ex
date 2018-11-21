package net.daw.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.daw.bean.ReplyBean;
import net.daw.bean.TipousuarioBean;
import net.daw.bean.UsuarioBean;
import net.daw.connection.publicinterface.ConnectionInterface;
import net.daw.constant.ConnectionConstants;
import net.daw.dao.TipousuarioDao;
import net.daw.dao.UsuarioDao;
import net.daw.factory.ConnectionFactory;
import net.daw.helper.EncodingHelper;
import net.daw.helper.ParameterCook;

public class TipousuarioService {

	HttpServletRequest oRequest;
	String ob = null;

	public TipousuarioService(HttpServletRequest oRequest) {
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
			TipousuarioDao oTipousuarioDao = new TipousuarioDao(oConnection, ob);
			TipousuarioBean oTipousuarioBean = oTipousuarioDao.get(id,1);
			Gson oGson = new Gson();
			oReplyBean = new ReplyBean(200, oGson.toJson(oTipousuarioBean));
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
			TipousuarioDao oTipousuarioDao = new TipousuarioDao(oConnection, ob);
			int iRes = oTipousuarioDao.remove(id);
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
			TipousuarioDao oTipousuarioDao = new TipousuarioDao(oConnection, ob);
			int registros = oTipousuarioDao.getcount();
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
			TipousuarioBean oTipousuarioBean = new TipousuarioBean();
			oTipousuarioBean = oGson.fromJson(strJsonFromClient, TipousuarioBean.class);
			oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
			oConnection = oConnectionPool.newConnection();
			TipousuarioDao oTipousuarioDao = new TipousuarioDao(oConnection, ob);
			oTipousuarioBean = oTipousuarioDao.create(oTipousuarioBean);
			oReplyBean = new ReplyBean(200, oGson.toJson(oTipousuarioBean));
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
			TipousuarioBean oTipousuarioBean = new TipousuarioBean();
			oTipousuarioBean = oGson.fromJson(strJsonFromClient, TipousuarioBean.class);
			oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
			oConnection = oConnectionPool.newConnection();
			TipousuarioDao oTipousuarioDao = new TipousuarioDao(oConnection, ob);
			iRes = oTipousuarioDao.update(oTipousuarioBean);
                        oReplyBean = new ReplyBean(200,Integer.toString(iRes));
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
                TipousuarioDao oTipousuarioDao = new TipousuarioDao(oConnection, ob);
                ArrayList<TipousuarioBean> alTipousuarioBean = oTipousuarioDao.getpage(iRpp, iPage, hmOrder, 1);
                Gson oGson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
                oReplyBean = new ReplyBean(200, oGson.toJson(alTipousuarioBean));
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