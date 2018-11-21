package net.daw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import net.daw.bean.TipousuarioBean;
import net.daw.bean.UsuarioBean;
import net.daw.helper.SqlBuilder;

public class TipousuarioDao {

	Connection oConnection;
	String ob = null;

	public TipousuarioDao(Connection oConnection, String ob) {
		super();
		this.oConnection = oConnection;
		this.ob = ob;
	}

	public TipousuarioBean get(int id, int expand) throws Exception {
		String strSQL = "SELECT * FROM " + ob + " WHERE id=?";
		TipousuarioBean oTipousuarioBean;
		ResultSet oResultSet = null;
		PreparedStatement oPreparedStatement = null;
		try {
			oPreparedStatement = oConnection.prepareStatement(strSQL);
			oPreparedStatement.setInt(1, id);
			oResultSet = oPreparedStatement.executeQuery();
			if (oResultSet.next()) {
				oTipousuarioBean = new TipousuarioBean();
				oTipousuarioBean.fill(oResultSet, oConnection, expand);
			} else {
				oTipousuarioBean = null;
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
		return oTipousuarioBean;
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

	public TipousuarioBean create(TipousuarioBean oTipousuarioBean) throws Exception {
		String strSQL = "INSERT INTO " + ob + " (`id`, `desc`) VALUES (NULL, ?); ";
		ResultSet oResultSet = null;
		PreparedStatement oPreparedStatement = null;
		try {
			oPreparedStatement = oConnection.prepareStatement(strSQL);
			oPreparedStatement.setString(1, oTipousuarioBean.getDesc());
			oPreparedStatement.executeUpdate();
			oResultSet = oPreparedStatement.getGeneratedKeys();
			if (oResultSet.next()) {
				oTipousuarioBean.setId(oResultSet.getInt(1));
			} else {
				oTipousuarioBean.setId(0);
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
		return oTipousuarioBean;
	}

	public int update(TipousuarioBean oTipousuarioBean) throws Exception {
		int iResult = 0;
		String strSQL = "UPDATE " + ob + " SET " + ob + ".desc=? WHERE " + ob + ".id=?;";

		PreparedStatement oPreparedStatement = null;
		try {
			oPreparedStatement = oConnection.prepareStatement(strSQL);
			oPreparedStatement.setString(1, oTipousuarioBean.getDesc());
			oPreparedStatement.setInt(2, oTipousuarioBean.getId());
			iResult = oPreparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new Exception("Error en Dao update de " + ob , e);
		} finally {
			if (oPreparedStatement != null) {
				oPreparedStatement.close();
			}
		}
		return iResult;
	}

	public ArrayList<TipousuarioBean> getpage(int iRpp, int iPage, HashMap<String, String> hmOrder, int expand) throws Exception {
		String strSQL = "SELECT * FROM " + ob;
		strSQL += SqlBuilder.buildSqlOrder(hmOrder);
		ArrayList<TipousuarioBean> alTipousuarioBean;
		if (iRpp > 0 && iRpp < 100000 && iPage > 0 && iPage < 100000000) {
			strSQL += " LIMIT " + (iPage - 1) * iRpp + ", " + iRpp;
			ResultSet oResultSet = null;
			PreparedStatement oPreparedStatement = null;
			try {
				oPreparedStatement = oConnection.prepareStatement(strSQL);
				oResultSet = oPreparedStatement.executeQuery();
				alTipousuarioBean = new ArrayList<TipousuarioBean>();
				while (oResultSet.next()) {
					TipousuarioBean oTipousuarioBean = new TipousuarioBean();
					oTipousuarioBean.fill(oResultSet, oConnection, expand);
					alTipousuarioBean.add(oTipousuarioBean);
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
		return alTipousuarioBean;

	}

}