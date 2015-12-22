package Business;

import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author zcbg
 */
public class Ronda {
    private int numero;
    private float abstencao;
    private Calendar dataRealizacao;
    private List<Candidato> candidatos;
    private List<Voto> votos;
    private List<Eleitor> eleitores;

    private int votosValidos;
    private int votosNulos;
    private int votosBrancos;


    // GETTERS & SETTERS

    /** Construtor parametrizado.
     *  @param n         Número de Ronda das Eleições Presidenciais.
     *  @param dr        Data de Realização da Ronda n.
     */
    public Ronda(int n, int abs, Calendar dr) {
        this.numero          = n;
        this.abstencao       = abs;
        this.dataRealizacao  = dr;
    }

    /** Número de Ronda das Eleições Presidenciais. */
    public int getNumero() { return this.numero; }

    /** Abstenção na Ronda das Eleições Presidenciais. */
    public float getAbstencao() { return this.abstencao; }

    /** Data de Realização da Ronda n. */
    public Calendar getDataRealizacao() { return this.dataRealizacao; }

    /** Número de Votos Válidos para essa Secção de Voto. */
    public int Validos() { return this.votosValidos; }

    /** Número de Votos Nulos para essa Secção de Voto. */
    public int Nulos() { return this.votosNulos; }

    /** Número de Votos Brancos para essa Secção de Voto. */
    public int Brancos() { return this.votosBrancos; }


    /** Alterar Número de Ronda das Eleições Presidenciais.
     *  @param c novo número de Ronda */
    public void setNumero(int c) { this.numero = c; }

    /** Alterar Abstenção na Ronda das Eleições Presidenciais.
     *  @param a nova abstenção na Ronda */
    public void setAbstencao(float a) { this.abstencao = a; }

    /** Alterar Data de Realização da Ronda n.
     *  @param d nova Data de Realização da Ronda n. */
    public void setDataRealizacao(Calendar d) { this.numero = d; }

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

    //TODO Acrescentar ligação Diagrama Classes entre Ronda e Votos

    public void contabilizaVotosRonda(){
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

    /** Calcula o número total de Votos na Ronda. */
    public int totalDeVotos(){ return (totalDeVotosValidos() + totalDeVotosNulos() + totalDeVotosBrancos()); }

    /** Calcula o número total de Votos Validos na Ronda. */
    public int totalDeVotosValidos(){ return this.votosValidos; }

    /** Calcula o número total de Votos Nulos na Ronda.. */
    public int totalDeVotosNulos(){ return this.votosNulos; }

    /** Calcula o número total de Votos Brancos na Ronda.. */
    public int totalDeVotosBrancos(){ return this.votosBrancos; }


    /** Calcula o número total de Votos na Ronda de um Candidato candidato. */
    public int votosDoCandidato(Candidato candidato){
      // TODO Implementar na Classe Voto opcao() que devolve o nome candidato voto
      // TODO Implementar na Classe Candidato getNome() que devolve o nome candidato
        int totalVotosCandidato=0;
        String esc;
        for(Voto v:votos){
          esc=v.opcao();
          if(esc.equals(candidato.getNome())) totalVotosCandidato++;
        }
        return totalVotosCandidato;
      }

      /** Calcula a abstencao aplicando o quociente entre o total de votos até ao momento pelo total de eleitores que é
        * uma quantidade fixa naquele Ano de Eleições */
      public float calculaAbstencao(){
          return (this.abstencao=((float)totalDeVotos()/(float)eleitores.size()));
      }
}
