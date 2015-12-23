/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

/**
 *
 * @author joaocosta
 */
public class Voto {
    // TODO: Implementar votos nulos e brancos.

    // Escolha associada ao voto.
    String escolha;
    // Id da eleição à qual está associado o voto.
    int idEleicao;

    /**
     * Construtor parametrizado.
     *
     * @param escolha   Escolha associada ao voto.
     * @param idEleicao Id da eleição à qual o voto está associado.
     */
    public Voto (String escolha, int idEleicao) {
        // TODO: Usar o DAO da Eleição para verificar que a eleição existe.
        this.escolha    = escolha;
        this.idEleicao  = idEleicao;
    }
    
    public String escolha () {
        return escolha;
    }
}
