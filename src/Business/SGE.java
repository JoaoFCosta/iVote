package Business;

import Data.*;
import Business.*;
import java.util.List;

/**
 *
 * @author joaocosta
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
  public List<Eleicao> eleicoes () {
    return eleicoes.eleicoes();
  }
  
  /** @return Lista dos eleitores presentes na base de dados. */
  public List<Eleitor> eleitores () {
    return cidadaos.eleitores ();
  }
  
  /** @return Se eleitor foi adicionado ou não. */
  public int addEleitor (String nome, String password, int ccidadao, int idEleitor){
    return cidadaos.addEleitor(nome,password,ccidadao,idEleitor);
  }
  
  /** @return Devolve último idEleitor na base de dados. */
  public int lastID(){
    return cidadaos.lastID();
  }
  
   /** @return Verifica se um eleitor está na base de dados. */
  public boolean existeID (int ccidadao){
    return cidadaos.existeID(ccidadao);
  }

  /** Verifica se um dado eleitor já voltou na eleição atual.
   *  @param ccidadao Número do cartão de cidadão do eleitor.
   *  @return true se o voto já tiver sido efectuado e false caso contrário.
   */
  public boolean votoEfetuado (int ccidadao) {
    // Adquirir id da eleição mais recente e verificar qual o tipo.
    int eleicao           = eleicoes.idMaisRecenteEleicao();
    boolean presidencial  = eleicoes.serPresidencial(eleicao);

    if (presidencial)
      return eleitores.votouPresidencial(eleicao, 
        eleicoes.rondaMaisRecente(eleicao), ccidadao);
    else
      return eleitores.votouLegislativa(eleicao, ccidadao);
  }

  /** Rondas da Eleição Presidencial com o id fornecido.
   *  @param id Identificador da Eleição.
   *  @return Lista com a rondas associadas à dada eleição ou null
   *          se não existir eleição com o id fornecido. */
  public List<EleicaoPresidencial> presidencialComId (int id) {
    // TODO: Implementar.
    return null;
  }

  /** Eleição Legislativa com o id fornecido.
   *  @param id Idenfiticado da Eleição.
   *  @return Eleição Legislativa para o id dado ou null. */
  public EleicaoLegislativa legislativaComId (int id) {
    // TODO: Implementar.
    return null;
  }
}
