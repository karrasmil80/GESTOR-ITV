DELETE FROM vehiculos;
DELETE FROM citas;
DELETE FROM cliente;

-- VEHICULOS
INSERT INTO vehiculos (
    id, matricula, marca, modelo, anio, tipo,
    consumo, cilindrada, capacidad,
    neumaticos, bateria, frenos, aceite
)
VALUES
    -- Vehículo eléctrico
    (1, 'ABC123', 'Tesla', 'Model S', 2022, 'electrico',
     '15kWh/100km', NULL, NULL,
     TRUE, TRUE, TRUE, NULL),

    -- Vehículo con motor gasolina
    (2, 'DEF456', 'Renault', 'Clio', 2019, 'motor',
     NULL, 1600, NULL,
     TRUE, TRUE, FALSE, TRUE),

    -- Vehículo público (diesel) con capacidad
    (3, 'GHI789', 'Seat', 'Ibiza', 2021, 'publico',
     NULL, NULL, 5,
     TRUE, TRUE, TRUE, NULL);

-- CITAS
INSERT INTO citas (id, fechaCita, hora, vehiculoId)
VALUES
    (1, '2025-06-20', '10:00:00', 1),
    (2, '2025-06-21', '11:30:00', 2),
    (3, '2025-06-22', '09:15:00', 3);

-- CLIENTES
INSERT INTO Cliente (id, nombre, email, password) VALUES
    (1, 'Pablo', 'pablo@kkarra.com', '123456'),
    (2, 'Ana',  'ana@kkarra.com', 'abcdef'),
    (3, 'Luis', 'alicia@kkarra.com', 'pass123');

