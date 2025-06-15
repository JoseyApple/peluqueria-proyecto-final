-- Usuarios
-- Contraseña: Josemanuelgmod1997
INSERT INTO users (user_name, email, password, created_at, active, role)
VALUES
    ('Administrador', 'admin@example.com',
     '$2a$12$WC5PnzjpokLeVHhqoGusXu6/uldWHOrtMZWLqtnB6diGeYFfPqB42',
     CURRENT_DATE, TRUE, 'ADMIN')
    ON CONFLICT (email) DO NOTHING;

-- Contraseña: usuariodeprueba12
INSERT INTO users (user_name, email, password, created_at, active, role)
VALUES
    ('UsuarioPrueba', 'user@example.com',
     '$2a$12$hPKcD8lGqSml8qr7ou1fxei77XAajhpcxc1QX.muO54MsHm/GkAKm',
     CURRENT_DATE, TRUE, 'USER')
    ON CONFLICT (email) DO NOTHING;

-- Servicios de Peluquería
INSERT INTO hairdressing_service (id, name, description, duration_minutes, price) VALUES
                                                                                      (1, 'Corte Masculino Estilizado', 'Corte moderno con técnicas de degradado y estilo personalizado.', 30, 20.0),
                                                                                      (2, 'Corte Femenino a Medida', 'Estilizado profesional adaptado a tus rasgos y estilo personal.', 60, 25.0),
                                                                                      (3, 'Corte + Lavado y Peinado', 'Un servicio completo que incluye lavado, corte y peinado.', 90, 30.0),
                                                                                      (4, 'Coloración Global Profesional', 'Aplicación de color completo con acabado uniforme y duradero.', 120, 50.0),
                                                                                      (5, 'Técnica de Mechas Personalizadas', 'Iluminación natural o fantasía para aportar brillo y profundidad.', 120, 60.0),
                                                                                      (6, 'Balayage Profesional', 'Coloración degradada y natural para un look sofisticado.', 150, 70.0),
                                                                                      (7, 'Peinado de Evento', 'Peinado profesional para bodas, fiestas u ocasiones especiales.', 60, 30.0),
                                                                                      (8, 'Ondas Glamour', 'Ondas suaves y definidas para un look elegante.', 60, 35.0),
                                                                                      (9, 'Recogido Profesional', 'Recogidos clásicos o modernos con fijación duradera.', 60, 40.0),
                                                                                      (10, 'Tratamiento de Hidratación Profunda', 'Recupera el brillo y suavidad con hidratación intensiva.', 60, 40.0),
                                                                                      (11, 'Alisado con Keratina', 'Elimina el encrespamiento y alisa tu cabello hasta 3 meses.', 120, 90.0),
                                                                                      (12, 'Botox Capilar Restaurador', 'Regenera la fibra capilar desde la raíz con efecto anti-edad.', 90, 75.0),
                                                                                      (13, 'Afeitado Clásico con Toalla Caliente', 'Ritual clásico con espuma caliente y cuidado facial.', 30, 15.0),
                                                                                      (14, 'Diseño de Barba Profesional', 'Perfilado y definición de barba para un estilo impecable.', 30, 18.0),
                                                                                      (15, 'Pack Corte + Barba', 'Combinación perfecta para un cambio completo de look.', 60, 30.0),
                                                                                      (16, 'Diseño de Cejas con Hilo', 'Precisión y definición con técnica profesional de hilo.', 30, 12.0),
                                                                                      (17, 'Limpieza Facial Exprés', 'Elimina impurezas y revitaliza tu piel en minutos.', 30, 25.0),
                                                                                      (18, 'Maquillaje Social Profesional', 'Ideal para eventos o sesiones fotográficas, adaptado a tu estilo.', 60, 35.0)
    ON CONFLICT (id) DO NOTHING;

-- (Opcional) Otro usuario para pruebas múltiples
INSERT INTO users (user_name, email, password, created_at, active, role)
VALUES
    ('ClienteSecundario', 'seconduser@example.com',
     '$2a$12$hPKcD8lGqSml8qr7ou1fxei77XAajhpcxc1QX.muO54MsHm/GkAKm',
     CURRENT_DATE, TRUE, 'USER')
    ON CONFLICT (email) DO NOTHING;







