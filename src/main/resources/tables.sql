DROP TABLE IF EXISTS citas;
DROP TABLE IF EXISTS vehiculos;
DROP TABLE IF EXISTS cliente;

CREATE TABLE vehiculos (
    id INT PRIMARY KEY,
    matricula VARCHAR(20) NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio INT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    consumo VARCHAR(20),
    cilindrada INT,
    capacidad INT,
    neumaticos BOOLEAN NOT NULL,
    bateria BOOLEAN NOT NULL,
    frenos BOOLEAN NOT NULL,
    aceite INT
);

CREATE TABLE citas (
    id INT PRIMARY KEY,
    fechaCita VARCHAR(200) NOT NULL,
    hora VARCHAR(200) NOT NULL,
    vehiculoId INT NOT NULL,
    FOREIGN KEY (vehiculoId) REFERENCES vehiculos(id) ON DELETE CASCADE
);

CREATE TABLE cliente (
    id INT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL,
    password VARCHAR(200) NOT NULL
);
