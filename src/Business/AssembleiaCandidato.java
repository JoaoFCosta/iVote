/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

/**
 *
 * @author martinhoaragao
 */
public class AssembleiaCandidato {
    public int idAssembleiaVoto;
    public int idCandidato;
    public int votos;
    
    public AssembleiaCandidato (int idCandidato, int votos) {
        this.idCandidato = idCandidato;
        this.votos = votos;
    }
}
