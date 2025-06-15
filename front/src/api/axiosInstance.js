import axios from 'axios'
import router from '@/router'
import { useAuthStore } from '@/stores/auth'

const axiosInstance = axios.create({
    baseURL: 'https://peluqueria-proyecto-final.onrender.com'
})

axiosInstance.interceptors.request.use(config => {
    const token = localStorage.getItem('jwt_token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
}, error => Promise.reject(error))

axiosInstance.interceptors.response.use(
    response => response,
    error => {
        if (error.response?.status === 401) {
            const auth = useAuthStore()
            auth.logout()
            if (router.currentRoute.value.path !== '/login') {
                router.push('/login')
            }
        }
        return Promise.reject(error)
    }
)

export default axiosInstance



