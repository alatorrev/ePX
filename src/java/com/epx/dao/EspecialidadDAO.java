/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;

import com.epx.entity.Especialidad;
import com.epx.entity.Medico;
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
public class EspecialidadDAO implements Serializable {

    public List<Especialidad> findAllEspecialidades() {
        Conexion con = new Conexion();
        List<Especialidad> listadoEspecialidad = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select * from especialidad";
        try {
            pst = con.getConnection().prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                Especialidad esp = new Especialidad();
                esp.setIdEspecialidad(rs.getInt(1));
                esp.setDescripcion(rs.getString(2));
                listadoEspecialidad.add(esp);
            }
        } catch (Exception e) {
            System.out.println("DAO LISTADO ESPECIALIDADES: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listadoEspecialidad;
    }

    public boolean AsignarEspecialidad(int[] idespecialidad, Medico med) throws SQLException {
        boolean done = false;
        Conexion con = new Conexion();
        PreparedStatement pst;
        con.getConnection().setAutoCommit(false);
        String query = "insert into med_espe(idmedico, idespecialidad) "
                + "values(?,?)";
        try {
            pst = con.getConnection().prepareStatement(query);
            for (int id : idespecialidad) {
                pst.setLong(1, med.getIdMedico());
                pst.setInt(2, id);
                pst.executeUpdate();
            }
            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO ASIGNA ESPECIALIDAD: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }
}
