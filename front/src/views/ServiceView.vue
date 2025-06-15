<template>
  <div class="services-page">
    <h1 class="title">Glow Up Studio</h1>
    <p class="intro">Descubre todos los servicios que ofrecemos para cuidar tu estilo y bienestar.</p>

    <div class="card-grid">
      <div
        class="service-card"
        v-for="servicio in servicios"
        :key="servicio.slug"
        :style="{ backgroundImage: `url(${servicio.imagen})` }"
      >
        <div class="icon">{{ servicio.icon }}</div>
        <h2>{{ servicio.nombre }}</h2>
        <p>{{ servicio.descripcion }}</p>
        <router-link :to="`/services/${servicio.slug}`" class="ver-mas">Ver más</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
const servicios = [
  {
    nombre: "Corte de Cabello",
    slug: "corte",
    descripcion: "Corte moderno o clásico, adaptado a tu estilo.",
    imagen: "/20.jpg",
  },
  {
    nombre: "Coloración",
    slug: "coloracion",
    descripcion: "Tintes, mechas, balayage y más técnicas de color.",
    imagen: "/21.jpg",
  },
  {
    nombre: "Peinados",
    slug: "peinados",
    descripcion: "Peinados para eventos, bodas y ocasiones especiales.",
    imagen: "/22.jpg",
  },
  {
    nombre: "Tratamientos Capilares",
    slug: "tratamientos",
    descripcion: "Hidratación, keratina, botox capilar y más.",
    imagen: "/24.jpg",
  },
  {
    nombre: "Barbería",
    slug: "barberia",
    descripcion: "Arreglo de barba, afeitado clásico y perfilado.",
    imagen: "/25.jpg",
  },
  {
    nombre: "Cejas y Rostro",
    slug: "cejas-rostro",
    descripcion: "Depilación facial, diseño de cejas y maquillaje.",
    imagen: "/23.jpg",
  },
];
</script>

<style scoped>
.services-page {
  max-width: 1200px;
  margin: 100px auto 40px;
  padding: 20px;
  text-align: center;
  font-family: 'Orbitron', sans-serif;
  background: linear-gradient(to bottom, #fbeff2 0%, #f3f4f1 100%);
}

.title {
  font-size: 2.8rem;
  margin-top: 50px;
  margin-bottom: 20px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.intro {
  font-size: 1.4rem;
  max-width: 800px;
  margin: 0 auto 50px;
  color: #555;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
}

.service-card {
  position: relative;
  border-radius: 16px;
  padding: 30px 20px;
  color: white;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  height: 300px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  text-align: left;
  overflow: hidden;
}

.service-card::before {
  content: "";
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.6), transparent);
  z-index: 0;
}

.service-card * {
  position: relative;
  z-index: 1;
}

.service-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 10px 24px rgba(0, 0, 0, 0.15);
}

.service-card h2 {
  font-size: 1.5rem;
  margin-bottom: 8px;
  color: #fff;
}

.service-card p {
  font-size: 1rem;
  color: #eee;
  margin-bottom: 12px;
}

.ver-mas {
  display: inline-block;
  padding: 10px 18px;
  font-size: 1rem;
  font-weight: bold;
  background-color: #8b3a62;
  color: white;
  border-radius: 10px;
  text-decoration: none;
  transition: background 0.3s ease;
}

.ver-mas:hover {
  background-color: #a44c77;
}

.icon {
  font-size: 2.2rem;
  margin-bottom: 12px;
  color: #ffc107;
}
</style>
