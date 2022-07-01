import request from '@/utils/request'
import da from "element-ui/src/locale/lang/da";

// 请求连接数据源
export function reqConn(dataSourceVo) {
  return request({
    url:"/system/dataSource/databases",
    method: "post",
    data: dataSourceVo
  })
}

// 测试连接
export function reqTestConn(dataSourceConfig) {
  return request({
    url:"/system/dataSource/test",
    method: "post",
    data: dataSourceConfig
  })
}

// 请求 数据库类型 数据
export function reqDataBaseType() {
  return request({
    url:"/system/database/all",
    method:"get"
  })
}

// 请求历史连接
export function reqHistory() {
  return request({
    url: "/system/dataSource/history",
    method:"get"
  })
}

// 请求关闭连接
export function reqCloseConn(configId) {
  return request({
       url: "/system/dataSource/close/"+ configId,
       method:"post"
  })
}

// 请求关闭连接
export function reqDataSourceConfig(configId) {
  return request({
       url: "/system/dataSource/"+ configId,
       method:"get"
  })
}

// 请求获取数据库信息
export function reqGetDataBase(configId,dataBaseName) {
  return request({
    url:"/system/dataSource/dataBase/"+configId+"?dataBaseName="+dataBaseName,
    method:"get",
  })
}


// 请求创建数据库
export function reqAddDataBase(configId,dataBaseConfig) {
  return request({
    url:"/system/dataSource/addDB/"+configId,
    method:"post",
    data: dataBaseConfig
  })
}

// 请求删除数据库
export function reqDelDataBase(configId,dataBaseName) {
  return request({
    url:"/system/dataSource/delDB/"+configId+"?dataBaseName="+dataBaseName,
    method:"delete",
  })
}

// 请求修改数据库信息
export function reqEditDataBase(configId,dataBaseConfig) {
  return request({
    url:"/system/dataSource/upDB/"+configId,
    method:"put",
    data: dataBaseConfig
  })
}

// 请求添加表信息
export function reqAddTable(configId,dataBaseName,table) {
  return request({
    url:"/system/dataSource/addTB/"+configId+"?dataBaseName="+dataBaseName,
    method:"post",
    data: table
  })
}

// 获取表数据
export function reqGetTableData(configId,dataBaseName,tableName,current,size) {
  return request({
    url:"/system/dataSource/tbData/"+configId+"?dataBaseName="+dataBaseName+"&tableName="+tableName+"&current="+current+"&size="+size,
    method:"post",
  })
}
// 删除表
export function reqDelTable(configId,dataBaseName,tableName) {
  return request({
    url:"/system/dataSource/delTB/"+configId+"?dataBaseName="+dataBaseName+"&tableName="+tableName,
    method:"post",
  })
}
// 删除表数据
export function reqDelTableData(configId,dataBaseName,tableName,map) {
  return request({
    url:"/system/dataSource/delTbData/"+configId+"?dataBaseName="+dataBaseName+"&tableName="+tableName,
    method:"post",
    data: map
  })
}
// 删除表数据
export function reqUpdateTableData(configId,dataBaseName,tableName,map) {
  return request({
    url:"/system/dataSource/editTbData/"+configId+"?dataBaseName="+dataBaseName+"&tableName="+tableName,
    method:"post",
    data: map
  })
}

// 请求删除连接
export function reqDeleteConfig(configId) {
  return request({
    url:"/system/dataSource/"+configId,
    method:"delete",
  })
}

// 请求字符集设置数据
export function reqCharacterSets() {
  return request({
    url:"/system/character/list",
    method:"get",
  })
}



