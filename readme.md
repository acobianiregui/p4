# SOLUCION PRÁCTICA 4
Siguiendo una idea relacionada con la práctica 2 (en mi caso era Telecomaster, información sobre teleco), se ha diseñadi una API rest que permita gestionar información sobre carreras universitarias.

Las carreras se almacenan en un modelo con estructura similar a la siguiente:
```java
public record ModeloCarrera(long id,
                            @NotBlank @Size(max=100) String nombre,
                            @NotBlank String descripcion,
                            @NotBlank String nivel) {
}
```

A continuacion se muestra una tabla con todas las operaciones que contempla la API

<div align="center">

| **Función**                     | **Método** | **URL**                                      | **Descripción**                                               |
|----------------------------------|-----------|----------------------------------------------|---------------------------------------------------------------|
| **Añadir carrera**               | **POST**  | `/telecomaster/carreras`                            | Crea una nueva carrera, deberá cumplir las restriciones de ModeloCarrera |
| **Obtener carrera**              | **GET**   | `/telecomaster/carreras/{objetivo}`         | Devuelve la información de una carrera específica según índice o nombre (cualquiera de los dos vale)           |
| **Obtener todas las carreras**   | **GET**   | `/telecomaster/carreras`                    | Devuelve la lista de todas las carreras disponibles          |
| **Actualizar descripcion**          | **PUT**   | `/telecomaster/carreras/{nombre}/descripcion`     | Actualiza la descripcion de una carrera en específico   |
| **Eliminar carrera**          | **DELETE**   | `/telecomaster/carreras/{objetivo}`     | Elimina una carrera bien buscándola por id o por nombre (ambas funcionan)   |

</div>

## Errores

<p align="justify">
También se ha incluido una funcionalidad de manejo de errores por si el fetch no cumpliera los requisitos de modelo carrera.
Por ejemplo, si hace un post vacío, la API devuelve el siguiente json:
</p>

``` json
[
    {
        "mensaje": "Se requiere nombre",
        "campo": "nombre",
        "valor": ""
    },
    {
        "mensaje": "Se requiere descripcion",
        "campo": "descripcion",
        "valor": ""
    },
    {
        "mensaje": "Se requiere nivel",
        "campo": "nivel",
        "valor": ""
    }
]
```


## Ejemplos

1. Introducir el grado de GITT

Comando bash equivalente a la acción en Postman
```sh
curl -X POST "http://localhost:8080/telecomaster/carreras" \
     -H "Content-Type: application/json" \
     -d '{
           "nombre": "Ingeniería de Telecomunicaciones",
           "descripcion": "Carrera enfocada en redes, sistemas y comunicaciones",
           "nivel": "Grado"
         }'
```
El fetch devuelve el siguiente json:
```json
{
    "id": 1,
    "nombre": "gitt",
    "descripcion": "Grado en Ingenieria de Telecomunicaciones",
    "nivel": "grado"
}
```
2. Obtener una carrera 

```sh
curl -X GET "http://tudominio.com/telecomaster/carreras/gitt" | jq
//Tambien valdría poner 1 en vez de gitt
//El jq es para que devuelva "bonito" el json
```
El resultado del fetch entonces es:

```json
{
    "id": 1,
    "nombre": "gitt",
    "descripcion": "Grado en Ingenieria de Telecomunicaciones",
    "nivel": "grado"
}
```
3. Actualizar carrera
```sh
curl -X PUT "http://localhost:8080/telecomaster/carreras/gitt/descripcion" \
     -H "Content-Type: application/json" \
     -d '{"descripcion": "Grado de 4 años en telecomunicaciones"}'
```
La API rest devuelve en consecuencia el siguiente json:
```json
{
    "id": 1,
    "nombre": "gitt",
    "descripcion": "Grado de 4 años en telecomunicaciones",
    "nivel": "grado"
}
```
4. Borrar carrera
```sh
curl -X DELETE "http://localhost:8080/telecomaster/carreras/gitt" 
//Tambien valdría el id en vez de gitt
```
El servidor devuelve el siguiente json de confirmación:
```json
{
    "id": 1,
    "nombre": null,
    "descripcion": null,
    "nivel": null
}
```

