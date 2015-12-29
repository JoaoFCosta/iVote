package Business;

import Data.*;
import Business.*;
import Exception.FailedInsert;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors joaocosta,zcbg
 */
public class SGE extends Observable{
  // DAOs.
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
  
}
