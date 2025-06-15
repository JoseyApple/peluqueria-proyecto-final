<template>
  <div class="reservas-admin-wrapper">
    <section class="admin-section">
      <h1> Todas las Reservas</h1>

      <div class="cards-container">
        <div
          class="reserva-card"
          :class="{ confirmadaAnimada: reserva.recentlyConfirmed }"
          v-for="reserva in reservas"
          :key="reserva.id"
        >
          <h3>Reserva del {{ formatDate(new Date(reserva.startTime)) }}</h3>
          <p><strong>Fecha:</strong> {{ formatDate(reserva.startTime) }}</p>
          <p><strong>Inicio:</strong> {{ formatTime(reserva.startTime) }}</p>
          <p><strong>Fin:</strong> {{ formatTime(reserva.endTime) }}</p>
          <p><strong>Servicios:</strong> {{ reserva.services.join(', ') }}</p>

          <div class="estado">
            <span
              v-if="reserva.status === 'CANCELLED'"
              class="cancelada-texto"
            >
              Cancelada ❌
            </span>
            <span
              v-else-if="['CONFIRMED', 'COMPLETED'].includes(reserva.status)"
              class="estado-texto"
            >
              Confirmada ✅
            </span>
          </div>

          <div v-if="reserva.status !== 'CANCELLED'" class="acciones">
            <template v-if="['CONFIRMED', 'COMPLETED'].includes(reserva.status)">
              <span class="confirmada-indicador">✅ Confirmada</span>
            </template>

            <template v-else>
              <button
                class="btn-confirmar"
                @click="confirmarReserva(reserva)"
                title="Confirmar"
              >
                Confirmar ✅
              </button>

              <button
                class="btn-cancelar"
                @click="cancelarReserva(reserva)"
                title="Cancelar"
              >
                Cancelar ❌
              </button>
            </template>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, onActivated } from 'vue'
import axios from 'axios'
import { useModal } from '@/plugins/useModal.js'

const { showModal } = useModal()

const reservas = ref([])

const fetchReservas = async () => {
  try {
    const response = await axios.get('/appointments/search', {
      params: { page: 0, size: 100 }
    })

    reservas.value = response.data.content.map(r => ({
      ...r,
      recentlyConfirmed: false
    }))
  } catch {
    await showModal({
      type: 'alert',
      title: 'Error',
      message: 'No se pudieron cargar las reservas. Por favor, inténtalo más tarde.'
    })
  }
}

import axios from '@/api/axiosInstance'

const confirmarReserva = async (reserva) => {
  try {
    await axios.patch(`/appointments/${reserva.id}?newStatus=CONFIRMED`, {})

    reserva.status = 'CONFIRMED'
    reserva.recentlyConfirmed = true

    setTimeout(() => {
      reserva.recentlyConfirmed = false
    }, 2000)

    await showModal({
      type: 'alert',
      title: 'Reserva Confirmada',
      message: `La reserva ${reserva.id} ha sido confirmada correctamente.`
    })
  } catch {
    await showModal({
      type: 'alert',
      title: 'Error',
      message: 'No se pudo confirmar la reserva.'
    })
  }
}

const cancelarReserva = async (reserva) => {
  const confirmacion = await showModal({
    type: 'confirm',
    title: 'Confirmar cancelación',
    message: `¿Estás seguro de que quieres cancelar la reserva ${reserva.id}?`
  })

  if (!confirmacion) return

  try {
    await axios.patch(`/appointments/${reserva.id}?newStatus=CANCELLED`, {})

    reserva.status = 'CANCELLED'

    await showModal({
      type: 'alert',
      title: 'Reserva Cancelada',
      message: `La reserva ${reserva.id} ha sido cancelada.`
    })
  } catch {
    await showModal({
      type: 'alert',
      title: 'Error',
      message: 'Error al cancelar la reserva.'
    })
  }
}

const formatDate = (dateString) => new Date(dateString).toLocaleDateString()
const formatTime = (dateString) => new Date(dateString).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })

onMounted(fetchReservas)
onActivated(fetchReservas)
</script>


<style scoped>
.reservas-admin-wrapper {
  background: linear-gradient(135deg, #f3f4f1, #fbeff2);
  padding: 60px 20px;
  font-family: 'Orbitron', sans-serif;
  min-height: 100vh;
}

.admin-section {
  max-width: 1200px;
  margin: 0 auto;
  background: white;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

h1 {
  font-size: 2.6rem;
  margin-bottom: 30px;
  text-align: center;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.cards-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
  padding-top: 20px;
}

.reserva-card {
  background-color: white;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.06);
  transition: transform 0.2s ease;
  font-family: 'Orbitron', sans-serif;
}

.reserva-card:hover {
  transform: translateY(-6px);
}

.reserva-card h3 {
  font-size: 1.4rem;
  margin-bottom: 10px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.reserva-card p {
  font-size: 1rem;
  color: #333;
  margin: 6px 0;
}

.acciones {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 14px;
}

.btn-confirmar,
.btn-cancelar {
  font-size: 0.95rem;
  padding: 10px 16px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: transform 0.2s ease, background-color 0.3s;
}

.btn-confirmar {
  background-color: #28a745;
  color: white;
}

.btn-cancelar {
  background-color: #dc3545;
  color: white;
}

.btn-confirmar:hover:not(:disabled),
.btn-cancelar:hover:not(:disabled) {
  transform: scale(1.05);
}

.btn-confirmar:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.estado-texto {
  color: #2c7a00;
  font-weight: bold;
  text-align: center;
  margin-top: 10px;
}

.cancelada-texto {
  color: #b30000;
  font-weight: bold;
  text-align: center;
  margin-top: 10px;
}

.confirmada-indicador {
  display: inline-block;
  padding: 10px 16px;
  background-color: #28a745;
  color: white;
  border-radius: 8px;
  font-weight: bold;
  font-size: 0.95rem;
  text-align: center;
}

.confirmadaAnimada {
  animation: glowConfirmado 2s ease-out;
}

@keyframes glowConfirmado {
  0% {
    background-color: #e6ffed;
    box-shadow: 0 0 0px rgba(40, 167, 69, 0);
  }
  50% {
    background-color: #d4f8e2;
    box-shadow: 0 0 20px rgba(40, 167, 69, 0.4);
  }
  100% {
    background-color: white;
    box-shadow: none;
  }
}


@media (max-width: 768px) {
  .reservas-admin-wrapper {
    padding: 40px 10px;
  }

  .admin-section {
    padding: 20px;
  }

  h1 {
    font-size: 2rem;
    margin-bottom: 24px;
  }

  .cards-container {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .reserva-card {
    padding: 20px;
  }

  .reserva-card h3 {
    font-size: 1.2rem;
  }

  .reserva-card p {
    font-size: 0.95rem;
  }

  .btn-confirmar,
  .btn-cancelar {
    padding: 8px 14px;
    font-size: 0.9rem;
  }
}
</style>
