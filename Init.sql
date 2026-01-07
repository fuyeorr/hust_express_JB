-- 1. 创建数据库
DROP DATABASE IF EXISTS express_db;
CREATE DATABASE express_db;
USE express_db;

-- 2. 用户表 vf
DROP TABLE IF EXISTS user;
CREATE TABLE user (
    userID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    phone VARCHAR(20),
    address VARCHAR(100),
    sex CHAR(1),
    userType VARCHAR(10) -- 收件/寄件
) ENGINE=InnoDB;

-- 3. 订单表
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    orderID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    senderID INT NOT NULL,
    receiverID INT NOT NULL,
    startTime DATETIME,
    orderStatus VARCHAR(10), -- 正常/异常
    cost DECIMAL(10,2),
    CONSTRAINT fk_sender FOREIGN KEY (senderID) REFERENCES user(userID),
    CONSTRAINT fk_receiver FOREIGN KEY (receiverID) REFERENCES user(userID)
) ENGINE=InnoDB;

-- 4. 包裹表
DROP TABLE IF EXISTS package;
CREATE TABLE package (
    packageID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orderID INT NOT NULL,
    weight DECIMAL(10,2),
    volume DECIMAL(10,2),
    packageStatus VARCHAR(20), -- 正常/易碎物/贵重物/保鲜物
    currentStatus VARCHAR(50),
    CONSTRAINT fk_order FOREIGN KEY (orderID) REFERENCES orders(orderID)
) ENGINE=InnoDB;

-- 5. 快递公司表
DROP TABLE IF EXISTS company;
CREATE TABLE company (
    companyID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    companyName VARCHAR(50),
    companyCode VARCHAR(20),
    companyPhone VARCHAR(20)
) ENGINE=InnoDB;

-- 6. 运单表
DROP TABLE IF EXISTS waybill;
CREATE TABLE waybill (
    wayID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    packageID INT NOT NULL,
    companyID INT NOT NULL,
    origin VARCHAR(100),
    destination VARCHAR(100),
    sendTime DATETIME,
    wayStatus VARCHAR(20), -- 已揽收/运输中
    CONSTRAINT fk_way_package FOREIGN KEY (packageID) REFERENCES package(packageID),
    CONSTRAINT fk_way_company FOREIGN KEY (companyID) REFERENCES company(companyID)
) ENGINE=InnoDB;

-- 7. 快递员表
DROP TABLE IF EXISTS deliveryman;
CREATE TABLE deliveryman (
    deliveryID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    companyID INT NOT NULL,
    delName VARCHAR(50),
    delPhone VARCHAR(20),
    delSex CHAR(1),
    delType VARCHAR(20), -- 同城运送/干线转运
    CONSTRAINT fk_delivery_company FOREIGN KEY (companyID) REFERENCES company(companyID)
) ENGINE=InnoDB;

-- 8. 轨迹表
DROP TABLE IF EXISTS track;
CREATE TABLE track (
    trackID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    wayID INT NOT NULL,
    deliveryID INT NOT NULL,
    trackTime DATETIME,
    currentLocation VARCHAR(100),
    trackInfo VARCHAR(100),
    CONSTRAINT fk_track_way FOREIGN KEY (wayID) REFERENCES waybill(wayID),
    CONSTRAINT fk_track_delivery FOREIGN KEY (deliveryID) REFERENCES deliveryman(deliveryID)
) ENGINE=InnoDB;

-- 9. 驿站表
DROP TABLE IF EXISTS station;
CREATE TABLE station (
    stationID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    stationName VARCHAR(50),
    location VARCHAR(100),
    stationCode VARCHAR(20),
    stationPhone VARCHAR(20)
) ENGINE=InnoDB;

-- 10. 工作人员表
DROP TABLE IF EXISTS staff;
CREATE TABLE staff (
    staffID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    stationID INT NOT NULL,
    staffName VARCHAR(50),
    staffSex CHAR(1),
    staffPhone VARCHAR(20),
    staffRole VARCHAR(20), -- 管理员/派送员/整理仓库者
    CONSTRAINT fk_staff_station FOREIGN KEY (stationID) REFERENCES station(stationID)
) ENGINE=InnoDB;

-- 11. 异常信息表
DROP TABLE IF EXISTS exception;
CREATE TABLE exception (
    exceptionID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    packageID INT NOT NULL,
    exceptionType VARCHAR(20), -- 缺货/丢失/破损/延误
    exceptionName VARCHAR(50),
    description VARCHAR(200),
    CONSTRAINT fk_exception_package FOREIGN KEY (packageID) REFERENCES package(packageID)
) ENGINE=InnoDB;

