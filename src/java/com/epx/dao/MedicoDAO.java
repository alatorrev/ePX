/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;

import com.epx.entity.Medico;
import com.epx.conexion.Conexion;
import com.epx.entity.Usuario;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bottago SA
 */
public class MedicoDAO implements Serializable {

    public List<Medico> findAllMedicos() {
        Conexion con = new Conexion();
        List<Medico> listadoMedicos = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select * from medico_bottago";
        try {
            pst = con.getConnection().prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                Medico med = new Medico();
                med.setIdMedico(rs.getLong(1));
                med.setFuente(rs.getString(2));
                med.setNombres(rs.getString(3));
                med.setApellidos(rs.getString(4));
                med.setCedula(rs.getString(5));
                med.setDireccion(rs.getString(6));
                med.setFechaNacimiento(rs.getDate(7));
                listadoMedicos.add(med);
            }
        } catch (Exception e) {
            System.out.println("DAO FINDALL MEDICO: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listadoMedicos;
    }

    public List<Medico> autocompletarmedicos(String cadena) {
        List<Medico> listadoMedicos = new ArrayList<>();
        Conexion con = new Conexion();
        PreparedStatement pst;
        String sql = "select * from medico_bottago where upper(nombres+' '+apellidos) like upper(?)";
        try {
            pst = con.getConnection().prepareStatement(sql);
            pst.setString(1, cadena.trim().concat("%"));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Medico med = new Medico();
                med.setIdMedico(rs.getLong(1));
                med.setFuente(rs.getString(2));
                med.setNombres(rs.getString(3));
                med.setApellidos(rs.getString(4));
                med.setCedula(rs.getString(5));
                med.setDireccion(rs.getString(6));
                med.setFechaNacimiento(rs.getDate(7));
                listadoMedicos.add(med);
            }
        } catch (Exception e) {
            System.out.println("DAO AUTOCOMPLETE MEDICO: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(MedicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listadoMedicos;
    }

    public boolean createMedico(Medico med, Usuario u) throws SQLException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into medico_bottago (idmedico, fuentemedico, nombres, apellidos, cedula, "
                + "direccion, fechanacimiento, fechacreacion, usuariocreacion) "
                + "values((select (1 + isnull(max(idmedico),0)) from medico_bottago), 'B', ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, med.getNombres().toUpperCase());
            pst.setString(2, med.getApellidos().toUpperCase());
            pst.setString(3, med.getCedula());
            pst.setString(4, med.getDireccion());
            pst.setDate(5, java.sql.Date.valueOf(format.format(med.getFechaNacimiento())));
            pst.setString(6, u.getLoginname());
            pst.executeUpdate();
            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO CREAR MEDICO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }

    public boolean editarMedico(Medico med, Usuario u) throws SQLException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        boolean done = false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "update medico_bottago "
                + "set nombres=?, apellidos=?, cedula=?, direccion=?, fechanacimiento=?, "
                + "fechamodificacion=CURRENT_TIMESTAMP, usuariomod=? "
                + "where idmedico = ?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, med.getNombres().toUpperCase());
            pst.setString(2, med.getApellidos().toUpperCase());
            pst.setString(3, med.getCedula());
            pst.setString(4, med.getDireccion());
            pst.setDate(5, java.sql.Date.valueOf(format.format(med.getFechaNacimiento())));
            pst.setString(6, u.getLoginname());
            pst.setLong(7, med.getIdMedico());
            pst.executeUpdate();
            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO EDITAR MEDICO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }
}
