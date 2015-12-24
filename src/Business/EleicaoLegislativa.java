package Business;

import java.util.Calendar;

/**
 *  Classe representante de uma eleição legislativa.
 * 
 *  @author joaocosta
 */
public class EleicaoLegislativa extends Eleicao {

  public EleicaoLegislativa(Calendar data, int id) {
    super(data, id);
  }
   
  @Override
  public String toString () {
    return "[LEGISLATIVA] " + super.toString();
  }
}
