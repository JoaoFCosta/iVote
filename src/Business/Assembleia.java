package Business;

import java.util.ArrayList;

/**
 *
 * @author zcbg
 */
public class Assembleia {
    private List<SeccaoVoto> seccoesVoto;
    private List<Eleitor> eleitores;

    /** Construtor parametrizado.
     *  @param sc         Lista com todas as Seções de Voto dessa Assembleia.
     */
    public Assembleia (List<SeccaoVoto> sc) {
        this.secoes = new ArrayList<SeccaoVoto>();
        for(SeccaoVoto s: sc){
          seccoesVoto.add(s);
        }
    }

    // METODOS

    /** Adicionar uma Secção de Voto a uma Assembleia.
     *  @param v que é uma Seção de Voto a adicionar. */
    public void adicionaSeccaoVoto(SeccaoVoto v) {
      if(!(seccoesVoto.contains(v))) this.seccoesVoto.add(v); //Só se adiciona se ainda não estiver lá
    }

    /** Lista com todas as Seções de Voto dessa Assembleia. */
    public List<Secao> seccoesVoto() {return this.seccoesVoto; }

    /** Calcula o número total de Votos nessa Assembleia. */
    public int totalDeVotos(SeccaoVoto v){
      if(seccoesVoto.contains(v)) return v.totalDeVotos();
      else return -1;//SeccaoVoto inválida
    }

    /** Calcula o número total de Votos Validos nessa Assembleia. */
    public int totalDeVotosValidos(SeccaoVoto v){
      if(seccoesVoto.contains(v)) return v.totalDeVotosValidos();
      else return -1;//SeccaoVoto inválida
    }

    /** Calcula o número total de Votos Nulos nessa Assembleia. */
    public int totalDeVotosNulos(SeccaoVoto v){
      if(seccoesVoto.contains(v)) return v.totalDeVotosNulos();
      else return -1;//SeccaoVoto inválida
    }

    /** Calcula o número total de Votos Brancos nessa Assembleia. */
    public int totalDeVotosBrancos(SeccaoVoto v){
      if(seccoesVoto.contains(v)) return v.totalDeVotosBrancos();
      else return -1;//SeccaoVoto inválida
    }
}
