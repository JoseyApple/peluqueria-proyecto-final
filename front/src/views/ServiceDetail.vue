<template>
  <div class="detalle-servicio">
  
    <button class="btn-back" @click="volverAServices" aria-label="Volver a servicios">
      ← Volver a Servicios
    </button>

    <div v-if="serviciosFiltrados.length" class="tarjetas-container">
      <div
        v-for="(servicio, index) in serviciosFiltrados"
        :key="index"
        class="servicio-contenido"
      >
        <h1>{{ servicio.nombre }}</h1>
        <p>{{ servicio.descripcion }}</p>
        <p><strong>Precio:</strong> {{ servicio.precio }}</p>
        <p><strong>Duración:</strong> {{ servicio.duracion }}</p>

        <router-link
          :to="{
            name: 'Reservar',
            params: { slug: servicio.slug },
            query: {
              id: servicio.id,
              nombre: servicio.nombre,
              descripcion: servicio.descripcion,
              precio: servicio.precio,
              duracion: servicio.duracion
            }
          }"
          class="btn-reservar"
        >
          Reservar
        </router-link>
      </div>
    </div>
    <div v-else class="servicio-contenido">
      <p>Servicio no encontrado</p>
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const servicios = [
  {
    id: 1,
    nombre: "Corte Masculino Estilizado",
    slug: "corte",
    descripcion: "Corte moderno con técnicas de degradado y estilo personalizado.",
    precio: "20 €",
    duracion: "30 min"
  },
  {
    id: 2,
    nombre: "Corte Femenino a Medida",
    slug: "corte",
    descripcion: "Estilizado profesional adaptado a tus rasgos y estilo personal.",
    precio: "25 €",
    duracion: "60 min"
  },
  {
    id: 3,
    nombre: "Corte + Lavado y Peinado",
    slug: "corte",
    descripcion: "Un servicio completo que incluye lavado, corte y peinado.",
    precio: "30 €",
    duracion: "90 min"
  },
  {
    id: 4,
    nombre: "Coloración Global Profesional",
    slug: "coloracion",
    descripcion: "Aplicación de color completo con acabado uniforme y duradero.",
    precio: "50 €",
    duracion: "120 min"
  },
  {
    id: 5,
    nombre: "Técnica de Mechas Personalizadas",
    slug: "coloracion",
    descripcion: "Iluminación natural o fantasía para aportar brillo y profundidad.",
    precio: "60 €",
    duracion: "120 min"
  },
  {
    id: 6,
    nombre: "Balayage Profesional",
    slug: "coloracion",
    descripcion: "Coloración degradada y natural para un look sofisticado.",
    precio: "70 €",
    duracion: "150 min"
  },
  {
    id: 7,
    nombre: "Peinado de Evento",
    slug: "peinados",
    descripcion: "Peinado profesional para bodas, fiestas u ocasiones especiales.",
    precio: "30 €",
    duracion: "60 min"
  },
  {
    id: 8,
    nombre: "Ondas Glamour",
    slug: "peinados",
    descripcion: "Ondas suaves y definidas para un look elegante.",
    precio: "35 €",
    duracion: "60 min"
  },
  {
    id: 9,
    nombre: "Recogido Profesional",
    slug: "peinados",
    descripcion: "Recogidos clásicos o modernos con fijación duradera.",
    precio: "40 €",
    duracion: "60 min"
  },
  {
    id: 10,
    nombre: "Tratamiento de Hidratación Profunda",
    slug: "tratamientos",
    descripcion: "Recupera el brillo y suavidad con hidratación intensiva.",
    precio: "40 €",
    duracion: "60 min"
  },
  {
    id: 11,
    nombre: "Alisado con Keratina",
    slug: "tratamientos",
    descripcion: "Elimina el encrespamiento y alisa tu cabello hasta 3 meses.",
    precio: "90 €",
    duracion: "120 min"
  },
  {
    id: 12,
    nombre: "Botox Capilar Restaurador",
    slug: "tratamientos",
    descripcion: "Regenera la fibra capilar desde la raíz con efecto anti-edad.",
    precio: "75 €",
    duracion: "90 min"
  },
  {
    id: 13,
    nombre: "Afeitado Clásico con Toalla Caliente",
    slug: "barberia",
    descripcion: "Ritual clásico con espuma caliente y cuidado facial.",
    precio: "15 €",
    duracion: "30 min"
  },
  {
    id: 14,
    nombre: "Diseño de Barba Profesional",
    slug: "barberia",
    descripcion: "Perfilado y definición de barba para un estilo impecable.",
    precio: "18 €",
    duracion: "30 min"
  },
  {
    id: 15,
    nombre: "Pack Corte + Barba",
    slug: "barberia",
    descripcion: "Combinación perfecta para un cambio completo de look.",
    precio: "30 €",
    duracion: "60 min"
  },
  {
    id: 16,
    nombre: "Diseño de Cejas con Hilo",
    slug: "cejas-rostro",
    descripcion: "Precisión y definición con técnica profesional de hilo.",
    precio: "12 €",
    duracion: "30 min"
  },
  {
    id: 17,
    nombre: "Limpieza Facial Exprés",
    slug: "cejas-rostro",
    descripcion: "Elimina impurezas y revitaliza tu piel en minutos.",
    precio: "25 €",
    duracion: "30 min"
  },
  {
    id: 18,
    nombre: "Maquillaje Social Profesional",
    slug: "cejas-rostro",
    descripcion: "Ideal para eventos o sesiones fotográficas, adaptado a tu estilo.",
    precio: "35 €",
    duracion: "60 min"
  }
]

const serviciosFiltrados = servicios.filter(s => s.slug === route.params.slug)

function volverAServices() {
  router.push('/services')
}
</script>

<style scoped>
.detalle-servicio {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 80px 20px;
  min-height: 100vh;
  background: linear-gradient(to bottom, #fbeff2, #f3f4f1);
  font-family: 'Orbitron', sans-serif;
}

.btn-back {
  align-self: flex-start;
  background: none;
  border: none;
  color: #8b3a62;
  font-weight: bold;
  cursor: pointer;
  margin-bottom: 30px;
  font-family: 'Orbitron', sans-serif;
  font-size: 1.2rem;
  transition: color 0.3s ease;
}

.btn-back:hover {
  color: #a44c77;
}

.tarjetas-container {
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
  justify-content: center;
  width: 100%;
  max-width: 1200px;
}

.servicio-contenido {
  width: 320px;
  background-color: white;
  padding: 30px;
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
  text-align: center;
  transition: transform 0.3s ease;
}

.servicio-contenido:hover {
  transform: translateY(-5px);
}

.servicio-contenido h1 {
  font-size: 1.8rem;
  margin-bottom: 20px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.servicio-contenido p {
  font-size: 1.05rem;
  margin-bottom: 10px;
  color: #444;
}

.servicio-contenido strong {
  font-weight: bold;
  color: #000;
}

.btn-reservar {
  display: inline-block;
  margin-top: 20px;
  padding: 12px 24px;
  font-size: 1rem;
  font-weight: bold;
  background-color: #8b3a62;
  color: white;
  border-radius: 10px;
  text-decoration: none;
  transition: background-color 0.3s ease;
}

.btn-reservar:hover {
  background-color: #a44c77;
}

@media (max-width: 768px) {
  .tarjetas-container {
    flex-direction: column;
    align-items: center;
  }

  .servicio-contenido {
    width: 90%;
  }
}
</style>
