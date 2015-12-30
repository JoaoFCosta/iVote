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
import java.util.logging.Level;
import java.util.logging.Logger;

public class EleitorDAO {

  public boolean votouPresidencial (int idEleicao, int ronda, int idCidadao) {
    Connection con  = null;
    int votou = 0;

    try {
      con = Connect.connect();

      PreparedStatement seVotou  = con.prepareStatement(
      "SELECT votou FROM RondaPresidencial AS RP INNER JOIN AssembleiaVoto AS AV\n" +
      "ON RP.id = AV.idRondaPresidencial INNER JOIN Eleitor AS E\n" +
      "ON AV.id = E.idAssembleiaVoto\n" +
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

    if (votou == 1)
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
      "SELECT votou FROM Legislativa AS LL INNER JOIN Circulo AS C\n" +
      "ON LL.id = C.idLegislativa INNER JOIN AssembleiaVoto AS AV\n" +
      "ON C.id = AV.idCirculo INNER JOIN Eleitor AS E\n" +
      "ON AV.id = E.idAssembleiaVoto\n" +
      "WHERE LL.idEleicao = " + idEleicao +
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

    if (votou == 1)
      booleanVotou = true;
    else
      booleanVotou = false;

    return booleanVotou;
  }

  public void votoPresidencial (int idEleicao, int ronda, int idCidadao) {
         Connection con  = null;

    try {
      con = Connect.connect();

      PreparedStatement getIdAssembleiaVoto = con.prepareStatement(
        "SELECT AV.id FROM RondaPresidencial AS RP INNER JOIN AssembleiaVoto AS AV\n" +
        "ON RP.id = AV.idRondaPresidencial INNER JOIN Eleitor AS E\n" +
        "ON AV.id = E.idAssembleiaVoto\n" +
        "WHERE RP.idEleicao = " + idEleicao + " AND RP.ronda = " + ronda + " AND E.idCidadao = " + idCidadao + ";");

      ResultSet rs = getIdAssembleiaVoto.executeQuery();

      int idAssembleiaVoto = 0;
      if (rs.next())
        idAssembleiaVoto = rs.getInt("id");


      PreparedStatement votar  = con.prepareStatement(
        "UPDATE Eleitor\n" +
        "SET votou = 1\n" +
        "WHERE idCidadao = " + idCidadao + " AND idAssembleiaVoto = " + idAssembleiaVoto + ";");

      votar.executeUpdate();

    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }
  }

  public void votoLegislativa (int idEleicao, int idCidadao) {
    Connection con  = null;

    try {
      con = Connect.connect();

      PreparedStatement getIdAssembleiaVoto = con.prepareStatement(
        "SELECT AV.id FROM Legislativa AS LL INNER JOIN Circulo AS C\n" +
        "ON LL.id = C.idLegislativa INNER JOIN AssembleiaVoto AS AV\n" +
        "ON C.id = AV.idCirculo INNER JOIN Eleitor AS E\n" +
        "ON AV.id = E.idAssembleiaVoto\n" +
        "WHERE LL.idEleicao = " + idEleicao + " AND E.idCidadao = " + idCidadao + ";");
      
      ResultSet rs = getIdAssembleiaVoto.executeQuery();

      int idAssembleiaVoto = 0;
      if (rs.next())
        idAssembleiaVoto = rs.getInt("id");


      PreparedStatement votar  = con.prepareStatement(
        "UPDATE Eleitor\n" +
        "SET votou = 1\n" +
        "WHERE idCidadao = " + idCidadao + " AND idAssembleiaVoto = " + idAssembleiaVoto + ";");

      votar.executeUpdate();

    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }
  }

}
