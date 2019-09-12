CREATE TABLE `service-point`.`t_point`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NULL DEFAULT NULL,
  `point` int(10) NULL DEFAULT NULL,
  `preparePoint` int(10) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

INSERT INTO `service-point`.`t_point`(`id`, `userId`, `point`, `preparePoint`) VALUES (1, 1, 10000, NULL);