/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;
import com.epx.entity.Rol;
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
public class RolDAO implements Serializable{
    public List<Rol> findAll() {
        Conexion con = new Conexion();
        List<Rol> listadoRoles = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select * from rol where estado = 1";
        try {
            pst = con.getConnection().prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setIdrol(rs.getInt(1));
                rol.setDescripcion(rs.getString(2));
                listadoRoles.add(rol);
            }
        } catch (Exception e) {
            System.out.println("DAO ROL: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listadoRoles;
    }
    
    public boolean editRol(Rol rol, Usuario u) throws SQLException {
        boolean done=false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "update rol set descripcion=?, fechamod=CURRENT_TIMESTAMP, usumod=? "
                + " where idrol=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, rol.getDescripcion().toUpperCase());
            pst.setString(2, u.getLoginname());
            pst.setInt(3, rol.getIdrol());
            pst.executeUpdate();
            con.getConnection().commit();
            done=true;
        } catch (Exception e) {
            System.out.println("DAO ROL: " + e.getMessage());
            con.getConnection().rollback();
            done=false;
        } finally {
            con.desconectar();
        }
        return done;
    }
    
    public boolean deleteRol(Rol rol, Usuario u) throws SQLException {
        boolean done=false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "update rol set estado=?, fechamod=CURRENT_TIMESTAMP, usumod=? "
                + "where idrol=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, 0);
            pst.setString(2, u.getLoginname());
            pst.setInt(3, rol.getIdrol());
            pst.executeUpdate();
            con.getConnection().commit();
            done=true;
        } catch (Exception e) {
            System.out.println("DAO ROL: " + e.getMessage());
            con.getConnection().rollback();
            done=false;
        } finally {
            con.desconectar();
        }
        return done;
    }
    
    public boolean createRol(Rol rol, Usuario u) throws SQLException {
        boolean done=false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into rol(descripcion, fechacrea, usucrea, estado) "
                + "values(?,CURRENT_TIMESTAMP, ?, 1)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, rol.getDescripcion().toUpperCase());
            pst.setString(2, u.getLoginname());
            pst.executeUpdate();
            con.getConnection().commit();
            done=true;
        } catch (Exception e) {
            System.out.println("DAO ROL: " + e.getMessage());
            con.getConnection().rollback();
            done=false;
        } finally {
            con.desconectar();
        }
        return done;
    }
}
