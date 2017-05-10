/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;

import com.epx.entity.Usuario;
import com.epx.conexion.Conexion;

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
public class UsuarioDAO implements Serializable {

    public Usuario loginAction(String loginname, String contrasena, Usuario u) throws SQLException {
        Conexion con = new Conexion();
        Usuario us = new Usuario();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select u.nombres,u.apellidos,u.correo,u.loginname,r.idrol,r.descripcion,u.estado "
                + "from usuario u "
                + "inner join rol r on r.idrol=u.idrol "
                + "where u.loginname=? and u.contrasena=? and u.estado=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, loginname);
            pst.setString(2, contrasena);
            pst.setInt(3, 1);
            rs = pst.executeQuery();
            while (rs.next()) {
                us.setNombres(rs.getString(1));
                us.setApellidos(rs.getString(2));
                us.setCorreo(rs.getString(3));
                us.setLoginname(rs.getString(4));
                us.setIdRol(rs.getInt(5));
                us.setDescripcionRol(rs.getString(6));
                us.setActivo(rs.getInt(7));
                return us;
            }
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return null;
    }

    public List<Usuario> findAllRoles() {
        Conexion con = new Conexion();
        List<Usuario> listadoRoles = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select r.idrol, r.descripcion, r.estado "
                + "from rol r "
                + "where r.estado = 1";
        try {
            pst = con.getConnection().prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                Usuario us = new Usuario();
                us.setIdRol(rs.getInt(1));
                us.setDescripcionRol(rs.getString(2));
                us.setEstadorol(rs.getInt(3));
                listadoRoles.add(us);
            }
        } catch (Exception e) {
            System.out.println("DAO LISTADO ROLES PARA EL USUARIO: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listadoRoles;
    }

    public List<Usuario> findAllUsuarios() {
        Conexion con = new Conexion();
        List<Usuario> listadoUsuarios = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select u.idusuario, u.nombres, u.apellidos, u.correo, r.idrol, r.descripcion "
                + "from usuario u "
                + "left outer join rol r on u.idrol = r.idrol "
                + "where u.estado = 1";
        try {
            pst = con.getConnection().prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                Usuario us = new Usuario();
                us.setIdusuario(rs.getInt(1));
                us.setNombres(rs.getString(2));
                us.setApellidos(rs.getString(3));
                us.setCorreo(rs.getString(4));
                us.setIdRol(rs.getInt(5));
                us.setDescripcionRol(rs.getString(6));
                listadoUsuarios.add(us);
            }
        } catch (Exception e) {
            System.out.println("DAO ALL USUARIO: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listadoUsuarios;
    }

    public boolean createUsuario(Usuario us, Usuario session) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String sql = "insert into usuario(idrol, nombres, apellidos, correo, loginname, contrasena, fechacrea, usucre, estado) "
                + "values(null, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, 1)";
        pst = con.getConnection().prepareStatement(sql);
        try {
            pst.setString(1, us.getNombres().toUpperCase());
            pst.setString(2, us.getApellidos().toUpperCase());
            pst.setString(3, us.getCorreo().toLowerCase());
            pst.setString(4, us.getLoginname().toUpperCase());
            pst.setString(5, us.getPassword());
            pst.setString(6, session.getLoginname());
            pst.executeUpdate();

            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO CREATE USUARIO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }

    public boolean verificarUsuario(Usuario us) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        PreparedStatement pst;
        String sql = "select loginname "
                + "from usuario "
                + "where loginname = ?";
        pst = con.getConnection().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        try {
            pst.setString(1, us.getLoginname());
            ResultSet rs = pst.executeQuery();
            rs.last();
            int rows = rs.getRow();
            done=rows>0;
        } catch (Exception e) {
            System.out.println("DAO VERIFICAR LOGIN: " + e.getMessage());
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }

    public boolean editUsuario(Usuario us, Usuario session) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "update usuario "
                + "set nombres=?, apellidos=?, correo=?, fechamod=CURRENT_TIMESTAMP, usumod=? "
                + "where idusuario=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, us.getNombres().toUpperCase());
            pst.setString(2, us.getApellidos().toUpperCase());
            pst.setString(3, us.getCorreo().toLowerCase());
            pst.setString(4, session.getLoginname());
            pst.setInt(5, us.getIdusuario());

            pst.executeUpdate();
            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO EDIT USUARIO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }

    public boolean deleteUsuario(Usuario us, Usuario session) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "update usuario set estado=0 where idusuario=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, us.getIdusuario());
            pst.executeUpdate();

            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO DELETE USUARIO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }

    public boolean AsignaRol(Usuario us, int RolIdSelected) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "update usuario set idrol=? where idusuario=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, RolIdSelected);
            pst.setInt(2, us.getIdusuario());
            pst.executeUpdate();

            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO ASIGNA ROL A USUARIO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }
}
