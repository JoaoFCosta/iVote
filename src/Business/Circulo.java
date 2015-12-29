/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  Contem os nomes das freguesias de um dado distrito
 *  Usada para criar eleiçao
 * @author Gustavo
 */
public class Circulo {
    private List<String> freguesias;
    private String distrito;
    
    public Circulo(String d,Collection<String> f){
        freguesias = new ArrayList(f);
        distrito = d;
    }
    
    public Collection<String> getFreguesias(){ return freguesias;}
    public String getDistrito(){ return distrito; }
    
}
