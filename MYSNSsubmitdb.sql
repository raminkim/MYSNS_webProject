-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: submitdb
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `commentlikes`
--

DROP TABLE IF EXISTS `commentlikes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `commentlikes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `postId` int DEFAULT NULL,
  `commentId` int DEFAULT NULL,
  `memberId` varchar(255) DEFAULT NULL,
  `createdTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `postId` (`postId`),
  KEY `commentId` (`commentId`),
  KEY `memberId` (`memberId`),
  CONSTRAINT `commentlikes_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `posts` (`id`),
  CONSTRAINT `commentlikes_ibfk_2` FOREIGN KEY (`commentId`) REFERENCES `comments` (`id`),
  CONSTRAINT `commentlikes_ibfk_3` FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commentlikes`
--

LOCK TABLES `commentlikes` WRITE;
/*!40000 ALTER TABLE `commentlikes` DISABLE KEYS */;
/*!40000 ALTER TABLE `commentlikes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `postId` int DEFAULT NULL,
  `memberId` varchar(255) DEFAULT NULL,
  `createdTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `comment` text,
  PRIMARY KEY (`id`),
  KEY `post_id` (`postId`),
  KEY `member_id` (`memberId`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `posts` (`id`),
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (43,12,'test4','2024-06-24 05:31:53','test');
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follows`
--

DROP TABLE IF EXISTS `follows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follows` (
  `followerId` varchar(255) NOT NULL,
  `followeeId` varchar(255) NOT NULL,
  `followedTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`followerId`,`followeeId`),
  KEY `followee_id` (`followeeId`),
  CONSTRAINT `follows_ibfk_1` FOREIGN KEY (`followerId`) REFERENCES `members` (`id`),
  CONSTRAINT `follows_ibfk_2` FOREIGN KEY (`followeeId`) REFERENCES `members` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follows`
--

LOCK TABLES `follows` WRITE;
/*!40000 ALTER TABLE `follows` DISABLE KEYS */;
INSERT INTO `follows` VALUES ('test2','test','2024-06-23 15:01:20'),('test3','test','2024-06-24 03:26:18'),('test4','test3','2024-06-24 04:55:51'),('test5','test3','2024-06-24 06:22:32');
/*!40000 ALTER TABLE `follows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `members`
--

DROP TABLE IF EXISTS `members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `members` (
  `id` varchar(20) NOT NULL,
  `passwd` varchar(20) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `follower` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `members`
--

LOCK TABLES `members` WRITE;
/*!40000 ALTER TABLE `members` DISABLE KEYS */;
INSERT INTO `members` VALUES ('test','12','김상수',0),('test2','12','서상수',0),('test3','12','서지수',0),('test4','12','김상수',0),('test5','12','김상수',0);
/*!40000 ALTER TABLE `members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `postlikes`
--

DROP TABLE IF EXISTS `postlikes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `postlikes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `postId` int NOT NULL,
  `memberId` varchar(255) NOT NULL,
  `createdTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `postId` (`postId`,`memberId`),
  KEY `memberId` (`memberId`),
  CONSTRAINT `postlikes_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `posts` (`id`),
  CONSTRAINT `postlikes_ibfk_2` FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `postlikes`
--

LOCK TABLES `postlikes` WRITE;
/*!40000 ALTER TABLE `postlikes` DISABLE KEYS */;
/*!40000 ALTER TABLE `postlikes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `posts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `memberId` varchar(255) DEFAULT NULL,
  `title` text,
  `content` text,
  `fileName` varchar(255) DEFAULT NULL,
  `createdTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `member_id` (`memberId`),
  CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (10,'test','12','','','2024-06-23 15:47:24'),(12,'test3','test','1','','2024-06-24 03:25:16');
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-24 15:50:42
