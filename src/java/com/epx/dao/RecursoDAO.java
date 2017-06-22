/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;

import com.epx.entity.Recurso;
import com.epx.entity.Usuario;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.epx.conexion.Conexion;

/**
 *
 * @author Bottago SA
 */
public class RecursoDAO implements Serializable {

    public List<Recurso> findAllRecursoByRol(Usuario u) throws SQLException {
        Conexion con = new Conexion();
        List<Recurso> lista = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "SELECT RE.IDRECURSO,R.IDROL,RE.ITEM_LABEL,RE.SUBITEM_LABEL,RE.RUTA,RE.ITEM_ICON,RE.SUBITEM_ICON "
                + "FROM ROL R "
                + "INNER JOIN RECURSOROL RR ON R.IDROL=RR.IDROL "
                + "INNER JOIN RECURSO RE ON RR.IDRECURSO=RE.IDRECURSO "
                + "WHERE R.IDROL=? AND RR.ESTADO=1 "
                + "order by re.prioridad";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, u.getIdRol());
            rs = pst.executeQuery();
            while (rs.next()) {
                Recurso recurso = new Recurso();
                recurso.setIdRecurso(rs.getInt(1));
                recurso.setIdRol(rs.getInt(2));
                recurso.setItemLabel(rs.getString(3));
                recurso.setSubItemLabel(rs.getString(4));
                recurso.setRuta(rs.getString(5));
                recurso.setItemIcon(rs.getString(6));
                recurso.setSubItemIcon(rs.getString(7));
                lista.add(recurso);
            }
        } catch (Exception e) {
            System.out.println("DAO RECURSO: " + e.getMessage());
        } finally {
            con.desconectar();
        }

        return lista;
    }
}
