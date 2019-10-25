DROP TABLE IF EXISTS User;

CREATE TABLE User (
UserId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
UserName VARCHAR(30) NOT NULL,
EmailAddress VARCHAR(30) NOT NULL);

CREATE UNIQUE INDEX idx_ue on User(UserName,EmailAddress);

DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
AccountId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
UserName VARCHAR(30),
Balance DECIMAL(19,4),
CurrencyCode VARCHAR(30));

CREATE UNIQUE INDEX idx_acc on Account(UserName,CurrencyCode);