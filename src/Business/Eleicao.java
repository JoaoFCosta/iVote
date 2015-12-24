package Business;
import java.util.Calendar;

/**
 * @author joaocosta
 */
public class Eleicao {
  private Calendar data;

  /** Construtor parametrizado.
   *  @param data Data da Eleição. */
  public Eleicao (Calendar data) {
    this.data = (Calendar) data.clone();
  }

  // GETTERS & SETTERS

  public Calendar getData () { return (Calendar) data.clone(); }

  public void setData (Calendar data) {
    this.data = (Calendar) data.clone();
  }
  
  @Override
  public String toString () {
      return "Eleição de " + data.get(Calendar.YEAR);
  }
}
