import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

import bancario.OperacionesBanco;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/banco";
        String user = "postgres";
        String password = "Colombia2024";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Mensaje de conexión exitosa
            JOptionPane.showMessageDialog(null, "Conexión exitosa", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            Statement statement = connection.createStatement();

            // Consulta para obtener datos de cuentas
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cuentas");
            StringBuilder mensajeCuentas = new StringBuilder("Datos de las cuentas:\n");

            while (resultSet.next()) {
                int idCuenta = resultSet.getInt("id_cuenta");
                String numeroCuenta = resultSet.getString("numero_cuenta");
                String tipoCuenta = resultSet.getString("tipo_cuenta");
                double saldo = resultSet.getDouble("saldo_cuenta");

                // Construir mensaje
                mensajeCuentas.append("ID: ").append(idCuenta)
                              .append(", Número: ").append(numeroCuenta)
                              .append(", Tipo: ").append(tipoCuenta)
                              .append(", Saldo: ").append(saldo).append("\n");
            }

            // Mostrar datos de cuentas
            JOptionPane.showMessageDialog(null, mensajeCuentas.toString(), "Cuentas", JOptionPane.INFORMATION_MESSAGE);

            // Consulta para obtener datos de clientes
            ResultSet resultSetClientes = statement.executeQuery("SELECT * FROM Clientes");
            StringBuilder mensajeClientes = new StringBuilder("Datos de los clientes:\n");

            while (resultSetClientes.next()) {
                int idCliente = resultSetClientes.getInt("id_Cliente");
                String nombreCliente = resultSetClientes.getString("nombre_Cliente");
                String cedulaCliente = resultSetClientes.getString("cedula_Cliente");

                // Construir mensaje
                mensajeClientes.append("ID: ").append(idCliente)
                               .append(", Nombre: ").append(nombreCliente)
                               .append(", Cédula: ").append(cedulaCliente).append("\n");
            }

            // Mostrar datos de clientes
            JOptionPane.showMessageDialog(null, mensajeClientes.toString(), "Clientes", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            // Mostrar mensaje de error
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Invocar el menú de operaciones del banco
        OperacionesBanco operacionesBanco = new OperacionesBanco();
        operacionesBanco.menuOpciones();
    }
}