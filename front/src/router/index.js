import { createRouter, createWebHistory } from 'vue-router';
import PaginaInicio from '../views/PaginaInicio.vue';
import ServicesView from '../views/ServiceView.vue';
import AboutView from '../views/AboutView.vue';
import ContactView from '../views/ContactView.vue';
import LoginView from '../views/LoginView.vue';
import RegisterView from '../views/RegisterView.vue';
import ServiceDetail from '@/views/ServiceDetail.vue';
import Reservar from '@/views/Reservar.vue';
import Reservas from '@/views/Reservas.vue';
import AdminPanel from '@/views/AdminPanel.vue';
import AdminReservas from '@/views/AdminReservas.vue';

const AdminFacturas = () => import('@/views/AdminFacturas.vue');
const FacturaView = () => import('@/views/FacturaView.vue');

import { useAuthStore } from '@/stores/auth';

const routes = [
  { path: '/', component: PaginaInicio },
  { path: '/services', component: ServicesView },
  { path: '/about', component: AboutView },
  { path: '/contact', component: ContactView },
  { path: '/login', component: LoginView },
  { path: '/register', component: RegisterView },
  { path: '/services/:slug', name: 'ServiceDetail', component: ServiceDetail },
  { path: '/reservar/:slug', name: 'Reservar', component: Reservar },
  { path: '/reservas', name: 'Reservas', component: Reservas, meta: { requiresAuth: true } },
  { path: '/admin', name: 'AdminPanel', component: AdminPanel, meta: { requiresAuth: true, adminOnly: true } },
  { path: '/admin/reservas', name: 'AdminReservas', component: AdminReservas, meta: { requiresAuth: true, adminOnly: true } },
  { path: '/admin/facturas', name: 'AdminFacturas', component: AdminFacturas, meta: { requiresAuth: true, adminOnly: true } },
  { path: '/factura', name: 'Factura', component: FacturaView, meta: { requiresAuth: true } }
];

const BASE_URL = '/';

const router = createRouter({
  history: createWebHistory(BASE_URL),
  routes,

  scrollBehavior(to, from, savedPosition) {
    return { left: 0, top: 0 };
  }
});

router.beforeEach((to, from, next) => {
  const auth = useAuthStore();
  const token = localStorage.getItem("jwt_token");

  if (to.meta.requiresAuth && !token) {
    return next('/login');
  }

  if (to.meta.adminOnly && auth.user?.role !== 'ADMIN') {
    return next('/'); 
  }

  next();
});

export default router;
