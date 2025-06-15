<template>
  <div class="home">
    <Navbar />
    <section class="hero">
      <div class="hero-content">
        <h1 class="hero-title">GLOW UP STUDIO</h1>
      </div>
    </section>
    <Fotos />
  </div>
</template>

<script setup>
import Navbar from "@/components/Navbar.vue"; 
import Fotos from "../components/Fotos.vue";
</script>

<style scoped>
.home {
  text-align: center;
  font-family: 'Orbitron', sans-serif;
}

.hero {
  background: url("https://source.unsplash.com/1600x900/?barbershop,makeup") center/cover;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.hero::after {
  content: "";
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(139, 58, 98, 0.4), rgba(232, 142, 0, 0.3), transparent);
  pointer-events: none;
}

.hero-content {
  position: relative;
  z-index: 1;
}

.hero-title {
  font-size: 4rem;
  margin-top: 80px;
  background: linear-gradient(135deg, #8b3a62, #c26947, #e88e00);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
  text-shadow: 1px 1px 2px rgba(150, 120, 80, 0.25);
  animation: fadeInUp 1s ease-out both;
}

.fotos {
  margin-top: 0;
  padding: 0;
  background: none;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(40px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
