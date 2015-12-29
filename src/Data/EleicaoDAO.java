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
import java.util.*;

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

    public Map<Integer, Integer> votosNulosPorAssembleia (int idEleicao) {
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



    public Map<Integer, Integer> votosBrancosPorAssembleia (int idEleicao) {
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

    public void votoLegislativa (int idEleicao, int idCidadao, int idLista) throws Exception {
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

            ResultSet rs = votoPresidencial.executeQuery();
            con.commit();

        } catch (SQLException | ClassNotFoundException e) {
            if (con != null)
                con.rollback();
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }
    }

    public void votoPresidencial (int idEleicao, int ronda, int idCidadao, int idCandidato) throws Exception {
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

            ResultSet rs = votoPresidencial.executeQuery();
            con.commit();

        } catch (SQLException | ClassNotFoundException e) {
            if (con != null)
                con.rollback();
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }
    }
}
