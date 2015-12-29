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

/**
 *
 * @author Gustavo
 */
public class AssembleiaVotoSDAO {
        
    public int lastID() {
        Connection con = null;
        int r = -1;
        
        try {
            con                     = Connect.connect();
            PreparedStatement ps    = con.prepareStatement(
                    "select id" +
                    " from AssembleiaVoto " +
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
    
    public void insert(Connection con,int id,String freguesia,int idCirculo) throws Exception{
      PreparedStatement insertAssembleiaVotoSt = con.prepareStatement(
       "INSERT INTO AssembleiaVoto " +
        "  (id, nome, idCirculo) " +
         "	VALUES " +
      "	("+id+",'"+freguesia+"',"+idCirculo+");");
      insertAssembleiaVotoSt.executeUpdate();  
    }
    
    public void insert(int id,String freguesia,int idCirculo) {
        Connection con = null;
        
        try {
            con                     = Connect.connect();
      PreparedStatement insertAssembleiaVotoSt = con.prepareStatement(
       "INSERT INTO AssembleiaVoto " +
        "  (id, nome, idCirculo) " +
         "	VALUES " +
      "	("+id+",'"+freguesia+"',"+idCirculo+");");
      insertAssembleiaVotoSt.executeUpdate();
       } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }
    }
}
