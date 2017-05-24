-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Počítač: 127.0.0.1
-- Vytvořeno: Pát 17. úno 2017, 07:55
-- Verze serveru: 5.7.14
-- Verze PHP: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Databáze: `videoconference`
--

-- --------------------------------------------------------

--
-- Struktura tabulky `app_role`
--

CREATE TABLE `app_role` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `authority` varchar(20) NOT NULL,
  `title` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `app_user`
--

CREATE TABLE `app_user` (
  `id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `password` binary(60) NOT NULL,
  `avatar` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Vypisuji data pro tabulku `app_user`
--

INSERT INTO `app_user` (`id`, `email`, `first_name`, `last_name`, `password`, `avatar`) VALUES
(1, 'janhav@seznam.cz', 'Jan', 'Havlíček', 0x6a65646e6161000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000, NULL),
(2, 'kropacjar@fit.cvut.cz', 'Jaroslav', 'Kropáč', 0x6a656e646161000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000, NULL);

-- --------------------------------------------------------

--
-- Struktura tabulky `attachment`
--

CREATE TABLE `attachment` (
  `id` int(11) NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_data` longblob NOT NULL,
  `message_id` int(11) NOT NULL,
  `content_type` varchar(256) NOT NULL,
  `hash` char(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `conversation`
--

CREATE TABLE `conversation` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `conversation_type` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `friendship`
--

CREATE TABLE `friendship` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `friend_id` int(11) NOT NULL COMMENT 'id tabulky app_user',
  `conversation_id` int(11) NOT NULL,
  `friendship_state` tinyint(4) NOT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `friendship_notification`
--

CREATE TABLE `friendship_notification` (
  `id` int(11) NOT NULL,
  `friend_id` int(11) NOT NULL,
  `action` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `group_conversation`
--

CREATE TABLE `group_conversation` (
  `id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `group_event`
--

CREATE TABLE `group_event` (
  `id` int(11) NOT NULL,
  `date_from` datetime NOT NULL,
  `date_to` datetime NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `group_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `group_event_conversation`
--

CREATE TABLE `group_event_conversation` (
  `id` int(11) NOT NULL,
  `group_event_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `group_membership`
--

CREATE TABLE `group_membership` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `membership_state` tinyint(4) NOT NULL,
  `membership_role` mediumint(9) DEFAULT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `group_membership_notification`
--

CREATE TABLE `group_membership_notification` (
  `id` int(11) NOT NULL,
  `group_membership_id` int(11) NOT NULL,
  `action` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `group_topic`
--

CREATE TABLE `group_topic` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `group_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `group_topic_conversation`
--

CREATE TABLE `group_topic_conversation` (
  `id` int(11) NOT NULL,
  `group_topic_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `message`
--

CREATE TABLE `message` (
  `id` int(11) NOT NULL,
  `sender_id` int(11) NOT NULL COMMENT 'id do tabulky app_user',
  `message` mediumtext NOT NULL,
  `date_sent` datetime NOT NULL,
  `is_read` tinyint(1) NOT NULL DEFAULT '0',
  `conversation_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `multichat_conversation`
--

CREATE TABLE `multichat_conversation` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `notification`
--

CREATE TABLE `notification` (
  `id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `is_read` tinyint(1) NOT NULL DEFAULT '0',
  `notification_type` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `private_conversation`
--

CREATE TABLE `private_conversation` (
  `id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `role_for_user`
--

CREATE TABLE `role_for_user` (
  `role_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `user_for_group_event`
--

CREATE TABLE `user_for_group_event` (
  `user_id` int(11) NOT NULL,
  `group_event_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `user_for_group_topic`
--

CREATE TABLE `user_for_group_topic` (
  `user_id` int(11) NOT NULL,
  `group_topic_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `user_for_multichat_conversation`
--

CREATE TABLE `user_for_multichat_conversation` (
  `user_id` int(11) NOT NULL,
  `conversation_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `user_for_private_conversation`
--

CREATE TABLE `user_for_private_conversation` (
  `user_id` int(11) NOT NULL,
  `conversation_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabulky `user_group`
--

CREATE TABLE `user_group` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `type` tinyint(1) NOT NULL,
  `date_created` datetime NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Klíče pro exportované tabulky
--

--
-- Klíče pro tabulku `app_role`
--
ALTER TABLE `app_role`
  ADD PRIMARY KEY (`id`);

--
-- Klíče pro tabulku `app_user`
--
ALTER TABLE `app_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`email`);

--
-- Klíče pro tabulku `attachment`
--
ALTER TABLE `attachment`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `hash` (`hash`),
  ADD KEY `message_id` (`message_id`);

--
-- Klíče pro tabulku `conversation`
--
ALTER TABLE `conversation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `discriminator` (`conversation_type`);

--
-- Klíče pro tabulku `friendship`
--
ALTER TABLE `friendship`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `friend_id` (`friend_id`),
  ADD KEY `conversation_id` (`conversation_id`),
  ADD KEY `user_id_2` (`user_id`),
  ADD KEY `friend_id_2` (`friend_id`),
  ADD KEY `conversation_id_2` (`conversation_id`);

--
-- Klíče pro tabulku `friendship_notification`
--
ALTER TABLE `friendship_notification`
  ADD PRIMARY KEY (`id`),
  ADD KEY `friend_id` (`friend_id`);

--
-- Klíče pro tabulku `group_conversation`
--
ALTER TABLE `group_conversation`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `group_id` (`group_id`);

--
-- Klíče pro tabulku `group_event`
--
ALTER TABLE `group_event`
  ADD PRIMARY KEY (`id`),
  ADD KEY `group_id` (`group_id`);

--
-- Klíče pro tabulku `group_event_conversation`
--
ALTER TABLE `group_event_conversation`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `group_event_id` (`group_event_id`);

--
-- Klíče pro tabulku `group_membership`
--
ALTER TABLE `group_membership`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_user_for_room_room_id` (`group_id`),
  ADD KEY `id` (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `group_id` (`group_id`),
  ADD KEY `id_2` (`id`),
  ADD KEY `id_3` (`id`);

--
-- Klíče pro tabulku `group_membership_notification`
--
ALTER TABLE `group_membership_notification`
  ADD PRIMARY KEY (`id`);

--
-- Klíče pro tabulku `group_topic`
--
ALTER TABLE `group_topic`
  ADD PRIMARY KEY (`id`),
  ADD KEY `group_id` (`group_id`);

--
-- Klíče pro tabulku `group_topic_conversation`
--
ALTER TABLE `group_topic_conversation`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `group_topic_id` (`group_topic_id`);

--
-- Klíče pro tabulku `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sender_id` (`sender_id`),
  ADD KEY `conversation_id` (`conversation_id`);

--
-- Klíče pro tabulku `multichat_conversation`
--
ALTER TABLE `multichat_conversation`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Klíče pro tabulku `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`id`),
  ADD KEY `receiver_id` (`receiver_id`),
  ADD KEY `is_read` (`is_read`),
  ADD KEY `notification_type` (`notification_type`);

--
-- Klíče pro tabulku `private_conversation`
--
ALTER TABLE `private_conversation`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Klíče pro tabulku `role_for_user`
--
ALTER TABLE `role_for_user`
  ADD PRIMARY KEY (`role_id`,`user_id`);

--
-- Klíče pro tabulku `user_for_group_event`
--
ALTER TABLE `user_for_group_event`
  ADD PRIMARY KEY (`user_id`,`group_event_id`);

--
-- Klíče pro tabulku `user_for_group_topic`
--
ALTER TABLE `user_for_group_topic`
  ADD PRIMARY KEY (`user_id`,`group_topic_id`);

--
-- Klíče pro tabulku `user_for_multichat_conversation`
--
ALTER TABLE `user_for_multichat_conversation`
  ADD PRIMARY KEY (`user_id`,`conversation_id`),
  ADD KEY `FK_user_for_conversation_conversation_id` (`conversation_id`);

--
-- Klíče pro tabulku `user_for_private_conversation`
--
ALTER TABLE `user_for_private_conversation`
  ADD PRIMARY KEY (`user_id`,`conversation_id`);

--
-- Klíče pro tabulku `user_group`
--
ALTER TABLE `user_group`
  ADD PRIMARY KEY (`id`),
  ADD KEY `active` (`active`);

--
-- AUTO_INCREMENT pro tabulky
--

--
-- AUTO_INCREMENT pro tabulku `app_role`
--
ALTER TABLE `app_role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pro tabulku `app_user`
--
ALTER TABLE `app_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT pro tabulku `attachment`
--
ALTER TABLE `attachment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pro tabulku `conversation`
--
ALTER TABLE `conversation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pro tabulku `friendship`
--
ALTER TABLE `friendship`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pro tabulku `group_event`
--
ALTER TABLE `group_event`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pro tabulku `group_membership`
--
ALTER TABLE `group_membership`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pro tabulku `group_topic`
--
ALTER TABLE `group_topic`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pro tabulku `message`
--
ALTER TABLE `message`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pro tabulku `notification`
--
ALTER TABLE `notification`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pro tabulku `user_group`
--
ALTER TABLE `user_group`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Omezení pro exportované tabulky
--

--
-- Omezení pro tabulku `friendship`
--
ALTER TABLE `friendship`
  ADD CONSTRAINT `friendship_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
  ADD CONSTRAINT `friendship_ibfk_2` FOREIGN KEY (`friend_id`) REFERENCES `app_user` (`id`);

--
-- Omezení pro tabulku `group_membership`
--
ALTER TABLE `group_membership`
  ADD CONSTRAINT `group_membership_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
  ADD CONSTRAINT `group_membership_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `user_group` (`id`);

--
-- Omezení pro tabulku `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `message_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `app_user` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
