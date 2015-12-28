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
  /*
    Cria uma eleicao presidencial, uma ronda presidencial e todas as assembleias de voto 
  */
  public int criaEleicaoPresidencial(Calendar data){
    Connection con  = null;
    int idEleicao = -1;
    int idRondaP = -1;
    int idAV = -1;
    
    try {
      con = Connect.connect();
      
      con.setAutoCommit(false); // transaction block start
       
      /*FIND last id's */
      PreparedStatement idEleicaoSt  = con.prepareStatement(
      "SELECT id FROM Eleicao ORDER BY id DESC LIMIT 1;");
      ResultSet rsE = idEleicaoSt.executeQuery();
      if (rsE.next())
        idEleicao = rsE.getInt("id") + 1;
      
      PreparedStatement idRondaPSt  = con.prepareStatement(
      "SELECT id FROM rondapresidencial ORDER BY id DESC LIMIT 1;");
      ResultSet rsRP = idEleicaoSt.executeQuery();
      if (rsRP.next())
        idRondaP = rsRP.getInt("id") + 1;
   
      PreparedStatement idAVSt = con.prepareStatement(
      "select id from assembleiavoto order by id DESC LIMIT 1;");
      ResultSet rsAV = idAVSt.executeQuery();
      if (rsAV.next())
          idAV = rsAV.getInt("id") + 1;
      
      /*INSERT Statements*/
      PreparedStatement insertEleicaoSt = con.prepareStatement(
        "insert into Eleicao (id) values ("+idEleicao+");");
      
      String dataE = "'" + data.get(Calendar.YEAR) + "-" + 
              data.get(Calendar.MONTH) + "-" + data.get(Calendar.DAY_OF_MONTH) + "'";
      
      PreparedStatement insertRondaPresidencialSt = con.prepareStatement(
        "insert into rondapresidencial (id, idEleicao, ronda, data) " +
"	VALUES " +
"	("+idRondaP+","+idEleicao+",1,"+ dataE +");");
      
      PreparedStatement insertAssembleiaVotoSt = con.prepareStatement("INSERT INTO AssembleiaVoto" +
"	(id, nome, idRondaPresidencial)" +
"	VALUES" +
           "	("+idAV+",'Gualtar',"+idRondaP+"), " +
"	(" +(idAV + 1) +",'Parque das Nações',"+idRondaP+"), " +
"	(" +(idAV + 2) +",'Campolide',"+idRondaP+"), " +
"	(" +(idAV + 3) +",'Mouros',"+idRondaP+")," +
"	(" +(idAV + 4) +",'São João',"+idRondaP+"), " +
"	(" +(idAV + 5) +",'Grândola',"+idRondaP +"), " +

"	(" +(idAV + 6) +",'São Vicente',"+idRondaP+"), " +
"	(" +(idAV + 7) +",'São Lázaro',"+idRondaP + "), " +
"	(" +(idAV + 8) +",'Santa Maria'," +idRondaP +"), " +
"	(" +(idAV + 9) +",'Feira',"+idRondaP +"), " +
"	(" +(idAV + 10) +",'Bandidos',"+idRondaP +"), " +
"	(" +(idAV + 11) +",'Alvalade',"+idRondaP +"), " +
               
"	(" +(idAV + 12) +",'Santo Tirso',"+idRondaP+"), " +
"	(" +(idAV + 13) +",'Santidade',"+idRondaP+"), " +
"	(" +(idAV + 14) +",'São Demagogo',"+idRondaP+"), " +
"	(" +(idAV + 15) +",'Dão',"+idRondaP + "), " +
"	(" +(idAV + 16) +",'Dona',"+ idRondaP + "), " +
"	(" +(idAV + 17) +",'Balte',"+ idRondaP +"), " +
               
"	(" +(idAV + 18) +",'Malte'," +idRondaP + "), " +
"	(" +(idAV + 19) +",'Ranhoso'," +idRondaP +"), " +
"	(" +(idAV + 20) +",'Campolide'," +idRondaP +"), " +
"	(" +(idAV + 21) +",'Mouros'," +idRondaP +"), " +
"	(" +(idAV + 22) +",'São João'," +idRondaP +"), " +
"	(" +(idAV + 23) +",'Grândola'," +idRondaP +");"
      );
      
      insertEleicaoSt.executeUpdate();
      insertRondaPresidencialSt.executeUpdate();
      insertAssembleiaVotoSt.executeUpdate();
      
      con.commit(); // transaction block end
      return idEleicao;
    }catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }
       
       return -1;
   }
   
   
  /*
    Cria  uma eleicao legislativa, os seus circulos e as assembleias de voto
  */
   public int criaEleicaoLegislativa(Calendar data){
    Connection con  = null;
    int idEleicao = -1;
    int idLegislativa = -1;
    int idCirculo = -1;
    int idAV = -1;
    try {
      con = Connect.connect();
      
      con.setAutoCommit(false); // transaction block start

            /*FIND last id's */
      PreparedStatement idEleicaoSt  = con.prepareStatement(
      "SELECT id FROM Eleicao ORDER BY id DESC LIMIT 1;");
      ResultSet rsE = idEleicaoSt.executeQuery();
      if (rsE.next())
        idEleicao = rsE.getInt("id") + 1;
      
      PreparedStatement idLegislativaSt = con.prepareStatement(
      "select id from legislativa order by id DESC LIMIT 1;");
      ResultSet rsL = idLegislativaSt.executeQuery();
      if (rsL.next())
          idLegislativa = rsL.getInt("id") + 1;
      
      PreparedStatement idCirculoSt = con.prepareStatement(
      "select id from circulo order by id DESC LIMIT 1;");
      ResultSet rsC = idCirculoSt.executeQuery(); 
      if (rsC.next())
          idCirculo = rsC.getInt("id") + 1;

      PreparedStatement idAVSt = con.prepareStatement(
      "select id from assembleiavoto order by id DESC LIMIT 1;");
      ResultSet rsAV = idAVSt.executeQuery();
      if (rsAV.next())
          idAV = rsAV.getInt("id") + 1;
      
      String dataE = "'" + data.get(Calendar.YEAR) + "-" + 
              data.get(Calendar.MONTH) + "-" + data.get(Calendar.DAY_OF_MONTH) + "'";
      
            /*INSERT Statements*/
      PreparedStatement insertEleicaoSt = con.prepareStatement(
        "insert into Eleicao (id) values ("+idEleicao+");");
      PreparedStatement insertEleicaoLSt = con.prepareStatement(
        "insert into legislativa (id,idEleicao,data)"
                + " values ("+idLegislativa+","+idEleicao+","+dataE+ ");");
      PreparedStatement insertCirculosSt = con.prepareStatement(
      "Insert into circulo (id, distrito, idlegislativa) values" +
		 "(" + (idCirculo) +",'Braga',"+idLegislativa + ")," +
		 "(" + (idCirculo + 1) +",'Porto',"+idLegislativa +")," +
		 "(" + (idCirculo + 2) +",'Lisboa',"+idLegislativa +")," +
		"(" + (idCirculo + 3) +",'Setúbal',"+idLegislativa +")," +
		"(" + (idCirculo + 4) + ",'Bragança',"+idLegislativa+ ")," +
		"(" + (idCirculo + 5) + ",'Vila Real',"+idLegislativa+");"
      );
      PreparedStatement insertAssembleiaVotoSt = con.prepareStatement(
       "INSERT INTO AssembleiaVoto " +
"	(id, nome, idCirculo) " +
"	VALUES " +
      "	("+idAV+",'Gualtar',"+idCirculo+"), " +
"	(" +(idAV + 1) +",'Parque das Nações',"+(idCirculo+1)+"), " +
"	(" +(idAV + 2) +",'Campolide',"+(idCirculo+2)+"), " +
"	(" +(idAV + 3) +",'Mouros',"+(idCirculo+3)+")," +
"	(" +(idAV + 4) +",'São João',"+(idCirculo+4)+"), " +
"	(" +(idAV + 5) +",'Grândola',"+(idCirculo+5) +"), " +

"	(" +(idAV + 6) +",'São Vicente',"+idCirculo+"), " +
"	(" +(idAV + 7) +",'São Lázaro',"+(idCirculo+1) + "), " +
"	(" +(idAV + 8) +",'Santa Maria'," +(idCirculo +2) +"), " +
"	(" +(idAV + 9) +",'Feira',"+(idCirculo +3) +"), " +
"	(" +(idAV + 10) +",'Bandidos',"+(idCirculo + 4) +"), " +
"	(" +(idAV + 11) +",'Alvalade',"+(idCirculo + 5) +"), " +
               
"	(" +(idAV + 12) +",'Santo Tirso',"+idCirculo+"), " +
"	(" +(idAV + 13) +",'Santidade',"+(idCirculo+1)+"), " +
"	(" +(idAV + 14) +",'São Demagogo',"+(idCirculo+2)+"), " +
"	(" +(idAV + 15) +",'Dão',"+(idCirculo+3) + "), " +
"	(" +(idAV + 16) +",'Dona',"+ (idCirculo+4) + "), " +
"	(" +(idAV + 17) +",'Balte',"+ (idCirculo+5) +"), " +
               
"	(" +(idAV + 18) +",'Malte'," +idCirculo + "), " +
"	(" +(idAV + 19) +",'Ranhoso'," +(idCirculo + 1) +"), " +
"	(" +(idAV + 20) +",'Campolide'," +(idCirculo + 2) +"), " +
"	(" +(idAV + 21) +",'Mouros'," +(idCirculo + 3) +"), " +
"	(" +(idAV + 22) +",'São João'," +(idCirculo + 4) +"), " +
"	(" +(idAV + 23) +",'Grândola'," +(idCirculo + 5) +");"
      );
      
      insertEleicaoSt.executeUpdate();
      insertEleicaoLSt.executeUpdate();
      insertCirculosSt.executeUpdate();
      insertAssembleiaVotoSt.executeUpdate();
      
      con.commit(); // transaction block end
      return idEleicao;
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }
    
    return -1;
  }
}
