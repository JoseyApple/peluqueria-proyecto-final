import { defineStore } from 'pinia'

export const useReservasStore = defineStore('reservas', {
  state: () => ({
    reservas: JSON.parse(localStorage.getItem('reservas')) || []
  }),
  actions: {
    agregarReserva(reserva) {
      this.reservas.push(reserva)

      localStorage.setItem('reservas', JSON.stringify(this.reservas))
    },
    eliminarReserva(id) {

      this.reservas = this.reservas.filter(reserva => reserva.id !== id)

      localStorage.setItem('reservas', JSON.stringify(this.reservas))
    }
  }
})
