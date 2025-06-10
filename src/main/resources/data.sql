-- Usuarios
-- Contraseña: Josemanuelgmod1997
INSERT INTO users (user_name, email, password, created_at, active, role)
VALUES
    ('Administrador', 'admin@example.com',
     '$2a$12$WC5PnzjpokLeVHhqoGusXu6/uldWHOrtMZWLqtnB6diGeYFfPqB42',
     CURRENT_DATE, TRUE, 'ADMIN');

-- Contraseña: usuariodeprueba12
INSERT INTO users (user_name, email, password, created_at, active, role)
VALUES
    ('UsuarioPrueba', 'user@example.com',
     '$2a$12$hPKcD8lGqSml8qr7ou1fxei77XAajhpcxc1QX.muO54MsHm/GkAKm',
     CURRENT_DATE, TRUE, 'USER');

-- Servicios de Peluquería
INSERT INTO hairdressing_service (name, description, duration_minutes, price) VALUES
('Corte de Cabello', 'Corte personalizado según el estilo del cliente', 25, 15.00),
('Coloración', 'Aplicación de tintes o mechas para cambiar el color del cabello', 120, 45.00),
('Peinados', 'Peinados para ocasiones especiales o uso diario', 30, 20.00),
('Tratamientos Capilares', 'Tratamientos nutritivos y reparadores para el cabello', 60, 35.00),
('Barbería', 'Servicios de afeitado, arreglo de barba y corte masculino', 30, 18.00),
('Tratamiento Facial', 'Limpieza profunda y revitalización del rostro', 30, 30.00);

-- (Opcional) Otro usuario para pruebas múltiples
INSERT INTO users (user_name, email, password, created_at, active, role)
VALUES
    ('ClienteSecundario', 'seconduser@example.com',
     '$2a$12$hPKcD8lGqSml8qr7ou1fxei77XAajhpcxc1QX.muO54MsHm/GkAKm',
     CURRENT_DATE, TRUE, 'USER');





