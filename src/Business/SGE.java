package Business;

import Data.*;
import Business.*;
import Exception.FailedInsert;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SGE extends Observable{

  private final CidadaoDAO  cidadaos;
  private final AdminDAO    admins;
  private final EleicaoDAO  eleicoes;
  private final EleitorDAO  eleitores;
  private final CandidatoDAO candidatos;

  private final LegislativaSDAO legislativas;
  private final CirculoSDAO circulos;
  private final AssembleiaVotoSDAO assembleias;
    private final RondaPresidencialSDAO rondasPresidenciais;

  public SGE () {
    this.cidadaos   = new CidadaoDAO();
    this.admins     = new AdminDAO();
    this.eleicoes   = new EleicaoDAO();
    this.eleitores  = new EleitorDAO();
    this.candidatos = new CandidatoDAO();
    legislativas = new LegislativaSDAO();
    circulos = new CirculoSDAO();
    assembleias = new AssembleiaVotoSDAO();
    rondasPresidenciais = new RondaPresidencialSDAO();
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

  /** @return Id da eleição mais recente na base de dados. */
  public int idMaisRecenteEleicao () {
    return eleicoes.idMaisRecenteEleicao ();
  }

  /** @return Ronda da eleição presidencial mais recente na base de dados. */
  public int rondaMaisRecente (int idEleicao){
    return eleicoes.rondaMaisRecente(idEleicao);
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

    /** Verifica se um dado eleitor já votou na eleição atual.
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

    /* Primeiro da lista tem mais votos */
    public List<Candidato> votosPorCandidatos (int idEleicao, int ronda) {
        Map<Integer, Integer> resultados = eleicoes.votosCandidatos(idEleicao, ronda);
        List<Candidato> candidatos = new ArrayList<Candidato>();
        Eleitor e;
        Candidato c;
        for (Integer idCidadao : resultados.keySet()) {
            e = cidadaos.get(idCidadao);
            c = new Candidato(e.getNome(), e.getCodigo(), e.getCC(), resultados.get(idCidadao));
            candidatos.add(c);
        }

        return candidatos;
    }
    
    /*
        Map de idCirculo para uma List com classe Lista.
        Cada Lista tem informação sobre o idLista e votos.
        As variáveis são públicas para aceder rapidamente.
    */
    public Map<Integer, List<Lista>> votosCirculoLista (int idEleicao) {
        return eleicoes.votosCirculoLista(idEleicao);
    }
    
    public int abstencaoPresidencial (int idEleicao, int ronda) {
        int abstencao = eleicoes.totalEleitoresPresidencial(idEleicao, ronda) - votosTotaisPresidencial(idEleicao, ronda);
        
        if (abstencao < 0)
            abstencao = 0;
        
        return abstencao;
    }
    
    public int abstencaoLegislativa (int idEleicao) {
        int abstencao = eleicoes.totalEleitoresLegislativa(idEleicao) - votosTotaisLegislativa(idEleicao);
        
        if (abstencao < 0)
            abstencao = 0;
        
        return abstencao;
    }
    
    public int votosTotaisPresidencial (int idEleicao, int ronda) {
        Map<Integer, Integer> resultados = eleicoes.votosCandidatos(idEleicao, ronda);
        int votosValidos = 0;

        for (Integer votos : resultados.values())
            votosValidos += votos;
        
        return votosBrancosPresidencial(idEleicao, ronda) + votosNulosPresidencial(idEleicao, ronda) + votosValidos;
    }
    
    public int votosTotaisLegislativa (int idEleicao) {
        Map<Integer, List<Lista>> votosCirculoLista = eleicoes.votosCirculoLista(idEleicao);
        int votosValidos = 0;
        
        for (List<Lista> l : votosCirculoLista.values())
            for (Lista lista : l)
                votosValidos += lista.votos;
        
        return votosBrancosLegislativa(idEleicao) + votosNulosLegislativa(idEleicao) + votosValidos;
    }

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
  /**
   *    Obtem as listas possíveis para o eleitor votar
   *
   * @param idEleicao id da eleicao
   * @param cc cartao de cidadao do eleitor
   * @return Colecçao com strings do tipo "Lista " + idLista,exemplo "Lista 1"
   */

   public Collection<String> opcoesVotoLegislativa(int idEleicao,int cc){
       return eleicoes.opcoesVotoLegislativa(idEleicao, cc);
   }

   /**
    *   Devolve os candidatos da eleiçao presidencial mais recente
    *
    * @return Lista de candidatos
    */
   public Collection<Candidato> opcoesVotoPresidencial(){
       return candidatos.getCandidatosUltimaP();
   }

  public int criaEleicaoPresidencial(Calendar data){
    String d             = "Eleição Presidencial criada ";
    eleicoes.criaEleicaoPresidencial(data);
    setChanged();
    notifyObservers(d);
    return 0;
  }

  public int criaEleicaoLegislativa(Calendar data){
    String d             = "Eleição Presidencial criada ";
    eleicoes.criaEleicaoLegislativa(data);
    setChanged();
    notifyObservers(d);
    return 0;
  }

 /**
   * Cria uma nova eleicao legislativa
   * @param data data da eleição
   * @param nomes nomes dos distritos e das suas respetivas freguesias
   * @return 0 Caso bem sucedido
  */
  public int criaEleicaoLegislativa(Calendar data,Collection<Circulo> nomes){
    String d             = "Eleição Legislativa criada ";

    int idEleicaoGeral   = 1 + eleicoes.idMaisRecenteEleicao();

    int idLegislativa    = 1 + legislativas.lastID();
    int idCirculo        = 1 + circulos.lastID();
    int idAssembleiaVoto = 1 + assembleias.lastID();
    String dataE = "'" + data.get(Calendar.YEAR) + "-" +
              data.get(Calendar.MONTH) + "-" + data.get(Calendar.DAY_OF_MONTH) + "'";
    int r = 0;
    try{

    eleicoes.insert(idEleicaoGeral);

    legislativas.insert(idLegislativa, idEleicaoGeral, dataE);

    int idC = idCirculo;
    for(Circulo e : nomes){
        circulos.insert(idC,e.getDistrito(),idLegislativa);
        idC++;
    }

    for(Circulo e : nomes){

        for(String freguesia : e.getFreguesias()){
         assembleias.insert(idAssembleiaVoto,freguesia,idCirculo);
         idAssembleiaVoto++;
        }
        idCirculo++;
    }
    }catch(FailedInsert e){
        r = 1;
        e.printStackTrace();
    }

    if(r==0){
        setChanged();
        notifyObservers(d);
        }
    return r;
  }
  
  /**
   * Cria uma nova eleicao presidencial
   * @param data data da eleição
   * @param freguesias Freguesia de cada secçao de voto
   * @return 0 Caso bem sucedido
  */
  public int criaEleicaoPresidencial(Calendar data,Collection<String> freguesias){

      String d             = "Eleição Presidencial criada ";

      int r = 0;

      int idEleicaoGeral   = 1 + eleicoes.idMaisRecenteEleicao();
      int idRonda    = 1 + rondasPresidenciais.lastID();
      int idAssembleiaVoto = 1 + assembleias.lastID();
      String dataE = "'" + data.get(Calendar.YEAR) + "-" +
              data.get(Calendar.MONTH) + "-" + data.get(Calendar.DAY_OF_MONTH) + "'";

      try{
        eleicoes.insert(idEleicaoGeral);

        rondasPresidenciais.insert(idRonda,idEleicaoGeral,dataE);

        for(String freguesia : freguesias){
            assembleias.insert(idAssembleiaVoto, freguesia, idRonda);
            idAssembleiaVoto++;
        }

       }catch(FailedInsert e){
        r = 1;
        e.printStackTrace();
        }

      if(r==0){
        setChanged();
        notifyObservers(d);
      }

      return r;
  }

    public void votoBrancoPresidencial (int idEleicao, int ronda, int idCidadao) {
        eleicoes.voto(idEleicao, ronda, idCidadao, true);
        eleitores.votoPresidencial(idEleicao, ronda, idCidadao);
    }

    public void votoNuloPresidencial (int idEleicao, int ronda, int idCidadao) {
        eleicoes.voto(idEleicao, ronda, idCidadao, false);
        eleitores.votoPresidencial(idEleicao, ronda, idCidadao);

    }
        
    public void votoBrancoLegislativa (int idEleicao, int idCidadao) {
        eleicoes.voto(idEleicao, -1, idCidadao, true);
        eleitores.votoLegislativa (idEleicao, idCidadao);
    }
            
    public void votoNuloLegislativa (int idEleicao, int idCidadao) {
        eleicoes.voto(idEleicao, -1, idCidadao, false);
        eleitores.votoLegislativa (idEleicao, idCidadao);

    }

    public void votoPresidencial (int idEleicao, int ronda, int idCidadao, int idCandidato) {
        eleicoes.votoPresidencial(idEleicao, ronda, idCidadao, idCandidato);
        eleitores.votoPresidencial(idEleicao, ronda, idCidadao);

    }

    public void votoLegislativa (int idEleicao, int idCidadao, int idLista) {
        eleicoes.votoLegislativa(idEleicao, idCidadao, idLista);
        eleitores.votoLegislativa (idEleicao, idCidadao);
    }
    
    public boolean votouPresidencial (int idEleicao, int ronda, int idCidadao) {
       return eleitores.votouPresidencial(idEleicao, ronda, idCidadao);
    }
    
    public boolean votouLegislativa (int idEleicao, int idCidadao) {
        return eleitores.votouLegislativa(idEleicao, idCidadao);
    }
    
    /** Devolve a lista de candidatos de uma Assembleia de Voto do Partido nome*/
    public ArrayList<Candidato> listas(String nome){
        return new ArrayList<>();
    }
    
    /** Actualiza a lista de candidatos de uma Assembleia de Voto do Partido nome na base de dados*/
    public int guardarLista(String nome, ArrayList<Candidato> c){
        String d = "Lista de Candidatos"+nome+" Alterada!";
        setChanged();
        notifyObservers(d);
        return 1;
    }
    
    /** Verifica se um dado id de eleição é uma eleeção presidencial.
     *  @param idEleicao  ID da Eleição a ser verificada.
     *  @return true se a eleição for presidencial e false caso contrário. */
     public boolean ePresidencial (int idEleicao) {
       return eleicoes.ePresidencial(idEleicao);
     }

    public Map<Integer, List<String>> alocarMandatos (int idEleicao) {
        Map<Integer, List<Lista>> votosCirculoLista = votosCirculoLista(idEleicao);
        EleicaoLegislativa eleicaoL = new EleicaoLegislativa(Calendar.getInstance(), 1);
        
        Map<Integer, List<String>> resultados = new HashMap<Integer, List<String>>();
                
        List<Integer> mandatos = new ArrayList<Integer>();
        
        for (Integer idCirculo : votosCirculoLista.keySet()) {
            List<Lista> listas = votosCirculoLista.get(idCirculo);
            
            mandatos = eleicaoL.alocarMandatos(listas);
            
            List<String> deputados = new ArrayList<String>();
            for (Lista lista : listas) {
                
                deputados.addAll(circulos.alocarMandatos(idEleicao, idCirculo, lista.idLista, mandatos.get(lista.idLista)));
            }
            
            resultados.put(idCirculo, deputados);
        }
        
        return resultados;
    }
    
    public int eleicaoAberta () {
      return eleicoes.eleicaoAberta();
    }
}
