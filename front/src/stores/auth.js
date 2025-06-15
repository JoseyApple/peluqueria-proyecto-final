import { defineStore } from 'pinia'
import axios from 'axios'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: JSON.parse(localStorage.getItem('user')) || null
  }),
  actions: {
async fetchCurrentUser() {
  const token = localStorage.getItem('jwt_token')
  if (!token) {
    this.logout()
    return
  }

  try {
    const { data } = await axios.get('/users/me', {
      headers: { Authorization: `Bearer ${token}` }
    })

    this.user = {
      id: data.id,
      userName: data.userName,
      email: data.email,
      role: data.role
    }

    localStorage.setItem('user', JSON.stringify(this.user))
  } catch (error) {
    console.error('Failed to fetch user:', error)

    if (error.response?.status === 401) {
      this.logout()
    }
  }
},


    async login(credentials) {
      try {
        // Usa la instancia axios importada, sin URL completa
        const { data } = await axios.post('/auth/login', credentials)

        const token = data.token
        localStorage.setItem('jwt_token', token)

        await this.fetchCurrentUser()

      } catch (error) {
        console.error('Login failed:', error)
        throw error
      }
    },
    logout() {
      this.user = null
      localStorage.removeItem('user')
      localStorage.removeItem('jwt_token')
    }
  },
  getters: {
    isAuthenticated: (state) => !!state.user
  }
})
