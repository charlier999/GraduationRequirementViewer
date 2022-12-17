-- phpMyAdmin SQL Dump
-- version 5.1.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 17, 2022 at 03:43 PM
-- Server version: 5.7.24
-- PHP Version: 8.1.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gradview`
--
CREATE DATABASE IF NOT EXISTS `gradview` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `gradview`;

-- --------------------------------------------------------

--
-- Table structure for table `acc-class`
--

CREATE TABLE `acc-class` (
  `id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL DEFAULT 'default name',
  `description` varchar(1024) NOT NULL DEFAULT 'default description',
  `number` varchar(32) NOT NULL DEFAULT 'EXMP-404',
  `credits` int(11) NOT NULL DEFAULT '-1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `acc-class-prerequisite`
--

CREATE TABLE `acc-class-prerequisite` (
  `classID` int(11) NOT NULL,
  `required-classID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `acc-general-education-classes`
--

CREATE TABLE `acc-general-education-classes` (
  `classID` int(11) NOT NULL,
  `competencyID` int(11) NOT NULL,
  `ba-of-arts` tinyint(4) NOT NULL DEFAULT '0',
  `ba-of-science` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `acc-general-education-competency`
--

CREATE TABLE `acc-general-education-competency` (
  `id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL DEFAULT 'default name',
  `description` varchar(1024) NOT NULL DEFAULT 'default desciption',
  `minimum-credits` int(11) NOT NULL DEFAULT '-1',
  `maximum-credits` int(11) NOT NULL DEFAULT '-1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `acc-program`
--

CREATE TABLE `acc-program` (
  `id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL DEFAULT 'default name',
  `description` varchar(1024) NOT NULL DEFAULT 'default description',
  `level` varchar(32) NOT NULL DEFAULT 'Bachelor, Masters, Doctor',
  `ba-of-arts` tinyint(4) NOT NULL DEFAULT '0',
  `ba-of-science` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `acc-program-classes`
--

CREATE TABLE `acc-program-classes` (
  `programID` int(11) NOT NULL,
  `classID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `acc-program-electives-credits`
--

CREATE TABLE `acc-program-electives-credits` (
  `programID` int(11) NOT NULL,
  `minimum` int(11) NOT NULL DEFAULT '-1',
  `maximum` int(11) NOT NULL DEFAULT '-1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `acc-program-general-education-credits`
--

CREATE TABLE `acc-program-general-education-credits` (
  `programID` int(11) NOT NULL,
  `minimum` int(11) NOT NULL DEFAULT '-1',
  `maximum` int(11) NOT NULL DEFAULT '-1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `acc-program-major-credits`
--

CREATE TABLE `acc-program-major-credits` (
  `programID` int(11) NOT NULL,
  `credits` int(11) NOT NULL DEFAULT '-1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `acc-program-total-credits`
--

CREATE TABLE `acc-program-total-credits` (
  `programID` int(11) NOT NULL,
  `credits` int(11) NOT NULL DEFAULT '-1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `permissions`
--

CREATE TABLE `permissions` (
  `id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL DEFAULT 'default name'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `role-permissions`
--

CREATE TABLE `role-permissions` (
  `permissionID` int(11) NOT NULL,
  `roleID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user-assigned-role`
--

CREATE TABLE `user-assigned-role` (
  `userID` int(11) NOT NULL,
  `roleID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user-programs`
--

CREATE TABLE `user-programs` (
  `userID` int(11) NOT NULL,
  `programID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user-roles`
--

CREATE TABLE `user-roles` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL DEFAULT 'default name'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user-roles`
--

INSERT INTO `user-roles` (`id`, `name`) VALUES
(1, 'User'),
(2, 'Administrator'),
(3, 'Banned');

-- --------------------------------------------------------

--
-- Table structure for table `user-scheduled-classes`
--

CREATE TABLE `user-scheduled-classes` (
  `userID` int(11) NOT NULL,
  `classID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user-taken-classes`
--

CREATE TABLE `user-taken-classes` (
  `userID` int(11) NOT NULL,
  `classID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(128) NOT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `acc-class`
--
ALTER TABLE `acc-class`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `acc-class-prerequisite`
--
ALTER TABLE `acc-class-prerequisite`
  ADD KEY `fk_acc-class-prerequisite_acc-class_classID_idx` (`classID`),
  ADD KEY `fk_acc-class-prerequisite_acc-class_reqired-classID_idx` (`required-classID`);

--
-- Indexes for table `acc-general-education-classes`
--
ALTER TABLE `acc-general-education-classes`
  ADD KEY `fk_acc-general-education-classes_acc-class_classID_idx` (`classID`),
  ADD KEY `fk_acc-general-education-classes_acc-general-education-comp_idx` (`competencyID`);

--
-- Indexes for table `acc-general-education-competency`
--
ALTER TABLE `acc-general-education-competency`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `acc-program`
--
ALTER TABLE `acc-program`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `acc-program-classes`
--
ALTER TABLE `acc-program-classes`
  ADD KEY `fk_acc-program-classes_acc-program_id_idx` (`programID`),
  ADD KEY `fk_acc-program-classes_acc-class_id_idx` (`classID`);

--
-- Indexes for table `acc-program-electives-credits`
--
ALTER TABLE `acc-program-electives-credits`
  ADD KEY `fk_acc-program-total-credits_acc-program_id_idx` (`programID`);

--
-- Indexes for table `acc-program-general-education-credits`
--
ALTER TABLE `acc-program-general-education-credits`
  ADD KEY `fk_acc-program-total-credits_acc-program_id_idx` (`programID`);

--
-- Indexes for table `acc-program-major-credits`
--
ALTER TABLE `acc-program-major-credits`
  ADD KEY `fk_acc-program-total-credits_acc-program_id_idx` (`programID`);

--
-- Indexes for table `acc-program-total-credits`
--
ALTER TABLE `acc-program-total-credits`
  ADD KEY `fk_acc-program-total-credits_acc-program_id_idx` (`programID`);

--
-- Indexes for table `permissions`
--
ALTER TABLE `permissions`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `role-permissions`
--
ALTER TABLE `role-permissions`
  ADD KEY `fk_role-permissions_permissions_id_idx` (`permissionID`),
  ADD KEY `fk_role-permissions_user-roles_id_idx` (`roleID`);

--
-- Indexes for table `user-assigned-role`
--
ALTER TABLE `user-assigned-role`
  ADD KEY `fk_user-assigned-role_users_id_idx` (`userID`),
  ADD KEY `fk_user-assigned-role_user-roles_id_idx` (`roleID`);

--
-- Indexes for table `user-programs`
--
ALTER TABLE `user-programs`
  ADD KEY `fk_user-programs_users_userID_idx` (`userID`),
  ADD KEY `fk_user-programs_acc-program_programID_idx` (`programID`);

--
-- Indexes for table `user-roles`
--
ALTER TABLE `user-roles`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user-scheduled-classes`
--
ALTER TABLE `user-scheduled-classes`
  ADD KEY `fk_user-programs_users_userID_idx` (`userID`),
  ADD KEY `fk_user-programs_acc-class_classID_idx` (`classID`);

--
-- Indexes for table `user-taken-classes`
--
ALTER TABLE `user-taken-classes`
  ADD KEY `fk_user-programs_users_userID_idx` (`userID`),
  ADD KEY `fk_user-programs_acc-class_classID_idx` (`classID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `acc-class-prerequisite`
--
ALTER TABLE `acc-class-prerequisite`
  ADD CONSTRAINT `fk_acc-class-prerequisite_acc-class_classID` FOREIGN KEY (`classID`) REFERENCES `acc-class` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_acc-class-prerequisite_acc-class_reqired-classID` FOREIGN KEY (`required-classID`) REFERENCES `acc-class` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `acc-general-education-classes`
--
ALTER TABLE `acc-general-education-classes`
  ADD CONSTRAINT `fk_a-g-e-class_ac_classID` FOREIGN KEY (`classID`) REFERENCES `acc-class` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_a-g-e-class_ac_compentencyID` FOREIGN KEY (`competencyID`) REFERENCES `acc-general-education-competency` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `acc-program-classes`
--
ALTER TABLE `acc-program-classes`
  ADD CONSTRAINT `fk_acc-program-classes_acc-class_classID` FOREIGN KEY (`classID`) REFERENCES `acc-class` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_acc-program-classes_acc-program_programID` FOREIGN KEY (`programID`) REFERENCES `acc-program` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `acc-program-electives-credits`
--
ALTER TABLE `acc-program-electives-credits`
  ADD CONSTRAINT `fk_acc-program-electives-credits_acc-program_programID` FOREIGN KEY (`programID`) REFERENCES `acc-program` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `acc-program-general-education-credits`
--
ALTER TABLE `acc-program-general-education-credits`
  ADD CONSTRAINT `fk_acc-program-general-education-credits_acc-program_programID` FOREIGN KEY (`programID`) REFERENCES `acc-program` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `acc-program-major-credits`
--
ALTER TABLE `acc-program-major-credits`
  ADD CONSTRAINT `fk_acc-program-major-credits_acc-program_programID` FOREIGN KEY (`programID`) REFERENCES `acc-program` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `acc-program-total-credits`
--
ALTER TABLE `acc-program-total-credits`
  ADD CONSTRAINT `fk_acc-program-total-credits_acc-program_programID` FOREIGN KEY (`programID`) REFERENCES `acc-program` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `role-permissions`
--
ALTER TABLE `role-permissions`
  ADD CONSTRAINT `fk_role-permissions_permissions_permissionID` FOREIGN KEY (`permissionID`) REFERENCES `permissions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_role-permissions_user-roles_roleID` FOREIGN KEY (`roleID`) REFERENCES `user-roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `user-assigned-role`
--
ALTER TABLE `user-assigned-role`
  ADD CONSTRAINT `fk_user-assigned-role_user-roles_roleID` FOREIGN KEY (`roleID`) REFERENCES `user-roles` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_user-assigned-role_users_userID` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `user-programs`
--
ALTER TABLE `user-programs`
  ADD CONSTRAINT `fk_user-prog_a-p_programID` FOREIGN KEY (`programID`) REFERENCES `acc-program` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_user-prog_users_userID` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `user-scheduled-classes`
--
ALTER TABLE `user-scheduled-classes`
  ADD CONSTRAINT `fk_user-sched-class_a-c_classID` FOREIGN KEY (`classID`) REFERENCES `acc-class` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_user-sched-class_users_userID` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `user-taken-classes`
--
ALTER TABLE `user-taken-classes`
  ADD CONSTRAINT `fk_user-taken-class_a-c_classID` FOREIGN KEY (`classID`) REFERENCES `acc-class` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_user-taken-class_users_userID` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
