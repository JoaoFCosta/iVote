package Data;

// Imports.
import Business.Eleicao;
import Business.EleicaoPresidencial;
import Business.EleicaoLegislativa;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author joaocosta
 */
public class EleicaoDAO {

  /** Lista de Eleições.
   *  @return Lista de todas as eleições que estão na base de dados. */
  public List<Eleicao> eleicoes () {
    Connection con  = null;
    List lista      = (List) new ArrayList<Eleicao>();


    try {
      con                     = Connect.connect();

      // Statement para as eleições legislativas e presidenciais.
      PreparedStatement legislativas  = con.prepareStatement(
          "select * from Legislativa;");
      PreparedStatement presidenciais = con.prepareStatement(
          "select * from RondaPresidencial where ronda=1;");

      // Consultar eleições presidenciais.
      ResultSet rs = presidenciais.executeQuery();

      while (rs.next()) {
        // Fazer parse da data e criar nova eleicao.
        DateFormat df   = new SimpleDateFormat("yyyy-MM-dd");
        Date dt         = df.parse(rs.getString("data"));
        Calendar cal    = Calendar.getInstance();
        cal.setTime(dt);
        int id          = rs.getInt("idEleicao");

        Eleicao e       = new EleicaoPresidencial(cal, id);
        lista.add(e);
      }

      // Consultar eleições legislativas.
      rs = legislativas.executeQuery();

      while (rs.next()) {
        // Fazer parse da data e criar nova eleicao.
        DateFormat df   = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dt         = df.parse(rs.getString("data"));
        Calendar cal    = Calendar.getInstance();
        cal.setTime(dt);
        int id          = rs.getInt("idEleicao");

        Eleicao e       = new EleicaoLegislativa(cal, id);
        lista.add(e);
      }
    } catch (ParseException | SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }

    return lista;
  }
  
  public int idMaisRecenteEleicao () {
    Connection con = null;
    int id = -1;

    try {
      con = Connect.connect();

      PreparedStatement idEleicao  = con.prepareStatement(
      "SELECT id FROM Eleicao ORDER BY id DESC LIMIT 1;");

      ResultSet rs = idEleicao.executeQuery();

      if (rs.next())
        id = rs.getInt("id");

    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }

    return id;
  }

  // Retorna se é eleição presidencial.
  public boolean ePresidencial (int idEleicao) {
    Connection con = null;
    boolean ePresidencial = false;

    try {
      con = Connect.connect();

      PreparedStatement rondasPresendenciais  = con.prepareStatement(
      "SELECT * FROM Eleicao AS E INNER JOIN RondaPresidencial AS RP " +
      "ON E.id = RP.idEleicao WHERE E.id = " + idEleicao + ";");

      ResultSet rs = rondasPresendenciais.executeQuery();

      if (rs.next())
        ePresidencial = true;

    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }

    return ePresidencial;
  }

  public int rondaMaisRecente (int idEleicao) {
    Connection con  = null;
    int ronda = 1;

    try {
      con = Connect.connect();

      PreparedStatement rondaPresidencial  = con.prepareStatement(
      "SELECT RP.ronda FROM Eleicao AS E INNER JOIN RondaPresidencial AS RP " +
      "ON E.id = RP.idEleicao WHERE E.id = " + idEleicao +
      " ORDER BY RP.ronda DESC LIMIT 1;");

      ResultSet rs = rondaPresidencial.executeQuery();

      if (rs.next())
        ronda = rs.getInt("ronda");

    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }

    return ronda;
  }
  
  //TODO @return number of rows updated on DB
  public int criaEleicaoPresidencial(String data){
    return 1;
  }
  public int criaEleicaoLegislativa(String data){
    return 1;
  }
}
