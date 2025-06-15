<template>
  <div class="swiper-row">
    <section class="swiper-container">
      <Swiper
        :modules="[Autoplay]"
        :spaceBetween="20"
        :slidesPerView="1"
        :loop="true"
        :autoplay="{ delay: 5000 }"
        @slideChange="onSlideChange"
        class="mySwiper"
      >
        <SwiperSlide
          v-for="(slide, index) in slides"
          :key="index"
          class="slide-wrapper"
        >
          <img
            :src="slide.image"
            :alt="'Peinado ' + (index + 1)"
            :style="{
              background: currentIndex === 0
                ? 'linear-gradient(to right, #F3F4F1 0%, #F3F4F1 30%, #fbeff2 45%, #fbeff2 55%, #DDD8D7 60%, #DDD8D7 100%)'
                : 'linear-gradient(to right, #EDEDE9 0%, #EDEDE9 40%, #EDEBEB 60%, #EDEBEB 100%)'
            }"
          />
          <transition name="slide-left">
            <div
              v-if="index === phraseIndex"
              class="slide-caption left"
              key="caption-left"
            >
              {{ slide.phraseLeft }}
            </div>
          </transition>
          <transition name="slide-left">
            <div
              v-if="index === phraseRightIndex"
              class="slide-caption right"
              key="caption-right"
            >
              {{ slide.phraseRight }}
            </div>
          </transition>
        </SwiperSlide>
      </Swiper>
    </section>

    <section class="services-preview">
      <h2>Nuestros Servicios</h2>
      <div class="services-grid">
        <div
          v-for="servicio in serviciosPaginados"
          :key="servicio.id"
          class="service-card"
        >
          <h3>{{ servicio.name }}</h3>
          <p>{{ servicio.description }}</p>
        </div>
      </div>
      <div class="paginador">
        <button
          @click="paginaActual--"
          :disabled="paginaActual === 1"
          class="btn-paginador"
        >
          ◀️
        </button>
        <span class="pagina-indicador">Página {{ paginaActual }}</span>
        <button
          @click="paginaActual++"
          :disabled="!hayPaginaSiguiente"
          class="btn-paginador"
        >
          ▶️
        </button>
      </div>
    </section>

    <section class="about-section">
      <h2>Glow Up Studio</h2>
      <p>
        En nuestro estudio combinamos estilo, técnica y pasión por el detalle.
        Más que estética, ofrecemos una experiencia personal que refleja tu esencia
        y potencia tu confianza.
      </p>
    </section>

    <section class="cta-reserva">
      <h2>¿Listo para tu transformación?</h2>
      <router-link to="/services" class="btn-reserva">Reserva tu cita</router-link>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from '@/api/axiosInstance'
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Autoplay } from 'swiper/modules'
import 'swiper/css'

import { useModal } from '@/plugins/useModal.js'

const { showModal } = useModal()

const slides = [
  {
    image: '/1.png',
    phraseLeft: 'Inspiración natural',
    phraseRight: 'Belleza auténtica',
  },
  {
    image: '/2.png',
    phraseLeft: 'Elegancia atemporal',
    phraseRight: 'Estilo impecable',
  },
]

const currentIndex = ref(0)
const phraseIndex = ref(0)
const phraseRightIndex = ref(0)
let phraseTimeout = null
let rightPhraseTimeout = null

function onSlideChange(swiper) {
  currentIndex.value = swiper.realIndex
  phraseIndex.value = -1
  phraseRightIndex.value = -1
  clearTimeout(phraseTimeout)
  clearTimeout(rightPhraseTimeout)

  phraseTimeout = setTimeout(() => {
    phraseIndex.value = swiper.realIndex
  }, 500)

  rightPhraseTimeout = setTimeout(() => {
    phraseRightIndex.value = swiper.realIndex
  }, 1000)
}

const servicios = ref([])
const paginaActual = ref(1)
const porPagina = 9

const fetchServicios = async () => {
  try {
    const res = await axios.get('/services', {
      params: { page: 0, size: 100 }
    })
    servicios.value = res.data.content || []
  } catch (err) {
    await showModal({
      type: 'alert',
      title: 'Error',
      message: 'No se pudieron cargar los servicios. Por favor, intenta más tarde.'
    })
  }
}

