<template>
  <div class="facturas-contenedor">
    <h1> Todas las Facturas</h1>

    <div class="facturas-grid">
      <div class="factura-card" v-for="factura in facturas" :key="factura.id">
        <h2>Factura del {{ formatDate(factura.date || new Date()) }}</h2>
        <p><strong>Total:</strong> {{ factura.totalAmount.toFixed(2) }} €</p>
        <p><strong>Estado:</strong> {{ traducirEstadoFactura(factura.status) }}</p>
        <p><strong>Cita:</strong> {{ traducirEstadoReserva(factura.appointmentStatus) || '—' }}</p>

        <button
          v-if="factura.status !== 'PAID'"
          @click="confirmarPago(factura.id)"
          class="btn-pagar"
        >
          Marcar como pagada
        </button>
        <span v-else class="pagada">✅ Pagada</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '@/stores/auth'
import { useModal } from '@/plugins/useModal.js'

const auth = useAuthStore()
const { showModal } = useModal()

const formatDate = (date) =>
  new Date(date).toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })

const facturas = ref([])

const fetchFacturas = async () => {
  const token = localStorage.getItem('jwt_token')
  try {
    const response = await axios.get('/orders', {
      headers: { Authorization: `Bearer ${token}` }
    })
    facturas.value = response.data.content.filter(
      f => f.appointmentStatus !== 'CANCELLED'
    )
  } catch (error) {
    await showModal({
      type: 'alert',
      title: 'Error',
      message: 'No se pudieron cargar las facturas. Por favor, inténtalo más tarde.'
    })
  }
}

const confirmarPago = async (id) => {
  try {
    await axios.patch(`/orders/${id}?newStatus=PAID`, {})

    const factura = facturas.value.find(f => f.id === id)
    if (factura) factura.status = 'PAID'

    await showModal({
      type: 'alert',
      title: 'Éxito',
      message: 'La factura ha sido marcada como pagada.'
    })
  } catch (error) {
    await showModal({
      type: 'alert',
      title: 'Error',
      message: 'No se pudo marcar como pagada.'
    })
  }
}

const traducirEstadoFactura = (estado) => {
  return estado === 'PAID' ? 'Pagada' : 'Pendiente'
}

const traducirEstadoReserva = (estado) => {
  switch (estado) {
    case 'CONFIRMED':
      return 'Confirmada'
    case 'PENDING':
      return 'Pendiente'
    case 'CANCELLED':
      return 'Cancelada'
    case 'COMPLETED':
      return 'Completada'
    default:
      return estado
  }
}

onMounted(fetchFacturas)
</script>


<style scoped>
.facturas-contenedor {
  padding: 60px 20px;
  background: linear-gradient(to bottom, #fbeff2, #f3f4f1);
  min-height: 100vh;
  font-family: 'Orbitron', sans-serif;
  color: #333;
  text-align: center;
}

h1 {
  font-size: 2.5rem;
  margin-bottom: 40px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.facturas-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
  justify-items: center;
}

.factura-card {
  background-color: white;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  width: 100%;
  max-width: 300px;
  text-align: left;
  transition: transform 0.3s ease;
}

.factura-card:hover {
  transform: translateY(-5px);
}

.factura-card h2 {
  font-size: 1.4rem;
  margin-bottom: 12px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.factura-card p {
  font-size: 1rem;
  margin: 6px 0;
  color: #444;
}

.btn-pagar {
  background-color: #e88e00;
  color: #fff;
  padding: 10px 16px;
  border: none;
  border-radius: 10px;
  font-weight: bold;
  cursor: pointer;
  font-family: 'Orbitron', sans-serif;
  margin-top: 10px;
  transition: background 0.3s;
}

.btn-pagar:hover {
  background-color: #ffaa33;
}

.pagada {
  display: inline-block;
  margin-top: 12px;
  font-weight: bold;
  color: #39b68d;
}

@media (max-width: 768px) {
  .facturas-contenedor {
    padding: 40px 10px;
  }

  h1 {
    font-size: 2rem;
    margin-bottom: 24px;
  }

  .factura-card {
    max-width: 100%;
    padding: 20px;
  }

  .factura-card h2 {
    font-size: 1.2rem;
    margin-bottom: 10px;
  }

  .factura-card p {
    font-size: 0.95rem;
  }

  .btn-pagar {
    padding: 8px 14px;
    font-size: 0.9rem;
  }
}
</style>
