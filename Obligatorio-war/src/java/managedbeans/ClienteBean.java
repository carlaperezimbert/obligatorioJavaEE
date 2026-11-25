package managedbeans;

import entities.Cliente;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import services.ClienteService;

@Named("clienteBean")
@ViewScoped
public class ClienteBean implements Serializable {

    @EJB
    private ClienteService clienteService;

    private List<Cliente> clientes;
    private Cliente clienteSeleccionado;

    @PostConstruct
    public void init() {
        cargarClientes();
    }

    public void cargarClientes() {
        clientes = clienteService.listarTodos();
    }

    public void nuevoCliente() {
        clienteSeleccionado = new Cliente();
    }

    public void cancelar() {
        clienteSeleccionado = null;
    }

    public void seleccionar(Cliente cliente) {
        clienteSeleccionado = cliente;
    }

    public void guardar() {
        try {
            if (clienteSeleccionado.getCodigo() > 0) {
                clienteService.actualizar(clienteSeleccionado);
                mensaje(FacesMessage.SEVERITY_INFO, "Cliente actualizado correctamente");
            } else {
                clienteService.crear(clienteSeleccionado);
                mensaje(FacesMessage.SEVERITY_INFO, "Cliente creado correctamente");
            }
            clienteSeleccionado = null;
            cargarClientes();
        } catch (RuntimeException e) {
            mensaje(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public void eliminar(Cliente cliente) {
        try {
            clienteService.eliminar(cliente.getCodigo());
            mensaje(FacesMessage.SEVERITY_INFO, "Cliente eliminado correctamente");
            cargarClientes();
        } catch (RuntimeException e) {
            mensaje(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    private void mensaje(FacesMessage.Severity severidad, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severidad, detalle, null));
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(Cliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }
}
