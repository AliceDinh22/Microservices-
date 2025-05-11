CREATE TABLE `orders`
(
    `id`           BIGINT(20) NOT NULL AUTO_INCREMENT,
    `email`        VARCHAR(255) DEFAULT NULL,
    `order_number` VARCHAR(255) DEFAULT NULL,
    `sku_code`     VARCHAR(255),
    `name`         VARCHAR(255) DEFAULT NULL,
    `price`        DECIMAL(19, 2),
    `quantity`     INT(11),
    PRIMARY KEY (`id`)
);
