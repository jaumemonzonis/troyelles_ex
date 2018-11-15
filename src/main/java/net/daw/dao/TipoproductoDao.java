package net.daw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import net.daw.bean.TipoproductoBean;
import net.daw.helper.SqlBuilder;

/**
 *
 * @author Jesus
 */

public class TipoproductoDao {
    Connection oConnection;
	String ob = null;

	public TipoproductoDao(Connection oConnection, String ob) {
		super();
		this.oConnection = oConnection;
		this.ob = ob;
	}

	public TipoproductoBean get(int id, Integer expand) throws Exception {
		String strSQL = "SELECT * FROM " + ob + " WHERE id=?";
		TipoproductoBean oTipoproductoBean;
		ResultSet oResultSet = null;
		PreparedStatement oPreparedStatement = null;
		try {
			oPreparedStatement = oConnection.prepareStatement(strSQL);
			oPreparedStatement.setInt(1, id);
			oResultSet = oPreparedStatement.executeQuery();
			if (oResultSet.next()) {
				oTipoproductoBean = new TipoproductoBean();
                                oTipoproductoBean.fill(oResultSet, oConnection);
			} else {
				oTipoproductoBean = null;
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
		return oTipoproductoBean;
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

	public TipoproductoBean create(TipoproductoBean oTipoproductoBean) throws Exception {
		//String strSQL = "INSERT INTO " + ob + " (`id`, `desc`) VALUES (NULL, ?); ";
                String strSQL = "INSERT INTO " + ob;
                strSQL += "(" + oTipoproductoBean.getColumns() + ")";
                strSQL += " VALUES ";
                strSQL += "(" + oTipoproductoBean.getValues() + ")";
                ResultSet oResultSet = null;
		PreparedStatement oPreparedStatement = null;
		try {
			oPreparedStatement = oConnection.prepareStatement(strSQL);
			oPreparedStatement.executeUpdate();
			oResultSet = oPreparedStatement.getGeneratedKeys();
			if (oResultSet.next()) {
				oTipoproductoBean.setId(oResultSet.getInt(1));
			} else {
				oTipoproductoBean.setId(0);
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
		return oTipoproductoBean;
	}

	public int update(TipoproductoBean oTipoproductoBean) throws Exception {
		int iResult = 0;
		//String strSQL = "UPDATE " + ob + " SET " + ob +".desc = ? WHERE id = ?;";
                String strSQL = "UPDATE " + ob + " SET ";
                strSQL += oTipoproductoBean.getPairs();

		PreparedStatement oPreparedStatement = null;
		try {
			oPreparedStatement = oConnection.prepareStatement(strSQL);
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

	public ArrayList<TipoproductoBean> getpage(int iRpp, int iPage, HashMap<String, String> hmOrder, Integer expand) throws Exception {
		String strSQL = "SELECT * FROM " + ob;
                strSQL += SqlBuilder.buildSqlOrder(hmOrder);
                ArrayList<TipoproductoBean> alTipoproductoBean;
		if (iRpp > 0 && iRpp < 100000 && iPage > 0 && iPage < 100000000) {
			strSQL += " LIMIT " + (iPage - 1) * iRpp + ", " + iRpp;
			ResultSet oResultSet = null;
			PreparedStatement oPreparedStatement = null;
			try {
				oPreparedStatement = oConnection.prepareStatement(strSQL);
				oResultSet = oPreparedStatement.executeQuery();
				alTipoproductoBean = new ArrayList<TipoproductoBean>();
				while (oResultSet.next()) {
					TipoproductoBean oTipoproductoBean = new TipoproductoBean();
                                        oTipoproductoBean.fill(oResultSet, oConnection);
					alTipoproductoBean.add(oTipoproductoBean);
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
		return alTipoproductoBean;

	}
}
