package p4.telecomaster.controllers;


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
    //las carrareras tendran id
    private Long contador_id=1L;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModeloCarrera addCarrera(@Valid @RequestBody ModeloCarrera nueva, BindingResult resultado){
        if (resultado.hasErrors()){
            throw new ExcepcionCarreraMal(resultado);
        }
        ModeloCarrera sol= new ModeloCarrera(contador_id,nueva.nombre(),nueva.descripcion(),nueva.nivel());
        carreras.put(sol.nombre(),sol);
        contador_id++;
        return sol;
    }
    //Puede buscar por nombre o por id
    @GetMapping("/{nombre}")
    public ModeloCarrera getCarrera(@PathVariable String nombre){
        try{
            Long id=Long.parseLong(nombre);
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

        }catch (NumberFormatException e){ //Mala conversion
            //Si hay catch es que es por nombre
            ModeloCarrera solucion= carreras.get(nombre);
            if (solucion== null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no encontrada");
            }
            return solucion;
        }
    }
    //Hacer get de todas las carreras
    @GetMapping
    public List<ModeloCarrera> getTodos(){
        return new ArrayList<>(carreras.values());
    }

    @PutMapping("/{nombre}/descripcion")
    public ModeloCarrera actualizaDescripcion(@PathVariable String nombre, @Valid @RequestBody Descripcion texto,
                                              BindingResult resultado){
        if(resultado.hasErrors()){
            throw new ExcepcionCarreraMal(resultado);
        }

        ModeloCarrera carrera=carreras.get(nombre);
        if (carrera == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no encontrada");
        }
        //Remplazamos
        ModeloCarrera remplazo = new ModeloCarrera(carrera.id(),carrera.nombre(),texto.descripcion(),carrera.nivel());
        carreras.remove(carrera.nombre());
        carreras.put(carrera.nombre(),remplazo);

        return remplazo;
    }

    //Quiero borrar la carrera por id o por nombre
    //ojo a la jugada, llamare primero al delete por nombre
    // y si puede hacer conversiona long llamo a delete por id
    @DeleteMapping("/{objetivo}")
    public ModeloCarrera eliminarCarrera(@PathVariable("objetivo") String objetivo) {
        try{
            Long id=Long.parseLong(objetivo);
            for (HashMap.Entry<String, ModeloCarrera> c : carreras.entrySet()){

                if (Objects.equals(c.getValue().id(), id)) {
                    carreras.remove(c.getKey());
                    break;
                }
            }
            return new ModeloCarrera(id,null,null,null);

        }catch (NumberFormatException e){ //Mala conversion
            //Si hay catch es que es por nombre
            ModeloCarrera borra= carreras.get(objetivo);
            carreras.remove(objetivo);
            return new ModeloCarrera(borra.id(), null, null,null);
        }

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
