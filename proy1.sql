--PRIMER PROYECTO PROGRAMADO BASES DE DATOS 1 - INTERPRETE DE ALGEBRA RELACIONAL
--Creo la base de datos
CREATE DATABASE bdproy1

--Trabajo sobre esta DB
USE bdproy1
GO
SELECT * FROM #r9
--Creo esquema
CREATE SCHEMA proy1 authorization usproy1

--Se debe de crear la bases de datos(Banco) en el esquema
CREATE TABLE proy1.cliente
	(
	nombre_cliente	char(20),
	calle_cliente	char(20),
	ciudad_cliente	char(20),
	primary key(nombre_cliente)
	)
CREATE TABLE proy1.sucursal
	(
	nombre_sucursal	char(15),
	ciudad_sucursal	char(30),
	activos			numeric(16,2),
	primary	key(nombre_sucursal)
	)
CREATE TABLE proy1.cuenta
	(
	número_cuenta	char(10),
	nombre_sucursal	char(15),
	saldo			numeric(12,2),
	primary key(número_cuenta)
	)
CREATE TABLE proy1.impositor
	(
	nombre_cliente	char(20),
	número_cuenta	char(10),
	primary key(nombre_cliente, número_cuenta)
	)
CREATE TABLE proy1.prestatario
	(
	nombre_cliente	char(20),
	número_préstamo	char(10)
	primary	key(nombre_cliente, número_préstamo)
	)
CREATE TABLE proy1.préstamo
	(
	número_préstamo	char(10),
	nombre_sucursal	char(15),
	importe			numeric(12,2),
	primary	key(número_préstamo)
	)

--Inserto los datos en la base de datos
INSERT INTO proy1.cliente(nombre_cliente, calle_cliente, ciudad_cliente)
VALUES('Abril','Preciados','Valsaín'),('Amo','Embajadores','Arganzuela'),('Badorrey','Delicias','Valsaín'),('Fernández','Jasmín','León'),
('Gómez','Carretas','Cerceda'),('González','Arenal','La Granja'),('López','Mayor','Peguerinos'),('Pérez','Carretas','Cerceda'),('Rodríguez','Yeserías','Cádiz'),
('Rupérez','Ramblas','León'),('Santos','Mayor','Peguerinos'),('Valdivieso','Goya','Vigo')
INSERT INTO proy1.sucursal(nombre_sucursal, ciudad_sucursal, activos)
VALUES('Becerril','Aluche',400000),('Centro','Arganzuela',9000000),('Colado Mediano','Aluche',8000000),('Galapagar','Arganzuela',7100000),
('Moralzarzal','La Granja',2100000),('Navacerrada','Aluche',1700000),('Navas de la Asunción','Alcalá de Henares',300000),('Segovia','Cerceda',3700000)
INSERT INTO proy1.cuenta(número_cuenta, nombre_sucursal, saldo)
VALUES('C-101','Centro',500),('C-215','Becerril',700),('C-102','Navacerrada',400),('C-305','Collado Mediano',350),('C-201','Galapagar',900),
('C-222','Moralzarzal',700),('C-217','Galapagar',750)
INSERT INTO proy1.impositor(nombre_cliente,número_cuenta)
VALUES('Abril','C-305'),('Gómez','C-215'),('González','C-101'),('González','C-201'),('López','C-102'),('Rupérez','C-222'),
('Santos','C-217')
INSERT INTO proy1.prestatario(nombre_cliente, número_préstamo)
VALUES('Fernández','P-16'),('Gómez','P-11'),('Gómez','P-23'),('López','P-15'),('Pérez','P-93'),('Santos','P-17'),('Sotoca','P-14'),
('Valdivieso','P-17')
INSERT INTO proy1.préstamo(número_préstamo, nombre_sucursal, importe)
VALUES('P-11','Collado Mediano',900),('P-14','Centro',1500),('P-15','Navacerrada',1500),('P-16','Navacerrada',1300),('P-17','Centro',1000),
('P-23','Moralzarzal',2000),('P-93','Becerril',500)

--Creo login administrador
CREATE LOGIN jeremylive
WITH PASSWORD='12345'
DEFAULT_DATABASE=bdproy1

--Creo usuario dbaproy1
CREATE USER dbaproy1
FOR LOGIN jeremylive
WITH DEFAULT_SCHEMA=proy1
--Tiene todos los privilegios como DBA, se van a crear los objetos de la DBm con este usuario
GRANT CREATE PROC, EXECUTE, CREATE TABLE, CREATE ROLE, SELECT, INSERT, UPDATE, DELETE TO dbaproy1

--Creo usuario usproy1
CREATE USER usproy1
FOR LOGIN jeremylive
WITH DEFAULT_SCHEMA=proy1
--Tiene lectura para los datos de las tablas permanentes y CRUD para las tablas temporales
GRANT SELECT TO usproy1
GRANT TO usproy1

--


--


--creo tabla temporal, estas deben de borrarse en cuanto se cierra la APP
CREATE TABLE #r9(num1 int)

Use tempdb
SELECT * FROM dbo.#r88


