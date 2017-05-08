/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;
import com.epx.entity.AsignaRecurso;
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
public class AsignaRecursoDAO implements Serializable{
    public List<AsignaRecurso> recursosAsignadosbyRol(int idRol) {
        List<AsignaRecurso> lista = new ArrayList<>();
        Conexion con = new Conexion();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = "SELECT R.IDRECURSO,R.ITEM_LABEL,A.ESTADO FROM RECURSO R LEFT JOIN "
                + "(SELECT RR.IDRECURSO,RR.ESTADO FROM RECURSOROL RR INNER JOIN ROL R ON "
                + "RR.IDROL = R.IDROL WHERE R.IDROL = ?) A ON A.IDRECURSO = R.IDRECURSO";
        try {
            pst = con.getConnection().prepareStatement(query);
            pst.setInt(1, idRol);
            rs = pst.executeQuery();
            while (rs.next()) {
                AsignaRecurso ar = new AsignaRecurso();
                ar.setIdRecurso(rs.getInt(1));
                ar.setDescripcionRecurso(rs.getString(2));
                ar.setEstado((rs.getInt(3) != 0));
                lista.add(ar);
            }
        } catch (Exception e) {
            System.out.println("DAO ASIGNARECURSO: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(AsignaRecursoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }
    
    public boolean saveResourcesbyProfile(List<AsignaRecurso> listadoAR, int idrol, Usuario u) throws SQLException {
        boolean done=false;
        Conexion con = new Conexion();
        PreparedStatement pst;
        con.getConnection().setAutoCommit(false);
        String query = "MERGE RECURSOROL AS A "
                + "USING (SELECT ? AS IDROL,? AS IDRECURSO, ? AS ESTADO) AS T "
                + "ON (A.IDROL=T.IDROL AND A.IDRECURSO=T.IDRECURSO) "
                + "WHEN MATCHED THEN UPDATE SET A.ESTADO=T.ESTADO "
                + "WHEN NOT MATCHED THEN INSERT (IDROL,IDRECURSO,ESTADO)VALUES(?,?,?);";
        try {
            pst = con.getConnection().prepareStatement(query);
            for (AsignaRecurso ar : listadoAR) {
                pst.setInt(1, idrol);
                pst.setInt(2, ar.getIdRecurso());
                pst.setInt(3, ar.getEstado() == true ? 1 : 0);
                pst.setInt(4, idrol);
                pst.setInt(5, ar.getIdRecurso());
                pst.setInt(6, ar.getEstado() == true ? 1 : 0);
                pst.executeUpdate();
            }
            con.getConnection().commit();
            done=true;
        } catch (Exception e) {
            System.out.println("DAO ASIGNARECURSO: " + e.getMessage());
            con.getConnection().rollback();
            done=false;
        } finally {
            con.desconectar();
        }
        return done;
    }
}