const serviciosPaginados = computed(() => {
  const start = (paginaActual.value - 1) * porPagina
  return servicios.value.slice(start, start + porPagina)
})

const hayPaginaSiguiente = computed(() => {
  return paginaActual.value * porPagina < servicios.value.length
})

onMounted(fetchServicios)
</script>

<style scoped>
.swiper-row {
  padding: 0;
  margin: 0;
  background: none;
}

.swiper-container {
  padding: 0;
  margin: 0;
}

.slide-wrapper {
  position: relative;
}

.mySwiper img {
  width: 100%;
  max-height: 600px;
  height: auto;
  display: block;
  margin: 0 auto;
  object-fit: contain;
  object-position: center;
  background-color: #f3f4f1;
}

.slide-caption {
  position: absolute;
  font-size: 2.5rem;
  font-family: 'Orbitron', sans-serif;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  color: transparent;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
}

.slide-caption.left {
  top: 100px;
  left: 100px;
}

.slide-caption.right {
  bottom: 100px;
  right: 100px;
  text-align: right;
}

:deep(.slide-left-enter-active),
:deep(.slide-left-leave-active) {
  transition: all 0.6s ease;
}

:deep(.slide-left-enter-from),
:deep(.slide-left-leave-to) {
  opacity: 0;
  transform: translateX(-40px);
}

:deep(.slide-left-enter-to),
:deep(.slide-left-leave-from) {
  opacity: 1;
  transform: translateX(0);
}

.services-preview {
  padding: 60px 20px;
  text-align: center;
  background-color: #f3f4f1;
}

.services-preview h2 {
  font-size: 2.5rem;
  margin-bottom: 30px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
 background-clip: text;
-webkit-background-clip: text;
-webkit-text-fill-color: transparent;

}

.services-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  justify-items: center;
}

.service-card {
  width: 100%;
  max-width: 280px;
  background-color: rgba(255, 255, 255, 0.05);
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  text-align: left;
  transition: transform 0.3s ease;
}

.service-card:hover {
  transform: translateY(-5px);
}

.service-card h3 {
  font-size: 1.4rem;
  margin-bottom: 10px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
 background-clip: text;
-webkit-background-clip: text;
-webkit-text-fill-color: transparent;

}

.service-card p {
  font-size: 1rem;
  line-height: 1.6;
  color: #444;
}

.paginador {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-top: 20px;
}

.btn-paginador {
  background-color: #8b3a62;
  color: white;
  border: none;
  padding: 10px 18px;
  font-size: 1.2rem;
  font-weight: bold;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.btn-paginador:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.pagina-indicador {
  font-weight: bold;
  font-size: 1rem;
}

.about-section {
  padding: 60px 30px;
  text-align: center;
}

.about-section h2 {
  font-size: 2.2rem;
  margin-bottom: 20px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
-webkit-background-clip: text;
-webkit-text-fill-color: transparent;

}

.about-section p {
  font-size: 1.1rem;
  max-width: 800px;
  margin: 0 auto;
  line-height: 1.6;
  color: #333;
}

.cta-reserva {
  text-align: center;
  padding: 50px 20px;
  background: rgba(255, 255, 255, 0.3);
  border: 2px solid #e88e00;
  border-radius: 12px;
  margin: 60px auto;
  width: 80%;
}

.cta-reserva h2 {
  font-size: 2rem;
  margin-bottom: 20px;
  font-weight: bold;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
-webkit-background-clip: text;
-webkit-text-fill-color: transparent;

}

.btn-reserva {
  display: inline-block;
  padding: 12px 28px;
  font-size: 1.1rem;
  font-weight: bold;
  background: #8b3a62;
  color: white;
  border-radius: 8px;
  text-decoration: none;
  transition: background 0.3s;
}

.btn-reserva:hover {
  background: #a44c77;
}

@media (max-width: 768px) {
  .mySwiper img {
    height: 300px;
  }

  .slide-caption {
    font-size: 1.2rem;
  }

  .slide-caption.left {
    top: 40px;
    left: 20px;
  }

  .slide-caption.right {
    bottom: 40px;
    right: 20px;
  }

  .services-grid {
    grid-template-columns: 1fr;
  }
}
</style>
