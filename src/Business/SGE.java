package Business;

import Data.CidadaoDAO;
import Data.AdminDAO;

/**
 *
 * @author joaocosta
 */
public class SGE {
    private final CidadaoDAO cidadaos;
    private final AdminDAO admins;
    
    public SGE () {
        this.cidadaos   = new CidadaoDAO();
        this.admins     = new AdminDAO();
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
}
