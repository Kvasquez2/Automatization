import java.sql.Connection; // Importar la clase Connection
import java.sql.ResultSet; // Importar la clase ResultSet
import java.sql.SQLException; // Importar la clase SQLException
import java.sql.Statement; // Importar la clase Statement

public class ConsultaBD {
    public void realizarConsulta() {
        if (Conexion_BD.validarConexion()) { // Validar la conexión
            try (Connection connection = Conexion_BD.getConnection(); // Obtener la conexión
                 Statement statement = connection.createStatement()) { // Crear un Statement

                String sql = "SELECT * FROM tu_tabla"; // Especifica tu consulta
                ResultSet resultSet = statement.executeQuery(sql); // Ejecutar la consulta

                while (resultSet.next()) { // Iterar sobre los resultados
                    // Suponiendo que tu tabla tiene una columna 'nombre'
                    String nombre = resultSet.getString("nombre"); // Obtener el valor de la columna 'nombre'
                    System.out.println("Nombre: " + nombre); // Imprimir el valor
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Manejo de excepciones
            }
        } else {
            System.out.println("No se pudo conectar a la base de datos."); // Mensaje de error
        }
    }
}
