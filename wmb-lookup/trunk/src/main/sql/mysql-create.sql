create database lookup CHARACTER SET utf8 ;

GRANT ALL PRIVILEGES ON lookup.* TO 'sa'@'localhost' IDENTIFIED BY 'sa';

use lookup;

CREATE TABLE cache (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL UNIQUE ,
  PRIMARY KEY(id)
) ENGINE=INNODB;


CREATE TABLE lookup (
  id INT NOT NULL AUTO_INCREMENT,
  cache_id INT NOT NULL ,
  name VARCHAR(255) NOT NULL UNIQUE,
  value VARCHAR(255),
  ttl INT,
  ttd INT,
  PRIMARY KEY(id),
  FOREIGN KEY (cache_id) REFERENCES cache(id)
) ENGINE=INNODB;
