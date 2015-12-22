package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Exception.InvalidAdmin;
import java.sql.SQLException;

/** Classe DAO que trata do acesso dos administradores à plataforma.
 *  @author joaocosta
 */
public class AdminDAO {
    /** Confirma se a password para uma dada conta de Administrador existe.
     *  @param admin    Identificador da conta de administrador.
     *  @param pass     Password do administrador.
     *  @return True se a password estiver correcta para o utilizador fornecido
     *          ou false caso contrário. */
    public boolean confirmPassword (String admin, String pass) {
        Connection con = null;
        
        try {
            con                  = Connect.connect();
            PreparedStatement ps = con.prepareStatement("select Password "
            + "from Admins where id=" + admin);
            ResultSet rs            = ps.executeQuery();
            
            // Aceder a password e comparar.
            if (rs.next()) {
                String password = rs.getString("Password");
                
                return password.equals(pass);
            }
        }
        catch (SQLException | ClassNotFoundException e) { return false; }
        finally { 
            try { con.close(); }
            catch (Exception e) { System.out.println(e); }
        }
        
        // Devolver false por default para evitar acessos indevidos.
        return false;
    }
}
