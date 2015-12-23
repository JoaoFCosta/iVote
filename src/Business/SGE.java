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
    
    /** Lista de eleições. */
    public List<Eleicao> eleicoes () {
        return eleicoes.eleicoes();
    } 
}
