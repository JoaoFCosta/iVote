package Data;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import Business.ScriptRunner;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.util.logging.Level;


public class EleicaoDAOTest {

    EleicaoDAO eleicao;

    @BeforeClass
    public static void EleicaoDAOTest() throws Exception {
        Connection con  = null;
        try {
            con = Connect.connect();
            ScriptRunner runner = new ScriptRunner(con, false, false);
            runner.runScript(new BufferedReader(new FileReader("scripts/iVoteCriacao.sql")));
            runner.runScript(new BufferedReader(new FileReader("scripts/iVotePopulate.sql")));
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }

    }

    @Before
    public void setup() throws Exception {
        eleicao = new EleicaoDAO();
    }

    @Test
    public void validaOrdenacao() throws Exception {
        assertEquals(12, eleicao.idMaisRecenteEleicao());
        assertEquals(1, eleicao.rondaMaisRecente(10));
        assertEquals(2, eleicao.rondaMaisRecente(2));
    }

    @Test
    public void testeSePresidencial() throws Exception {
        assertEquals(true, eleicao.ePresidencial(2));
        assertEquals(true, eleicao.ePresidencial(8));
        assertEquals(false, eleicao.ePresidencial(1));
        assertEquals(false, eleicao.ePresidencial(7));
    }

    @Test
    public void validarVotosNulos() throws Exception {
        Map<Integer, Integer> votosNulosEleicaoUm     = eleicao.votosNulosPorAssembleia(1);
        Map<Integer, Integer> votosNulosEleicaoQuatro = eleicao.votosNulosPorAssembleia(4);

        assertEquals(1, (int) votosNulosEleicaoQuatro.get(209));
        assertEquals(1, (int) votosNulosEleicaoQuatro.get(215));

        assertEquals(4, (int) votosNulosEleicaoUm.get(19));
        assertEquals(0, (int) votosNulosEleicaoUm.get(24));
    }


    @Test
    public void validarVotosBrancos() throws Exception {
        Map<Integer, Integer> votosBrancosEleicaoUm     = eleicao.votosBrancosPorAssembleia(1);
        Map<Integer, Integer> votosBrancosEleicaoQuatro = eleicao.votosBrancosPorAssembleia(4);

        assertEquals(1, (int) votosBrancosEleicaoQuatro.get(209));
        assertEquals(5, (int) votosBrancosEleicaoQuatro.get(215));

        assertEquals(2, (int) votosBrancosEleicaoUm.get(19));
        assertEquals(5, (int) votosBrancosEleicaoUm.get(24));
    }
    
    @Test
    public void votoPresidencialTest() throws Exception {
        assertEquals(eleicao.votoLegislativa(1, 12345678, 1))
    }

}
