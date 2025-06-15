<template>
  <div class="reservar-servicio">
    <div v-if="servicio" class="servicio-contenido">
      <button class="btn-back" @click="router.back()" aria-label="Volver atrás">
        ← Volver atrás
      </button>
      <button class="btn-back" @click="volverAServices" aria-label="Volver a servicios" style="margin-left: 12px;">
        ← Volver a Servicios
      </button>

      <h1 class="titulo-servicio">{{ servicio.nombre }}</h1>
      <p class="descripcion">{{ servicio.descripcion }}</p>
      <p><strong>Precio:</strong> {{ servicio.precio }}</p>
      <p><strong>Duración:</strong> {{ servicio.duracion }}</p>

      <form @submit.prevent="confirmarReserva" class="form">
        <div class="form-group">
          <label for="fecha">Fecha</label>
          <input
            v-model="fecha"
            type="date"
            id="fecha"
            :min="minFecha"
            @change="cargarHorasDisponibles"
            required
            :disabled="loadingHoras"
          />
        </div>

        <div class="form-group">
          <label for="hora">Hora</label>
          <select v-model="hora" id="hora" required :disabled="loadingHoras || !horasDisponibles.length">
            <option value="" disabled>Selecciona una hora</option>
            <option
              v-for="h in tramosHorarios"
              :key="h"
              :value="h"
              :disabled="!horasDisponibles.includes(h)"
            >
              {{ h }} <span v-if="!horasDisponibles.includes(h)"> (Reservada)</span>
            </option>
          </select>
        </div>

        <p v-if="errorMsg" class="error-message">{{ errorMsg }}</p>

        <button type="submit" class="btn-confirmar" :disabled="loadingHoras">Confirmar Reserva</button>
      </form>
    </div>
    <div v-else class="servicio-contenido">
      <p>Servicio no encontrado</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import axios from '../api/axiosInstance'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const servicio = route.query.id
  ? {
      id: Number(route.query.id),
      nombre: route.query.nombre,
      descripcion: route.query.descripcion,
      precio: route.query.precio,
      duracion: route.query.duracion,
      slug: route.params.slug
    }
  : null

const fecha = ref('')
const hora = ref('')
const horasDisponibles = ref([])
const loadingHoras = ref(false)
const errorMsg = ref('')

const tramosHorarios = [
  "10:00", "10:30", "11:00", "11:30", "12:00", "12:30",
  "13:00", "13:30", "17:00", "17:30", "18:00", "18:30",
  "19:00", "19:30", "20:00", "20:30"
]
const minFecha = new Date().toISOString().split("T")[0]

function parseDuracionADuracionMinutos(duracionStr) {
  if (!duracionStr) return 0
  const horasMatch = duracionStr.match(/(\d+)\s*h/)
  const minutosMatch = duracionStr.match(/(\d+)\s*min/)
  const horas = horasMatch ? parseInt(horasMatch[1]) : 0
  const minutos = minutosMatch ? parseInt(minutosMatch[1]) : 0
  return horas * 60 + minutos
}

function expandirHorasOcupadas(startTime, endTime) {
  const ocupadas = []
  const start = new Date(startTime)
  const end = new Date(endTime)
  while (start < end) {
    const hh = start.getHours().toString().padStart(2, '0')
    const mm = start.getMinutes().toString().padStart(2, '0')
    ocupadas.push(`${hh}:${mm}`)
    start.setMinutes(start.getMinutes() + 30)
  }
  return ocupadas
}