-- 12. 入库信息表
DROP TABLE IF EXISTS storage;
CREATE TABLE storage (
    packageID INT NOT NULL,
    stationID INT NOT NULL,
    storageTime DATETIME,
    storageCode VARCHAR(20),
    storageStatus VARCHAR(20), -- 正常/异常
    PRIMARY KEY(packageID, stationID),
    CONSTRAINT fk_storage_package FOREIGN KEY (packageID) REFERENCES package(packageID),
    CONSTRAINT fk_storage_station FOREIGN KEY (stationID) REFERENCES station(stationID)
) ENGINE=InnoDB;

-- 13. 签收记录表
DROP TABLE IF EXISTS sign;
CREATE TABLE sign (
    signID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    packageID INT NOT NULL,
    stationID INT NOT NULL,
    receiverID INT NOT NULL,
    signType VARCHAR(20), -- 送货上门/驿站自取
    signTime DATETIME,
    CONSTRAINT fk_sign_package FOREIGN KEY (packageID) REFERENCES package(packageID),
    CONSTRAINT fk_sign_station FOREIGN KEY (stationID) REFERENCES station(stationID),
    CONSTRAINT fk_sign_receiver FOREIGN KEY (receiverID) REFERENCES user(userID)
) ENGINE=InnoDB;


USE express_db;

-- 1. 用户表
INSERT INTO user (name, phone, address, sex, userType) VALUES
('张三', '13800000001', '北京市海淀区', 'M', '寄件'),
('李四', '13800000002', '上海市浦东新区', 'F', '收件'),
('王五', '13800000003', '广州市天河区', 'M', '收件');

-- 2. 订单表（使用 userID: 1,2,3）
INSERT INTO orders (senderID, receiverID, startTime, orderStatus, cost) VALUES
(1, 2, '2026-01-06 09:00:00', '正常', 25.50),
(1, 3, '2026-01-06 10:00:00', '异常', 40.00);

-- 3. 包裹表（使用 orderID: 1,2）
INSERT INTO package (orderID, weight, volume, packageStatus, currentStatus) VALUES
(1, 1.5, 0.01, '正常', '在运输中'),
(2, 2.0, 0.02, '易碎物', '已入库');

-- 4. 快递公司表
INSERT INTO company (companyName, companyCode, companyPhone) VALUES
('顺丰', 'SF', '400-811-1111'),
('中通', 'ZT', '400-821-2222');

-- 5. 运单表（使用 packageID: 1,2 和 companyID: 1,2）
INSERT INTO waybill (packageID, companyID, origin, destination, sendTime, wayStatus) VALUES
(1, 1, '北京市', '上海市', '2026-01-06 09:30:00', '运输中'),
(2, 2, '北京市', '广州市', '2026-01-06 10:30:00', '已揽收');

-- 6. 快递员表（使用 companyID: 1,2）
INSERT INTO deliveryman (companyID, delName, delPhone, delSex, delType) VALUES
(1, '小李', '13800138001', 'M', '同城运送'),
(2, '小王', '13800138002', 'F', '干线转运');

-- 7. 轨迹表（使用 wayID: 1,2 和 deliveryID: 1,2）
INSERT INTO track (wayID, deliveryID, trackTime, currentLocation, trackInfo) VALUES
(1, 1, '2026-01-06 10:00:00', '北京市海淀区', '已揽收，出发'),
(2, 2, '2026-01-06 11:00:00', '北京市丰台区', '已揽收，等待转运');

-- 8. 驿站表
INSERT INTO station (stationName, location, stationCode, stationPhone) VALUES
('海淀驿站', '北京市海淀区', 'HD001', '010-88888888'),
('浦东驿站', '上海市浦东新区', 'PD001', '021-88888888');

-- 9. 工作人员表（使用 stationID: 1,2）
INSERT INTO staff (stationID, staffName, staffSex, staffPhone, staffRole) VALUES
(1, '张助理', 'M', '13800111111', '管理员'),
(2, '李仓库', 'F', '13800222222', '整理仓库者');

-- 10. 异常信息表（使用 packageID: 2）
INSERT INTO exception (packageID, exceptionType, exceptionName, description) VALUES
(2, '易碎物破损', '破碎', '包裹在运输中破损');

-- 11. 入库信息表（使用 packageID: 2, stationID: 2）
INSERT INTO storage VALUES
(2, 2, '2026-01-06 10:45:00', 'ST001', '正常');

-- 12. 签收记录表（使用 packageID: 1,2, stationID: 1,2, receiverID: 2,3）
INSERT INTO sign (packageID, stationID, receiverID, signType, signTime) VALUES
(1, 1, 2, '送货上门', '2026-01-06 12:00:00'),
(2, 2, 3, '驿站自取', '2026-01-06 13:00:00');
