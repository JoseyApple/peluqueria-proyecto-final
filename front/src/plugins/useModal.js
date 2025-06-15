import { reactive, readonly, watchEffect } from 'vue'

const state = reactive({
  visible: false,
  type: 'alert',
  title: '',
  message: '',
  resolve: null,
  reject: null,
  inputValue: ''
})

function showModal({ type = 'alert', title = 'Glow Up Studio', message = '' }) {
  return new Promise((resolve) => {
    state.visible = true
    state.type = type
    state.title = title
    state.message = message
    state.resolve = resolve
    state.inputValue = ''
  })
}

function onResult(result) {
  state.visible = false
  if (state.type === 'prompt' && result !== false) {
    state.resolve(result)
  } else {
    state.resolve(result)
  }
}

export function useModal() {
  return {
    state: readonly(state),
    showModal,
    onResult
  }
}
