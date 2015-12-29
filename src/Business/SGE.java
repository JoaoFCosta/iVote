package Business;

import Data.*;
import Business.*;
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
  
  
  LegislativaSDAO legislativas = new LegislativaSDAO();
  CirculoSDAO circulos = new CirculoSDAO();
  AssembleiaVotoSDAO assembleias = new AssembleiaVotoSDAO();
  
  /* Versão sem conecção, sem ser uma transaction
     Assume que as freguesias estão distribuidas pelos distritos por ordem
  */ 
  public int criaEleicaoLegislativa(Calendar data,Collection<String> distritos,Collection<String> freguesias){
    String d             = "Eleição Legislativa criada ";
  
    int idEleicaoGeral   = 1 + eleicoes.idMaisRecenteEleicao();
    
    int idLegislativa    = 1 + legislativas.lastID();
    int idCirculo        = 1 + circulos.lastID();
    int idAssembleiaVoto = 1 + assembleias.lastID();
    String dataE = "'" + data.get(Calendar.YEAR) + "-" + 
              data.get(Calendar.MONTH) + "-" + data.get(Calendar.DAY_OF_MONTH) + "'";
      

      
    eleicoes.insert(idEleicaoGeral);
    ArrayList<Integer> circulosIds = new ArrayList<Integer>();
    for(String distrito : distritos){
        circulos.insert(idCirculo, distrito , idLegislativa);
        circulosIds.add(idCirculo); 
        idCirculo++;
    }
    
    int ncirculos = circulosIds.size();
    int iC = 0;
    for(String freguesia : freguesias){
        assembleias.insert(idLegislativa,freguesia,circulosIds.get(iC));
        idLegislativa++;
        iC++;
        if(iC == ncirculos ) iC = 0;    
    } 
    
    
    setChanged();
    notifyObservers(d);

    return 0;
    //return res;
  }

  /*
    Versao com transação
  */
 public int criaEleicaoLegislativa2(Calendar data,Collection<String> distritos,Collection<String> freguesias){
    String d             = "Eleição Legislativa criada ";
  
    int idEleicaoGeral   = 1 + eleicoes.idMaisRecenteEleicao();
    
    int idLegislativa    = 1 + legislativas.lastID();
    int idCirculo        = 1 + circulos.lastID();
    int idAssembleiaVoto = 1 + assembleias.lastID();
    String dataE = "'" + data.get(Calendar.YEAR) + "-" + 
              data.get(Calendar.MONTH) + "-" + data.get(Calendar.DAY_OF_MONTH) + "'";
      
    Connection con = null;
    try {
        con = Connect.connect();
        con.setAutoCommit(false);
        eleicoes.insert(con,idEleicaoGeral);
    ArrayList<Integer> circulosIds = new ArrayList<Integer>();
    for(String distrito : distritos){
        circulos.insert(con,idCirculo, distrito , idLegislativa);
        circulosIds.add(idCirculo); 
        idCirculo++;
    }
    
    int ncirculos = circulosIds.size();
    int iC = 0;
    for(String freguesia : freguesias){
        assembleias.insert(con,idLegislativa,freguesia,circulosIds.get(iC));
        idLegislativa++;
        iC++;
        if(iC == ncirculos ) iC = 0;    
    } 
    
       con.commit(); // transaction block end
      return idEleicaoGeral;
    } catch (Exception e) {
           try {
                con.rollback(); //anula transacao
            } catch (SQLException ex) {
                Logger.getLogger(EleicaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }
    
    setChanged();
    notifyObservers(d);

    return 0;
    //return res;
  }
  
}
