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
