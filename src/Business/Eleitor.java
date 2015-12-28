package Business;

import java.util.Calendar;

/**
 *
 * @author joaocosta
 */
public class Eleitor {
    private String nome;
    private String codigoEleitor;
    
    // GETTERS & SETTERS
    
    /** Construtor parametrizado.
     *  @param nome         Nome do eleitor.
     *  @param codigo       Código do cartão de cidadão do eleitor,
     */
    public Eleitor (String nome, String codigo) {
        this.nome           = nome;
        this.codigoEleitor  = codigo;    
    }   
    
    /** Nome do eleitor. */
    public String getNome () { return this.nome; }
    
    /** Codigo do cartão de cidadão do eleitor. */
    public String getCodigo () { return this.codigoEleitor; }
    
    // TODO: Adicionar validações aos setters.
    
    /** Alterar nome do eleitor.
     *  @param nome Novo nome para o eleitor. */
    public void setNome (String nome) { this.nome = nome; }
    
    /** Alterar codigo do cartão de cidadão do eleitor. 
     *  @param codigo Novo código do cartão de cidadão. */
    public void setCodigo (String codigo) { this.codigoEleitor = codigo; }
    
}
