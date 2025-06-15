<template>
  <div class="reservas-contenedor">
    <div class="reservas-caja">
      <div class="filtros">
        <input type="date" v-model="fecha" />

        <select v-model="hora">
          <option value="">Todas las horas</option>
          <option v-for="h in tramosHorarios" :key="h" :value="h">{{ h }}</option>
        </select>

        <select v-model="estado">
          <option value="">Todos los estados</option>
          <option value="PENDING">Pendiente</option>
          <option value="CONFIRMED">Confirmada</option>
          <option value="CANCELLED">Cancelada</option>
          <option value="COMPLETED">Completada</option>
        </select>
      </div>

      <template v-if="reservas.length">
        <div v-for="reserva in reservas" :key="reserva.id" class="reserva-card">
          <h2>Reserva</h2>
          <p><strong>Fecha:</strong> {{ formatDate(reserva.startTime) }}</p>
          <p><strong>Hora:</strong> {{ formatTime(reserva.startTime) }}</p>
          <p><strong>Servicios:</strong> {{ reserva.services.join(', ') }}</p>
          <p><strong>Estado de la reserva:</strong> {{ traducirEstadoReserva(reserva.status) }}</p>
          <p><strong>Estado de pago:</strong> {{ traducirEstadoFactura(reserva.order?.status) }}</p>
          <p><strong>Total:</strong> {{ reserva.order?.totalAmount }} €</p>

          <router-link
            v-if="reserva.order?.status === 'PAID'"
            :to="{ name: 'Factura', query: { reserva: encodeURIComponent(JSON.stringify(reserva)) } }"
            class="btn-factura"
          >
            Ver Factura
          </router-link>
        </div>
      </template>
      <template v-else>
        <p>No tienes reservas que coincidan con los filtros.</p>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import axios from '@/api/axiosInstance'

const reservas = ref([])
const fecha = ref('')
const hora = ref('')
const estado = ref('')

const tramosHorarios = [
  "10:00", "10:30", "11:00", "11:30", "12:00", "12:30",
  "13:00", "13:30", "17:00", "17:30", "18:00", "18:30",
  "19:00", "19:30", "20:00", "20:30"
]

const fetchReservas = async () => {
  try {
    const params = { page: 0, size: 20 }

    if (fecha.value) {
      if (hora.value) {
        params.startDateTime = `${fecha.value}T${hora.value}:00`
        params.endDateTime = `${fecha.value}T${hora.value}:59`
      } else {
        params.startDateTime = `${fecha.value}T00:00:00`
        params.endDateTime = `${fecha.value}T23:59:59`
      }
    }

    if (estado.value) params.status = estado.value

    const { data } = await axios.get('/appointments/my-appointments/search', { params })

    reservas.value = data.content || []
  } catch (error) {
    console.error('Error fetching reservations:', error)
  }
}

const traducirEstadoReserva = (estado) => {
  switch (estado) {
    case 'CONFIRMED': return 'Confirmada'
    case 'PENDING': return 'Pendiente'
    case 'CANCELLED': return 'Cancelada'
    case 'COMPLETED': return 'Completada'
    default: return estado
  }
}

const traducirEstadoFactura = (estado) => {
  switch (estado) {
    case 'PAID': return 'Pagada'
    case 'PENDING_PAYMENT': return 'Pendiente de pago'
    default: return estado || '—'
  }
}

const formatDate = (dateString) =>
  new Date(dateString).toLocaleDateString()

const formatTime = (dateString) =>
  new Date(dateString).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })

onMounted(fetchReservas)
watch([fecha, hora, estado], fetchReservas)
</script>


<style scoped>
.reservas-contenedor {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 80px 20px;
  background: linear-gradient(to bottom, #fbeff2, #f3f4f1);
  min-height: 100vh;
  font-family: 'Orbitron', sans-serif;
}

.reservas-caja {
  background-color: white;
  padding: 40px 30px;
  border-radius: 16px;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.08);
  max-width: 1000px;
  width: 100%;
  color: #333;
  text-align: center;
}

.filtros {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  margin-bottom: 40px;
  background-color: #fff;
  padding: 20px 30px;
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(139, 58, 98, 0.15);
  max-width: 900px;
  margin-left: auto;
  margin-right: auto;
}

.filtros input,
.filtros select {
  padding: 14px 16px;
  font-size: 1.1rem;
  border-radius: 12px;
  border: 2px solid #ddd;
  min-width: 200px;
  font-family: 'Orbitron', sans-serif;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
  background-color: #fafafa;
}

.filtros input:focus,
.filtros select:focus {
  border-color: #e88e00;
  box-shadow: 0 0 8px rgba(232, 142, 0, 0.6);
  outline: none;
}

@media (max-width: 600px) {
  .filtros input,
  .filtros select {
    min-width: 100%;
  }
}

.reserva-card {
  border: 1px solid rgba(200, 200, 200, 0.2);
  padding: 30px;
  margin-bottom: 30px;
  border-radius: 16px;
  background-color: #ffffff;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
  text-align: left;
}

.reserva-card:hover {
  transform: translateY(-5px);
}

.reserva-card h2 {
  font-size: 1.8rem;
  margin-bottom: 15px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.reserva-card p {
  font-size: 1.05rem;
  margin: 6px 0;
  color: #444;
}

.btn-factura {
  margin-top: 15px;
  display: inline-block;
  background-color: #8b3a62;
  color: #fff;
  padding: 10px 22px;
  border-radius: 10px;
  text-decoration: none;
  font-weight: bold;
  font-size: 1rem;
  transition: background 0.3s ease;
}

.btn-factura:hover {
  background-color: #a44c77;
}
</style>
