/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import Data.Connect;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author martinhoaragao
 */
public class SGETest {
    SGE sge;
            
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
        sge = new SGE();
    }

    @Test
    public void votosBrancosPresidenciais() throws Exception {
        assertEquals(false, sge.votouPresidencial(2, 1, 12345679));
        int votos = sge.votosBrancosPresidencial(2, 1);
        assertEquals(60, sge.votosBrancosPresidencial(2, 1));
        
        sge.votoBrancoPresidencial(2, 1, 12345679);
        assertEquals(true, sge.votouPresidencial(2, 1, 12345679));

        votos = sge.votosBrancosPresidencial(2, 1);
        assertEquals(61, sge.votosBrancosPresidencial(2, 1));   
    }
    
    @Test
    public void votosBrancosLegislativas() throws Exception {
        assertEquals(false, sge.votouLegislativa(1, 12345681));
        int votos = sge.votosBrancosLegislativa(1);
        assertEquals(54, sge.votosBrancosLegislativa(1));
        
        sge.votoBrancoLegislativa(1, 12345681);
        assertEquals(true, sge.votouLegislativa(1, 12345681));

        votos = sge.votosBrancosLegislativa(1);
        assertEquals(55, sge.votosBrancosLegislativa(1));   
    }
    
    @Test
    public void votosValidosPresidenciais() throws Exception {
        assertEquals(0, sge.abstencaoPresidencial(4,1));
        assertEquals(false, sge.votouPresidencial(4, 1, 12345682));
        assertEquals(16105, sge.votosTotaisPresidencial(4, 1));
        
        sge.votoPresidencial(4, 1, 12345682, 12345679);
        
        assertEquals(true, sge.votouPresidencial(4, 1, 12345682));
        assertEquals(16106, sge.votosTotaisPresidencial(4, 1));

    }
/*
    private void assertEquals(int i, int votosBrancosPresidencial) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
}
