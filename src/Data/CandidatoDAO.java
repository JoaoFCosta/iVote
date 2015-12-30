/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Business.Candidato;
import Business.Eleicao;
import Business.EleicaoLegislativa;
import Business.EleicaoPresidencial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Gustavo
 */
public class CandidatoDAO {
    
    /*
    *   Devolve os candidatos da eleiçao presidencial id
    */
    
    public List<Candidato> getCandidatosP(int id,int nr){
    
    Connection con  = null;
    List lista      = (List) new ArrayList<Candidato>();
    
    try {
      con                     = Connect.connect();

      PreparedStatement candidatos  = con.prepareStatement(
          "select distinct C.id, C.nome, C.password from Eleicao as E" +
            "	inner join RondaPresidencial as RP" +
            "	on E.id=RP.id" +
            "		inner join AssembleiaVoto as AV" +
                    "        on AV.idRondaPresidencial=RP.id" +
        "			inner join AssembleiaCandidato as AC" +
                        "            on AC.idAssembleiaVoto=AV.id" +
        "				inner join Candidato as CA" +
                        "                on CA.idCidadao=AC.idCidadao" +
                                            " inner join Cidadao as C" +
						"	on CA.idCidadao = C.id" +
"                where E.id=" + id +" AND RP.ronda=" + nr +";");
      
      
      ResultSet rs = candidatos.executeQuery();

      while (rs.next()) {       
        int idCidadao = rs.getInt("id");
        String pass   = rs.getString("password");
        String nome   = rs.getString("nome");
        Candidato c   = new Candidato(nome,pass,idCidadao);
        lista.add(c);
      }
      
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }

    return lista;
    }
    
    public List<Candidato> getCandidatosUltimaP(){
        Connection con  = null;
        List lista      = (List) new ArrayList<Candidato>();
    
    try {
      con                     = Connect.connect();

      PreparedStatement candidatos  = con.prepareStatement(
          " select distinct Ci.nome , Ci.id,Ci.password\n" +
"	from (Select * from rondapresidencial order by data DESC LIMIT 1) as RP\n" +
"		inner join assembleiavoto as AV\n" +
"			ON AV.idRondaPresidencial = RP.id\n" +
"				inner join assembleiacandidato as ac\n" +
"					on ac.idAssembleiaVoto = av.id\n" +
"						inner join candidato as C\n" +
"							on C.idCidadao = ac.idCidadao\n" +
"								inner join Cidadao as Ci\n" +
"									on Ci.id = C.idCidadao;");
      
      
      ResultSet rs = candidatos.executeQuery();

      while (rs.next()) {       
        int idCidadao = rs.getInt("id");
        String pass   = rs.getString("password");
        String nome   = rs.getString("nome");
        Candidato c   = new Candidato(nome,pass,idCidadao);
        lista.add(c);
      }
      
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e);
    } finally {
      try { con.close(); }
      catch (Exception e) { System.out.println(e); }
    }

    return lista;
    }

    
     public int lastID() {
        Connection con = null;
        int r = -1;
        
        try {
            con                     = Connect.connect();
            PreparedStatement ps    = con.prepareStatement(
                    "select idLista " +
                    " from Candidato " +
                    " order by idLista DESC " +
                    " limit 1; "
            );
            ResultSet rs            = ps.executeQuery();
            
            if (rs.next()) {
               r = rs.getInt("idLista");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }
        return r;
    }
     
     /** @return Se candidato foi adicionado ou não. */
    public int addCandidato (int ccidadao, int idLista){
       
        Connection con  = null;
    
        //Guarda o número de registos alterados
        int count=-1;
    
         try {
            con = Connect.connect();
      
            // Statement para a tabela cidadao.
            PreparedStatement eleitoresBD  = con.prepareStatement(
                    "insert into Candidato (idCidadao, idLista) "+
                    "values "+
                    "("+ccidadao+","+idLista+");"
                    );

            // Executar inserção
            count = eleitoresBD.executeUpdate();

        } catch ( SQLException | ClassNotFoundException e) {
             System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }
        return count;
        }
    }
    
