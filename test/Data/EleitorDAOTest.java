package Data;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class EleitorDAOTest {

    EleitorDAO eleitor;

    @Before
    public void setup() throws Exception {
        eleitor = new EleitorDAO();
    }
    
    @Test
    public void seVotouPresidencial() throws Exception {
        assertEquals(true, eleitor.votouPresidencial(4, 1, 12345689));
        assertEquals(false, eleitor.votouPresidencial(6, 1, 12345687));

        assertEquals(true, eleitor.votouPresidencial(2, 2, 12345678));
        assertEquals(false, eleitor.votouPresidencial(2, 2, 12345819));
    }
    
    @Test
    public void seVotouLegislativa() throws Exception {
        assertEquals(true, eleitor.votouLegislativa(1, 12345678));
        assertEquals(false, eleitor.votouLegislativa(1, 12345683));
        
        assertEquals(true, eleitor.votouLegislativa(5, 12345814));
        assertEquals(false, eleitor.votouLegislativa(5, 12345686));

    }
}
