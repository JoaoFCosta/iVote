package Business;

import Data.*;
import Business.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class SGE extends Observable{
    // DAOs.
    private final CidadaoDAO  cidadaos;
    private final AdminDAO    admins;
    private final EleicaoDAO  eleicoes;
    private final EleitorDAO  eleitores;
    private final CandidatoDAO candidatos;


    public SGE () {
        this.cidadaos   = new CidadaoDAO();
        this.admins     = new AdminDAO();
        this.eleicoes   = new EleicaoDAO();
        this.eleitores  = new EleitorDAO();
        this.candidatos = new CandidatoDAO();
    }

    /** Fazer login de eleitor.
     *  @param  ccidadao    Número do cartão de cidadão do eleitor.
     *  @param  password    Password para login.
     *  @return True se as informações estiverem correctas e false
     *          caso contrário. */
    public boolean loginEleitor (int ccidadao, String password) {
        return cidadaos.confirmaPassword(ccidadao, password);
    }

    /** Fazer login de administrador.
     *  @param  username    Username do administrador.
     *  @param  password     Password do administrador.
     *  @return True se as informações estiverem correctas e false
     *          caso contrário. */
    public boolean loginAdministrador (String username, String password) {
        return admins.confirmaPassword(username, password);
    }

    /** @return Lista das eleições presentes na base de dados. */
    public Collection<Eleicao> eleicoes () {
        return eleicoes.eleicoes();
    }

    /** @param cc Número do cartão de cidadão do eleitor.
     *  @return Eleitor com um dado número de cidadão. */
    public Eleitor eleitorComCC (int cc) {
        return cidadaos.get(cc);
    }

    public void removeEleitor (int cc) {
        cidadaos.remove(cc);
        setChanged();
        notifyObservers("Eleitor Removido: "+Integer.toString(cc));
    }

    /** @return Lista dos eleitores presentes na base de dados. */
    public Collection<Eleitor> eleitores () {
        return cidadaos.values ();
    }

    /** @return Número de eleitores no sistema. */
    public int numEleitores () {
        return cidadaos.size();
    }

    /** @return Se eleitor foi adicionado ou não. */
    public int addEleitor (String nome, String password, int ccidadao){
        cidadaos.put(ccidadao,
                new Eleitor(nome,Integer.toString(ccidadao)), password);
        setChanged();
        notifyObservers("Eleitor Criado: "+nome);
        return 1;
    }

    /** @return Devolve último idEleitor na base de dados. */
    public int lastID(){
        return cidadaos.size();
    }

    /** @return Se candidato foi adicionado ou não. */
    public int addCandidato (int ccidadao, int lista){
        int res=candidatos.addCandidato(ccidadao, lista);
        setChanged();
        notifyObservers("Candidato Criado: "+Integer.toString(ccidadao));
        return res;
    }


    /** @return Devolve último idlista na base de dados. */
    public int lastIDL(){
        return candidatos.lastID();
    }

    /** @return Verifica se um eleitor está na base de dados. */
    public boolean existeID (int ccidadao){
        return cidadaos.containsKey(ccidadao);
    }

    /** Verifica se um dado eleitor já voltou na eleição atual.
     *  @param ccidadao Número do cartão de cidadão do eleitor.
     *  @return true se o voto já tiver sido efectuado e false caso contrário.
     */
    public boolean votoEfetuado (int ccidadao) {
        // Adquirir id da eleição mais recente e verificar qual o tipo.
        int eleicao           = eleicoes.idMaisRecenteEleicao();
        boolean presidencial  = eleicoes.ePresidencial(eleicao);

        if (presidencial)
            return eleitores.votouPresidencial(eleicao,
                    eleicoes.rondaMaisRecente(eleicao), ccidadao);
        else
            return eleitores.votouLegislativa(eleicao, ccidadao);
    }

    public int criaEleicaoPresidencial(Calendar data){
        String d             = "Eleição Presidencial criada ";
    /*
    int idEleicaoGeral   = 1 + eleicoes.idMaisRecenteEleicao();
    int ronda            = 1;
    int idPresidencial   = 1 + eleicoes.lastIDP();
    int idAssembleiaVoto = 1 + eleicoes.lastIDAV();
    int res              = eleicoes.criaEleicaoPresidencial(data,idEleicaoGeral,idPresidencial,ronda,idAssembleiaVoto);
    */
        setChanged();
        notifyObservers(d);
        return 0;
    }
    public int criaEleicaoLegislativa(Calendar data){
        String d             = "Eleição Legislativa criada ";
  /*
    int idEleicaoGeral   = 1 + eleicoes.idMaisRecenteEleicao();
    int idLegislativa    = 1 + eleicoes.lastIDL();
    int idCirculo        = 1 + eleicoes.lastIDCR();
    int idAssembleiaVoto = 1 + eleicoes.lastIDAV();
    int res              = eleicoes.criaEleicaoLegislativa(data, idEleicaoGeral, idLegislativa, idCirculo, idAssembleiaVoto);
*/
        setChanged();
        notifyObservers(d);

        return 0;
        //return res;
    }
    
    /* Primeiro da lista tem mais votos */
    public List<Candidato> votosPorCandidatos (int idEleicao) {
        Map<Integer, Integer> resultados = eleicoes.votosCandidatos(idEleicao);
        List<Candidato> candidatos = new ArrayList<Candidato>();
        
        for (Integer idCidadao : resultados.keySet())
            candidatos.add((Candidato) cidadaos.get(idCidadao));
        
        return candidatos;
    }
    
    public int abstencaoPresidencial (int idEleicao, int ronda) {
        return eleicoes.totalEleitoresPresidencial(idEleicao, ronda) - votosTotaisPresidencial(idEleicao, ronda);
    }
    /*
    public int abstencaoLegislativa (int idEleicao) {
        return eleicoes.totalEleitoresLegislativa(idEleicao) - votosTotaisLegislativa(idEleicao);
    }*/
    
    public int votosTotaisPresidencial (int idEleicao, int ronda) {
        Map<Integer, Integer> resultados = eleicoes.votosCandidatos(idEleicao);
        int votosValidos = 0;
        
        for (Integer votos : resultados.values())
            votosValidos += votos;
        
        return votosBrancosPresidencial(idEleicao, ronda) + votosNulosPresidencial(idEleicao, ronda) + votosValidos;
        
    }
    
    /*public int votosTotaisLegislativa (int idEleicao) {
        
    }*/

    public int votosBrancosPresidencial (int idEleicao, int ronda) {
        int votos = 0;

        for (Integer votosAssembleia : eleicoes.votosBrancosPorAssembleia(idEleicao, ronda).values())
            votos += votosAssembleia;
        return votos;
    }

    public int votosNulosPresidencial (int idEleicao, int ronda) {
        int votos = 0;

        for (Integer votosAssembleia : eleicoes.votosNulosPorAssembleia(idEleicao, ronda).values())
            votos += votosAssembleia;
        return votos;
    }
    
    public int votosBrancosLegislativa (int idEleicao) {
        int votos = 0;

        for (Integer votosAssembleia : eleicoes.votosBrancosPorAssembleia(idEleicao, 0).values())
            votos += votosAssembleia;
        return votos;
    }

    public int votosNulosLegislativa (int idEleicao) {
        int votos = 0;

        for (Integer votosAssembleia : eleicoes.votosNulosPorAssembleia(idEleicao, 0).values())
            votos += votosAssembleia;
        return votos;
    }
    
    public void votoBrancoPresidencial (int idEleicao, int ronda, int idCidadao) {
        eleicoes.voto(idEleicao, ronda, idCidadao, true);
        eleitores.votouPresidencial(idEleicao, ronda, idCidadao);
    }
    
    public void votoNuloPresidencial (int idEleicao, int ronda, int idCidadao) {
        eleicoes.voto(idEleicao, ronda, idCidadao, false);
        eleitores.votouPresidencial(idEleicao, ronda, idCidadao);

    }
        
    public void votoBrancoLegislativa (int idEleicao, int idCidadao, boolean votoBranco) {
        eleicoes.voto(idEleicao, -1, idCidadao, true);
        eleitores.votouLegislativa (idEleicao, idCidadao);
    }
            
    public void votoNuloLegislativa (int idEleicao, int idCidadao, boolean votoBranco) {
        eleicoes.voto(idEleicao, -1, idCidadao, false);
        eleitores.votouLegislativa (idEleicao, idCidadao);

    }
    
    public void votoPresidencial (int idEleicao, int ronda, int idCidadao, int idCandidato) {
        eleicoes.votoPresidencial(idEleicao, ronda, idCidadao, idCandidato);
        eleitores.votouPresidencial(idEleicao, ronda, idCidadao);

    }
    
    public void votoLegislativa (int idEleicao, int idCidadao, int idLista) {
        eleicoes.votoLegislativa(idEleicao, idCidadao, idLista);
        eleitores.votouLegislativa (idEleicao, idCidadao);
    }

            
}
