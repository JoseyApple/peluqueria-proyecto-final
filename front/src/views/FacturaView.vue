<template>
  <div class="factura-container">
    <div v-if="factura" class="factura-box">
      <h2 class="factura-title">Factura</h2>
      <p><strong>Cliente:</strong> {{ auth.user?.userName }}</p>
      <p><strong>Fecha:</strong> {{ formatDate(new Date()) }}</p>
      <p><strong>Total:</strong> {{ factura.order.totalAmount }} €</p>
      <p><strong>Estado:</strong> {{ traducirEstado(factura.order.status) }}</p>

      <div class="factura-buttons">
        <button
          v-if="factura.order.status === 'PAID'"
          @click="descargarPDF"
          class="btn-pdf"
        >
          📄 Descargar PDF
        </button>
        <router-link to="/reservas" class="btn-link">← Volver a reservas</router-link>
      </div>
    </div>
    <div v-else class="factura-box">
      <p>Cargando factura...</p>
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import jsPDF from 'jspdf'
import autoTable from 'jspdf-autotable'
import { useModal } from '@/plugins/useModal.js'

const auth = useAuthStore()
const route = useRoute()
const factura = ref(null)

const { showModal } = useModal()

const traducirEstado = (estado) => {
  switch (estado) {
    case 'PAID': return 'Pagada'
    case 'PENDING_PAYMENT': return 'Pendiente de pago'
    default: return estado
  }
}

onMounted(() => {
  const encoded = route.query.reserva
  if (encoded) {
    try {
      factura.value = JSON.parse(decodeURIComponent(encoded))
    } catch (e) {
      showModal({
        type: 'alert',
        title: 'Error',
        message: 'No se pudo cargar la factura. Datos inválidos.'
      })
    }
  }
})

const formatDate = (date) => date.toLocaleDateString('es-ES', {
  year: 'numeric',
  month: 'long',
  day: 'numeric'
})

const descargarPDF = () => {
  try {
    const doc = new jsPDF()
    const margin = 20
    const pageWidth = doc.internal.pageSize.getWidth()

    doc.setFontSize(24)
    doc.setTextColor(139, 58, 98)
    doc.setFont('helvetica', 'bold')
    doc.text('GLOW UP STUDIO', pageWidth / 2, 20, { align: 'center' })

    doc.setFontSize(12)
    doc.setTextColor(100)
    doc.text('Transformación y Belleza Personal', pageWidth / 2, 27, { align: 'center' })

    doc.setDrawColor(232, 142, 0)
    doc.setLineWidth(1)
    doc.line(margin, 32, pageWidth - margin, 32)

    const infoY = 40
    doc.setFontSize(13)
    doc.setTextColor(0, 0, 0)
    doc.text(`Cliente: ${auth.user?.userName}`, margin, infoY)
    doc.text(`Fecha: ${formatDate(new Date())}`, margin, infoY + 7)

    autoTable(doc, {
      startY: infoY + 25,
      head: [['Servicios']],
      body: (factura.value.services ?? []).map(serv => [serv]),
      headStyles: {
        fillColor: [139, 58, 98],
        textColor: 255,
        fontSize: 12
      },
      bodyStyles: {
        fontSize: 11
      },
      theme: 'grid',
      margin: { left: margin, right: margin }
    })

    autoTable(doc, {
      startY: doc.lastAutoTable.finalY + 10,
      head: [['Total (€)', 'Estado']],
      body: [[`${factura.value.order.totalAmount}`, traducirEstado(factura.value.order.status)]],
      headStyles: {
        fillColor: [232, 142, 0],
        textColor: 255,
        fontSize: 12
      },
      bodyStyles: {
        fontSize: 12,
        textColor: [50, 50, 50]
      },
      theme: 'grid',
      margin: { left: margin, right: margin }
    })

    const pageHeight = doc.internal.pageSize.height
    doc.setFontSize(10)
    doc.setTextColor(120)
    doc.text('Gracias por confiar en Glow Up Studio ✨', margin, pageHeight - 20)
    doc.text('www.glowupstudio.com | contacto@glowupstudio.com', margin, pageHeight - 14)

    doc.save(`factura_${factura.value.id}.pdf`)
  } catch (error) {
    showModal({
      type: 'alert',
      title: 'Error',
      message: 'Error al generar el PDF. Por favor, inténtalo nuevamente.'
    })
  }
}
</script>

<style scoped>
.factura-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 80px 20px;
  min-height: 100vh;
  background: linear-gradient(145deg, #fbeff2 0%, #f3f4f1 100%);
}

.factura-box {
  background-color: white;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.08);
  max-width: 600px;
  width: 100%;
  text-align: center;
  font-family: 'Orbitron', sans-serif;
  color: #333;
  transition: transform 0.3s ease;
}

.factura-box:hover {
  transform: translateY(-5px);
}

.factura-title {
  font-size: 2.5rem;
  margin-bottom: 30px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.factura-box p {
  font-size: 1.1rem;
  margin: 10px 0;
}

.factura-buttons {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 20px;
}

.btn-pdf {
  background-color: #8b3a62;
  color: white;
  font-weight: bold;
  padding: 10px 24px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-family: 'Orbitron', sans-serif;
  transition: background 0.3s ease;
}

.btn-pdf:hover {
  background-color: #a44c77;
}

.btn-link {
  font-size: 1rem;
  text-decoration: none;
  font-weight: bold;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
  font-family: 'Orbitron', sans-serif;
  transition: opacity 0.3s ease;
}

.btn-link:hover {
  opacity: 0.7;
}


@media (max-width: 768px) {
  .factura-box {
    padding: 20px;
  }

  .factura-title {
    font-size: 2rem;
    margin-bottom: 20px;
  }

  .factura-box p {
    font-size: 1rem;
  }

  .btn-pdf {
    padding: 8px 16px;
    font-size: 0.9rem;
  }
}
</style>
