const API_URL = "http://localhost:8080/telecomaster/carreras";
window.addEventListener("load", cargarCarreras);

const img_url = document.getElementById("imagen_url");
const imagenPreview = document.getElementById("imagenPreview");
const seccionAñadir = document.getElementById("seccionAñadir");
const seccionActualizar = document.getElementById("seccionActualizar");
const seccionBorrar = document.getElementById("seccionBorrar");
const menuOpciones = document.getElementById("menuOpciones");
const inputImagen = document.getElementById("imagen_url");
const previewImagen = document.createElement("img");

menuOpciones.addEventListener("change", function () {
    seccionAñadir.style.display = "none";
    seccionActualizar.style.display = "none";
    seccionBorrar.style.display = "none";
    
    if (menuOpciones.value === "añadir") {
        seccionAñadir.style.display = "block";
    } else if (menuOpciones.value === "actualizar") {
        seccionActualizar.style.display = "block";
    } else if (menuOpciones.value === "borrar") {
        seccionBorrar.style.display = "block";
    }
});

function cargarCarreras() {
    fetch(API_URL)
        .then(response => response.json())
        .then(data => {
            const lista = document.getElementById("listaCarreras");
            data.sort((a, b) => Number(a.id) - Number(b.id));
            lista.innerHTML = "";
            data.forEach(carrera => {
                
                const div = document.createElement("div");
                div.classList.add("carrera");
                div.innerHTML = `
                    <h3 align="center">${carrera.nombre}</h3>
                    <p><strong>Especialidad:</strong> ${carrera.especialidad}</p>
                    <p>${carrera.descripcion}</p>
                    <img  src="${carrera.imagen}" alt="Imagen de ${carrera.nombre}"
                    style="max-width: 500px; height: 350px ; display: block; margin: 10px 0;"
                    align="center">
                `;
                lista.appendChild(div);
            });
        })
        .catch(error => console.error("Error cargando carreras:", error));
}

document.getElementById("formCarrera").addEventListener("submit", function(event) {
    event.preventDefault();

    const nuevaCarrera = {
        nombre: document.getElementById("nombre").value,
        descripcion: document.getElementById("descripcion").value,
        especialidad: document.getElementById("especialidad").value,
        imagen: document.getElementById("imagen_url").value
    };

    fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(nuevaCarrera)
    })
    .then(response => response.json())
    .then(() => {
        cargarCarreras();  
        document.getElementById("formCarrera").reset();
    })
    .catch(error => console.error("Error añadiendo carrera:", error));
});
inputImagen.addEventListener("input", () => {
    const url = inputImagen.value.trim();
    if (url) {
        previewImagen.src = url;
        previewImagen.style.maxWidth = "200px";
        previewImagen.style.display = "block";
        previewImagen.style.alignSelf="center";
    } else {
        previewImagen.style.display = "none";
    }
});


inputImagen.insertAdjacentElement("afterend", previewImagen);

//Esto tambien se podria hcaer en el css 
previewImagen.style.maxWidth = "200px";
previewImagen.style.display = "none"; 
previewImagen.style.margin = "15px auto"; 
previewImagen.style.display = "block";

document.getElementById("formActualizar").addEventListener("submit", function(event) {
    event.preventDefault();

    const nombre = document.getElementById("nombreActualizar").value;
    const especialidad = document.getElementById("especialidadActualizar").value;
    const descripcion = document.getElementById("descripcionActualizar").value;

    fetch(`${API_URL}/${nombre}/${especialidad}/descripcion`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ descripcion })
    })
    .then(response => response.json())
    .then(() => {
        cargarCarreras();  
        document.getElementById("formActualizar").reset();
    })
    .catch(error => console.error("Error actualizando carrera:", error));
});

document.getElementById("formBorrar").addEventListener("submit", function(event) {
    event.preventDefault();

    const nombre = document.getElementById("nombreBorrar").value;
    const especialidad = document.getElementById("especialidadBorrar").value;

    fetch(`${API_URL}/${nombre}/${especialidad}`, {
        method: "DELETE"
    })
    .then(() => {
        cargarCarreras();  
        document.getElementById("formBorrar").reset();
    })
    .catch(error => console.error("Error borrando carrera:", error));
});

const verImagenUrl = () => actualizaImagen(img_url.value);

const actualizaImagen = (src) => {
    const image = new Image();
    image.onload = () => {
        imagenPreview.src = src;
        imagenPreview.style.alignSelf="center";
    };
    image.onerror = () => {
        imagenPreview.src = ''; // Por si la url no me vale
    };
    image.src = src;
    img_url.setCustomValidity('');
};

img_url.addEventListener('input', verImagenUrl);
