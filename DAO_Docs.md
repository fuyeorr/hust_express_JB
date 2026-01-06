# DAO Documentation


## 基类：BaseDAO<T, PKT>
- `List<T> findAll()`：获取表内所有实体，供业务层做列表展示或批处理。
- `T findByID(PKT id)`：按主键读取单条记录，便于业务逻辑定位个体。
- `boolean insert(T entity)`：新增一条记录，业务层通过返回值判断是否写入成功。
- `boolean update(T entity)`：根据实体中的主键更新现有记录，实现状态或属性变更。
- `boolean delete(PKT id)`：删除主键对应行，用于撤销或清理逻辑。
- 各 DAO 继承此基类，仅暴露 CRUD 意图，具体字段映射由实现者处理。

## `UserDAO`（对应 `User` 表，主键型 `Integer`）
- `findAll`：列出所有用户，支持业务层枚举可选用户。
- `findByID`：根据 `userID` 读取单用户详情。
- `insert`：创建新的用户账户数据。
- `update`：修改已有用户的信息。
- `delete`：移除用户记录。

## `OrderDAO`（`Order` 表，主键 `Integer`）
- `findAll`：获取所有订单供日程或报表使用。
- `findByID`：用于业务流程中检查指定订单的状态。
- `insert`：新增订单，标记寄件/收件关系。
- `update`：更新订单状态、费用或起始时间。
- `delete`：取消订单，清除相关记录。

## `PackageDAO`（`Package` 表，主键 `Integer`）
- `findAll`：列出所有包裹，含状态/体积等数据。
- `findByID`：查询指定包裹的当前状态与归属。
- `insert`：新增包裹条目。
- `update`：调整包裹的状态、重量或体积。
- `delete`：删除包裹记录（用于异常清理）。

## `CompanyDAO`（`Company` 表，主键 `Integer`）
- `findAll`：获取快递公司目录供选择。
- `findByID`：读取具体公司的联系方式。
- `insert`：新增快递合作公司。
- `update`：更新公司编码或电话等信息。
- `delete`：移除无效合作方。

## `WayBillDAO`（`WayBill` 表，主键 `Integer`）
- `findAll`：分页或展示全部运单。
- `findByID`：查看指定运单的路线与状态。
- `insert`：生成新的运单记录。
- `update`：同步运单状态/时间/目的地。
- `delete`：撤销运单记录。

## `DeliveryManDAO`（`DeliveryMan` 表，主键 `Integer`）
- `findAll`：列出所有快递员及其所属公司。
- `findByID`：获取单个快递员详情。
- `insert`：注册新的快递员。
- `update`：调整快递员角色或联系方式。
- `delete`：解除快递员权限或记录。

## `TrackDAO`（`Track` 表，主键 `Integer`）
- `findAll`：获取完整轨迹列表供可视化。
- `findByID`：查看某条轨迹的详情。
- `insert`：记录新的物流轨迹节点。
- `update`：修正已有轨迹描述或时间。
- `delete`：移除错误轨迹记录。

## `StationDAO`（`Station` 表，主键 `Integer`）
- `findAll`：列出所有驿站信息供调度。
- `findByID`：读取某个驿站的联系方式与位置。
- `insert`：新增驿站。
- `update`：变更驿站地址或编码。
- `delete`：停用某个驿站。

## `StaffDAO`（`Staff` 表，主键 `Integer`）
- `findAll`：获取站点工作人员名单。
- `findByID`：查看某员工当前角色与所属站点。
- `insert`：新增工作人员记录。
- `update`：调整员工角色或联系方式。
- `delete`：删除员工档案。

## `ExceptionRecordDAO`（`Exception` 表，主键 `Integer`）
- `findAll`：枚举所有异常事件。
- `findByID`：查看特定异常的包裹与描述。
- `insert`：登记新的异常说明。
- `update`：更新异常状态或备注。
- `delete`：清理已解决异常。

## `StorageDAO`（`Storage` 表，复合主键 `{packageID, stationID}`）
- `findAll`：列出所有包裹在各站点的入库记录。
- `findByID`：查找指定包裹在某站点的入库信息。
- `insert`：记录包裹进站数据。
- `update`：更新入库状态或编码。
- `delete`：删除特定站点下的入库记录。

## `SignRecordDAO`（`Sign` 表，主键 `Integer`）
- `findAll`：获取所有签收记录。
- `findByID`：查看某次签收详情。
- `insert`：新增签收记录（上门或自取）。
- `update`：修正在签收时间或类型。
- `delete`：撤销入错的签收条目。
