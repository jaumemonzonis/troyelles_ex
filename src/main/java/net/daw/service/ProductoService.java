package net.daw.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import net.daw.bean.ReplyBean;
import net.daw.bean.UsuarioBean;
import net.daw.bean.ProductoBean;
import net.daw.connection.publicinterface.ConnectionInterface;
import net.daw.constant.ConnectionConstants;
import net.daw.dao.ProductoDao;
import net.daw.dao.UsuarioDao;
import net.daw.factory.ConnectionFactory;
import net.daw.helper.EncodingHelper;
import net.daw.helper.ParameterCook;

public class ProductoService {

    HttpServletRequest oRequest;
    String ob = null;

    public ProductoService(HttpServletRequest oRequest) {
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
            ProductoDao oProductoDao = new ProductoDao(oConnection, ob);
            ProductoBean oProductoBean = oProductoDao.get(id, 2);
            Gson oGson = new Gson();
            oReplyBean = new ReplyBean(200, oGson.toJson(oProductoBean));
        
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
            ProductoDao oProductoDao = new ProductoDao(oConnection, ob);
            int iRes = oProductoDao.remove(id);
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
            ProductoDao oProductoDao = new ProductoDao(oConnection, ob);
            int registros = oProductoDao.getcount();
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
            Gson oGson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
            ProductoBean oProductoBean = new ProductoBean();
            oProductoBean = oGson.fromJson(strJsonFromClient, ProductoBean.class);
            oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
            oConnection = oConnectionPool.newConnection();
            ProductoDao oProductoDao = new ProductoDao(oConnection, ob);
            oProductoBean = oProductoDao.create(oProductoBean);
            oReplyBean = new ReplyBean(200, oGson.toJson(oProductoBean));
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
       // if (this.checkPermission("update")) {
            try {
                String strJsonFromClient = oRequest.getParameter("json");
                Gson oGson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
                ProductoBean oProductoBean = new ProductoBean();
                oProductoBean = oGson.fromJson(strJsonFromClient, ProductoBean.class);
                oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
                oConnection = oConnectionPool.newConnection();
                ProductoDao oProductoDao = new ProductoDao(oConnection, ob);
                iRes = oProductoDao.update(oProductoBean);
                oReplyBean = new ReplyBean(200, Integer.toString(iRes));
            } catch (Exception ex) {
                oReplyBean = new ReplyBean(500,
                        "ERROR: " + EncodingHelper.escapeQuotes(EncodingHelper.escapeLine(ex.getMessage())));
            } finally {
                oConnectionPool.disposeConnection();
            }
      //  } else {
           // oReplyBean = new ReplyBean(401, "Unauthorized");
       // }
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
            ProductoDao oProductoDao = new ProductoDao(oConnection, ob);
            ArrayList<ProductoBean> alProductoBean = oProductoDao.getpage(iRpp, iPage,hmOrder,1);
            Gson oGson = new Gson();
            oReplyBean = new ReplyBean(200, oGson.toJson(alProductoBean));
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

    public ReplyBean fill() throws Exception {
        ReplyBean oReplyBean;
        ConnectionInterface oConnectionPool = null;
        Connection oConnection;
        ArrayList<ProductoBean> productos = new ArrayList<>();
        RellenarService oRellenarService = new RellenarService();
        if (this.checkPermission("fill")) {
        try {
            Integer number = Integer.parseInt(oRequest.getParameter("number"));
            oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
            oConnection = oConnectionPool.newConnection();
            ProductoDao oProductoDao = new ProductoDao(oConnection, ob);
            productos = oRellenarService.RellenarProducto(number);
            for (ProductoBean producto : productos) {
                oProductoDao.create(producto);
            }
            Gson oGson = new Gson();
            oReplyBean = new ReplyBean(200, oGson.toJson("Productos creados: " + number));
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
}