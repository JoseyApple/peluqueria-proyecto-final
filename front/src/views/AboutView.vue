<template>
  <div class="about-container">
    <h1 class="pagina-titulo">Sobre Nosotros</h1>

    <section class="about-section">
      <p class="about-intro">
        En <strong>Glow Up Studio</strong> combinamos técnica, arte y pasión para ofrecer una experiencia de belleza única.
        Nuestro objetivo no es solo transformar tu imagen, sino también elevar tu confianza.
      </p>
    </section>

    <div class="about-grid">
      <div class="about-card">
        <img src="/30.jpg" alt="Historia" class="about-img" />
        <h2>Nuestra Historia</h2>
        <p>
          Fundado en 2018, Glow Up Studio nació del sueño de crear un espacio donde cada detalle contara. Desde entonces,
          hemos trabajado incansablemente por posicionarnos como un referente de estilo y autenticidad.
        </p>
      </div>

      <div class="about-card">
        <img src="/31.jpg" alt="Filosofía" class="about-img" />
        <h2>Filosofía de Estilo</h2>
        <p>
          Apostamos por la individualidad. No seguimos modas, las reinterpretamos para ti. Cada cliente es un lienzo,
          y cada resultado, una obra personal.
        </p>
      </div>

      <div class="about-card">
        <img src="/32.jpg" alt="Equipo" class="about-img" />
        <h2>Conoce al Equipo</h2>
        <p>
          Nuestro equipo está formado por estilistas certificados, expertos en colorimetría y artistas del detalle.
          Cada miembro comparte un mismo compromiso: hacerte sentir espectacular.
        </p>
      </div>

      <div class="about-card">
        <img src="/25.jpg" alt="Valores" class="about-img" />
        <h2>Valores</h2>
        <p>
          Profesionalismo, honestidad, innovación y empatía. Estos pilares guían cada corte, cada tratamiento y cada sonrisa
          que ofrecemos día a día.
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.about-container {
  padding: 60px 20px;
  background-color: #f3f4f1;
}

.pagina-titulo {
  font-size: 3rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 40px;
  font-family: 'Orbitron', sans-serif;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.about-intro {
  max-width: 800px;
  margin: 0 auto 60px auto;
  text-align: center;
  font-size: 1.2rem;
  line-height: 1.7;
  color: #444;
  font-family: 'Open Sans', sans-serif;
}

.about-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 40px;
  max-width: 1200px;
  margin: 0 auto;
}

.about-card {
  background-color: rgba(255, 255, 255, 0.3);
  padding: 30px;
  border-radius: 16px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  text-align: center;
  transition: transform 0.3s ease;
}

.about-card:hover {
  transform: translateY(-6px);
}

.about-card h2 {
  font-size: 1.6rem;
  margin: 20px 0 10px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.about-card p {
  font-size: 1rem;
  color: #333;
  line-height: 1.6;
  font-family: 'Open Sans', sans-serif;
}

.about-img {
  width: 100%;
  height: 500px;
  object-fit: cover;
  border-radius: 12px;
}

@media (max-width: 768px) {
  .pagina-titulo {
    font-size: 2rem;
    margin-bottom: 24px;
  }

  .about-intro {
    font-size: 1rem;
    margin-bottom: 40px;
  }

  .about-grid {
    grid-template-columns: 1fr;
    gap: 24px;
  }

  .about-img {
    height: 200px;
  }

  .about-card h2 {
    font-size: 1.4rem;
  }

  .about-card p {
    font-size: 0.95rem;
  }

  .about-card {
    padding: 20px;
  }
}
</style>
