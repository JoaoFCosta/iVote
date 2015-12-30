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
    int votos;

    public Candidato(String nome, String codigo, int cc, int votos) {
        super(nome, codigo, cc);
        this.votos = votos;
    }
   
    public int getVotos () {
        return votos;
    }
}
