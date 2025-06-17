DROP TABLE IF EXISTS vehiculos;
DROP TABLE IF EXISTS citas;

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

CREATE TABLE citas (
    id INT PRIMARY KEY,
    fechaCita DATE NOT NULL,
    hora TIME NOT NULL,
    vehiculoId INT NOT NULL
);