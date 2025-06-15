<template>
  <div class="login-container">
    <h1 class="login-title">Iniciar Sesión</h1>
    <form @submit.prevent="handleLogin" class="login-form">
      <label for="email">Email:</label>
      <input type="email" id="email" v-model="email" required />

      <label for="password">Contraseña:</label>
      <input type="password" id="password" v-model="password" required />

      <button type="submit" class="btn-login">Ingresar</button>
    </form>
    <p class="register-link">
      ¿No tienes cuenta?
      <router-link to="/register">Regístrate aquí</router-link>
    </p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useModal } from '@/plugins/useModal.js' 

const email = ref('')
const password = ref('')
const router = useRouter()
const auth = useAuthStore()
const { showModal } = useModal() 

const handleLogin = async () => {
  if (!email.value || !password.value) {
    showModal({
      type: 'alert',
      title: 'Campos incompletos',
      message: 'Por favor, completa todos los campos antes de continuar.'
    })
    return
  }

  try {
    await auth.login({ email: email.value, password: password.value })
    router.push('/')
  } catch {
    showModal({
      type: 'alert',
      title: 'Error de inicio de sesión',
      message: 'Credenciales incorrectas. Verifica tu email y contraseña.'
    })
  }
}
</script>


<style scoped>
.login-container {
  max-width: 460px;
  margin: 100px auto;
  padding: 40px 30px;
  background-color: #ffffff;
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
  font-family: 'Orbitron', sans-serif;
  text-align: center;
}

.login-title {
  font-size: 2.2rem;
  margin-bottom: 30px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

input {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  font-family: 'Orbitron', sans-serif;
  transition: border 0.3s;
}

input:focus {
  border-color: #e88e00;
  outline: none;
}

.btn-login {
  padding: 12px;
  background-color: #8b3a62;
  color: white;
  font-weight: bold;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.3s ease;
}

.btn-login:hover {
  background-color: #a44c77;
}

.register-link {
  margin-top: 20px;
  font-size: 0.95rem;
  color: #555;
}

.register-link a {
  font-weight: bold;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
  text-decoration: none;
  transition: opacity 0.3s ease;
}

.register-link a:hover {
  opacity: 0.7;
}


@media (max-width: 480px) {
  .login-container {
    margin: 50px 15px;
    padding: 30px 20px;
  }

  .login-title {
    font-size: 1.8rem;
    margin-bottom: 20px;
  }

  input {
    font-size: 0.95rem;
    padding: 10px;
  }

  .btn-login {
    font-size: 0.95rem;
    padding: 10px;
  }

  .register-link {
    font-size: 0.9rem;
  }
}
</style>
