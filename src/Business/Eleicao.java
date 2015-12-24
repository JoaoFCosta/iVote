package Business;
import java.util.Calendar;

/**
 * @author joaocosta
 */
public abstract class Eleicao {
  private final int id;
  private Calendar data;

  /** Construtor parametrizado.
   *  @param data Data da Eleição.
   *  @param id   Identificador da Eleição. */
  public Eleicao (Calendar data, int id) {
    this.data = (Calendar) data.clone();
    this.id   = id;
  }

  // GETTERS & SETTERS

  public int id () { return this.id; }
  
  public Calendar getData () { return (Calendar) data.clone(); }

  public void setData (Calendar data) {
    this.data = (Calendar) data.clone();
  }
  
  @Override
  public String toString () {
      return "Eleição de " + data.get(Calendar.YEAR);
  }
}
