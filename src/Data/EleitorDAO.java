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

public class EleitorDAO {

  public boolean votouPresidencial (int idEleicao, int ronda, int idCidadao) {
    Connection con  = null;
    int votou = 0;

    try {
      con = Connect.connect();

      PreparedStatement seVotou  = con.prepareStatement(
      "SELECT votou FROM RondaPresidencial AS RP INNER JOIN AssembleiaVoto AS AV " +
      "ON RP.id = AV.idRondaPresidencial INNER JOIN Eleitor AS E " +
      "ON AV.id = E.idAssembleiaVoto " +
      "WHERE RP.idEleicao = " + idEleicao +
      " AND RP.ronda = " + ronda +
      " AND E.idCidadao = " + idCidadao + ";");

      ResultSet rs = seVotou.executeQuery();

      if (rs.next())
        votou = rs.getInt("votou");

    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }

    boolean booleanVotou;

    if (ronda == 0)
      booleanVotou = true;
    else
      booleanVotou = false;

    return booleanVotou;
  }

  public boolean votouLegislativa (int idEleicao, int idCidadao) {
    Connection con  = null;
    int votou = 0;

    try {
      con = Connect.connect();

      PreparedStatement seVotou  = con.prepareStatement(
      "SELECT votou FROM Legislativa AS LL INNER JOIN Circulo AS C " +
      "ON LL.id = C.idLegislativa INNER JOIN AssembleiaVoto AS AV " +
      "ON C.id = AV.idCirculo INNER JOIN Eleitor AS E " +
      "ON AV.id = E.idAssembleiaVoto " +
      "WHERE AV.idEleicao = " + idEleicao +
      " AND E.idCidadao = " + idCidadao + ";");

      ResultSet rs = seVotou.executeQuery();

      if (rs.next())
        votou = rs.getInt("votou");

    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }

    boolean booleanVotou;

    if (votou == 0)
      booleanVotou = true;
    else
      booleanVotou = false;

    return booleanVotou;
  }
  
}
