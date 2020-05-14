CREATE TABLE `service-order`.`t_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderNo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `status` int(5) NULL DEFAULT NULL,
  `productId` bigint(20) NULL DEFAULT NULL,
  `userId` bigint(20) NULL DEFAULT NULL,
  `buyNumber` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

INSERT INTO `service-order`.`t_order`(`id`, `orderNo`, `status`, `productId`, `userId`, `buyNumber`) VALUES (1, '1568277244427', 0, 2, 1, 10);