-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Mag 19, 2020 alle 17:18
-- Versione del server: 10.4.6-MariaDB
-- Versione PHP: 7.3.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `officina`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `auto`
--

CREATE TABLE `auto` (
  `idAuto` int(11) NOT NULL,
  `marca` varchar(30) NOT NULL,
  `modello` varchar(30) NOT NULL,
  `cilindrata` int(11) NOT NULL,
  `alimentazione` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `auto`
--

INSERT INTO `auto` (`idAuto`, `marca`, `modello`, `cilindrata`, `alimentazione`) VALUES
(1, 'Renault', 'Clio', 1500, 'Diesel'),
(2, 'Ford', 'Focus', 1000, 'Benzina'),
(3, 'Peugeot', '3008', 1800, 'Diesel'),
(4, 'Mercedes', 'Classe A', 1900, 'Benzina');

-- --------------------------------------------------------

--
-- Struttura della tabella `riparazioni`
--

CREATE TABLE `riparazioni` (
  `idRip` int(11) NOT NULL,
  `idAuto` int(11) NOT NULL,
  `data` date NOT NULL,
  `causa` varchar(50) NOT NULL,
  `costo` float NOT NULL,
  `pagato` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `riparazioni`
--

INSERT INTO `riparazioni` (`idRip`, `idAuto`, `data`, `causa`, `costo`, `pagato`) VALUES
(1, 1, '2020-05-11', 'Impianto frenante', 150, 0),
(2, 2, '2020-04-11', 'Condizionatore', 254, 1),
(3, 3, '2020-05-15', 'Sostituzione abitacolo', 856, 0),
(4, 4, '2020-04-04', 'Tagliando 150000km', 365, 1),
(5, 1, '2020-01-07', 'Preparazione assetto', 251, 0),
(6, 2, '2020-02-08', 'Impianto frenante', 125, 1),
(7, 3, '2020-03-09', 'Sostituzione vetro anteriore', 25, 1),
(8, 3, '2020-04-10', 'Cambio gomme', 65, 0),
(9, 4, '2020-01-11', 'Impianto stereo', 698, 0),
(10, 4, '2020-03-15', 'Sostituzione fari', 458, 0),
(11, 4, '2020-02-19', 'Preparazione assetto', 245, 1),
(12, 1, '2020-04-04', 'Sostituzione pneumatici', 251, 1),
(13, 1, '2020-05-07', 'Riparazione freno a mano', 354, 1),
(14, 2, '2020-02-09', 'Tuning totale', 752, 0),
(15, 4, '2020-03-07', 'Impianto frenante', 254, 1);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `auto`
--
ALTER TABLE `auto`
  ADD PRIMARY KEY (`idAuto`);

--
-- Indici per le tabelle `riparazioni`
--
ALTER TABLE `riparazioni`
  ADD PRIMARY KEY (`idRip`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `auto`
--
ALTER TABLE `auto`
  MODIFY `idAuto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT per la tabella `riparazioni`
--
ALTER TABLE `riparazioni`
  MODIFY `idRip` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
