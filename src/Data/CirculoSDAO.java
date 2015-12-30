/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Exception.FailedInsert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
     
      public void insert(int idCirculo,String nCirculo,int idLegislativa) throws FailedInsert{
        Connection con = null;
        try {
            con                     = Connect.connect();
          PreparedStatement insertCirculosSt = con.prepareStatement(
            "Insert into circulo (id, distrito, idlegislativa) values" +
		 "(" + (idCirculo) +",'Braga',"+idLegislativa + ");"
              );
             insertCirculosSt.executeUpdate();
              } catch (SQLException | ClassNotFoundException e) {
                         throw new FailedInsert(e.toString());
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }
     
     }
      
      public List<String> alocarMandatos(int idEleicao, int idCirculo, int idLista, int numeroMandatos) {
        Connection con = null;
        List lista = new ArrayList<String>();
          
          try {
            con = Connect.connect();

            PreparedStatement mandatos = con.prepareStatement(
                "SELECT DISTINCT CID.nome,  CID.id\n" +
                "FROM Legislativa AS L INNER JOIN Circulo AS C\n" +
                "ON L.id = C.idLegislativa INNER JOIN AssembleiaVoto AS AV\n" +
                "ON C.id = AV.idCirculo INNER JOIN AssembleiaLista AS AL\n" +
                "ON AV.id = AL.idAssembleiaVoto INNER JOIN Lista AS LI\n" +
                "ON AL.idLista = LI.id INNER JOIN Candidato AS CA\n" +
                "ON LI.id = CA.idLista INNER JOIN Cidadao AS CID\n" +
                "ON CA.idCidadao = CID.id\n" +
                "WHERE L.idEleicao = " + idEleicao + " AND LI.id = " + idLista + " AND C.id = " + idCirculo + "\n" +
                "LIMIT " + numeroMandatos + ";");

            ResultSet rs = mandatos.executeQuery();
            
            while (rs.next()) {                
                String nome = rs.getString("nome");
                
                lista.add(nome);                
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }

        return lista;
          
      }
}
