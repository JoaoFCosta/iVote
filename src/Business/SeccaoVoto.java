package Business;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author zcbg
 */
public class SeccaoVoto {
    private int codPostal;
    private List<Eleitor> votantes;
    private int votosValidos;
    private int votosNulos;
    private int votosBrancos;
    private List<Voto> votos;


    // GETTERS & SETTERS

    /** Construtor parametrizado.
     *  @param cp         Código Postal da Secção de Voto.
     *  @param el         Lista com todos os votantes da respetiva Secção de Voto.
     *  @param vv         Número de Votos Válidos para essa Secção de Voto.
     *  @param vn         Número de Votos Nulos para essa Secção de Voto.
     *  @param vb         Número de Votos Brancos para essa Secção de Voto.
     */
    public SeccaoVoto(int cp, List<Eleitor> el, int vv, int vn, int vb, List<Voto> vt) {
        this.codPostal      = cp;
        this.votosValidos   = vv;
        this.votosNulos     = vn;
        this.votosBrancos   = vb;
        this.votantes       = new ArrayList<Eleitor>();
        this.votos          = new ArrayList<Voto>();
        for(Eleitor e: el){
          votantes.add(e);
        }
        for(Voto v: vt){
          votos.add(v);
        }
    }

    /** Código Postal da Secção de Voto. */
    public int codPostal () { return this.codPostal; }

    /** Número de Votos Válidos para essa Secção de Voto. */
    public int Validos() { return this.votosValidos; }

    /** Número de Votos Nulos para essa Secção de Voto. */
    public int Nulos() { return this.votosNulos; }

    /** Número de Votos Brancos para essa Secção de Voto. */
    public int Brancos() { return this.votosBrancos; }

    /** Lista com todos os votantes da respetiva Secção de Voto. */
    public List<Eleitor> listaSeccao() {return this.votantes; }

    /** Lista com todos os votos da respetiva Secção de Voto. */
    public List<Voto> votosSeccao() {return this.votos; }


    /** Alterar Código Postal da Secção de Voto.
     *  @param cp Novo Código Postal da Secção de Voto. */
    public void setCodPostal (int cp) { this.codPostal = cp; }

    /** Alterar Número de Votos Validos nessa Secção de Voto.
     *  @param v Novo Número de Votos Validos para essa Secção de Voto. */
    public void setValidos (int v) { this.votosValidos = v; }

    /** Alterar Número de Votos Nulos nessa Secção de Voto.
     *  @param n Novo Número de Votos Nulos para essa Secção de Voto. */
    public void setNulos (int n) { this.votosBrancos = n; }

    /** Alterar Número de Votos Brancos nessa Secção de Voto.
     *  @param b Novo Número de Votos Brancos para essa Secção de Voto. */
    public void setBrancos (int b) { this.votosBrancos = b; }


    // METODOS
    public void contabilizaVotosSeccaoVoto(){
      //TODO Implementar método escolha() na classe Voto
      String esc;
      for(Voto v:votos){
        esc=v.escolha();
        switch(esc){
          case "branco":  votosBrancos++;
                          break;

          case "nulo"  :  votosNulos++;
                          break;

          // por defeito são validos
          default      :  votosValidos++;
                          break;
        }
      }
    }

    /** Calcula o número total de Votos nessa Secção de Voto. */
    public int totalDeVotos(){ return (totalDeVotosValidos() + totalDeVotosNulos() + totalDeVotosBrancos()); }

    /** Calcula o número total de Votos Validos nessa Secção de Voto. */
    public int totalDeVotosValidos(){ return this.votosValidos; }

    /** Calcula o número total de Votos Nulos nessa Secção de Voto. */
    public int totalDeVotosNulos(){ return this.votosNulos; }

    /** Calcula o número total de Votos Brancos nessa Secção de Voto. */
    public int totalDeVotosBrancos(){ return this.votosBrancos; }

    /** Valida um eleitor, ou seja, verifica se está presente na lista de votantes
     *  @param Eleitor e indica se esse eleitor está inserido nesta Secção de Voto. */
    public boolean validaEleitor(Eleitor e){
      return (votantes.contains(e));
    }
}
