DELETE FROM vehiculos;

INSERT INTO vehiculos (id, matricula, marca, modelo, anio, tipo, consumo, cilindrada, capacidad)
VALUES
    (1, 'ABC123', 'Tesla', 'Model S', 2022, 'electrico', '15kWh/100km', NULL, NULL),
    (2, 'DEF456', 'Renault', 'Clio', 2019, 'gasolina', '6.5L/100km', 1600, NULL),
    (3, 'GHI789', 'Seat', 'Ibiza', 2021, 'diesel', '4.3L/100km', 1500, 5);
