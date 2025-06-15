<template>
  <div id="app">
    <Navbar />
    <router-view />
    <ModalDialog
      :visible="modalState.visible"
      :type="modalState.type"
      :title="modalState.title"
      :message="modalState.message"
      @update:visible="modalState.visible = $event"
      @result="handleModalResult"
    />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import Navbar from './components/Navbar.vue'
import ModalDialog from '@/components/ModalDialog.vue'
import { useModal } from '@/plugins/useModal.js'

const auth = useAuthStore()
const { state: modalState, onResult } = useModal()

onMounted(async () => {
  if (!auth.user) {
    await auth.fetchCurrentUser()
  }
})

function handleModalResult(result) {
  onResult(result)
}
</script>

<style>

</style>
