<template>
  <nav class="navbar">
    <div class="navbar-content">
      <ul>
        <li v-if="auth.user?.role === 'ADMIN'" class="nav-item">
          <router-link to="/admin" class="nav-link">Gestión</router-link>
        </li>

        <li v-if="auth.user?.role === 'ADMIN'" class="nav-item">
          <button @click="goToAdminReservas" class="notification-bell" aria-label="Notificaciones">
            🔔
            <span v-if="hayPendientes" class="notificacion-badge"></span>
          </button>
        </li>

        <li class="nav-item"><router-link to="/" class="nav-link">Inicio</router-link></li>
        <li class="nav-item"><router-link to="/services" class="nav-link">Servicios</router-link></li>
        <li class="nav-item"><router-link to="/about" class="nav-link">Nosotros</router-link></li>
        <li class="nav-item"><router-link to="/contact" class="nav-link">Contacto</router-link></li>

        <li v-if="!auth.user">
          <router-link to="/login" class="btn-ingreso">Iniciar Sesión</router-link>
        </li>

        <li v-else class="user-info">
          <router-link to="/reservas" class="btn-reserva">Reservas</router-link>
          <span class="username">👤 {{ auth.user.userName }}</span>
          <button @click="handleLogout" class="btn-logout">Cerrar Sesión</button>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script setup>
import { ref, watchEffect, onMounted } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";
import axios from "@/api/axiosInstance";

import { useModal } from '@/plugins/useModal.js'

const auth = useAuthStore();
const router = useRouter();
const { showModal } = useModal();

const hayPendientes = ref(false);

const handleLogout = async () => {

  const confirmado = await showModal({
    type: 'confirm',
    title: 'Cerrar Sesión',
    message: '¿Seguro que quieres cerrar sesión?'
  });
  if (confirmado) {
    auth.logout();
    router.push("/login");
  }
};

const goToAdminReservas = () => {
  router.push('/admin/reservas');
};

const checkReservasPendientes = async () => {
  try {
    const today = new Date().toISOString().split("T")[0];
    const res = await axios.get(`/appointments/pending-appointments/${today}`);
    hayPendientes.value = res.data > 0;
  } catch (error) {
    hayPendientes.value = false;
    await showModal({
      type: 'alert',
      title: 'Error',
      message: 'No se pudo comprobar las reservas pendientes. Por favor, inténtalo más tarde.'
    });
  }
};

watchEffect(() => {
  if (auth.user?.role === 'ADMIN') {
    checkReservasPendientes();
  }
});

setInterval(() => {
  if (auth.user?.role === 'ADMIN') {
    checkReservasPendientes();
  }
}, 30000);
</script>


<style scoped>
.navbar {
  position: fixed;
  top: 0;
  width: 100%;
  height: 70px;
  padding: 0 20px;
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.navbar-content {
  display: flex;
  justify-content: center;
  width: 100%;
}

ul {
  list-style: none;
  display: flex;
  gap: 30px;
  padding: 0;
  margin: 0;
  align-items: center;
  flex-wrap: wrap;
  justify-content: center;
}

.nav-item .nav-link {
  font-size: 1.3rem;
  font-family: 'Orbitron', sans-serif;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
  text-decoration: none;
  transition: transform 0.3s ease;
}

.nav-item .nav-link:hover {
  transform: scale(1.1);
}

.btn-ingreso {
  padding: 10px 20px;
  font-size: 1rem;
  font-weight: bold;
  background: #8b3a62;
  color: white;
  border-radius: 8px;
  text-decoration: none;
  transition: background 0.3s ease;
}

.btn-ingreso:hover {
  background: #a44c77;
}

.btn-reserva {
  font-size: 1rem;
  font-family: 'Orbitron', sans-serif;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.4);
  font-weight: bold;
  text-decoration: none;
  padding: 6px 12px;
  transition: transform 0.3s ease;
}

.btn-reserva:hover {
  transform: scale(1.05);
}

.btn-logout {
  padding: 8px 16px;
  background: #e88e00;
  border: none;
  border-radius: 8px;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s ease;
}

.btn-logout:hover {
  background: #f09e20;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.username {
  font-family: 'Orbitron', sans-serif;
  font-size: 1rem;
  color: #333;
}

.notification-bell {
  position: relative;
  background: none;
  border: none;
  font-size: 1.6rem;
  cursor: pointer;
  color: #8b3a62;
  transition: transform 0.2s ease;
}

.notification-bell:hover {
  transform: scale(1.1);
}

.notificacion-badge {
  position: absolute;
  top: 2px;
  right: 2px;
  width: 10px;
  height: 10px;
  background-color: #ff3b3b;
  border-radius: 50%;
  border: 2px solid white;
  animation: pulse 1.2s infinite ease-in-out;
}

@keyframes pulse {
  0% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.3); opacity: 0.6; }
  100% { transform: scale(1); opacity: 1; }
}
@media (max-width: 768px) {
  .navbar {
    height: auto;
    padding: 10px;
  }

  .navbar-content {
    flex-direction: column;
    align-items: center;
  }

  ul {
    flex-direction: column;
    gap: 16px;
  }

  .nav-item .nav-link {
    font-size: 1.1rem;
    text-align: center;
  }

  .btn-ingreso,
  .btn-logout,
  .btn-reserva {
    font-size: 0.95rem;
    padding: 8px 16px;
  }

  .user-info {
    flex-direction: column;
    gap: 6px;
  }

  .notification-bell {
    font-size: 1.4rem;
  }

  .username {
    font-size: 0.95rem;
    text-align: center;
  }
}

</style>
