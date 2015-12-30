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
import java.sql.Statement;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @authors joaocosta, zcbg
 */
public class CidadaoDAO implements Map<Integer, Eleitor> {
  /** @return Número de eleitores no sistema. */
  @Override
  public int size () {
    Connection con  = null;
    int size        = 0;
    PreparedStatement ps;
    ResultSet rs;

    try {
      con = Connect.connect();
      ps  = con.prepareStatement(
          "select count(*) from Cidadao;");
      rs  = ps.executeQuery();

      if (rs.next())
        size = rs.getInt("count(*)");
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (SQLException e) { System.out.println(e); }
    }

    return size;
  }

  /** @return true se não existir nenhum cidadão na base de dados e false
   *          caso contrário. */
  @Override
  public boolean isEmpty () { return (size() == 0); }

  /** @param key Número do cartão de cidadão.
   *  @return true se o número do cartão de cidadão já estiver no sistema
   *          e false caso contrário. */
  @Override
  public boolean containsKey (Object key) {
    Connection con  = null;
    int ccidadao    = (int) key;
    boolean existe  = true;
    PreparedStatement ps;
    ResultSet rs;

    try {
      con = Connect.connect();
      ps  = con.prepareStatement(
        "select * from Cidadao where id=" + ccidadao + ";");
      rs  = ps.executeQuery();

      existe = rs.next();
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (SQLException e) { System.out.println(e); }
    }

    return existe;
  }

  /** Verificar se um dado cidadão já se encontra no sistema.
   *  @param value Eleitor que se deseja verificar se existe no sistema.
   *  @return true se o eleitor já estiver presente no sistema ou false
   *          caso contrário. */
  @Override
  public boolean containsValue (Object value) {
    Eleitor eleitor = (Eleitor) value;
    return containsKey(Integer.parseInt(eleitor.getCodigo()));
  }

  /** Obter a instância de um dado eleitor.
   *  @param key Número do cartão de cidadão do eleitor.
   *  @return null se não existir eleitor para o parâmetro fornecido ou a
   *          instância desse Eleitor se existir. */
  @Override
  public Eleitor get (Object key) {
    Connection con  = null;
    Eleitor eleitor = null;
    int ccidadao    = (int) key;
    PreparedStatement ps;
    ResultSet rs;

    try {
      con = Connect.connect();
      ps  = con.prepareStatement(
        "select * from Cidadao where id=" + ccidadao + ";");
      rs  = ps.executeQuery();

      if (rs.next()) {
        eleitor = new Eleitor(rs.getString("nome"),
          rs.getString("password"), rs.getInt("id"));
      }
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (SQLException e) { System.out.println(e); }
    }

    return eleitor;
  }

  /** Inserir um eleitor no sistema. Se este método for usado a password do
   *  eleitor vai ser igual a '0000'.
   *  @param key Número do cartão de cidadão.
   *  @param value Eleitor a ser inserido.
   *  @return Se já existisse um eleitor com o mesmo número do cartão de cidadão
   *          então é devolvida a instância desse eleitor, caso contrário é
   *          devolvido null. */
  @Override
  public Eleitor put (Integer key, Eleitor value) {
    Connection con  = null;
    Eleitor eleitor = null;
    Eleitor aux     = (Eleitor) value;
    PreparedStatement ps;
    ResultSet rs;

    try {
      con = Connect.connect();

      /*  Verificar se já existe eleitor associado ao número fornecido e
          remover da base de dados. */
      if (containsKey(key)) {
        eleitor = get(value);
        remove(key);
      }

      con = Connect.connect();
      ps  = con.prepareStatement(
        "insert into Cidadao"
        + "(id, idEleitor, password, nome)"
        + "values"
        + "(" + aux.getCodigo() + "," + (size() + 1) + ",'0000'," + aux.getNome() + ");"
      );
      rs  = ps.executeQuery();
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }

    return eleitor;
  }
  
  /** Inserir um eleitor no sistema.
   *  @param key Número do cartão de cidadão.
   *  @param value Eleitor a ser inserido.
   *  @param password Password do eleitor.
   *  @return Se já existisse um eleitor com o mesmo número do cartão de cidadão
   *          então é devolvida a instância desse eleitor, caso contrário é
   *          devolvido null. */
  public Eleitor put (Integer key, Eleitor value, String password) {
    Connection con  = null;
    Eleitor eleitor = null;
    Eleitor aux     = (Eleitor) value;
    PreparedStatement ps;
    ResultSet rs;

    try {
      con = Connect.connect();
      /*  Verificar se já existe eleitor associado ao número fornecido e
          remover da base de dados. */
      if (containsKey(key)) {
        eleitor = get(value);
        remove(key);
      }

      ps  = con.prepareStatement(
        "insert into Cidadao "
        + "(id, idEleitor, password, nome) "
        + "values "
        + "(" + aux.getCodigo() + "," + (size() + 1) + ",'"
          + password +"','" + aux.getNome() + "');");
      ps.executeUpdate();
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }

    return eleitor;
  }

