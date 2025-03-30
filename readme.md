# SOLUCION PRÁCTICA 4
Siguiendo una idea relacionada con la práctica 2 (en mi caso era Telecomaster, información sobre teleco), se ha diseñado una API rest que permita gestionar información sobre carreras universitarias.

En el directorio de <a href="./frontend/"> frontend </a> se puede encontrar la página donde se integra y se emplea la lógica de la API a diseñar.

Las carreras se almacenan en un modelo con estructura similar a la siguiente:
```java
public record ModeloCarrera(long id,
                            @NotBlank @Size(max=100) String nombre,
                            @NotBlank String especialidad,
                            @NotBlank String descripcion,
                            @NotBlank String imagen) {
}
```

A continuacion se muestra una tabla con todas las operaciones que contempla la API

<div align="center">

| **Función**                     | **Método** | **URL**                                      | **Descripción**                                               |
|----------------------------------|-----------|----------------------------------------------|---------------------------------------------------------------|
| **Añadir carrera**               | **POST**  | `/telecomaster/carreras`                            | Crea una nueva carrera, deberá cumplir las restriciones de ModeloCarrera |
| **Obtener carrera (ID)**              | **GET**   | `/telecomaster/carreras/{id}`         | Devuelve la información de una carrera específica según índice          |
| **Obtener carreras por nombre**              | **GET**   | `/telecomaster/carreras/nombre/{nombre}`         | Devuelve la información de todas las carreras que tengan el mismo nombre (tendrá diferentes especialidades)         |
| **Obtener carrera (nombre y especialidad)**              | **GET**   | `/telecomaster/carreras/{nombre}/{especialidad}`         | Devuelve la información de una carrera específica según nombre y especialidad         |
| **Obtener todas las carreras**   | **GET**   | `/telecomaster/carreras`                    | Devuelve la lista de todas las carreras disponibles          |
| **Actualizar descripcion**          | **PUT**   | `/telecomaster/carreras/{nombre}/{especialidad}/descripcion`     | Actualiza la descripcion de una carrera en específico   |
| **Eliminar carrera (ID)**          | **DELETE**   | `/telecomaster/carreras/{id}`     | Elimina una carrera bien buscándola por id   |
| **Eliminar carrera (nombre y especialidad)**          | **DELETE**   | `/telecomaster/carreras/{nombre}/{especialidad}`     | Elimina una carrera por nombre y especialidad   |

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
        "valor": null
    },
    {
        "mensaje": "Se requiere imagen (url)",
        "campo": "imagen",
        "valor": null
    },
    {
        "mensaje": "Se requiere especialidad",
        "campo": "especialidad",
        "valor": null
    },
    {
        "mensaje": "Se requiere descripcion",
        "campo": "descripcion",
        "valor": null
    }
]
```


## Ejemplos
NOTA: Omito las url para que no se vea pesado

1. Introducir el grado de GITT

Comando bash equivalente a la acción en Postman
```sh
curl -X POST "http://localhost:8080/telecomaster/carreras" \
     -H "Content-Type: application/json" \
     -d '{
    "nombre" : "Teleco",
    "especialidad": "Sistemas electrónicos",
    "descripcion" : "Teleco orientado a los sistemas electronicos",
    "imagen" : "(url)"
}'
```
El fetch devuelve el siguiente json:
```json
{
    "id": 3,
    "nombre": "Teleco",
    "descripcion": "Teleco orientado a los sistemas electronicos",
    "especialidad": "Sistemas electrónicos",
    "imagen": "(url)"
}
```
2. Obtener una carrera 

```sh
curl -X GET "http://tudominio.com/telecomaster/carreras/nombre/GITT" | jq

//El jq es para que devuelva "bonito" el json
```
El resultado del fetch entonces es:

```json
[
    {
        "id": 1,
        "nombre": "GITT",
        "descripcion": "Grado universitario en Tecnologías de Telecomunicacion",
        "especialidad": "Tecnologias de la informacion",
        "imagen": "(url)"
    },
    {
        "id": 2,
        "nombre": "GITT",
        "descripcion": "Grado universitario en Tecnologías de Telecomunicacion",
        "especialidad": "Ingenieria biomedica",
        "imagen": "(url)"
    }
]
```
3. Actualizar carrera
```sh
curl -X PUT "http://localhost:8080/telecomaster/carreras/gitt/descripcion" \
     -H "Content-Type: application/json" \
     -d '{"descripcion": "Teleco-Bio"}'
```
La API rest devuelve en consecuencia el siguiente json:
```json
{
    "id": 2,
    "nombre": "GITT",
    "descripcion": "Teleco-Bio",
    "especialidad": "Ingenieria biomedica",
    "imagen": "(url)"
}
```
4. Borrar carrera
```sh
curl -X DELETE "http://localhost:8080/telecomaster/carreras/GITT/Ingenieria biomedica" 
//Tambien valdría el id en vez de gitt
```
El servidor devuelve el siguiente json de confirmación:
```json
{
    "id": 2,
    "nombre": null,
    "descripcion": null,
    "especialidad": null,
    "imagen": null
}
```

