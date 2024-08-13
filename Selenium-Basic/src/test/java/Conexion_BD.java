import java.sql.Connection; // Importar la clase Connection
import java.sql.DriverManager; // Importar la clase DriverManager
import java.sql.SQLException; // Importar la clase SQLException

public class Conexion_BD {
    // Cambia estos valores por tus credenciales y configuración de la base de datos
    private static final String url = "jdbc:mysql://10.250.3.67/system_pruebas01?serverTimezone=UTC";// URL de conexión a la base de datos
    private static final String username = "aprendiz";      // Nombre de usuario de la base de datos
    private static final String password = "Savia.2024*";   // Contraseña de la base de datos

    // Método para obtener la conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password); // Establecer y retornar la conexión
    }

    // Método para validar la conexión a la base de datos
    public static boolean validarConexion() {
        try (Connection connection = getConnection()) { // Intenta obtener una conexión
            return connection != null; // Retorna verdadero si la conexión no es nula
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de excepciones
            return false; // Retorna falso si hay un error
        }
    }
}
