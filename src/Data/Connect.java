/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

/**
 * Classe responsável por fazer a ligação à base de dados.
 * 
 * @author joaocosta
 */
        
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    public static Connection connect () 
        throws SQLException, ClassNotFoundException {
        // Carregar a driver do JDBC.
        Class.forName("com.mysql.jdbc.Driver");
        // TODO: Substituir nome final da base de dados e port.
        Connection c = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/Eleicoes?" + 
            "?user=root&password=");
        return c;
    }
}
