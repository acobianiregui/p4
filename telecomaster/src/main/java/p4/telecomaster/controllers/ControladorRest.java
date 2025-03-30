package p4.telecomaster.controllers;


import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import p4.telecomaster.controllers.errores.ExcepcionCarreraMal;

import java.util.*;


//Controlador REST
@RestController
@RequestMapping("telecomaster/carreras")
public class ControladorRest {
    private final HashMap<String,ModeloCarrera> carreras = new HashMap<>();
    //La clave del hashmap sera nombre_especialidad
    //las carrareras tendran id
    private Long contador_id=1L;

    @PostConstruct
    public void inicializar() {
        // Crea una carrera por defecto al inicio
        String n= "GITT";
        String desc= "Grado universitario en Tecnologías de Telecomunicacion";
        String esp="Tecnologias de la informacion";
        String link= "https://media.istockphoto.com/id/1306156001/es/foto/torre-de-telecomunicaciones-con-puntos-de-malla-part%C3%ADculas-brillantes-para-tecnolog%C3%ADa.jpg?s=612x612&w=0&k=20&c=0aiUOlX2U2mFR-qsG1DlxU1zVWTmwZK-rb5vC9Qh4PQ=";
        ModeloCarrera carreraPorDefecto = new ModeloCarrera(contador_id, n, desc, esp,link);
        carreras.put(carreraPorDefecto.nombre()+"_"+carreraPorDefecto.especialidad(), carreraPorDefecto);
        contador_id++;

        esp="Ingenieria biomedica";
        link="https://universidadeuropea.com/resources/media/images/ingenieria-biomedica-1200x630.original.jpg";
        carreraPorDefecto = new ModeloCarrera(contador_id, n, desc, esp,link);
        carreras.put(carreraPorDefecto.nombre()+"_"+carreraPorDefecto.especialidad(), carreraPorDefecto);
        contador_id++;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModeloCarrera addCarrera(@Valid @RequestBody ModeloCarrera nueva, BindingResult resultado){
        if (resultado.hasErrors()){
            throw new ExcepcionCarreraMal(resultado);
        }
        ModeloCarrera sol= new ModeloCarrera(contador_id,nueva.nombre(),nueva.descripcion(),nueva.especialidad(),nueva.imagen());
        carreras.put(sol.nombre()+"_"+sol.especialidad(),sol);
        contador_id++;
        return sol;
    }
    //El get por id (luego ns si lo usare)
    @GetMapping("/{id}")
    public ModeloCarrera getCarreraId(@PathVariable Long id){
        ModeloCarrera solucion=null;
        for (HashMap.Entry<String, ModeloCarrera> c : carreras.entrySet()){

            if (Objects.equals(c.getValue().id(), id)) {
                solucion= c.getValue();
                break;
            }
        }
        if (solucion== null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no encontrada");
        }
        return solucion;
    }
    //Obtener todas con el mismo nombre
    @GetMapping("/nombre/{nombre}")
    public List<ModeloCarrera> obtenerCarrerasPorNombre(@PathVariable String nombre) {
        List<ModeloCarrera> resultado = new ArrayList<>();

        for (ModeloCarrera carrera : carreras.values()) {
            // Verificar si el nombre de la carrera coincide con el nombre buscado
            if (carrera.nombre().equals(nombre)) {
                resultado.add(carrera);
            }
        }

        return resultado;
    }
    //Puede buscar por nombre y especialidad
    @GetMapping("/{nombre}/{especialidad}")
    public ModeloCarrera getCarrera(@PathVariable String nombre, @PathVariable String especialidad){
        //Si hay catch es que es por nombre y especialidad
        ModeloCarrera solucion= carreras.get(nombre+"_"+especialidad);
        if (solucion== null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no encontrada");
        }
        return solucion;
    }

    //Hacer get de todas las carreras
    @GetMapping
    public List<ModeloCarrera> getTodos(){
        return new ArrayList<>(carreras.values());
    }

    @PutMapping("/{nombre}/{especialidad}/descripcion")
    public ModeloCarrera actualizaDescripcion(@PathVariable String nombre, @PathVariable String especialidad,
                                              @Valid @RequestBody Descripcion texto,
                                              BindingResult resultado){
        if(resultado.hasErrors()){
            throw new ExcepcionCarreraMal(resultado);
        }

        ModeloCarrera carrera=carreras.get(nombre+"_"+especialidad);
        if (carrera == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no encontrada");
        }

        //Remplazamos
        ModeloCarrera remplazo = new ModeloCarrera(carrera.id(),carrera.nombre(),texto.descripcion(),carrera.especialidad(), carrera.imagen());
        carreras.remove(carrera.nombre());
        carreras.put(carrera.nombre()+"_"+carrera.especialidad(),remplazo);

        return remplazo;
    }
    //Borrar por id
    @DeleteMapping("/{id}")
    public ModeloCarrera eliminarCarreraId(@PathVariable Long id){
        for (HashMap.Entry<String, ModeloCarrera> c : carreras.entrySet()){
            if (Objects.equals(c.getValue().id(), id)) {
                carreras.remove(c.getKey());
                break;
            }
        }
        return new ModeloCarrera(id,null,null,null,null);
    }
    //Borrar por nombre y especialidad
    @DeleteMapping("/{nombre}/{especialidad}")
    public ModeloCarrera eliminarCarrera(@PathVariable String nombre, @PathVariable String especialidad) {

        ModeloCarrera borra= carreras.get(nombre+"_"+especialidad);
        carreras.remove(nombre+"_"+especialidad);
        return new ModeloCarrera(borra.id(), null, null,null, null);

    }
    //ahora gestion de errores (para httpStatus)
    @ExceptionHandler(ExcepcionCarreraMal.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ModeloCampoIncorrecto> contadorIncorrecto(ExcepcionCarreraMal ex) {
        return ex.getErrores().stream().map(error -> new ModeloCampoIncorrecto(
                error.getDefaultMessage(), error.getField(), error.getRejectedValue()
        )).toList();
    }

}
