CREATE DATABASE `@databaseName`;

CREATE TABLE `mv2`.`certificate` (`serialnumber` VARBINARY(120) NOT NULL, `content` BLOB NOT NULL, PRIMARY KEY (`serialnumber`));
CREATE TABLE `mv2`.`webspace` (`identifier` VARCHAR(200) NOT NULL, `certificate` VARBINARY(120) NOT NULL, `keypassphrase` BLOB NULL, `key` BLOB NULL, PRIMARY KEY (`identifier`), INDEX `webspace_certificate_link_idx` (`certificate` ASC), CONSTRAINT `webspace_certificate_link` FOREIGN KEY (`certificate`) REFERENCES `mv2`.`certificate` (`serialnumber`) ON DELETE RESTRICT ON UPDATE CASCADE);
CREATE TABLE `mv2`.`message` (`receiver` VARCHAR(200) NOT NULL, `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, `content` BLOB NULL, `idmessage` BIGINT NOT NULL AUTO_INCREMENT, `retrieved` BIT(1) NOT NULL DEFAULT 0, PRIMARY KEY (`idmessage`));
