-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: realestate
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bankaccounts`
--

DROP TABLE IF EXISTS `bankaccounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bankaccounts` (
  `accountno` int(11) NOT NULL AUTO_INCREMENT,
  `balance` double NOT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`accountno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bankaccounts`
--

LOCK TABLES `bankaccounts` WRITE;
/*!40000 ALTER TABLE `bankaccounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `bankaccounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customerproposals`
--

DROP TABLE IF EXISTS `customerproposals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customerproposals` (
  `proposalid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  PRIMARY KEY (`proposalid`,`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customerproposals`
--

LOCK TABLES `customerproposals` WRITE;
/*!40000 ALTER TABLE `customerproposals` DISABLE KEYS */;
INSERT INTO `customerproposals` VALUES (1,19);
/*!40000 ALTER TABLE `customerproposals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `userid` int(11) NOT NULL,
  `buyer` tinyint(1) DEFAULT NULL,
  `income` double DEFAULT NULL,
  `occupation` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (5,0,2122,'Student'),(7,1,0,NULL),(8,1,NULL,''),(9,0,22000,'worker'),(10,1,NULL,''),(19,1,NULL,''),(20,1,NULL,'');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `userid` int(11) NOT NULL,
  `salary` double NOT NULL,
  `hiredate` date NOT NULL,
  `etype` varchar(45) DEFAULT NULL,
  `parttime` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `userid_UNIQUE` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (15,55000,'2019-10-14','admin',NULL),(16,55000,'2019-10-14','manager',NULL),(17,55000,'2019-10-14','propertymanager',1),(18,55000,'2019-10-14','salesconsultant',1);
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspections`
--

DROP TABLE IF EXISTS `inspections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inspections` (
  `inspectionid` int(11) NOT NULL AUTO_INCREMENT,
  `submissiondate` timestamp NOT NULL,
  `propertyid` int(11) NOT NULL,
  `cancelled` tinyint(1) DEFAULT '0',
  `userid` int(11) NOT NULL,
  `duedate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`inspectionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspections`
--

LOCK TABLES `inspections` WRITE;
/*!40000 ALTER TABLE `inspections` DISABLE KEYS */;
/*!40000 ALTER TABLE `inspections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preferredsuburbs`
--

DROP TABLE IF EXISTS `preferredsuburbs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preferredsuburbs` (
  `userid` int(11) NOT NULL,
  `suburb` varchar(45) NOT NULL,
  PRIMARY KEY (`userid`,`suburb`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preferredsuburbs`
--

LOCK TABLES `preferredsuburbs` WRITE;
/*!40000 ALTER TABLE `preferredsuburbs` DISABLE KEYS */;
INSERT INTO `preferredsuburbs` VALUES (5,'Melbourne'),(7,'Melbourne'),(7,'South Yarra'),(8,'Melbourne'),(8,'Pascoe Vale'),(8,'Richmond'),(9,'Melb'),(10,'a'),(19,'Melbourne'),(19,'Test'),(20,'Melbourne'),(20,'test');
/*!40000 ALTER TABLE `preferredsuburbs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `properties`
--

DROP TABLE IF EXISTS `properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `properties` (
  `propertyid` int(11) NOT NULL AUTO_INCREMENT,
  `price` double NOT NULL,
  `listeddate` timestamp NOT NULL,
  `active` tinyint(1) NOT NULL,
  `address` varchar(45) NOT NULL,
  `suburb` varchar(45) NOT NULL,
  `beds` int(11) NOT NULL,
  `baths` int(11) NOT NULL,
  `carspaces` int(11) NOT NULL,
  `propertytype` varchar(45) NOT NULL,
  `listedby` int(11) NOT NULL COMMENT 'listed by',
  `assignedto` int(11) DEFAULT NULL,
  `rental` tinyint(1) DEFAULT NULL,
  `contractdurations` varchar(45) DEFAULT NULL,
  `inspected` tinyint(1) DEFAULT NULL,
  `prevmaintenance` date DEFAULT NULL,
  `nextmaintenance` date DEFAULT NULL,
  PRIMARY KEY (`propertyid`),
  UNIQUE KEY `propertyid_UNIQUE` (`propertyid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `properties`
--

LOCK TABLES `properties` WRITE;
/*!40000 ALTER TABLE `properties` DISABLE KEYS */;
INSERT INTO `properties` VALUES (1,5000,'2019-10-13 16:39:31',1,'123 B St','Melbourne',2,2,1,'House',11,18,0,'',1,NULL,NULL),(2,444,'2019-10-13 16:46:38',1,'10 A St','South Yarra',0,0,0,'Unit',12,17,1,'TWO_YEARS,ONE_YEAR',1,'2019-10-14','2019-10-19'),(5,12,'2019-10-13 17:10:24',1,'23 B Rd','Richmond',0,0,0,'House',12,17,1,'TWO_YEARS',1,'2019-10-14','2019-10-19'),(6,2122,'2019-10-17 08:01:19',0,'123 ABC Sales','Melbourne',0,0,0,'Flat',21,0,0,'',0,NULL,NULL);
/*!40000 ALTER TABLE `properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `propertyowners`
--

DROP TABLE IF EXISTS `propertyowners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `propertyowners` (
  `userid` int(11) NOT NULL,
  `vendor` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `propertyowners`
--

LOCK TABLES `propertyowners` WRITE;
/*!40000 ALTER TABLE `propertyowners` DISABLE KEYS */;
INSERT INTO `propertyowners` VALUES (11,1),(12,0),(21,1);
/*!40000 ALTER TABLE `propertyowners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proposals`
--

DROP TABLE IF EXISTS `proposals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proposals` (
  `proposalid` int(11) NOT NULL AUTO_INCREMENT,
  `offerprice` double NOT NULL,
  `submissiondate` timestamp NOT NULL,
  `accepted` tinyint(1) NOT NULL,
  `propertyid` int(11) NOT NULL,
  `contractduration` varchar(45) DEFAULT NULL,
  `paid` tinyint(1) DEFAULT NULL,
  `withdrawn` tinyint(1) DEFAULT NULL,
  `waitingforpay` tinyint(1) DEFAULT NULL,
  `pending` tinyint(1) DEFAULT NULL,
  `acceptdate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`proposalid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proposals`
--

LOCK TABLES `proposals` WRITE;
/*!40000 ALTER TABLE `proposals` DISABLE KEYS */;
INSERT INTO `proposals` VALUES (1,11112,'2019-10-16 16:11:52',0,1,'',0,0,0,1,NULL);
/*!40000 ALTER TABLE `proposals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `password` longtext,
  `lastlogin` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (5,'renter2','a@a.co','80f6660c159f392d8a882fab6e8898934c23eb8374ba22db7fe7ac9c82867a330559fb81dae539668e9ad1fedb93d73333b94f54a3e0b9daa8dbe1ff97a28e43',NULL),(7,'ab','a@a.com','80f6660c159f392d8a882fab6e8898934c23eb8374ba22db7fe7ac9c82867a330559fb81dae539668e9ad1fedb93d73333b94f54a3e0b9daa8dbe1ff97a28e43','2019-10-17 07:35:09'),(8,'aba','a@a.com','f0c0c067d20ff53eb95d6f297ad32de52d52ae7884d9778ea824be6bccacabe74986edf8bd9dd738ad2c1e7e2816b192e26ff29d090fe01c9eba380de10d98d0',NULL),(9,'renter','a@a.com','80f6660c159f392d8a882fab6e8898934c23eb8374ba22db7fe7ac9c82867a330559fb81dae539668e9ad1fedb93d73333b94f54a3e0b9daa8dbe1ff97a28e43','2019-10-15 11:58:25'),(10,'aaa','a@a.com','3741bcb880de3d1d4ad870349c46ff1e80b10a8f7001bd3a7d5745258107bfe4f343923a867a4bdbe6d064e44dc1986ebed98152be983940d231f146c653785a',NULL),(11,'vendor','vendor@a.com','80f6660c159f392d8a882fab6e8898934c23eb8374ba22db7fe7ac9c82867a330559fb81dae539668e9ad1fedb93d73333b94f54a3e0b9daa8dbe1ff97a28e43','2019-10-13 19:01:33'),(12,'landlord','landlord@a.com','80f6660c159f392d8a882fab6e8898934c23eb8374ba22db7fe7ac9c82867a330559fb81dae539668e9ad1fedb93d73333b94f54a3e0b9daa8dbe1ff97a28e43','2019-10-15 10:10:32'),(15,'admin','a@a.com','80f6660c159f392d8a882fab6e8898934c23eb8374ba22db7fe7ac9c82867a330559fb81dae539668e9ad1fedb93d73333b94f54a3e0b9daa8dbe1ff97a28e43','2019-10-15 10:19:39'),(16,'manager','a@a.com','80f6660c159f392d8a882fab6e8898934c23eb8374ba22db7fe7ac9c82867a330559fb81dae539668e9ad1fedb93d73333b94f54a3e0b9daa8dbe1ff97a28e43','2019-10-15 10:47:39'),(17,'rentals','a@a.com','80f6660c159f392d8a882fab6e8898934c23eb8374ba22db7fe7ac9c82867a330559fb81dae539668e9ad1fedb93d73333b94f54a3e0b9daa8dbe1ff97a28e43','2019-10-17 06:47:02'),(18,'sales','a@a.com','80f6660c159f392d8a882fab6e8898934c23eb8374ba22db7fe7ac9c82867a330559fb81dae539668e9ad1fedb93d73333b94f54a3e0b9daa8dbe1ff97a28e43','2019-10-15 11:56:05'),(19,'buyer','1@a.com','80f6660c159f392d8a882fab6e8898934c23eb8374ba22db7fe7ac9c82867a330559fb81dae539668e9ad1fedb93d73333b94f54a3e0b9daa8dbe1ff97a28e43','2019-10-17 08:28:04'),(20,'buyer2','1@a.com','80f6660c159f392d8a882fab6e8898934c23eb8374ba22db7fe7ac9c82867a330559fb81dae539668e9ad1fedb93d73333b94f54a3e0b9daa8dbe1ff97a28e43','2019-10-17 08:27:56'),(21,'seller','a@a.com','80f6660c159f392d8a882fab6e8898934c23eb8374ba22db7fe7ac9c82867a330559fb81dae539668e9ad1fedb93d73333b94f54a3e0b9daa8dbe1ff97a28e43','2019-10-17 08:01:42');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workingentries`
--

DROP TABLE IF EXISTS `workingentries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `workingentries` (
  `workingid` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `hours` double NOT NULL,
  `approved` tinyint(1) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`workingid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workingentries`
--

LOCK TABLES `workingentries` WRITE;
/*!40000 ALTER TABLE `workingentries` DISABLE KEYS */;
/*!40000 ALTER TABLE `workingentries` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-17 19:30:43
