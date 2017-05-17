/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;

import com.epx.conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author BOTTAGO
 */
public class ParametrosDAO {

    public String parametroDirectorioRaiz() {
        Conexion con = new Conexion();
        String directorio = null;
        String sql = "select valorparametro from parametros where nombreparametro='directorioRaiz'";
        try {
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                directorio = rs.getString(1);
            }
        } catch (Exception e) {
            System.out.println("DAO PARAMETROS DIRECTORIO RAIZ: " + e.getMessage());
        }
        return directorio;
    }
}
