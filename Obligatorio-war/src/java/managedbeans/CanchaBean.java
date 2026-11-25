package managedbeans;

import entities.Cancha;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import services.CanchaService;

@Named("canchaBean")
@ViewScoped
public class CanchaBean implements Serializable {

    @EJB
    private CanchaService canchaService;

    private List<Cancha> canchas;
    private Cancha canchaSeleccionada;

    @PostConstruct
    public void init() {
        cargarCanchas();
    }

    public void cargarCanchas() {
        canchas = canchaService.listarTodas();
    }

    public void nuevaCancha() {
        canchaSeleccionada = new Cancha();
    }

    public void cancelar() {
        canchaSeleccionada = null;
    }

    public void seleccionar(Cancha cancha) {
        canchaSeleccionada = cancha;
    }

    public void guardar() {
        try {
            if (canchaSeleccionada.getCodigo() > 0) {
                canchaService.actualizar(canchaSeleccionada);
                mensaje(FacesMessage.SEVERITY_INFO, "Cancha actualizada correctamente");
            } else {
                canchaService.crear(canchaSeleccionada);
                mensaje(FacesMessage.SEVERITY_INFO, "Cancha creada correctamente");
            }
            canchaSeleccionada = null;
            cargarCanchas();
        } catch (RuntimeException e) {
            mensaje(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public void eliminar(Cancha cancha) {
        try {
            canchaService.eliminarCancha(cancha.getCodigo());
            mensaje(FacesMessage.SEVERITY_INFO, "Cancha eliminada correctamente");
            cargarCanchas();
        } catch (RuntimeException e) {
            mensaje(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    private void mensaje(FacesMessage.Severity severidad, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severidad, detalle, null));
    }

    public List<Cancha> getCanchas() {
        return canchas;
    }

    public Cancha getCanchaSeleccionada() {
        return canchaSeleccionada;
    }

    public void setCanchaSeleccionada(Cancha canchaSeleccionada) {
        this.canchaSeleccionada = canchaSeleccionada;
    }
}
