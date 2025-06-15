<template>
  <div class="register-container">
    <h1 class="register-title">Registro</h1>
    <form @submit.prevent="handleRegister" class="register-form">
      <label for="nombre">Nombre:</label>
      <input type="text" id="nombre" v-model="nombre" required />

      <label for="email">Email:</label>
      <input type="email" id="email" v-model="email" required />

      <label for="password">Contraseña:</label>
      <input type="password" id="password" v-model="password" required />

      <p v-if="errorMsg" class="error-message">{{ errorMsg }}</p>

      <button :disabled="loading" type="submit" class="btn-register">
        {{ loading ? "Registrando..." : "Registrarse" }}
      </button>
    </form>
    <p class="login-link">
      ¿Ya tienes cuenta?
      <router-link to="/login">Inicia sesión aquí</router-link>
    </p>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();

const nombre = ref("");
const email = ref("");
const password = ref("");
const errorMsg = ref("");
const loading = ref(false);

const validarEmail = (email) => {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(email);
};

const handleRegister = async () => {
  errorMsg.value = "";
  if (!nombre.value || !email.value || !password.value) {
    errorMsg.value = "Completa todos los campos.";
    return;
  }
  if (!validarEmail(email.value)) {
    errorMsg.value = "El email no es válido.";
    return;
  }

  loading.value = true;
  try {
    await axios.post("http://localhost:8081/users", {
      userName: nombre.value,
      email: email.value,
      password: password.value,
    });
    alert("Registro exitoso, ahora inicia sesión.");
    router.push("/login");
  } catch (error) {
    if (error.response?.status === 400) {
      errorMsg.value = "Datos inválidos. Revisa el email y contraseña.";
    } else if (error.response?.status === 401) {
      errorMsg.value = "No autorizado. Verifica el backend.";
    } else {
      errorMsg.value = "Error al registrar el usuario.";
    }
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.register-container {
  max-width: 460px;
  margin: 100px auto;
  padding: 40px 30px;
  background-color: #ffffff;
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
  font-family: 'Orbitron', sans-serif;
  text-align: center;
}

.register-title {
  font-size: 2.2rem;
  margin-bottom: 30px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
}

.register-form {
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

.btn-register {
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

.btn-register:hover {
  background-color: #a44c77;
}

.login-link {
  margin-top: 20px;
  font-size: 0.95rem;
  color: #555;
}

.login-link a {
  font-weight: bold;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
  text-decoration: none;
  transition: opacity 0.3s ease;
}

.login-link a:hover {
  opacity: 0.7;
}

@media (max-width: 480px) {
  .register-container {
    margin: 50px 15px;
    padding: 30px 20px;
  }

  .register-title {
    font-size: 1.8rem;
    margin-bottom: 20px;
  }

  input {
    font-size: 0.95rem;
    padding: 10px;
  }

  .btn-register {
    font-size: 0.95rem;
    padding: 10px;
  }

  .login-link {
    font-size: 0.9rem;
  }
}
</style>
