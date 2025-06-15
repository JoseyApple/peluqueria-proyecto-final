import { useModal } from '@/plugins/useModal.js'

const { showModal } = useModal()

export function alerta(message, title = 'Glow Up Studio') {
  return showModal({ type: 'alert', title, message })
}

export function confirmar(message, title = 'Glow Up Studio') {
  return showModal({ type: 'confirm', title, message })
}

export function preguntar(message, title = 'Glow Up Studio') {
  return showModal({ type: 'prompt', title, message })
}