  /** Remover um eleitor do sistema.
   *  @param key  Número do cartão de cidadão do eleitor.
   *  @return Instância do Eleitor se existia algum ou null caso não
   *          estivesse presente no sistema. */
  @Override
  public Eleitor remove (Object key) {
    Connection con  = null;
    Eleitor eleitor = null;
    int ccidadao    = (int) key;
    PreparedStatement ps;
    ResultSet rs;

    try {
      con = Connect.connect();

      // Verificar se existe eleitor associado ao número do cc.
      if (containsKey(key)) eleitor = get(key);
      ps  = con.prepareStatement(
        "delete from Cidadao where id=" + ccidadao + ";");
      ps.executeUpdate();
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (SQLException e) { System.out.println(e); }
    }

    return eleitor;
   }

  /** Inserir uma lista de Eleitores na base de dados. Apenas serão inseridos
   *  eleitores cujo número do cartão de cidadão não esteja a ser utilizado.
   *  @param m  Map de int, que representa o número do cartão de cidadão, para
   *            Eleitor. */
  @Override
  public void putAll (Map<? extends Integer, ? extends Eleitor> m) {
    for (Eleitor e : m.values()) {
      if (!containsKey(e.getCodigo()))
        this.put(Integer.parseInt(e.getCodigo()), e);
    }
  }

  /** Remover todos os cidadãos presentes na base de dados. */
  @Override
  public void clear () {
    Connection con = null;
    PreparedStatement ps;
    ResultSet rs;

    try {
      con = Connect.connect();
      ps  = con.prepareStatement("delete from Cidadao;");
      rs  = ps.executeQuery();
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }
  }

  /** Lista dos números de cidadão presentes na base de dados.
   *  @return Set com todos os números de cartão de cidadão da base de dados. */
  @Override
  public Set<Integer> keySet () {
    Connection con        = null;
    TreeSet<Integer> set  = new TreeSet<>();
    PreparedStatement ps;
    ResultSet rs;

    try {
      con = Connect.connect();
      ps  = con.prepareStatement("select id from Cidadao;");
      rs  = ps.executeQuery();

      while (rs.next())
        set.add(rs.getInt("id"));
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (SQLException e) { System.out.println(e); }
    }

    return set;
  }

  /** Lista com todas as instâncias de Eleitor presentes na base de dados.
   *  @return Collection com todas as instâncias de Eleitor presentes na base
   *          de dados. */
  @Override
  public Collection<Eleitor> values () {
    Connection con        = null;
    Collection<Eleitor> c = new ArrayList<>();
    PreparedStatement ps;
    ResultSet rs;
    Eleitor el;

    try {
      con = Connect.connect();
      ps  = con.prepareStatement("select id, password, nome from Cidadao;");
      rs  = ps.executeQuery();

      while (rs.next()) {
        el = new Eleitor(rs.getString("nome"), rs.getString("password"), 
          rs.getInt("id"));
        c.add(el);
      }
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (SQLException e) { System.out.println(e); }
    }

    return c;
  }

  /** Todos os números de cartão de cidadão associados a todos os cidadãos
   *  presentes na base de dados.
   *  @return Set com todas correspondências.
   */
  @Override
  public Set<Entry<Integer, Eleitor>> entrySet () {
    Connection con                   = null;
    Set<Entry<Integer, Eleitor>> set = new TreeSet<>();
    PreparedStatement ps;
    ResultSet rs;

    try {
      con = Connect.connect();
      ps  = con.prepareStatement("select id, nome from Cidadao;");
      rs  = ps.executeQuery();

      while (rs.next()) {
        String id  = rs.getString("id");
        String nome = rs.getString("nome");

        set.add(new SimpleEntry(Integer.parseInt(id), new Eleitor(id, nome)));
      }
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (SQLException e) { System.out.println(e); }
    }

    return set;
  }

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
  
  public String nome (int idCidadao) {
        
        Connection con  = null;
        
        String nome = "";
    
        try {
            con = Connect.connect();
      
            // Statement para a tabela cidadao.
            PreparedStatement nomeQuery  = con.prepareStatement(
                    "SELECT nome\n"+
                    "FROM Cidadao\n"+
                    "WHERE id = " + idCidadao + ";");

            // Executar inserção
            ResultSet rs = nomeQuery.executeQuery();
            
            if (rs.next()) {
               nome = rs.getString("nome");
            }

        } catch ( SQLException | ClassNotFoundException e) {
             System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }
        return nome;
    }

  /*
  Main for testing purposes.

  // TODO: Testar os métodos.

  public static void main (String args[]) {
    System.out.println("1 - " + (size() == 5000));
    System.out.println("2 - " + (isEmpty() == false));
    System.out.println("3 - " + (containsKey(12345678) == true));
    System.out.println("4 - " + (containsKey(123) == false));

    Eleitor e = new Eleitor("João Portas", "12345678","Braga","Essa freguesia",
      new GregorianCalendar());
    System.out.println("5 - " + (containsValue(e) == true));
    e = new Eleitor("João Portas", "1234","Braga","Essa freguesia",
      new GregorianCalendar());
    System.out.println("6 - " + (containsValue(e) == false));
  }
  */
}
