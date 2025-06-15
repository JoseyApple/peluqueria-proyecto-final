<template>
  <div class="contact-container">
    <h1 class="titulo">Contáctanos</h1>

    <div class="contact-wrapper">
      <div class="contact-info">
        <div class="contact-card">
          <p><strong>Teléfono:</strong> +34 123 456 789</p>
          <p><strong>Email:</strong> contacto@peluqueriaestilo.com</p>
          <p><strong>Dirección:</strong> Av. Blas Infante, s/n, 23740 Andújar, Jaén</p>
          <p><strong>Horario:</strong> Lunes a Sábado, 10:00 - 14:00 / 17:00 - 21:00</p>
        </div>

        <div class="contact-extra">
          <h2>¿Tienes dudas o necesitas una asesoría?</h2>
          <p>Contáctanos por teléfono o email, o visítanos directamente. Estaremos encantados de ayudarte.</p>
        </div>
      </div>

      <div class="contact-map">
        <iframe
          src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3156.5376345206985!2d-4.048146384973763!3d38.037141779710566!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd6c1d1b9983761b%3A0x18617a1c9274bb3f!2sIES%20J%C3%A1ndula!5e0!3m2!1ses!2ses!4v1718370000000"
          width="100%"
          height="350"
          style="border:0; border-radius: 16px;"
          allowfullscreen=""
          loading="lazy"
          referrerpolicy="no-referrer-when-downgrade"
        ></iframe>
      </div>
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped>
.contact-container {
  padding: 60px 20px;
  background-color: #f3f4f1;
}

.titulo {
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

.contact-wrapper {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  max-width: 1200px;
  margin: 0 auto;
  align-items: start;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.contact-card {
  background-color: rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  font-size: 1rem;
  color: #333;
  line-height: 1.6;
}

.contact-card p {
  margin-bottom: 15px;
}

.contact-extra {
  text-align: left;
  padding: 20px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 16px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
}

.contact-extra h2 {
  font-size: 1.4rem;
  margin-bottom: 10px;
  font-family: 'Orbitron', sans-serif;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.contact-extra p {
  font-size: 1rem;
  color: #444;
}

.contact-map iframe {
  width: 100%;
  height: 100%;
  border: none;
  border-radius: 16px;
}

@media (max-width: 768px) {
  .contact-wrapper {
    grid-template-columns: 1fr;
  }

  .contact-map iframe {
    height: 300px;
  }
}
</style>
