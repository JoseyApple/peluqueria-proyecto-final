<template>
  <transition name="fade">
    <div v-if="visible" class="modal-overlay" @click.self="close">
      <div class="modal-box">
        <h3 class="modal-title">{{ title }}</h3>
        <p class="modal-message">{{ message }}</p>

        <div class="modal-buttons" v-if="type === 'confirm' || type === 'prompt'">
          <button class="btn btn-cancel" @click="cancel">Cancelar</button>
          <button class="btn btn-ok" @click="ok">Aceptar</button>
        </div>

        <div v-else-if="type === 'alert'">
          <button class="btn btn-ok" @click="ok">Aceptar</button>
        </div>

        <input
          v-if="type === 'prompt'"
          v-model="inputValue"
          type="text"
          class="modal-input"
          @keyup.enter="ok"
        />
      </div>
    </div>
  </transition>
</template>

<script setup>
import { ref, watch, defineEmits, defineProps } from 'vue'

const props = defineProps({
  type: { type: String, required: true }, // 'alert' | 'confirm' | 'prompt'
  title: { type: String, default: 'Glow Up Studio' },
  message: { type: String, required: true },
  visible: { type: Boolean, default: false }
})
const emits = defineEmits(['update:visible', 'result'])

const inputValue = ref('')

function ok() {
  emits('result', props.type === 'prompt' ? inputValue.value : true)
  emits('update:visible', false)
  inputValue.value = ''
}
function cancel() {
  emits('result', false)
  emits('update:visible', false)
  inputValue.value = ''
}

function close() {
  if (props.type === 'alert') ok()
  else cancel()
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}
.modal-box {
  background: white;
  border-radius: 16px;
  padding: 30px 25px;
  width: 320px;
  text-align: center;
  font-family: 'Orbitron', sans-serif;
  box-shadow: 0 6px 20px rgba(139, 58, 98, 0.25);
}
.modal-title {
  font-size: 1.8rem;
  margin-bottom: 15px;
  background: linear-gradient(135deg, #8b3a62 35%, #e88e00 65%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  color: transparent;
}
.modal-message {
  margin-bottom: 25px;
  font-size: 1.1rem;
  color: #333;
}
.modal-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
}
.btn {
  padding: 10px 24px;
  border-radius: 12px;
  border: none;
  font-weight: bold;
  cursor: pointer;
  font-family: 'Orbitron', sans-serif;
  font-size: 1rem;
  transition: background 0.3s ease;
}
.btn-ok {
  background-color: #8b3a62;
  color: white;
}
.btn-ok:hover {
  background-color: #a44c77;
}
.btn-cancel {
  background-color: #ddd;
  color: #555;
}
.btn-cancel:hover {
  background-color: #bbb;
}
.modal-input {
  width: 100%;
  padding: 10px 12px;
  font-size: 1rem;
  margin-bottom: 20px;
  border-radius: 10px;
  border: 1px solid #ccc;
  font-family: 'Orbitron', sans-serif;
  outline: none;
  transition: border-color 0.3s;
}
.modal-input:focus {
  border-color: #e88e00;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>
