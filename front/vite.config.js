import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'), // Asegúrate de que el alias apunte a la carpeta 'src'
    },
  },
  server: {
    hmr: {
      clientPort: 5173 // <-- 🔥 el puerto donde estás corriendo tu Vite frontend
    }
  }
});
