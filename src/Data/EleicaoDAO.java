package Data;

// Imports.
import Business.AssembleiaCandidato;
import Business.Eleicao;
import Business.EleicaoPresidencial;
import Business.EleicaoLegislativa;
import Exception.FailedInsert;
import Business.Lista;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @authors joaocosta,zcbg
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

            PreparedStatement idEleicao = con.prepareStatement(
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

            PreparedStatement rondasPresendenciais = con.prepareStatement(
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

            PreparedStatement rondaPresidencial = con.prepareStatement(
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

    public Map<Integer, Integer> votosNulosPorAssembleia (int idEleicao, int idRonda) {
        Connection con = null;

        Map<Integer,Integer> votosNulosPorAssembleia = new HashMap<>();

        try {
            con = Connect.connect();

            PreparedStatement votosNulosResult = con.prepareStatement(
                    "SELECT idAssembleiaVoto, votosNulos\n" +
                            "FROM (SELECT E.id AS idEleicao, AV.id AS idAssembleiaVoto, votosBrancos, votosNulos\n" +
                            "FROM Eleicao AS E INNER JOIN RondaPresidencial AS RP\n" +
                            "ON E.id = RP.idEleicao INNER JOIN AssembleiaVoto AS AV\n" +
                            "ON RP.id = AV.idRondaPresidencial\n" +
                            "WHERE RP.ronda = " + idRonda + "\n" +
                            "UNION\n" +
                            "SELECT E.id AS idEleicao, AV.id AS idAssembleiaVoto, votosBrancos, votosNulos\n" +
                            "FROM Eleicao AS E INNER JOIN Legislativa AS L\n" +
                            "ON E.id = L.idEleicao INNER JOIN Circulo AS C\n" +
                            "ON L.id = C.idLegislativa INNER JOIN AssembleiaVoto AS AV\n" +
                            "ON C.id = AV.idCirculo) AS Eleicoes\n" +
                            "WHERE idEleicao = " + idEleicao + ";");

            ResultSet rs = votosNulosResult.executeQuery();

            Integer idAssembleiaVoto, votosNulos;
            while (rs.next()) {
                idAssembleiaVoto = rs.getInt("idAssembleiaVoto");
                votosNulos = rs.getInt("votosNulos");
                votosNulosPorAssembleia.put(idAssembleiaVoto, votosNulos);
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }

        return votosNulosPorAssembleia;
    }


    public Map<Integer, Integer> votosBrancosPorAssembleia (int idEleicao, int idRonda) {
        Connection con  = null;
        Map<Integer,Integer> votosBrancosPorAssembleia = new HashMap<>();

        try {
            con = Connect.connect();

            PreparedStatement votosBrancosResult = con.prepareStatement(
                    "SELECT idAssembleiaVoto, votosBrancos\n" +
                            "FROM (SELECT E.id AS idEleicao, AV.id AS idAssembleiaVoto, votosBrancos, votosNulos\n" +
                            "FROM Eleicao AS E INNER JOIN RondaPresidencial AS RP\n" +
                            "ON E.id = RP.idEleicao INNER JOIN AssembleiaVoto AS AV\n" +
                            "ON RP.id = AV.idRondaPresidencial\n" +
                            "WHERE RP.ronda = " + idRonda + "\n" +
                            "UNION\n" +
                            "SELECT E.id AS idEleicao, AV.id AS idAssembleiaVoto, votosBrancos, votosNulos\n" +
                            "FROM Eleicao AS E INNER JOIN Legislativa AS L\n" +
                            "ON E.id = L.idEleicao INNER JOIN Circulo AS C\n" +
                            "ON L.id = C.idLegislativa INNER JOIN AssembleiaVoto AS AV\n" +
                            "ON C.id = AV.idCirculo) AS Eleicoes\n" +
                            "WHERE idEleicao = " + idEleicao + ";");

            ResultSet rs = votosBrancosResult.executeQuery();

            Integer idAssembleiaVoto, votosBrancos;
            while (rs.next()) {
                idAssembleiaVoto = rs.getInt("idAssembleiaVoto");
                votosBrancos = rs.getInt("votosBrancos");
                votosBrancosPorAssembleia.put(idAssembleiaVoto, votosBrancos);
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }

        return votosBrancosPorAssembleia;
    }

  public void insert(int idEleicao) throws FailedInsert{
    Connection con  = null;  
    try {
      con = Connect.connect();
          PreparedStatement insertEleicaoSt = con.prepareStatement(
        "insert into Eleicao (id) values ("+idEleicao+");");
         insertEleicaoSt.executeUpdate();}
        catch (SQLException | ClassNotFoundException e) {
            throw new FailedInsert();
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }
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
            try {
                con.rollback(); //anula transacao
            } catch (SQLException ex) {
                Logger.getLogger(EleicaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            try {
                con.rollback(); //anula transacao
            } catch (SQLException ex) {
                Logger.getLogger(EleicaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }

        return -1;
    }

    public void votoLegislativa (int idEleicao, int idCidadao, int idLista) {
        Connection con  = null;

        try {
            con = Connect.connect();

            PreparedStatement votoPresidencial = con.prepareStatement(
                    "UPDATE AssembleiaLista\n" +
                            "SET votos = votos +1\n" +
                            "WHERE idLista = " + idLista + " AND idAssembleiaVoto = (SELECT AV.id\n" +
                            "FROM Eleicao AS E INNER JOIN Legislativa AS L\n" +
                            "ON E.id = L.idEleicao INNER JOIN Circulo AS C\n" +
                            "ON L.id = C.idLegislativa INNER JOIN AssembleiaVoto AS AV\n" +
                            "ON C.id = AV.idCirculo INNER JOIN Eleitor AS EL\n" +
                            "ON AV.id = EL.idAssembleiaVoto\n" +
                            "WHERE E.id = " + idEleicao + " AND EL.idCidadao = " + idCidadao +");");

            votoPresidencial.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }
    }

    public void votoPresidencial (int idEleicao, int ronda, int idCidadao, int idCandidato) {
        Connection con  = null;

        try {
            con = Connect.connect();

            PreparedStatement votoPresidencial = con.prepareStatement(
                    "UPDATE AssembleiaCandidato\n" +
                            "SET votos = votos +1\n" +
                            "WHERE idCidadao = " + idCandidato + " AND idAssembleiaVoto = (SELECT AV.id\n" +
                            "FROM Eleicao AS E INNER JOIN RondaPresidencial AS RP\n" +
                            "ON E.id = RP.idEleicao INNER JOIN AssembleiaVoto AS AV\n" +
                            "ON RP.id = AV.idRondaPresidencial INNER JOIN Eleitor AS EL\n" +
                            "ON AV.id = EL.idAssembleiaVoto\n" +
                            "WHERE E.id = " + idEleicao + " AND RP.ronda = " + ronda + " AND EL.idCidadao = " + idCidadao +");");

            votoPresidencial.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }
    }
    public Collection<String> opcoesVotoLegislativa(int idEleicao,int cc){
        
        Connection con  = null;
        ArrayList<String> listas = new ArrayList<String>();
        try {
            con = Connect.connect();

            PreparedStatement listasParaVotar = con.prepareStatement("select distinct L.id " +
"	from cidadao AS C " +
"		inner join eleitor as E " +
"			ON E.idCidadao = C.id " +
"				inner join (select av.id " +
"						from Eleicao as E " +
"						 inner join Legislativa as L " +
"							ON L.idEleicao = E.id " +
"							  inner join circulo as C " +
"							    ON C.idLegislativa = L.id" +
"							     inner join assembleiavoto as av" +
"							      ON av.idCirculo = C.id" +
"							        where E.id = "+ idEleicao + ") as av" +
"					ON E.idAssembleiaVoto = av.id " +
"					  inner join AssembleiaLista as AL " +
"						ON AL.idAssembleiaVoto = av.id " +
"					          inner join Lista as L " +
"				        		ON L.id = AL.idLista " +
"    where c.id =" + cc +
"    order by L.id;"
                    );
            
            ResultSet rs = listasParaVotar.executeQuery();

            while (rs.next()) {
                int lista = rs.getInt("id");
                listas.add("Lista "+lista);
            }
            
      }catch (SQLException | ClassNotFoundException e) {
        System.out.println(e);
      } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
       }

        return listas;
    }
    
    private int idAssembleiaVoto (Connection con, int idEleicao, int ronda, int idCidadao) {
        
        int idAssembleiaVoto = 0;

        try {    
            PreparedStatement idAssembleiaVotoQuery = con.prepareStatement(
                "SELECT idAssembleiaVoto\n" +
                "FROM (SELECT E.id AS idEleicao, AV.id AS idAssembleiaVoto, EL.idCidadao\n" +
                "FROM Eleicao AS E INNER JOIN RondaPresidencial AS RP\n" +
                "ON E.id = RP.idEleicao INNER JOIN AssembleiaVoto AS AV\n" +
                "ON RP.id = AV.idRondaPresidencial INNER JOIN Eleitor AS EL\n" +
                "ON AV.id = EL.idAssembleiaVoto\n" +
                "WHERE RP.ronda = " + ronda +"\n" +
                "UNION\n" +
                "SELECT E.id AS idEleicao, AV.id AS idAssembleiaVoto, EL.idCidadao\n" +
                "FROM Eleicao AS E INNER JOIN Legislativa AS L\n" +
                "ON E.id = L.idEleicao INNER JOIN Circulo AS C\n" +
                "ON L.id = C.idLegislativa INNER JOIN AssembleiaVoto AS AV\n" +
                "ON C.id = AV.idCirculo INNER JOIN Eleitor AS EL\n" +
                "ON AV.id = EL.idAssembleiaVoto) AS Cenas\n" +
                "WHERE idEleicao = " + idEleicao + " AND idCidadao = " + idCidadao + ";");

            ResultSet rs = idAssembleiaVotoQuery.executeQuery();
            
            if (rs.next())
                idAssembleiaVoto = rs.getInt("idAssembleiaVoto");
            
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
        }
        
        return idAssembleiaVoto;
    }
    
    public void voto (int idEleicao, int ronda, int idCidadao, boolean votoBranco) {
        String set;
        
        if (votoBranco)
            set = "SET votosBrancos = votosBrancos +1\n";
        else
            set = "SET votosNulos = votosNulos +1\n";
              Connection con  = null;

        try {
            con = Connect.connect();
            int idAssembleiaVoto = idAssembleiaVoto(con, idEleicao, ronda, idCidadao);
            
            PreparedStatement idAssembleiaVotoQuery = con.prepareStatement(
                    "UPDATE AssembleiaVoto\n" + 
                    set + 
                    "WHERE id = " + idAssembleiaVoto + ";");
            
            idAssembleiaVotoQuery.executeUpdate();
        
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }     
    }    
    
     public int eleicaoAberta  () {
        Connection con  = null;
        int eleicao = 0;

        try {
            con = Connect.connect();

            // Statement para as eleições legislativas e presidenciais.
            PreparedStatement eleicoes = con.prepareStatement(
                "SELECT DISTINCT idEleicao, data\n" +
                "FROM (SELECT E.id AS idEleicao, AV.id AS idAssembleiaVoto, EL.idCidadao, RP.data\n" +
                "FROM Eleicao AS E INNER JOIN RondaPresidencial AS RP\n" +
                "ON E.id = RP.idEleicao INNER JOIN AssembleiaVoto AS AV\n" +
                "ON RP.id = AV.idRondaPresidencial INNER JOIN Eleitor AS EL\n" +
                "ON AV.id = EL.idAssembleiaVoto\n" +
                "UNION\n" +
                "SELECT E.id AS idEleicao, AV.id AS idAssembleiaVoto, EL.idCidadao, L.data\n" +
                "FROM Eleicao AS E INNER JOIN Legislativa AS L\n" +
                "ON E.id = L.idEleicao INNER JOIN Circulo AS C\n" +
                "ON L.id = C.idLegislativa INNER JOIN AssembleiaVoto AS AV\n" +
                "ON C.id = AV.idCirculo INNER JOIN Eleitor AS EL\n" +
                "ON AV.id = EL.idAssembleiaVoto) AS eleicoes" +
                "ORDER BY data DESC;");

            // Consultar eleições presidenciais.
            ResultSet rs = eleicoes.executeQuery();
            
            Date hoje = Calendar.getInstance().getTime();

            while (rs.next()) {
                int id          = rs.getInt("idEleicao");

                DateFormat df   = new SimpleDateFormat("yyyy-MM-dd");
                Date diaEleicao = df.parse(rs.getString("data"));     
                
                if (hoje.equals(diaEleicao))
                    eleicao = id;
            }
            
  
            
        } catch (ParseException | SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }

        return eleicao;
    }
     
    public Map<Integer, Integer> votosCandidatos (int idEleicao) {
        Connection con  = null;
        Map votosCandidatos = (Map) new HashMap<Integer, Integer>();

        try {
            con = Connect.connect();
            
            PreparedStatement rondaPresidencial = con.prepareStatement(
                    "SELECT RP.ronda FROM Eleicao AS E INNER JOIN RondaPresidencial AS RP " +
                            "ON E.id = RP.idEleicao WHERE E.id = " + idEleicao +
                            " ORDER BY RP.ronda DESC LIMIT 1;");

            ResultSet rs = rondaPresidencial.executeQuery();
            
            int ronda = 1;
            if (rs.next())
                ronda = rs.getInt("ronda");

            PreparedStatement presidencial = con.prepareStatement(
                    "SELECT AC.idCidadao, SUM(AC.votos) AS votos\n" +
                    "FROM AssembleiaCandidato AS AC INNER JOIN AssembleiaVoto AS AV\n" +
                    "ON AC.idAssembleiaVoto = AV.id INNER JOIN RondaPresidencial AS RP\n" +
                    "ON AV.idRondaPresidencial = RP.id\n" +
                    "WHERE RP.idEleicao = "+ idEleicao + " AND RP.ronda = " + ronda +"\n" +
                    "GROUP BY AC.idCidadao\n" + 
                    "ORDER BY votos DESC;");

            // Consultar eleições presidenciais.
            rs = presidencial.executeQuery();

            while (rs.next()) {
                // Fazer parse da data e criar nova eleicao.
            
                int id    = rs.getInt("idCandidato");
                int votos = rs.getInt("votos");
                
                votosCandidatos.put(id, votos);
                
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }

        return votosCandidatos;
    }
    
    public Map<Integer, List<Lista>> votosCirculoLista (int idEleicao) {
        Connection con  = null;
        Map votosCirculoLista = new HashMap<Integer, List<Lista>>();

        try {
            con = Connect.connect();

            PreparedStatement legislativa = con.prepareStatement(
                "SELECT C.id AS idCirculo, AL.idLista, SUM(AL.votos) AS votos\n" +
                "FROM AssembleiaLista AS AL INNER JOIN AssembleiaVoto AS AV\n" +
                "ON AL.idAssembleiaVoto = AV.id INNER JOIN Circulo AS C\n" +
                "ON AV.idCirculo = C.id INNER JOIN Legislativa AS L\n" +
                "ON C.idLegislativa = L.id\n" +
                "WHERE L.idEleicao = " + idEleicao + "\n" +
                "GROUP BY C.id, AL.idLista;");

            // Consultar eleições presidenciais.
            ResultSet rs = legislativa.executeQuery();
            
            List lista = new ArrayList<Lista>();
            while (rs.next()) {
                // Fazer parse da data e criar nova eleicao.
                
                int idCirculo = rs.getInt("idCirculo");
                int idLista   = rs.getInt("idLista");
                int votos     = rs.getInt("votos");
                

                if (votosCirculoLista.containsKey(idCirculo))
                    lista = (List) votosCirculoLista.get(idCirculo);
                else 
                    lista = new ArrayList<Lista>();
                
                lista.add(new Lista(idLista, votos));
                
                votosCirculoLista.put(idCirculo, lista);
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }

        return votosCirculoLista;
    }
    
    public int totalEleitoresPresidencial (int idEleicao, int ronda) {
        Connection con  = null;
        int total = 0;

        try {
            con = Connect.connect();

            PreparedStatement rondaPresidencial = con.prepareStatement(
                "SELECT COUNT(E.idCidadao) AS total\n" +
                "FROM RondaPresidencial AS RP INNER JOIN AssembleiaVoto AS AV\n" +
                "ON RP.id = AV.idRondaPresidencial INNER JOIN Eleitor AS E\n" +
                "ON AV.id = E.idAssembleiaVoto\n" +
                "WHERE RP.idEleicao = " + idEleicao + " AND Ronda = " + ronda +";");

            ResultSet rs = rondaPresidencial.executeQuery();

            if (rs.next())
                total = rs.getInt("total");

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }

        return total;
    }
    
    public int totalEleitoresLegislativa (int idEleicao) {
        Connection con  = null;
        int total = 0;

        try {
            con = Connect.connect();

            PreparedStatement rondaPresidencial = con.prepareStatement(
                "SELECT COUNT(E.idCidadao) AS total\n" +
                "FROM Legislativa AS L INNER JOIN Circulo AS C\n" +
                "ON L.id = C.idLegislativa INNER JOIN AssembleiaVoto AS AV\n" +
                "ON C.id = AV.idCirculo INNER JOIN Eleitor AS E\n" +
                "ON AV.id = E.idAssembleiaVoto\n" +
                "WHERE L.idEleicao = " + idEleicao + ";");

            ResultSet rs = rondaPresidencial.executeQuery();

            if (rs.next())
                total = rs.getInt("total");

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }

        return total;
    }
}
