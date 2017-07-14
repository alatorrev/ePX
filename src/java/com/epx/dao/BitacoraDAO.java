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
public class BitacoraDAO implements Serializable {

    public boolean auditLoginInactivo(Usuario us) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String sql = "insert into audit_logs(event_date, event_type, event_login, event_description) "
                + "values (CURRENT_TIMESTAMP, 'LOGIN INACTIVO', ?, 'Ingreso al sistema con cuenta inactiva')";
        pst = con.getConnection().prepareStatement(sql);
        try {
            pst.setString(1, us.getLoginname().toUpperCase());
            pst.executeUpdate();

            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO LOGIN INACTIVO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }
    
    public boolean auditLoginExito(Usuario us) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String sql = "insert into audit_logs(event_date, event_type, event_login, event_description) "
                + "values (CURRENT_TIMESTAMP, 'LOGIN EXITOSO', ?, 'Ingreso al sistema exitosamente')";
        pst = con.getConnection().prepareStatement(sql);
        try {
            pst.setString(1, us.getLoginname().toUpperCase());
            pst.executeUpdate();

            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO LOGIN EXITO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }
    
    public boolean auditLoginError(String loginname) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String sql = "insert into audit_logs(event_date, event_type, event_login, event_description) "
                + "values (CURRENT_TIMESTAMP, 'LOGIN ERROR', ?, 'Ingreso de usuario o contraseña incorrecto')";
        pst = con.getConnection().prepareStatement(sql);
        try {
            pst.setString(1, loginname.toUpperCase());
            pst.executeUpdate();

            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO LOGIN ERROR: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }
    
    public boolean auditLogoutButton(Usuario us) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String sql = "insert into audit_logs(event_date, event_type, event_login, event_description) "
                + "values (CURRENT_TIMESTAMP, 'LOGOUT EXITOSO', ?, 'Salida exitosa del sistema')";
        pst = con.getConnection().prepareStatement(sql);
        try {
            pst.setString(1, us.getLoginname().toUpperCase());
            pst.executeUpdate();

            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO LOGOUT EXITOSO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }
    
    public boolean auditLogoutError(Usuario us) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String sql = "insert into audit_logs(event_date, event_type, event_login, event_description) "
                + "values (CURRENT_TIMESTAMP, 'LOGOUT ERROR', ?, 'Ingreso a página no autorizada')";
        pst = con.getConnection().prepareStatement(sql);
        try {
            pst.setString(1, us.getLoginname().toUpperCase());
            pst.executeUpdate();

            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO LOGOUT ERROR: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }
    
    public boolean auditLogoutTime(Usuario us) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String sql = "insert into audit_logs(event_date, event_type, event_login, event_description) "
                + "values (CURRENT_TIMESTAMP, 'LOGOUT TIME', ?, 'Salida del sistema por tiempo de inactividad')";
        pst = con.getConnection().prepareStatement(sql);
        try {
            pst.setString(1, us.getLoginname().toUpperCase());
            pst.executeUpdate();

            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO LOGOUT TIME: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }
}
