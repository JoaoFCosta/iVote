/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Business.Eleitor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @authors joaocosta, zcbg
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
    
    /** Lista de Eleitores.
   *  @return Lista de todas os eleitores que estão na base de dados. */
  public List<Eleitor> eleitores () {
    Connection con  = null;
    List lista      = (List) new ArrayList<Eleitor>();


    try {
      con                     = Connect.connect();

      // Statement para cidadaos.
      
      //TODO Alterar query de modo a filtrar só os eleitores!
      PreparedStatement eleitoresBD  = con.prepareStatement(
          "select * from Cidadao;");


      // Consultar eleições presidenciais.
      ResultSet rs = eleitoresBD.executeQuery();

      while (rs.next()) {
        int id           = rs.getInt("id");
        String nome      = rs.getString("nome");
        String codigo    = rs.getString("password");
       
        Eleitor e  = new Eleitor (nome,codigo,id);
        lista.add(e);
      }

    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }

    return lista;
  }
  
  /** @Adiciona um Eleitor à base de dados. */
  public int addEleitor (String nome, String password, int ccidadao, int idEleitor) {
    Connection con  = null;
    
    //Guarda o número de registos alterados
    int count=-1;
    
    try {
      con = Connect.connect();
      
      // Statement para a tabela cidadao.
      PreparedStatement eleitoresBD  = con.prepareStatement(
          "insert into Cidadao (id, idEleitor, password, nome) "+
          "values "+
          "("+ccidadao+","+idEleitor+","+"'"+password+"'"+","+"'"+nome+"'"+");"
          );

      // Executar inserção
      count = eleitoresBD.executeUpdate();

    } catch ( SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }
    return count;
  }
  
   public int lastID() {
        Connection con = null;
        int r = -1;
        
        try {
            con                     = Connect.connect();
            PreparedStatement ps    = con.prepareStatement(
                    "select idEleitor " +
                    " from Cidadao " +
                    " order by idEleitor DESC " +
                    " limit 1; "
            );
            ResultSet rs            = ps.executeQuery();
            
            if (rs.next()) {
               r = rs.getInt("idEleitor");
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
