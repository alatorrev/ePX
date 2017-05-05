/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;

import com.epx.entity.Usuario;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.epx.conexion.Conexion;

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
}
