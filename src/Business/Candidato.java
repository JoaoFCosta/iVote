/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author joaocosta
 */
public class Candidato extends Eleitor {
    private String partido;

    /** Construtor parametrizado.
     * @param nome          Nome do candidato.
     * @param codigo        Código do cartão de cidadão.
     * @param distrito      Distrito de residência.
     * @param freguesia     Freguesia de residência.
     * @param nascimento    Data de nascimento.
     * @param partido       Partido do candidato.
     */
    public Candidato(String nome, String codigo, String distrito, 
            String freguesia, Calendar nascimento, String partido) {
        super(nome, codigo, distrito, freguesia, nascimento);
        this.partido = partido;
    }
    
    /** @return Idade do candidato.
     */
    public int idade () {
        Calendar now        = new GregorianCalendar();
        Calendar nascimento  = super.dataNascimento();
        int idade   = now.YEAR - nascimento.YEAR;
        
        if (nascimento.MONTH > now.MONTH)
            idade--;
        else if (nascimento.DAY_OF_MONTH > now.DAY_OF_MONTH)
            idade--;
        
        return idade;
    }
    
    // GETTERS & SETTERS
    
    /** Partido do candidato. */
    public String partido () { return this.partido; }
    
    /** Alterar partido do candidato.
     *  @param partido Novo partido do candidato. */
    public void setPartido (String partido) { this.partido = partido; }
    
    /** @return Nome do candidato. */
    public String getNome () {
        return super.nome();
    }
}
