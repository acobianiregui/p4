package p4.telecomaster.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.thymeleaf.messageresolver.IMessageResolver;

public record ModeloCarrera(long id,
                            @NotBlank(message = "Se requiere nombre") @Size(max=100, message = "Nombre largo")
                            String nombre,
                            @NotBlank(message = "Se requiere descripcion") @Size(max=600, message = "Descripcion larga")
                            String descripcion,
                            @NotBlank(message = "Se requiere especialidad") @Size(max=100, message = "Especialidad")
                            String especialidad,
                            @NotBlank ( message= "Se requiere imagen (url)")
                            String imagen){

}