async function cargarHorasDisponibles() {
  if (!fecha.value) {
    horasDisponibles.value = []
    hora.value = ''
    return
  }
  loadingHoras.value = true
  errorMsg.value = ''
  try {
    const res = await axios.get('/appointments/times-availability', {
      params: { date: fecha.value }
    })
    let horasOcupadas = []
    for (const cita of res.data) {
      horasOcupadas.push(...expandirHorasOcupadas(cita.startTime, cita.endTime))
    }
    const duracionMinutos = parseDuracionADuracionMinutos(servicio?.duracion)
    const franjasRequeridas = Math.ceil(duracionMinutos / 30)

    horasDisponibles.value = tramosHorarios.filter((horaActual, i, lista) => {
      const index = lista.indexOf(horaActual)
      const tramosContinuos = lista.slice(index, index + franjasRequeridas)
      return (
        tramosContinuos.length === franjasRequeridas &&
        tramosContinuos.every(h => !horasOcupadas.includes(h))
      )
    })

    if (!horasDisponibles.value.includes(hora.value)) {
      hora.value = ''
    }
  } catch (err) {
    errorMsg.value = 'Error cargando horarios disponibles, intenta más tarde.'
    horasDisponibles.value = []
    hora.value = ''
  } finally {
    loadingHoras.value = false
  }
}

async function confirmarReserva() {
  errorMsg.value = ''
  if (!fecha.value || !hora.value || !servicio) {
    errorMsg.value = 'Por favor, completa todos los datos de la reserva.'
    return
  }
  if (!horasDisponibles.value.includes(hora.value)) {
    errorMsg.value = 'La hora seleccionada no está disponible. Por favor elige otra.'
    return
  }

  const clientId = auth.user?.id
  if (!clientId) {
    errorMsg.value = 'Debes iniciar sesión para reservar.'
    return
  }

  try {
    const startTimeStr = `${fecha.value}T${hora.value}:00`
    const appointmentData = {
      startTime: startTimeStr,
      clientId,
      serviceIds: [servicio.id]
    }

    await axios.post('/appointments', appointmentData)
    alert('Reserva creada correctamente!')
    router.push('/reservas')
  } catch (error) {
    errorMsg.value = 'Error al crear la reserva. Por favor, intenta de nuevo.'
  }
}

const volverAServices = () => {
  router.push('/services')
}
</script>

<style scoped>
.reservar-servicio {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 80px 20px;
  background: linear-gradient(to bottom, #fbeff2, #f3f4f1);
  min-height: 100vh;
  font-family: 'Orbitron', sans-serif;
}
.servicio-contenido {
  background-color: white;
  padding: 40px 30px;
  border-radius: 16px;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.08);
  max-width: 600px;
  width: 100%;
  text-align: center;
  transition: transform 0.3s ease;
}
.servicio-contenido:hover {
  transform: translateY(-5px);
}
.btn-back {
  background: none;
  border: none;
  color: #8b3a62;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  margin-bottom: 20px;
  padding: 0;
  font-family: 'Orbitron', sans-serif;
  transition: color 0.3s ease;
}
.btn-back:hover {
  color: #a44c77;
}
.titulo-servicio {
  font-size: 2.5rem;
  margin-bottom: 20px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}
.descripcion {
  font-size: 1.2rem;
  margin-bottom: 15px;
  color: #444;
}
p {
  font-size: 1.1rem;
  color: #333;
  margin-bottom: 10px;
}
strong {
  font-weight: bold;
  color: #000;
}
.form {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 30px;
}
.form-group {
  text-align: left;
}
.form-group label {
  display: block;
  font-size: 1rem;
  margin-bottom: 8px;
  color: #555;
}
.form-group input,
.form-group select {
  width: 100%;
  padding: 12px;
  font-size: 1rem;
  border-radius: 10px;
  border: 1px solid #ccc;
  transition: border-color 0.3s;
}
.form-group input:focus,
.form-group select:focus {
  border-color: #e88e00;
  outline: none;
}
.btn-confirmar {
  background-color: #8b3a62;
  color: white;
  padding: 12px 24px;
  font-size: 1.1rem;
  font-weight: bold;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background 0.3s ease;
}
.btn-confirmar:hover {
  background-color: #a44c77;
}

@media (max-width: 768px) {
  .reservar-servicio {
    padding: 40px 10px;
  }

  .servicio-contenido {
    padding: 30px 20px;
  }

  .titulo-servicio {
    font-size: 2rem;
  }

  .descripcion,
  p,
  .form-group label {
    font-size: 1rem;
  }

  .btn-confirmar {
    font-size: 1rem;
    padding: 10px 20px;
  }
}
</style>
