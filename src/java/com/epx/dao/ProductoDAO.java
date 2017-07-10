/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;

import com.epx.entity.Producto;
import com.epx.conexion.Conexion;
import com.epx.entity.Usuario;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bottago SA
 */
public class ProductoDAO implements Serializable {

    public List<Producto> findAllProducto() {
        Conexion con = new Conexion();
        List<Producto> listadoProductos = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select idproducto, fuenteproducto, marca, sustituto, forma, concentracion from producto_bottago "
                + "where fuenteproducto = 'B'";
        try {
            pst = con.getConnection().prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                Producto pro = new Producto();
                pro.setIdProducto(rs.getLong(1));
                pro.setFuente(rs.getString(2));
                pro.setMarca(rs.getString(3));
                pro.setSustituto(rs.getString(4));
                pro.setForma(rs.getString(5));
                pro.setConcentracion(rs.getString(6));
                listadoProductos.add(pro);
            }
        } catch (Exception e) {
            System.out.println("DAO FIND ALL PRODUCTO: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listadoProductos;
    }

    public List<Producto> autocompletarproducto(String cadena) {
        List<Producto> listadoProducto = new ArrayList<>();
        Conexion con = new Conexion();
        PreparedStatement pst;
//        String sql = "select idproducto, fuenteproducto, marca, sustituto, forma1, concentracion, laboratorio,descripcion "
//                + "from producto_difare "
//                + "where upper(marca) like (?) or upper(sustituto) like (?) "
//                + "union all "
//                + "select idproducto, fuenteproducto, marca, sustituto, forma, concentracion, null as laboratorio,null as descripcion "
//                + "from producto_bottago "
//                + "where upper(marca) like (?) or upper(sustituto) like (?)"
//                + "order by ?";
        String sql = "select * from (select idproducto, fuenteproducto, marca, sustituto, forma1, concentracion, laboratorio,descripcion "
                + "from producto_difare "
                + "where upper(marca) like (?) or upper(sustituto) like (?) "
                + "union all "
                + "select idproducto, fuenteproducto, marca, sustituto, forma, concentracion, null as laboratorio,null as descripcion "
                + "from producto_bottago "
                + "where upper(marca) like (?) or upper(sustituto) like (?)) x "
                + "ORDER BY case when marca= ? THEN 0 ELSE 1 END";
        try {
            pst = con.getConnection().prepareStatement(sql);
            pst.setString(1, cadena.trim().concat("%"));
            pst.setString(2, cadena.trim().concat("%"));
            pst.setString(3, cadena.trim().concat("%"));
            pst.setString(4, cadena.trim().concat("%"));
            pst.setString(5, cadena.trim());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Producto pro = new Producto();
                pro.setIdProducto(rs.getLong(1));
                pro.setFuente(rs.getString(2));
                pro.setMarca(rs.getString(3));
                pro.setSustituto(rs.getString(4));
                pro.setForma(rs.getString(5));
                pro.setConcentracion(rs.getString(6));
                pro.setLaboratorio(rs.getString(7));
                pro.setDescripcion(rs.getString(8));
                listadoProducto.add(pro);
            }
        } catch (Exception e) {
            System.out.println("DAO AUTOCOMPLETE PRODUCTO: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(MedicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listadoProducto;
    }

    public boolean createProducto(Producto pro, Usuario u) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into producto_bottago(idproducto, fuenteproducto, marca, sustituto, forma, concentracion, "
                + "fechacrea, usucrea) "
                + "values((select (1 + isnull(max(idproducto),0)) from producto_bottago), 'B', ?, ?, ?, ?, "
                + "CURRENT_TIMESTAMP, ?)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, (pro.getMarca() == null || pro.getMarca().length() == 0) ? null : pro.getMarca().toUpperCase());
            pst.setString(2, (pro.getSustituto() == null || pro.getSustituto().length() == 0) ? null : pro.getSustituto().toUpperCase());
            pst.setString(3, (pro.getForma() == null || pro.getForma().length() == 0) ? null : pro.getForma().toUpperCase());
            pst.setString(4, (pro.getConcentracion() == null || pro.getConcentracion().length() == 0) ? null : pro.getConcentracion().toUpperCase());
            pst.setString(5, u.getLoginname());
            pst.executeUpdate();
            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO CREAR PRODUCTOS: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }

    public boolean editProducto(Producto pro, Usuario u) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "update producto_bottago "
                + "set marca=?, sustituto=?, forma=?, concentracion=?, fechamod=CURRENT_TIMESTAMP, "
                + "usumod=? "
                + "where idproducto=? and fuenteproducto = 'B'";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, pro.getMarca().toUpperCase());
            pst.setString(2, pro.getSustituto().toUpperCase());
            pst.setString(3, pro.getForma().toUpperCase());
            pst.setString(4, pro.getConcentracion().toUpperCase());
            pst.setString(5, u.getLoginname());
            pst.setLong(6, pro.getIdProducto());
            pst.executeUpdate();
            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO EDIT PRODUCTO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }

