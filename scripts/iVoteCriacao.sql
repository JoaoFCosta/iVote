-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA iVoteDB;

-- -----------------------------------------------------
-- Schema iVoteDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `iVoteDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `iVoteDB` ;

-- -----------------------------------------------------
-- Table `iVoteDB`.`Eleicao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iVoteDB`.`Eleicao` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iVoteDB`.`RondaPresidencial`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iVoteDB`.`RondaPresidencial` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `idEleicao` INT NOT NULL COMMENT '',
  `ronda` INT NOT NULL COMMENT '',
  `data` DATE NOT NULL COMMENT '',
  INDEX `fk_Presidencial_Eleicao1_idx` (`idEleicao` ASC)  COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  CONSTRAINT `fk_Presidencial_Eleicao1`
    FOREIGN KEY (`idEleicao`)
    REFERENCES `iVoteDB`.`Eleicao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iVoteDB`.`Legislativa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iVoteDB`.`Legislativa` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `idEleicao` INT NOT NULL COMMENT '',
  `data` DATETIME NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  INDEX `fk_Legislativa_Eleicao1_idx` (`idEleicao` ASC)  COMMENT '',
  CONSTRAINT `fk_Legislativa_Eleicao1`
    FOREIGN KEY (`idEleicao`)
    REFERENCES `iVoteDB`.`Eleicao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iVoteDB`.`Circulo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iVoteDB`.`Circulo` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `distrito` VARCHAR(45) NOT NULL COMMENT '',
  `idLegislativa` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  INDEX `fk_Circulo_Legislativa1_idx` (`idLegislativa` ASC)  COMMENT '',
  CONSTRAINT `fk_Circulo_Legislativa1`
    FOREIGN KEY (`idLegislativa`)
    REFERENCES `iVoteDB`.`Legislativa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iVoteDB`.`AssembleiaVoto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iVoteDB`.`AssembleiaVoto` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `nome` VARCHAR(45) NOT NULL COMMENT '',
  `idCirculo` INT NULL COMMENT '',
  `idRondaPresidencial` INT NULL COMMENT '',
  `votosBrancos` INT NOT NULL DEFAULT 0 COMMENT '',
  `votosNulos` INT NOT NULL DEFAULT 0 COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  INDEX `fk_Freguesia_Circulo1_idx` (`idCirculo` ASC)  COMMENT '',
  INDEX `fk_AssembleiaVoto_rondaPresidencial1_idx` (`idRondaPresidencial` ASC)  COMMENT '',
  CONSTRAINT `fk_Freguesia_Circulo1`
    FOREIGN KEY (`idCirculo`)
    REFERENCES `iVoteDB`.`Circulo` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_AssembleiaVoto_rondaPresidencial1`
    FOREIGN KEY (`idRondaPresidencial`)
    REFERENCES `iVoteDB`.`RondaPresidencial` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iVoteDB`.`Cidadao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iVoteDB`.`Cidadao` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `idEleitor` INT NOT NULL COMMENT '',
  `password` VARCHAR(45) NOT NULL COMMENT '',
  `nome` VARCHAR(45) NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iVoteDB`.`Eleitor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iVoteDB`.`Eleitor` (
  `idCidadao` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `idAssembleiaVoto` INT NOT NULL COMMENT '',
  `votou` INT NULL COMMENT '',
  `seccao` INT NOT NULL COMMENT '',
  INDEX `fk_Eleitor_Freguesia_idx` (`idAssembleiaVoto` ASC)  COMMENT '',
  PRIMARY KEY (`idCidadao`, `idAssembleiaVoto`)  COMMENT '',
  CONSTRAINT `fk_Eleitor_Freguesia`
    FOREIGN KEY (`idAssembleiaVoto`)
    REFERENCES `iVoteDB`.`AssembleiaVoto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Eleitor_Cidadao1`
    FOREIGN KEY (`idCidadao`)
    REFERENCES `iVoteDB`.`Cidadao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iVoteDB`.`Lista`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iVoteDB`.`Lista` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iVoteDB`.`AssembleiaLista`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iVoteDB`.`AssembleiaLista` (
  `idLista` INT NOT NULL COMMENT '',
  `idAssembleiaVoto` INT NOT NULL COMMENT '',
  `votos` INT NOT NULL DEFAULT 0 COMMENT '',
  PRIMARY KEY (`idLista`, `idAssembleiaVoto`)  COMMENT '',
  INDEX `fk_Lista_has_Freguesia_Freguesia1_idx` (`idAssembleiaVoto` ASC)  COMMENT '',
  INDEX `fk_Lista_has_Freguesia_Lista1_idx` (`idLista` ASC)  COMMENT '',
  CONSTRAINT `fk_Lista_has_Freguesia_Lista1`
    FOREIGN KEY (`idLista`)
    REFERENCES `iVoteDB`.`Lista` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Lista_has_Freguesia_Freguesia1`
    FOREIGN KEY (`idAssembleiaVoto`)
    REFERENCES `iVoteDB`.`AssembleiaVoto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iVoteDB`.`Candidato`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iVoteDB`.`Candidato` (
  `idCidadao` INT NOT NULL COMMENT '',
  `idLista` INT NOT NULL COMMENT '',
  PRIMARY KEY (`idCidadao`)  COMMENT '',
  INDEX `fk_Candidato_Cidadao1_idx` (`idCidadao` ASC)  COMMENT '',
  INDEX `fk_Candidato_Lista1_idx` (`idLista` ASC)  COMMENT '',
  CONSTRAINT `fk_Candidato_Cidadao1`
    FOREIGN KEY (`idCidadao`)
    REFERENCES `iVoteDB`.`Cidadao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Candidato_Lista1`
    FOREIGN KEY (`idLista`)
    REFERENCES `iVoteDB`.`Lista` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iVoteDB`.`Admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iVoteDB`.`Admin` (
  `id` VARCHAR(15) NOT NULL COMMENT '',
  `password` VARCHAR(45) NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iVoteDB`.`AssembleiaCandidato`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iVoteDB`.`AssembleiaCandidato` (
  `idAssembleiaVoto` INT NOT NULL COMMENT '',
  `idCidadao` INT NOT NULL COMMENT '',
  `votos` INT NULL COMMENT '',
  PRIMARY KEY (`idAssembleiaVoto`, `idCidadao`)  COMMENT '',
  INDEX `fk_AssembleiaVoto_has_Candidato_Candidato1_idx` (`idCidadao` ASC)  COMMENT '',
  INDEX `fk_AssembleiaVoto_has_Candidato_AssembleiaVoto1_idx` (`idAssembleiaVoto` ASC)  COMMENT '',
  CONSTRAINT `fk_AssembleiaVoto_has_Candidato_AssembleiaVoto1`
    FOREIGN KEY (`idAssembleiaVoto`)
    REFERENCES `iVoteDB`.`AssembleiaVoto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_AssembleiaVoto_has_Candidato_Candidato1`
    FOREIGN KEY (`idCidadao`)
    REFERENCES `iVoteDB`.`Candidato` (`idCidadao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
