DROP TABLE IF EXISTS vehiculos;

CREATE TABLE vehiculos (
    id INT PRIMARY KEY,
    matricula VARCHAR(20) NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio INT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    consumo VARCHAR(20),
    cilindrada INT,
    capacidad INT
);
