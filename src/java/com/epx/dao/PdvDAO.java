/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;

import com.epx.entity.Pdv;
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
public class PdvDAO implements Serializable {

    public List<Pdv> findAllPdv() {
        Conexion con = new Conexion();
        List<Pdv> listadoPdvs = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "Select p.codigopdv, u.loginname, p.idgrupopdv, p.pdv, p.coordx, p.coordy "
                + "from pdv p "
                + "left outer join usuario u on p.idusuario = u.idusuario";
        try {
            pst = con.getConnection().prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                Pdv pdv = new Pdv();
                pdv.setCodigopdv(rs.getString(1));
                pdv.setLoginname(rs.getString(2));
                pdv.setIdgrupopdv(rs.getInt(3));
                pdv.setPdv(rs.getString(4));
                pdv.setCoordx(rs.getLong(5));
                pdv.setCoordy(rs.getLong(6));
                listadoPdvs.add(pdv);
            }
        } catch (Exception e) {
            System.out.println("DAO FIND ALL PDV: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listadoPdvs;
    }

    public boolean asignarPdvAUsuario(Pdv pdv, int idusuario) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "update pdv "
                + "set idusuario = ? "
                + "where codigopdv =?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, idusuario);
            pst.setString(2, pdv.getCodigopdv());
            pst.executeUpdate();
            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO ASIGNAR PDV A USUARIO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }
    
    public boolean actualizarPdvAUsuario(Pdv pdv,int idusuario) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "update pdv "
                + "set idusuario = ? "
                + "where codigopdv =?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, idusuario);
            pst.setString(2, pdv.getCodigopdv());
            pst.executeUpdate();
            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO ACTUALIZAR PDV A USUARIO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }
    
    public List<Usuario> findAllUsuario() {
        Conexion con = new Conexion();
        List<Usuario> listadoUsuario = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select idusuario, nombres, apellidos "
                + "from usuario "
                + "where estado = 1 and "
                + "idrol = 4";
        try {
            pst = con.getConnection().prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                Usuario usu = new Usuario();
                usu.setIdusuario(rs.getInt(1));
                usu.setNombres(rs.getString(2));
                usu.setApellidos(rs.getString(3));
                listadoUsuario.add(usu);
            }
        } catch (Exception e) {
            System.out.println("DAO LISTADO USUARIOS PARA ASIGNACIONES DE PDV: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listadoUsuario;
    }
}
