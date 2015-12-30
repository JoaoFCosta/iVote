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
    /*
    @Test
    public void votosPresidenciais() throws Exception {
        votoPresidencial (int idEleicao, int ronda, int idCidadao, int idCandidato)
        
    }
        assertEquals(true, eleitor.votouLegislativa(1, 12345678));
    */
}
