package Business;

import Data.CidadaoDAO;

/**
 *
 * @author joaocosta
 */
public class SGE {
    private CidadaoDAO cidadaos;
    
    public SGE () {
        this.cidadaos = new CidadaoDAO();
    }
    
    /** Fazer login de eleitor.
     *  @param  ccidadao    Número do cartão de cidadão do eleitor.
     *  @param  password    Password para login.
     *  @return True se as informações estiverem correctas e false
     *          caso contrário. */
    public boolean loginEleitor (int ccidadao, String password) {
        return cidadaos.confirmaPassword(ccidadao, password);
    }
}
