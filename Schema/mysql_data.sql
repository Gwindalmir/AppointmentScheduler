-- MySQL dump 10.13  Distrib 5.6.39, for Linux (x86_64)
--

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'123 Main','',1,'11111','555-1212','2019-01-06 16:16:38','test','2019-01-06 16:16:38','test'),(2,'123 Anywhere','',2,'11112','555-1213','2019-01-06 16:17:04','test','2019-01-06 16:17:04','test'),(3,'123 King','',3,'11113','555-1214','2019-01-06 16:17:36','test','2019-01-06 16:18:38','test'),(4,'123 Broad','',13,'11123','555-1215','2019-01-06 16:18:01','test','2019-01-06 16:18:01','test');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES (1,3,1,'not needed','not needed','not needed','not needed','not needed','not needed','2019-01-10 16:00:00','2019-01-10 17:00:00','2019-01-06 16:23:08','test','2019-01-06 16:27:17','test'),(2,2,1,'not needed','not needed','not needed','not needed','not needed','not needed','2019-01-07 13:30:00','2019-01-07 13:45:00','2019-01-06 16:23:57','test','2019-01-06 16:26:44','test');
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
INSERT INTO `city` VALUES (1,'New York',1,'2019-01-06 16:12:07','test','2019-01-06 16:12:07','test'),(2,'Los Angeles',1,'2019-01-06 16:12:28','test','2019-01-06 16:12:28','test'),(3,'Houston',1,'2019-01-06 16:12:41','test','2019-01-06 16:12:41','test'),(4,'Salt Lake City',1,'2019-01-06 16:12:51','test','2019-01-06 16:12:51','test'),(5,'Lancaster',2,'2019-01-06 16:13:19','test','2019-01-06 16:13:19','test'),(6,'London',2,'2019-01-06 16:13:30','test','2019-01-06 16:13:30','test'),(7,'Glasgow',2,'2019-01-06 16:13:50','test','2019-01-06 16:13:50','test'),(8,'Toronto',3,'2019-01-06 16:14:10','test','2019-01-06 16:14:10','test'),(9,'Vancouver',3,'2019-01-06 16:14:20','test','2019-01-06 16:14:20','test'),(10,'Ottawa',3,'2019-01-06 16:14:32','test','2019-01-06 16:14:32','test'),(11,'Oslo',4,'2019-01-06 16:14:41','test','2019-01-06 16:14:41','test'),(12,'Bergen',4,'2019-01-06 16:14:56','test','2019-01-06 16:14:56','test'),(13,'Trondheim',4,'2019-01-06 16:15:06','test','2019-01-06 16:15:06','test');
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` VALUES (1,'US','2019-01-06 16:09:48','test','2019-01-06 16:09:48','test'),(2,'UK','2019-01-06 16:10:09','test','2019-01-06 16:10:09','test'),(3,'Canada','2019-01-06 16:10:46','test','2019-01-06 16:10:46','test'),(4,'Norway','2019-01-06 16:10:53','test','2019-01-06 16:10:53','test');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'John Doe',1,1,'2019-01-06 16:19:19','test','2019-01-06 16:19:19','test'),(2,'Jane Doe',2,1,'2019-01-06 16:19:19','test','2019-01-06 16:19:19','test'),(3,'Sally Test',3,1,'2019-01-06 16:19:19','test','2019-01-06 16:19:19','test');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'test','test',1,'2020-08-11 00:00:00','test','2020-08-11 00:00:00','test');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-11 13:18:06
