package bancario;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import bancario.base.Cuenta;

public class OperacionesBanco {
    private String decisionTxt;
    private int decision;
    private Banco banco = new Banco();
    private int numCuentas = 0;

    public OperacionesBanco() {
    }

    public void menuOpciones() {
        JOptionPane.showMessageDialog(null, "Buen día cliente, ¿qué desea hacer hoy?");
        decisionTxt = JOptionPane.showInputDialog(
                "1: Agregar nueva cuenta \n" +
                        "2: Efectuar una búsqueda de cuenta\n" +
                        "3: Realizar una consignación\n" +
                        "4: Retirar dinero de tu cuenta\n" +
                        "5: Consultar el dinero total del banco\n" +
                        "6: Cliente con mayor base económica");
        decision = Integer.parseInt(decisionTxt);
        menu(decision);
    }

    private void menu(int num) {
        switch (num) {
            case 1:
                numCuentas += 1;
                String numCuentasString = String.valueOf(numCuentas);
                String nombreTitular = JOptionPane.showInputDialog("Ingresa el nombre del titular de la cuenta\n");
                String cedulaTitular = JOptionPane.showInputDialog("Ingresa la cédula del titular de la cuenta\n");
                String tipoCuenta = JOptionPane.showInputDialog("Ingresa el tipo de la cuenta: Ahorros o Corriente\n");
                String saldoInicialStr = JOptionPane.showInputDialog("Ingresa el saldo inicial de la cuenta\n");
                double saldoInicial = Double.parseDouble(saldoInicialStr);

                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/banco", "postgres", "Colombia2024")) {
                    Statement statement = connection.createStatement();
                    boolean seAgrego = banco.adicionarCuenta(numCuentasString, saldoInicial, tipoCuenta, cedulaTitular, nombreTitular, statement);

                    if (seAgrego) {
                        JOptionPane.showMessageDialog(null, "Registro exitoso");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se registró la cuenta");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error en la base de datos: " + e.getMessage());
                }
                break;

            case 2:
                String cuentaABuscar = JOptionPane.showInputDialog("Ingresa el número de la cuenta a buscar\n");
                Cuenta busqueda = banco.buscarCuenta(cuentaABuscar);

                if (busqueda == null) {
                    JOptionPane.showMessageDialog(null, "La cuenta no existe");
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Titular: " + busqueda.getTitular().getNombre() + "\n" +
                                    "Cédula titular: " + busqueda.getTitular().getCedula() + "\n" +
                                    "Tipo de cuenta: " + busqueda.getTipo() + "\n" +
                                    "Saldo: " + busqueda.getSaldo());
                }
                break;

            case 3:
                cuentaABuscar = JOptionPane.showInputDialog("Ingresa el número de la cuenta a consignar\n");
                busqueda = banco.buscarCuenta(cuentaABuscar);

                if (busqueda == null) {
                    JOptionPane.showMessageDialog(null, "La cuenta no existe");
                } else {
                    String cantidadConsignarStr = JOptionPane.showInputDialog("Ingresa la cantidad a consignar");
                    double cantidadConsignar = Double.parseDouble(cantidadConsignarStr);

                    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/banco", "postgres", "Colombia2024")) {
                        Statement statement = connection.createStatement();

                        if (cantidadConsignar <= 0) {
                            JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0.");
                        } else {
                            busqueda.setSaldo(busqueda.getSaldo() + cantidadConsignar);
                            String updateQuery = "UPDATE cuentas SET saldo_cuenta = " + busqueda.getSaldo() + " WHERE numero_cuenta = '" + cuentaABuscar + "'";
                            statement.executeUpdate(updateQuery);

                            JOptionPane.showMessageDialog(null, "Consignación realizada exitosamente.\nNuevo saldo: " + busqueda.getSaldo());
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error al realizar la consignación: " + e.getMessage());
                    }
                }
                break;

            case 4:
                cuentaABuscar = JOptionPane.showInputDialog("Ingresa el número de la cuenta a retirar\n");
                busqueda = banco.buscarCuenta(cuentaABuscar);

                if (busqueda == null) {
                    JOptionPane.showMessageDialog(null, "La cuenta no existe");
                } else {
                    String cantidadRetirarStr = JOptionPane.showInputDialog("Ingresa la cantidad a retirar");
                    double cantidadRetirar = Double.parseDouble(cantidadRetirarStr);

                    if (busqueda.retirar(cantidadRetirar)) {
                        JOptionPane.showMessageDialog(null, "Retiro realizado exitosamente.\nNuevo saldo: " + busqueda.getSaldo());
                    } else {
                        JOptionPane.showMessageDialog(null, "Fondos insuficientes.");
                    }
                }
                break;

            case 5:
                double totalDineroBanco = banco.consultarTotalDinero();
                JOptionPane.showMessageDialog(null, "El total de dinero del banco es: " + totalDineroBanco);
                break;

            case 6:
                String nombreMayorDinero = banco.consultarClienteMayorSaldo();
                JOptionPane.showMessageDialog(null, "El cliente con mayor dinero es: " + nombreMayorDinero);
                break;

            default:
                JOptionPane.showMessageDialog(null, "Opción no válida.");
                break;
        }
    }
}