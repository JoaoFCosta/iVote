package Business;

import Data.*;
import Business.*;
import java.util.List;

/**
 *
 * @author joaocosta
 */
public class SGE {
    private final CidadaoDAO cidadaos;
    private final AdminDAO admins;
    private final EleicaoDAO eleicoes;
    
    public SGE () {
        this.cidadaos   = new CidadaoDAO();
        this.admins     = new AdminDAO();
        this.eleicoes   = new EleicaoDAO();
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
