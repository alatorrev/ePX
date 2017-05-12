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
            pst.setString(1, pro.getMarca().toUpperCase());
            pst.setString(2, pro.getSustituto().toUpperCase());
            pst.setString(3, pro.getForma().toUpperCase());
            pst.setString(4, pro.getConcentracion().toUpperCase());
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
}
