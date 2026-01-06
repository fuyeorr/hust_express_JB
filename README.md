# 快递驿站管理系统

考虑到一些原因，没有使用 maven 或 gradle 作为构建工具，依赖管理使用 IDEA 自带的 iml 格式
前往 [这里](https://mariadb.com/downloads/connectors/connectors-data-access/java8-connector) 下载 MariaDB Connector/J 并在 `IDEA - 项目结构` 中添加到依赖。

编辑 `Express/src/resources/config.properties` 文件, 写入正确的本地数据库用户名和密码 (不要把包含密码或敏感信息的文件纳入 git 管理! 如有需要自由增加 `.gitignore` 的规则)

## 系统架构设计

* DBUtil: 封装 JDBC 的基本 API，包括数据库连接、`pstmt` 执行等
* Model: 实体模型的 Java 对象表达
* DAO(Data Access Object): 使用 DBUtil，封装对 Model 的操作
* 业务层: 业务逻辑，异常处理
* 前段图形界面: 采用 swing

## 开发计划

### 当前进度
- [x] `DBUtil` 从配置文件读取数据库配置信息
- [x] `DBUtil` 配置正确的数据库信息并连接到数据库
- [ ] `DBUtil` 完成初步的 SQL 语句查询，采用 `PreparedStatement` 进行参数化查询
- [ ] 快递驿站需求分析和表设计

### 第一阶段

* 表设计（考虑到主要是 Java 课设，无需严格规范化）
* 开发 DBUtil 模块

### 第二阶段

1. 按照表设计将 Model 模块完成
1. 按照 Model 将 DAO模块完成

### 第三阶段

1. 设计业务逻辑，考虑需要支持什么业务
1. 开发业务逻辑
1. 开发图形界面


## 建库建表

#### PK:Primary Key(主键)
#### FK:Foreign Key(外键)

## 1. 用户 User
```sql
(
    userID(PK),
    name,
    phone,
    address,
    sex,
    userType(收件/寄件)
)
```

## 2.订单 Orders
```sql
(
    orderID(PK),   
    senderID (FK,User(userID)),   
    receiverID (FK,User(userID)),   
    startTime,
    orderStatus(正常/异常),
    cost
)
```

## 3.包裹 Package
```sql
(
    packageID(PK),
    orderID(FK,Order(OrderID)),
    weight,
    volume,
    packageStatus(正常/易碎物/贵重物/保鲜物/...),
    currentStatus()
)
```

## 4.快递公司 Company
```sql
(
    companyID(PK),
    companyName,
    companyCode(编码),
    companyPhone
)
```

## 5.运单 WayBill
```sql
(
    wayID(PK),
    packageID(FK,Package(packageID)),
    companyID(FK,Company(companyID)),
    origin,
    destination,
    sendTime,
    wayStatus(已揽收/运输中)
)
```

## 6. 快递员 DeliveryMan
```sql
(
    deliveryID(PK),
    companyID(FK,Company(companyID)),
    delName,
    delPhone,
    delSex,
    delType(同城运送/干线转运)
)
```

## 7.轨迹 Track
```sql
(
    trackID(PK),
    wayID(FK,wayBill(wayID)),
    deliveryID(FK,DeliveryMan(deliveryID)),
    trackTime,
    currentLocation,
    trackInfo(物流简述)
)
```

## 8.驿站 Station
```sql
(
    stationID(PK),
    stationName,
    location,
    stationCode,
    stationPhone
)
```

## 9.工作人员 Staff
```sql
(
    staffID(PK),
    stationID(FK,station(stationID)),
    staffName,
    staffSex,
    staffPhone,
    staffRole(管理员/派送员/整理仓库者/...)
)
```

## 10. 异常信息 Exception
```sql
(
    exceptionID(PK),
    packageID(FK,Package(packageID)),
    exceptionType(缺货/丢失/破损/延误/...),
    exceptionName,
    description(异常说明)
)
```

## 11. 入库信息 Storage
```sql
(
    packageID(FK,Package(packageID)),
    stationID(FK,Station(stationID)),
    storageTame,
    storageCode(入库编码),
    storageStatus(正常/异常),
    Primary Key{packageID,stationID}
)
```

## 12. 签收记录 Sign
```sql
(
    signID(PK),
    packageID(FK,Package(PackageID)),
    stationID(FK,Station(stationID)),
    receiverID(FK,User(UserID)),
    signType(送货上门/驿站自取),
    signTime
)
