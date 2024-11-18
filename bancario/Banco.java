package bancario;
import java.sql.Statement;
import java.util.ArrayList;
import bancario.base.Cliente;
import bancario.base.Cuenta;

public class Banco {
    String nombre;
    ArrayList<Cuenta> cuentas;

    public Banco() {
        this.cuentas = new ArrayList<>(4);
    }

    public Cuenta buscarCuenta(String numero) {
        
        for (Cuenta cuenta : this.cuentas) {
            if (cuenta.getNumero().equals(numero)) {
                return cuenta;
            }
        }
        return null;
    }

    public boolean adicionarCuenta(String numero, double saldoInicial, String tipo, String cedulaTitular, String nombreTitular, Statement statement) {
        Cliente cliente = new Cliente(cedulaTitular, nombreTitular);
        Cuenta cuentaBuscar = this.buscarCuenta(numero);
        
        // Verificamos si la cuenta ya existe
        if (cuentaBuscar == null) {
            Cuenta cuenta = new Cuenta(tipo, numero, saldoInicial, cliente);
            // Insertamos la cuenta en la base de datos
            boolean exito = cuenta.insertarCuenta(statement);
            if (exito) {
                // Si la inserción fue exitosa, agregamos la cuenta a la lista en memoria (opcional)
                this.cuentas.add(cuenta);
            }
            return exito;
        } else {
            return false;
        }
    }
    
        public double consultarTotalDinero() {
            double total = 0;
    
            for (Cuenta cuenta : this.cuentas) {
                total += cuenta.consultarSaldo();
            }
    
            return total;
        }
    
        public String consultarClienteMayorSaldo() {
            double mayorSaldo = 0;
            String nombreTitular = "";
    
            for (Cuenta cuenta : this.cuentas) {
                if (cuenta.consultarSaldo() > mayorSaldo) {
                    mayorSaldo = cuenta.consultarSaldo();
                    nombreTitular = cuenta.getTitular().getNombre();
                }
            }
            return nombreTitular.isEmpty() ? "Nadie": nombreTitular;
        }
    }