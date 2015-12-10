/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

// Importar SQL.
import java.sql.*;

/**
 *
 * @author joaocosta
 */
public class JDBCTest {
    public static void main (String args[]) throws SQLException {
        Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/Caes?" +
            "user=root&password=");
        Statement st;
        ResultSet rs;
        
        // Carregar a classe do driver.
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            // Fazer ligacao à base de dados.
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Caes?"
                    + "user=root&password=");
            
            // Ler toda a tabela dos caes.
            st  = con.createStatement();
            rs = st.executeQuery("select * from Caes");
            
            while (rs.next()) {
                System.out.println(rs.getString("Nome"));
            }
        }
        catch (SQLException e) {
            // Erro ao estabelecer a ligação.
            System.out.println("Impossivel estabelecer a ligação");
            System.out.println(e.getMessage());
        }
        catch (ClassNotFoundException e) {
            // Driver nao disponivel.
            System.out.println("Driver nao disponivel.");
        }
        finally {
            // Fechar ligacao.
            con.close();
        }
    }
}
