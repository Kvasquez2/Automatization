package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion_BD {

    // Método para establecer la conexión con la base de datos
    public Connection conectar() {
        Connection conexion = null;
        try {
            // Cambia la URL, usuario y contraseña según tu configuración
            String url = "jdbc:mysql://10.250.3.67/system_pruebas01?serverTimezone=UTC";// URL de conexión a la base de datos
            String username = "aprendiz";      // Nombre de usuario de la base de datos
            String password = "Savia.2024*";   // Contraseña de la base de datos de pruebas

            //String password = "$av14Aprendiz2024";   // Contraseña de la base de datos pre-prod

            // Establece la conexión
            conexion = DriverManager.getConnection(url, username, password);
            System.out.println("Conexión exitosa!");

        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return conexion;
    }
}

