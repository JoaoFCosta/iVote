/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author joaocosta
 */
public class CidadaoDAO {
    /** Confirma password de um dadao cidadao.
     *  @param ccidadao Número do cartão de cidadão.
     *  @param password Password a verificar.
     *  @return true se a password se confirmar e
     *          false caso contrário. */
    public boolean confirmaPassword (int ccidadao, String password) {
        Connection con = null;
        boolean r = false;
        
        try {
            con                     = Connect.connect();
            PreparedStatement ps    = con.prepareStatement(
                    "select * from Cidadao where id=" + ccidadao
            );
            ResultSet rs            = ps.executeQuery();
            
            if (rs.next()) {
               String pass  = rs.getString("password");
               r            = pass.equals(password);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }
        return r;
    }
}
