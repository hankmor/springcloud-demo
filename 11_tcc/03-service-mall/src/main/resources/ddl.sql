CREATE TABLE `service-mall`.`t_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `price` double(10, 3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

INSERT INTO `service-mall`.`t_product`(`id`, `name`, `price`) VALUES (1, 'iPhone', 5000.000);
INSERT INTO `service-mall`.`t_product`(`id`, `name`, `price`) VALUES (2, 'iPad', 3000.000);