--PRIMER PROYECTO PROGRAMADO BASES DE DATOS 1 - INTERPRETE DE ALGEBRA RELACIONAL

--Creo login administrador
CREATE LOGIN loglive
WITH PASSWORD='111'
--
CREATE LOGIN loglive2
WITH PASSWORD='222'

--Creo la base de datos
CREATE DATABASE bdproy1
GO
--Trabajo sobre esta DB
USE bdproy1
GO

--Creo usuario dbaproy1
CREATE USER dbaproy1
FOR LOGIN loglive
--Tiene todos los privilegios como DBA, se van a crear los objetos de la DBm con este usuario
GRANT CREATE PROC, EXECUTE, CREATE TABLE, CREATE ROLE, SELECT, INSERT, UPDATE, DELETE TO dbaproy1
GO
--Creo usuario usproy1
CREATE USER usproy1
FOR LOGIN loglive2
drop usproy1
--Tiene lectura para los datos de las tablas permanentes y CRUD para las tablas temporales
GRANT SELECT TO usproy1
GO

--Creo esquema
CREATE SCHEMA proy1 authorization dbaproy1
GO
--
CREATE TABLE proy1.cliente2
	(
	nombre_cliente	char(20),
	calle_cliente	char(20),
	ciudad_cliente	char(20),
	primary key(nombre_cliente)
	)
GO
--Se debe de crear la bases de datos(Banco) en el esquema
CREATE TABLE proy1.cliente
	(
	nombre_cliente	char(20),
	calle_cliente	char(20),
	ciudad_cliente	char(20),
	primary key(nombre_cliente)
	)
GO
CREATE TABLE proy1.sucursal
	(
	nombre_sucursal	char(15),
	ciudad_sucursal	char(30),
	activos			numeric(16,2),
	primary	key(nombre_sucursal)
	)
GO
CREATE TABLE proy1.cuenta
	(
	n�mero_cuenta	char(10),
	nombre_sucursal	char(15),
	saldo			numeric(12,2),
	primary key(n�mero_cuenta)
	)
GO
CREATE TABLE proy1.impositor
	(
	nombre_cliente	char(20),
	n�mero_cuenta	char(10),
	primary key(nombre_cliente, n�mero_cuenta)
	)
GO
CREATE TABLE proy1.prestatario
	(
	nombre_cliente	char(20),
	n�mero_pr�stamo	char(10)
	primary	key(nombre_cliente, n�mero_pr�stamo)
	)
GO
CREATE TABLE proy1.pr�stamo
	(
	n�mero_pr�stamo	char(10),
	nombre_sucursal	char(15),
	importe			numeric(12,2),
	primary	key(n�mero_pr�stamo)
	)
GO
----
INSERT INTO proy1.cliente2(nombre_cliente, calle_cliente, ciudad_cliente)
VALUES('Jeremy','007','tokyo'),('Jose','1996','CR'),('Abi','11','LP')
--Inserto los datos en la base de datos
INSERT INTO proy1.cliente(nombre_cliente, calle_cliente, ciudad_cliente)
VALUES('Abril','Preciados','Valsa�n'),('Amo','Embajadores','Arganzuela'),('Badorrey','Delicias','Valsa�n'),('Fern�ndez','Jasm�n','Le�n'),
('G�mez','Carretas','Cerceda'),('Gonz�lez','Arenal','La Granja'),('L�pez','Mayor','Peguerinos'),('P�rez','Carretas','Cerceda'),('Rodr�guez','Yeser�as','C�diz'),
('Rup�rez','Ramblas','Le�n'),('Santos','Mayor','Peguerinos'),('Valdivieso','Goya','Vigo')
GO
INSERT INTO proy1.cuenta(n�mero_cuenta, nombre_sucursal, saldo)
VALUES('C-101','Centro',500),('C-215','Becerril',700),('C-102','Navacerrada',400),('C-305','Collado Mediano',350),('C-201','Galapagar',900),
('C-222','Moralzarzal',700),('C-217','Galapagar',750)
GO
INSERT INTO proy1.impositor(nombre_cliente,n�mero_cuenta)
VALUES('Abril','C-305'),('G�mez','C-215'),('Gonz�lez','C-101'),('Gonz�lez','C-201'),('L�pez','C-102'),('Rup�rez','C-222'),
('Santos','C-217')
GO
INSERT INTO proy1.prestatario(nombre_cliente, n�mero_pr�stamo)
VALUES('Fern�ndez','P-16'),('G�mez','P-11'),('G�mez','P-23'),('L�pez','P-15'),('P�rez','P-93'),('Santos','P-17'),('Sotoca','P-14'),
('Valdivieso','P-17')
GO
INSERT INTO proy1.pr�stamo(n�mero_pr�stamo, nombre_sucursal, importe)
VALUES('P-11','Collado Mediano',900),('P-14','Centro',1500),('P-15','Navacerrada',1500),('P-16','Navacerrada',1300),('P-17','Centro',1000),
('P-23','Moralzarzal',2000),('P-93','Becerril',500)
GO
INSERT INTO proy1.sucursal(nombre_sucursal, ciudad_sucursal, activos)
VALUES('Becerril','Aluche',400000),('Centro','Arganzuela',9000000),('ColadoMediano','Aluche',8000000),('Galapagar','Arganzuela',7100000),
('Moralzarzal','La Granja',2100000),('Navacerrada','Aluche',1700000),('NavasAsunci�n','Alcal� de Henares',300000),('Segovia','Cerceda',3700000)
GO
----


