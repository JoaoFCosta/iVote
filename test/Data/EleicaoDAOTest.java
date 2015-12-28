package Data;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

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

}
