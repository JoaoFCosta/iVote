/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

/**
 * Data Access Object para os votos.
 * 
 * @author joaocosta
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Business.Voto;

public class VotoDAO {
    
    /**
     * Obter voto com um dado id.
     * @param   id    ID do voto.
     * @return  O voto se existir um voto com o id fornecido
     *          ou null se não existir.
     */
    public Voto votoComId (int id) {
        Voto voto = null;
        
        // TODO: Melhor o código dos catch e atualizar conforme
        // for decidio o nome para as colunas.
        try {
            Connection c            = Connect.connect();
            PreparedStatement st    = c.prepareStatement(
                "select * from Votos where id=" + id);
            ResultSet result        = st.executeQuery();
            
            while (result.next()) {
                voto = new Voto(result.getString("Escolha"),
                    result.getInt("Eleicao"));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        
        return voto;
    }
}
