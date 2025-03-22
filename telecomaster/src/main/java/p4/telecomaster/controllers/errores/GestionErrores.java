package p4.telecomaster.controllers.errores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GestionErrores {
    @ResponseBody
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity errorLanzado(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getStatusCode());
    }
}