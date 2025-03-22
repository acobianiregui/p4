package p4.telecomaster.controllers.errores;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ExcepcionCarreraMal extends RuntimeException{
        List<FieldError> errores;
        public ExcepcionCarreraMal(BindingResult resultado){
            this.errores=resultado.getFieldErrors();
        }
        public List<FieldError> getErrores() {
            return errores;
        }
}
