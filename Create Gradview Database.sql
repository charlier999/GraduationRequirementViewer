-- MySQL Script generated by MySQL Workbench
-- Sat Apr 29 10:50:35 2023
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema gradview
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gradview` DEFAULT CHARACTER SET utf8 ;
USE `gradview` ;

-- -----------------------------------------------------
-- Table `gradview`.`acc-class`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gradview`.`acc-class` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NOT NULL DEFAULT 'default name',
  `description` MEDIUMTEXT NOT NULL,
  `number` VARCHAR(32) NOT NULL DEFAULT 'EXMP-404',
  `credits` INT(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gradview`.`acc-class-prerequisite`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gradview`.`acc-class-prerequisite` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `classID` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_acc-class-prerequisite_acc-class_classID_idx` (`classID` ASC),
  CONSTRAINT `fk_acc-class-prerequisite_acc-class_classID`
    FOREIGN KEY (`classID`)
    REFERENCES `gradview`.`acc-class` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gradview`.`acc-class-prerequisite-and`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gradview`.`acc-class-prerequisite-and` (
  `prerequisiteID` INT(11) NOT NULL,
  `requiredClassID` INT(11) NOT NULL,
  INDEX `fk_acpa_PrerequisiteID_idx` (`prerequisiteID` ASC),
  INDEX `fk_acpa_RequiredClassID_idx` (`requiredClassID` ASC),
  CONSTRAINT `fk_acpa_PrerequisiteID`
    FOREIGN KEY (`prerequisiteID`)
    REFERENCES `gradview`.`acc-class-prerequisite` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_acpa_RequiredClassID`
    FOREIGN KEY (`requiredClassID`)
    REFERENCES `gradview`.`acc-class` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gradview`.`acc-class-prerequisite-or`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gradview`.`acc-class-prerequisite-or` (
  `prerequisiteID` INT(11) NOT NULL,
  `requiredClassID` INT(11) NOT NULL,
  INDEX `fk_acpo_PrerequisiteID_idx` (`prerequisiteID` ASC),
  INDEX `fk_acpo_RequiredClassID_idx` (`requiredClassID` ASC),
  CONSTRAINT `fk_acpo_PrerequisiteID`
    FOREIGN KEY (`prerequisiteID`)
    REFERENCES `gradview`.`acc-class-prerequisite` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_acpo_RequiredClassID`
    FOREIGN KEY (`requiredClassID`)
    REFERENCES `gradview`.`acc-class` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gradview`.`acc-general-education-competency`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gradview`.`acc-general-education-competency` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NOT NULL DEFAULT 'default name',
  `description` VARCHAR(1024) NOT NULL DEFAULT 'default desciption',
  `minimum-credits` INT(11) NOT NULL DEFAULT '-1',
  `maximum-credits` INT(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gradview`.`acc-general-education-classes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gradview`.`acc-general-education-classes` (
  `classID` INT(11) NOT NULL,
  `competencyID` INT(11) NOT NULL,
  `ba-of-arts` TINYINT(4) NOT NULL DEFAULT '0',
  `ba-of-science` TINYINT(4) NOT NULL DEFAULT '0',
  INDEX `fk_acc-general-education-classes_acc-class_classID_idx` (`classID` ASC),
  INDEX `fk_acc-general-education-classes_acc-general-education-comp_idx` (`competencyID` ASC),
  CONSTRAINT `fk_a-g-e-class_ac_classID`
    FOREIGN KEY (`classID`)
    REFERENCES `gradview`.`acc-class` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_a-g-e-class_ac_compentencyID`
    FOREIGN KEY (`competencyID`)
    REFERENCES `gradview`.`acc-general-education-competency` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gradview`.`acc-program`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gradview`.`acc-program` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(1024) NOT NULL DEFAULT 'default name',
  `description` MEDIUMTEXT NOT NULL,
  `level` VARCHAR(32) NOT NULL DEFAULT 'Bachelor, Masters, Doctor',
  `ba-of-arts` TINYINT(4) NOT NULL DEFAULT '0',
  `ba-of-science` TINYINT(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gradview`.`acc-program-classes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gradview`.`acc-program-classes` (
  `programID` INT(11) NOT NULL,
  `classID` INT(11) NOT NULL,
  INDEX `fk_acc-program-classes_acc-program_id_idx` (`programID` ASC),
  INDEX `fk_acc-program-classes_acc-class_id_idx` (`classID` ASC),
  CONSTRAINT `fk_acc-program-classes_acc-class_classID`
    FOREIGN KEY (`classID`)
    REFERENCES `gradview`.`acc-class` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_acc-program-classes_acc-program_programID`
    FOREIGN KEY (`programID`)
    REFERENCES `gradview`.`acc-program` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gradview`.`acc-program-electives-credits`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gradview`.`acc-program-electives-credits` (
  `programID` INT(11) NOT NULL,
  `minimum` INT(11) NOT NULL DEFAULT '-1',
  `maximum` INT(11) NOT NULL DEFAULT '-1',
  INDEX `fk_acc-program-total-credits_acc-program_id_idx` (`programID` ASC),
  CONSTRAINT `fk_acc-program-electives-credits_acc-program_programID`
    FOREIGN KEY (`programID`)
    REFERENCES `gradview`.`acc-program` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gradview`.`acc-program-general-education-credits`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gradview`.`acc-program-general-education-credits` (
  `programID` INT(11) NOT NULL,
  `minimum` INT(11) NOT NULL DEFAULT '-1',
  `maximum` INT(11) NOT NULL DEFAULT '-1',
  INDEX `fk_acc-program-total-credits_acc-program_id_idx` (`programID` ASC),
  CONSTRAINT `fk_acc-program-general-education-credits_acc-program_programID`
    FOREIGN KEY (`programID`)
    REFERENCES `gradview`.`acc-program` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gradview`.`acc-program-major-credits`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gradview`.`acc-program-major-credits` (
  `programID` INT(11) NOT NULL,
  `credits` INT(11) NOT NULL DEFAULT '-1',
  INDEX `fk_acc-program-total-credits_acc-program_id_idx` (`programID` ASC),
  CONSTRAINT `fk_acc-program-major-credits_acc-program_programID`
    FOREIGN KEY (`programID`)
    REFERENCES `gradview`.`acc-program` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gradview`.`acc-program-total-credits`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gradview`.`acc-program-total-credits` (
  `programID` INT(11) NOT NULL,
  `credits` INT(11) NOT NULL DEFAULT '-1',
  INDEX `fk_acc-program-total-credits_acc-program_id_idx` (`programID` ASC),
  CONSTRAINT `fk_acc-program-total-credits_acc-program_programID`
    FOREIGN KEY (`programID`)
    REFERENCES `gradview`.`acc-program` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gradview`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gradview`.`users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(64) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `enabled` TINYINT(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

INSERT INTO `gradview`.`users` (`username`, `password`, `enabled`) VALUES ('administrator','{YOUR PASSWORD HERE}',1);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;