    public boolean deleteProducto(Producto pro, Usuario u) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "delete producto_bottago "
                + "where idproducto =? and fuenteproducto='B'";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setLong(1, pro.getIdProducto());
            pst.executeUpdate();
            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO DELETE PRODUCTO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }

    public List<Producto> findAllProductoSearch(int first, int pageSize, StringBuilder filters) {
        Conexion con = new Conexion();
        List<Producto> listadoProductos = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String sql = "select * from ("
                + "select *,ROW_NUMBER() OVER(ORDER BY a.idproducto asc) as row from "
                + "(select idproducto, fuenteproducto, marca, sustituto, forma, concentracion, '' as medida "
                + "from producto_bottago "
                + "union all "
                + "select idproducto, fuenteproducto, marca, sustituto,"
                + "(case when forma1='NULL' then '' else forma1 end),"
                + "(case when concentracion='NULL' then '' else concentracion end),"
                + "(case when medida='NULL' then '' else medida end)"
                + "from producto_difare) a "
                + "[FILTERS]"
                //                + "where CONVERT(NVARCHAR,idproducto,120) like '520%'"
                + ") b where row >? and row <=?";
        if (filters.length() > 0) {
            sql = sql.replace("[FILTERS]", filters.toString());
        } else {
            sql = sql.replace("[FILTERS]", "");
        }
        System.out.println(sql);
        try {
            pst = con.getConnection().prepareStatement(sql);
            pst.setInt(1, first);
            pst.setInt(2, first + pageSize);
            rs = pst.executeQuery();
            while (rs.next()) {
                Producto pro = new Producto();
                pro.setIdProducto(rs.getLong(1));
                pro.setFuente(rs.getString(2));
                pro.setMarca(rs.getString(3));
                pro.setSustituto(rs.getString(4));
                pro.setForma(rs.getString(5));
                pro.setConcentracion(rs.getString(6));
                pro.setMedida(rs.getString(7));
                listadoProductos.add(pro);
            }
        } catch (Exception e) {
            System.out.println("DAO FIND ALL PRODUCTO SEARCH: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listadoProductos;
    }

    public int totalRecordsProducto(StringBuilder filters) {
        Conexion con = new Conexion();
        int totalRecords = 0;
        String sql = "select count(*) from "
                + "(select idproducto, fuenteproducto, marca, sustituto, forma, concentracion, '' as medida "
                + "from producto_bottago "
                + "union all "
                + "select idproducto, fuenteproducto, marca, sustituto,"
                + "(case when forma1='NULL' then '' else forma1 end),"
                + "(case when concentracion='NULL' then '' else concentracion end),"
                + "(case when medida='NULL' then '' else medida end)"
                + "from producto_difare) a "
                + "[FILTERS]";
        if (filters.length() > 0) {
            sql = sql.replace("[FILTERS]", filters.toString());
        } else {
            sql = sql.replace("[FILTERS]", "");
        }
        try {
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                totalRecords = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                System.out.println("ERROR COUNT RECORDS LAZY :" + ex.getMessage());
            }
        }
        System.out.println("totalRegistros "+totalRecords);
        return totalRecords;
    }
}
