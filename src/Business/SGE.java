package Business;

import Data.*;
import Business.*;
import java.util.Collection;

/**
 *
 * @authors joaocosta,zcbg
 */
public class SGE {
  // DAOs.
  private final CidadaoDAO  cidadaos;
  private final AdminDAO    admins;
  private final EleicaoDAO  eleicoes;
  private final EleitorDAO  eleitores;


  public SGE () {
    this.cidadaos   = new CidadaoDAO();
    this.admins     = new AdminDAO();
    this.eleicoes   = new EleicaoDAO();
    this.eleitores  = new EleitorDAO();
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
  
  /** @return Lista dos eleitores presentes na base de dados. */
  public Collection<Eleitor> eleitores () {
    return cidadaos.values ();
  }
  
  /** @return Se eleitor foi adicionado ou não. */
  public int addEleitor (String nome, String password, int ccidadao){
    return cidadaos.put(ccidadao, 
      new Eleitor(nome,Integer.toString(ccidadao)), password);
  }
  
  /** @return Devolve último idEleitor na base de dados. */
  public int lastID(){
    return cidadaos.size();
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
  
  public int criaEleicaoPresidencial(String data){
    return eleicoes.criaEleicaoPresidencial(data);
  }
  public int criaEleicaoLegislativa(String data){
    return eleicoes.criaEleicaoLegislativa(data);
  }
  
}
