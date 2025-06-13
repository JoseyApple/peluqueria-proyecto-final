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
-- Servicios de Peluquería
INSERT INTO hairdressing_service (name, description, duration_minutes, price) VALUES
('Corte de Cabello', 'Corte técnico personalizado adaptado a las facciones y preferencias del cliente.', 30, 15.00),
('Coloración', 'Servicio completo de coloración con tintes profesionales o técnicas de mechas avanzadas.', 120, 60.00),
('Peinados', 'Diseño de peinados para eventos especiales, sesiones fotográficas o uso cotidiano.', 90, 50.00),
('Tratamientos Capilares', 'Aplicación de tratamientos intensivos para fortalecer, nutrir y revitalizar el cabello.', 60, 35.00),
('Barbería', 'Corte masculino de precisión, perfilado de barba y afeitado clásico.', 60, 18.00),
('Tratamiento Facial', 'Higiene facial profunda con limpieza, exfoliación y técnicas revitalizantes.', 90, 30.00);


-- (Opcional) Otro usuario para pruebas múltiples
INSERT INTO users (user_name, email, password, created_at, active, role)
VALUES
    ('ClienteSecundario', 'seconduser@example.com',
     '$2a$12$hPKcD8lGqSml8qr7ou1fxei77XAajhpcxc1QX.muO54MsHm/GkAKm',
     CURRENT_DATE, TRUE, 'USER');





