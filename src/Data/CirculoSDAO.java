/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 * @author Gustavo
 */
public class CirculoSDAO {
     public int lastID() {
        Connection con = null;
        int r = -1;
        
        try {
            con                     = Connect.connect();
            PreparedStatement ps    = con.prepareStatement(
                    "select id" +
                    " from Circulo " +
                    " order by id DESC " +
                    " limit 1; "
            );
            ResultSet rs            = ps.executeQuery();
            
            if (rs.next()) {
               r = rs.getInt("id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }
        return r;
    }
     
     public void insert(Connection con,int idCirculo,String nCirculo,int idLegislativa) throws Exception {
             
          PreparedStatement insertCirculosSt = con.prepareStatement(
            "Insert into circulo (id, distrito, idlegislativa) values" +
		 "(" + (idCirculo) +",'Braga',"+idLegislativa + ");"
              );
             insertCirculosSt.executeUpdate();
     
     }
     
      public void insert(int idCirculo,String nCirculo,int idLegislativa){
        Connection con = null;
        try {
            con                     = Connect.connect();
          PreparedStatement insertCirculosSt = con.prepareStatement(
            "Insert into circulo (id, distrito, idlegislativa) values" +
		 "(" + (idCirculo) +",'Braga',"+idLegislativa + ");"
              );
             insertCirculosSt.executeUpdate();
              } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }
     
     }
}
