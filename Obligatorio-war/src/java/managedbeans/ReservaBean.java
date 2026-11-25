package managedbeans;

import entities.Cancha;
import entities.Cliente;
import entities.Reserva;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import services.CanchaService;
import services.ClienteService;
import services.ReservaService;

@Named("reservaBean")
@ViewScoped
public class ReservaBean implements Serializable {

    @EJB
    private ReservaService reservaService;

    @EJB
    private ClienteService clienteService;

    @EJB
    private CanchaService canchaService;

    private List<Reserva> reservas;
    private List<Cliente> clientes;
    private List<Cancha> canchas;

    private LocalDate fecha;
    private int turno = 1;
    private Integer clienteCodigo;
    private Integer canchaCodigo;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @PostConstruct
    public void init() {
        cargarDatos();
    }

    public void cargarDatos() {
        reservas = reservaService.listarTodas();
        clientes = clienteService.listarTodos();
        canchas = canchaService.listarTodas();
    }

    public void registrar() {
        try {
            Reserva reserva = new Reserva();
            reserva.setFecha(fecha);
            reserva.setTurno(turno);
            reserva.setCliente(clienteService.buscarPorId(clienteCodigo));
            reserva.setCancha(canchaService.buscarPorId(canchaCodigo));
            reservaService.registrarReserva(reserva);
            mensaje(FacesMessage.SEVERITY_INFO, "Reserva registrada correctamente");
            limpiarFormulario();
            cargarDatos();
        } catch (RuntimeException e) {
            mensaje(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public void eliminar(Reserva reserva) {
        try {
            reservaService.eliminar(reserva.getCodigo());
            mensaje(FacesMessage.SEVERITY_INFO, "Reserva eliminada correctamente");
            cargarDatos();
        } catch (RuntimeException e) {
            mensaje(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public void filtrarPorFechas() {
        try {
            LocalDate desde = fechaInicio != null ? fechaInicio : LocalDate.of(1000, 1, 1);
            LocalDate hasta = fechaFin != null ? fechaFin : LocalDate.of(9999, 12, 31);
            reservas = reservaService.listarReservasEntreFechas(desde, hasta);
        } catch (RuntimeException e) {
            mensaje(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public void verTodas() {
        reservas = reservaService.listarTodas();
    }

    public void limpiarFormulario() {
        fecha = null;
        turno = 1;
        clienteCodigo = null;
        canchaCodigo = null;
    }

    private void mensaje(FacesMessage.Severity severidad, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severidad, detalle, null));
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Cancha> getCanchas() {
        return canchas;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public Integer getClienteCodigo() {
        return clienteCodigo;
    }

    public void setClienteCodigo(Integer clienteCodigo) {
        this.clienteCodigo = clienteCodigo;
    }

    public Integer getCanchaCodigo() {
        return canchaCodigo;
    }

    public void setCanchaCodigo(Integer canchaCodigo) {
        this.canchaCodigo = canchaCodigo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
