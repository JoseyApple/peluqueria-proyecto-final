<template>
  <div class="admin-panel">
    <h1> Panel de Administración</h1>
    <div class="cards-container">
      <router-link to="/admin/reservas" class="admin-card">
        <h2> Reservas</h2>
        <p>Gestiona todas las reservas de los clientes.</p>
      </router-link>
      <router-link to="/admin/facturas" class="admin-card">
        <h2> Facturas</h2>
        <p>Consulta y administra las facturas generadas.</p>
      </router-link>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.admin-panel {
  padding: 60px 20px;
  min-height: 100vh;
  background: linear-gradient(to bottom, #fbeff2, #f3f4f1);
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: 'Orbitron', sans-serif;
}

h1 {
  font-size: 2.6rem;
  margin-bottom: 40px;
  background: linear-gradient(135deg, #8b3a62, #e88e00);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
  text-align: center;
}

.cards-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
  width: 100%;
  max-width: 1000px;
  padding: 0 20px;
}

.admin-card {
  background-color: white;
  padding: 30px;
  border-radius: 20px;
  text-align: center;
  text-decoration: none;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.admin-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 32px rgba(139, 58, 98, 0.2);
}

.admin-card h2 {
  font-size: 1.8rem;
  margin-bottom: 10px;
  background: linear-gradient(135deg, #8b3a62, #e88e00);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.admin-card p {
  font-size: 1rem;
  color: #444;
  line-height: 1.5;
}

a {
  text-decoration: none;
  color: inherit;
}

/* Responsive */
@media (max-width: 768px) {
  h1 {
    font-size: 2rem;
    margin-bottom: 24px;
  }

  .cards-container {
    grid-template-columns: 1fr;
    gap: 20px;
    padding: 0 10px;
  }

  .admin-card {
    padding: 20px;
  }

  .admin-card h2 {
    font-size: 1.5rem;
  }

  .admin-card p {
    font-size: 0.95rem;
  }
}
</style>
