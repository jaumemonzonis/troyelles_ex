/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import net.daw.bean.LineaBean;
import net.daw.bean.TipousuarioBean;

/**
 *
 * @author a044531896d
 */
public class LineaDao {

    Connection oConnection;
    String ob = null;

    public LineaDao(Connection oConnection, String ob) {
        super();
        this.oConnection = oConnection;
        this.ob = ob;
    }

    public LineaBean get(int id) throws Exception {
        String strSQL = "SELECT * FROM " + ob + " WHERE id=?";
        LineaBean oLineaBean;
        ResultSet oResultSet = null;
        PreparedStatement oPreparedStatement = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oPreparedStatement.setInt(1, id);
            oResultSet = oPreparedStatement.executeQuery();
            if (oResultSet.next()) {
                oLineaBean = new LineaBean();
                oLineaBean.setId(oResultSet.getInt("id"));
                oLineaBean.setCantidad(oResultSet.getInt("cantidad"));
                oLineaBean.setId_producto(oResultSet.getInt("id_producto"));
                oLineaBean.setId_factura(oResultSet.getInt("id_factura"));
            } else {
                oLineaBean = null;
            }
        } catch (SQLException e) {
            throw new Exception("Error en Dao get de " + ob, e);
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return oLineaBean;
    }

    public int remove(int id) throws Exception {
        int iRes = 0;
        String strSQL = "DELETE FROM " + ob + " WHERE id=?";
        PreparedStatement oPreparedStatement = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oPreparedStatement.setInt(1, id);
            iRes = oPreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error en Dao remove de " + ob, e);
        } finally {
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return iRes;
    }

    public int getcount() throws Exception {
        String strSQL = "SELECT COUNT(id) FROM " + ob;
        int res = 0;
        ResultSet oResultSet = null;
        PreparedStatement oPreparedStatement = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery();
            if (oResultSet.next()) {
                res = oResultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new Exception("Error en Dao get de " + ob, e);
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return res;
    }

    public LineaBean create(LineaBean oLineaBean) throws Exception {
        String strSQL = "INSERT INTO " + ob + " ("+ob+".id, "+ob+".cantidad, "+ob+".id_producto, "+ob+".id_factura) VALUES (NULL, ?, ?, ?); ";
        ResultSet oResultSet = null;
        PreparedStatement oPreparedStatement = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oPreparedStatement.setInt(1, oLineaBean.getCantidad());
            oPreparedStatement.setInt(2, oLineaBean.getId_producto());
            oPreparedStatement.setInt(3, oLineaBean.getId_factura());
            oPreparedStatement.executeUpdate();
            oResultSet = oPreparedStatement.getGeneratedKeys();
            if (oResultSet.next()) {
                oLineaBean.setId(oResultSet.getInt(1));
            } else {
                oLineaBean.setId(0);
            }
        } catch (SQLException e) {
            throw new Exception("Error en Dao create de " + ob, e);
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return oLineaBean;
    }

    public int update(LineaBean oLineaBean) throws Exception {
        int iResult = 0;
        String strSQL = "UPDATE " + ob + " SET " + ob + ".cantidad = ?, " + ob + ".id_producto = ?, " + ob + ".id_factura=? WHERE " + ob + ".id = ?;";

        PreparedStatement oPreparedStatement = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oPreparedStatement.setInt(1, oLineaBean.getCantidad());
            oPreparedStatement.setInt(2, oLineaBean.getId_producto());
            oPreparedStatement.setInt(3, oLineaBean.getId_factura());
            oPreparedStatement.setInt(4, oLineaBean.getId());
            iResult = oPreparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error en Dao update de " + ob, e);
        } finally {
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return iResult;
    }

    public ArrayList<LineaBean> getpage(int iRpp, int iPage) throws Exception {
        String strSQL = "SELECT * FROM " + ob;
        ArrayList<LineaBean> alLineaBean;
        if (iRpp > 0 && iRpp < 100000 && iPage > 0 && iPage < 100000000) {
            strSQL += " LIMIT " + (iPage - 1) * iRpp + ", " + iRpp;
            ResultSet oResultSet = null;
            PreparedStatement oPreparedStatement = null;
            try {
                oPreparedStatement = oConnection.prepareStatement(strSQL);
                oResultSet = oPreparedStatement.executeQuery();
                alLineaBean = new ArrayList<LineaBean>();
                while (oResultSet.next()) {
                    LineaBean oLineaBean = new LineaBean();
                    oLineaBean.setId(oResultSet.getInt("id"));
                    oLineaBean.setCantidad(oResultSet.getInt("cantidad"));
                    oLineaBean.setId_producto(oResultSet.getInt("id_producto"));
                    oLineaBean.setId_factura(oResultSet.getInt("id_factura"));
                    alLineaBean.add(oLineaBean);
                }
            } catch (SQLException e) {
                throw new Exception("Error en Dao getpage de " + ob, e);
            } finally {
                if (oResultSet != null) {
                    oResultSet.close();
                }
                if (oPreparedStatement != null) {
                    oPreparedStatement.close();
                }
            }
        } else {
            throw new Exception("Error en Dao getpage de " + ob);
        }
        return alLineaBean;

    }

}