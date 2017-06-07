/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;

import com.epx.entity.Medico;
import com.epx.conexion.Conexion;
import com.epx.entity.Especialidad;
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
        String sql = "Select idmedico, fuentemedico, "
                + "nombres, apellidos, direccion, (especialidad + ', ' + especialidad2) as especialidad "
                + "from medico_difare "
                + "where nombres+(case when apellidos IS NULL then '' else apellidos end) like upper(?) "
                + "union all "
                + "select m.idmedico, m.fuentemedico, "
                + "m.nombres, m.apellidos, m.direccion, "
                + "(STUFF(REPLACE((SELECT '#!' + LTRIM(RTRIM(e.descripcion)) AS 'data()' FROM especialidad e "
                + "left join med_espe med on m.idmedico = med.idmedico "
                + "where e.idespecialidad = med.idespecialidad "
                + "FOR XML PATH('')),' #!',', '), 1, 2, '')) as Brands "
                + "from medico_bottago m "
                + "left join med_espe e on m.idmedico = e.idmedico "
                + "left join especialidad es on e.idespecialidad = es.idespecialidad "
                + "where m.nombres+(case when m.apellidos IS NULL then '' else m.apellidos end) like upper(?) "
                + "group by m.idmedico, m.fuentemedico, m.nombres, m.apellidos, m.direccion";
        try {
            pst = con.getConnection().prepareStatement(sql);
            pst.setString(1, "%" + cadena.trim().concat("%"));
            pst.setString(2, "%" + cadena.trim().concat("%"));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Medico med = new Medico();
                med.setIdMedico(rs.getLong(1));
                med.setFuente(rs.getString(2));
                med.setNombres(rs.getString(3));
                med.setApellidos(rs.getString(4));
                med.setDireccion(rs.getString(5));
                med.setEspecialidad(rs.getString(6));
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
            pst.setTimestamp(5, med.getFechaNacimiento() == null ? null : new java.sql.Timestamp(med.getFechaNacimiento().getTime()));
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

    public boolean createOnFlyMedico(Medico med, List<Especialidad> idespecialidad, Usuario u) {
        Conexion con = new Conexion();
        boolean done = false;
        int id = 0;
        String sql = "select (1 + isnull(max(idmedico),0)) as id from medico_bottago";
        try {
            con.getConnection().setAutoCommit(false);
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }
            String sql2 = "insert into medico_bottago (idmedico, fuentemedico, nombres, apellidos, cedula, "
                    + "direccion, fechanacimiento, fechacreacion, usuariocreacion) "
                    + "values(?, 'B', ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
            pst = con.getConnection().prepareStatement(sql2);
            pst.setInt(1, id);
            pst.setString(2, (med.getNombres()==null || med.getNombres().length()==0)?null:med.getNombres().toUpperCase());
            pst.setString(3, (med.getApellidos()==null || med.getApellidos().length()==0)?null:med.getApellidos().toUpperCase());
            pst.setString(4, (med.getCedula()==null || med.getCedula().length()==0)?null:med.getCedula());
            pst.setString(5, (med.getDireccion()==null || med.getDireccion().length()==0)?null:med.getDireccion());
            pst.setTimestamp(6, med.getFechaNacimiento() == null ? null : new java.sql.Timestamp(med.getFechaNacimiento().getTime()));
            pst.setString(7, u.getLoginname());
            pst.executeUpdate();

            String sql3 = "insert into med_espe(idmedico, idespecialidad) values(?,?)";
            pst = con.getConnection().prepareStatement(sql3);
            for (Especialidad e : idespecialidad) {
                pst.setInt(1, id);
                pst.setInt(2, e.getIdEspecialidad());
                pst.executeUpdate();
            }
            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO MEDICO ONFLY: " + e.getMessage());
            done = false;
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(MedicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return done;
    }
}
