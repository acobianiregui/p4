package p4.telecomaster.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ModeloCarrera(long id,
                            @NotBlank(message = "Se requiere nombre") @Size(max=100, message = "Descripcion muy extensa")
                            String nombre,
                            @NotBlank(message = "Se requiere descripcion")
                            String descripcion,
                            @NotBlank(message = "Se requiere nivel")
                            String nivel) {

}
//Nivel se refiere si es FP,postgrado, master, doctorado ...