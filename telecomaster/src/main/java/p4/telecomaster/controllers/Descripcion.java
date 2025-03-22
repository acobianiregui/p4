package p4.telecomaster.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Descripcion(@NotBlank(message = "Descripción vacía")
                          String descripcion) {
}
