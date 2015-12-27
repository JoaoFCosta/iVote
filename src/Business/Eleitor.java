package Business;

import java.util.Calendar;

/**
 *
 * @author joaocosta
 */
public class Eleitor {
    private String nome;
    private String codigoEleitor;
    private String distrito;
    private String freguesia;
    private Calendar nascimento;
    
    // GETTERS & SETTERS
    
    /** Construtor parametrizado.
     *  @param nome         Nome do eleitor.
     *  @param codigo       Código do cartão de cidadão do eleitor,
     *  @param distrito     Distrito onde reside o eleitor.
     *  @param freguesia    Freguesia onde reside o eleitor.
     *  @param nascimento   Data de nascimento do eleitor.
     */
    public Eleitor (String nome, String codigo, String distrito, 
            String freguesia, Calendar nascimento) {
        this.nome           = nome;
        this.codigoEleitor  = codigo;
        this.distrito       = distrito;
        this.freguesia      = freguesia;
        this.nascimento     = nascimento;
    }   
    
    public Eleitor (String nome, String codigo) {
      this.nome           = nome;
      this.codigoEleitor  = codigo;
    }
    
    /** Nome do eleitor. */
    public String nome () { return this.nome; }
    
    /** Codigo do cartão de cidadão do eleitor. */
    public String codigo () { return this.codigoEleitor; }
    
    /** Distrito de residência do eleitor. */
    public String distrito () { return this.distrito; }
    
    /** Freguesia de residência do eleitor. */
    public String freguesia () { return this.freguesia; }
    
    /** Data de nascimento do eleitor. */
    public Calendar dataNascimento () { 
        return (Calendar) this.nascimento.clone(); }
    
    // TODO: Adicionar validações aos setters.
    
    /** Alterar nome do eleitor.
     *  @param nome Novo nome para o eleitor. */
    public void setNome (String nome) { this.nome = nome; }
    
    /** Alterar codigo do cartão de cidadão do eleitor. 
     *  @param codigo Novo código do cartão de cidadão. */
    public void setCodigo (String codigo) { this.codigoEleitor = codigo; }
    
    /** Alterar distrito de residência do eleitor.
     *  @param distrito Novo distrito. */
    public void setDistrito (String distrito) { this.distrito = distrito; }
    
    /** Alterar a freguesia de residência do eleitor.
     *  @param freguesia Nova freguesia onde reside o eleitor. */
    public void setFreguesia (String freguesia) { this.freguesia = freguesia; }
    
    /** Alterar data de nascimento do eleitor.
     *  @param nascimento Nova data de nascimento do eleitor. */
    public void setNascimento (Calendar nascimento) { 
        this.nascimento = nascimento; }
}
