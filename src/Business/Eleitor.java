package Business;

import java.util.Calendar;

/**
 *
 * @authors joaocosta,zcbg
 */
public class Eleitor {
    private String nome;
    private String codigoEleitor;
    private int ccidadao;

    // GETTERS & SETTERS

    /** Construtor parametrizado.
     *  @param nome         Nome do eleitor.
     *  @param codigo       Password do eleitor.
     *  @param cc           Número de Cartão de Cidadão do eleitor.
     */
    public Eleitor (String nome, String codigo, int cc) {
        this.nome           = nome;
        this.codigoEleitor  = codigo;
        this.ccidadao       = cc;
    }

    public Eleitor (String nome, String codigo) {
      this.nome           = nome;
      this.codigoEleitor  = codigo;
    }

    /** Nome do eleitor. */
    public String getNome () { return this.nome; }

    /** Password do eleitor. */
    public String getCodigo () { return this.codigoEleitor; }

    /** Número do cartão de cidadão. */
    public int getCC () { return this.ccidadao; }

    // TODO: Adicionar validações aos setters.

    /** Alterar nome do eleitor.
     *  @param nome Novo nome para o eleitor. */
    public void setNome (String nome) { this.nome = nome; }

    /** Alterar password do eleitor.
     *  @param codigo Nova password do eleitor. */
    public void setCodigo (String codigo) { this.codigoEleitor = codigo; }

    /** Alterar número do cartão de cidadão.
     *  @param  cc Novo número de cartão de cidadão*/
    public void setCC (int cc) { this.ccidadao = cc; }

}
