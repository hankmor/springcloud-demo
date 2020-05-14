CREATE TABLE `service-stock`.`t_stock`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `productId` bigint(20) NULL DEFAULT NULL,
  `stock` int(10) NULL DEFAULT NULL,
  `frozenStock` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

INSERT INTO `service-stock`.`t_stock`(`id`, `productId`, `stock`, `frozenStock`) VALUES (1, 1, 200, NULL);
INSERT INTO `service-stock`.`t_stock`(`id`, `productId`, `stock`, `frozenStock`) VALUES (2, 2, 100, NULL);