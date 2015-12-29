package Data;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class EleicaoDAOTest {

    EleicaoDAO eleicao;

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

}
