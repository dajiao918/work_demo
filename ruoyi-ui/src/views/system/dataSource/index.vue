<template>
  <div v-loading="loading">
    <div v-if="databases.length === 0">
      你还没有添加数据库连接！<el-button @click="addConnection">添加连接</el-button>
    </div>

    <div v-else>
      <el-button @click="addConnection">添加连接</el-button>
      <el-tree
        :data="databases"
        ref="tree"
        highlight-current
        :render-content="renderContent"
        :expand-on-click-node="false"
        :props="defaultProps">
      </el-tree>
    </div>
    <!--  新建连接  -->
    <el-dialog
      title="请输入数据库连接信息"
      :visible.sync="dialogVisible"
      width="30%"
      :close="closeDialog">
        <span>
          <el-form label-position="left" label-width="80px" :model="dataSourceConfig">
            <el-form-item label="连接名">
              <el-input v-model="dataSourceConfig.schemaName"></el-input>
            </el-form-item>
            <el-form-item label="类型">
              <el-select v-model="dataSourceConfig.type" placeholder="请选择数据库类型">
              <el-option
                v-for="item in databaseTypes"
                :key="item.typeId"
                :label="item.name"
                :value="item.typeId">
              </el-option>
            </el-select>
            </el-form-item>

            <el-form-item label="连接ip">
              <el-input v-model="dataSourceConfig.url"></el-input>
            </el-form-item>
            <el-form-item label="用户名">
              <el-input v-model="dataSourceConfig.username"></el-input>
            </el-form-item>
            <el-form-item label="用户密码">
              <el-input v-model="dataSourceConfig.password"></el-input>
            </el-form-item>
          </el-form>
        </span>
      <span slot="footer" class="dialog-footer">
          <el-button @click="handleTestConnBtn">测试连接</el-button>
          <el-button type="primary" @click="connDatabase">确定</el-button>
        </span>
    </el-dialog>
    <!--  创建数据库  -->
    <el-dialog
      title="请输入数据库信息"
      :visible.sync="addDatabaseFormVisible"
      width="30%"
      :close="closeDialog">
        <span>
          <el-form label-position="left" label-width="80px" :model="dataBaseConfig">
            <el-form-item label="数据库名">
              <el-input :disabled="edit" v-model="dataBaseConfig.schemaName"></el-input>
            </el-form-item>
            <el-form-item label="字符集">
              <el-select v-on:change="changeCharacter" filterable v-model="dataBaseConfig.characterSetName"  placeholder="请选择字符集">
                <el-option
                  v-for="item in characterSets"
                  :key="item.characterSetName"
                  :label="item.characterSetName"
                  :value="item.characterSetName">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="字符集比较规则">
              <el-select v-model="dataBaseConfig.collationName" placeholder="请选择字符集比较规则">
              <el-option
                v-for="item in collations"
                :key="item"
                :label="item"
                :value="item">
              </el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </span>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitDataBaseForm">确定</el-button>
        </span>
    </el-dialog>
    <!--  创建表  -->
    <el-dialog
      title="请输入表信息"
      :visible.sync="addTableFormVisible"
      width="75%"
      :close="closeDialog">
        <span>
          <el-form :model="table" :inline="true"  ref="dynamicValidateForm" label-width="50px" class="demo-dynamic">
            <el-form-item>
              <el-button @click="addField">新增字段</el-button>
            </el-form-item>
              <el-form-item>
                <el-input class="input_dog" placeholder="表名" v-model="table.tableName" ></el-input>
              </el-form-item>
            <el-form-item>
              <el-select class="input_dog" v-model="table.engine"  placeholder="请选择引擎">
                  <el-option v-for="item in engines" :key="item" :label="item" :value="item"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item >
              <el-select v-on:change="changeCharacter" filterable v-model="table.characterSetName"  placeholder="请选择字符集">
                <el-option
                  v-for="item in characterSets"
                  :key="item.characterSetName"
                  :label="item.characterSetName"
                  :value="item.characterSetName">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item >
              <el-select v-model="table.collateName" placeholder="请选择字符集比较规则">
              <el-option
                v-for="item in collations"
                :key="item"
                :label="item"
                :value="item">
              </el-option>
              </el-select>
            </el-form-item>
            <el-form-item
              v-for="(field, index) in table.fields"
              :label="'字段' + index"
              :key="field.key"
            >
              <el-input class="input_dog" placeholder="字段名" v-model="field.fieldName"></el-input>
              <el-select class="input_dog" v-model="field.fieldType" placeholder="请选择类型">
                <el-option
                  v-for="item in dataTypes"
                  :key="item"
                  :label="item"
                  :value="item">
                </el-option>
              </el-select>
              <el-select class="input_dog" v-model="field.constrict" placeholder="请选择约束">
                <el-option
                  v-for="item in constricts"
                  :key="item"
                  :label="item"
                  :value="item">
                </el-option>
              </el-select>
              <el-input class="input_dog" placeholder="长度" v-model="field.filedLength"></el-input>
              <el-checkbox style="margin-left: 20px" v-model="field.isNotNull">不为空</el-checkbox>
              <el-checkbox style="margin-left: 20px" v-model="field.isPrimaryKey">主键</el-checkbox>
              <el-input class="input_dog" placeholder="默认值" v-model="field.defaultValue"></el-input>
              <el-input class="input_dog" placeholder="注释" v-model="field.comment"></el-input>
              <el-button style="margin-left: 20px" @click.prevent="removeField(field)">删除</el-button>
            </el-form-item>
          </el-form>
        </span>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitCreateTableForm">确定</el-button>
        </span>
    </el-dialog>
    <!--  表数据  -->
    <el-dialog title="表数据" :visible.sync="tableDataVisible">
      <el-table :data="tableData">
        <el-table-column
          v-for="(field) in tableFields"
          :property="field"
          :key="field"
          :label="field"
          width="150">
        </el-table-column>
        <el-table-column
          label="操作"
          width="100">
          <template slot-scope="scope">
            <el-button @click="delTableData(scope.row,scope.$index)" type="text" size="small">删除</el-button>
            <el-button @click="editTableData(scope.row,scope.$index)" type="text" size="small">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        layout="prev, pager, next"
        @prev-click="handlePageChange"
        @next-click="handlePageChange"
        @current-change="handlePageChange"
        :current-page="page.current"
        :page-size="page.size"
        :total="page.totalSize">
      </el-pagination>
    </el-dialog>
    <!--  表的某一列  -->
    <el-dialog width="30%" title="表数据" :visible.sync="tableFieldDataVisible">
      <el-form label-position="left" label-width="80px" :model="tableFieldData">
        <el-form-item
          v-for="prop in propertyNames"
          :key="prop"
          :label="prop"
        >
          <el-input class="input_dog2" v-model="tableFieldData[prop]"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="handleSubmitTableField">确定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>

import {
  reqAddDataBase,
  reqAddTable,
  reqCharacterSets,
  reqCloseConn,
  reqConn,
  reqDataBaseType,
  reqDataSourceConfig,
  reqDelDataBase,
  reqDeleteConfig,
  reqDelTable,
  reqDelTableData,
  reqEditDataBase,
  reqGetDataBase,
  reqGetTableData,
  reqHistory,
  reqTestConn, reqUpdateTableData
} from "../../../api/system/dataSource";

export default {
  name: "index",
  created() {
    reqHistory().then(res=>{
      // console.log(res)
      this.databases = res.data;
      this.databases.forEach(d => {
        d.disabled = true;
      })
      this.loading = false;
    })
    reqDataBaseType().then(res=>{
      // console.log(res)
      this.databaseTypes = res.data;
    })
    // 请求字符集
    reqCharacterSets().then(res=>{
      // console.log(res)
      this.characterSets = res.data;
    })
  },
  data() {
    return {
      // 加载标识
      loading: true,
      // 第一级节点信息
      databases:[],
      // 数据库类型信息，用于渲染新建连接的下拉列表
      databaseTypes:[],
      // 新建连接的dialog标识
      dialogVisible: false,
      // 新建连接的表单渲染对象
      dataSourceConfig: {
        configId:null,
        userId:null,
        type:null,
        schemaName:'',
        url:"localhost",
        username:"root",
        password:"qwer",
        disabled:null,
        createTime:null,
        updateTime:null,
        paramsMap:null
      },
      // element-tree的显示属性
      defaultProps: {
        label:'schemaName',
        children:'children',
        disabled:'disabled'
      },
      // 创建数据库的表单渲染对象
      dataBaseConfig:{},
      // 创建数据库的dialog的标识
      addDatabaseFormVisible: false,
      // 创建mysql数据库的字符集下拉列表
      characterSets:[],
      // 创建mysql数据库的字符集比较规则下拉列表
      collations:[],
      // 数据库信息表单当前修改操作还是新增操作
      edit: false,
      // 创建表的dialog表单数据渲染对象
      table:{
        fields:[{}]
      },
      // 创建表的dialog显示标识
      addTableFormVisible: false,
      // 编辑表的数据时记录该数据的旧数据
      preData:{},
      // 当前点击的节点所处于的config的id
      configId:null,
      // 当前点击的节点所处于的数据库或者用户的信息
      dataBaseName:null,
      // mysql数据库引擎
      // 引擎
      engines:[
        'InnoDB',
        'MRG_MYISAM',
        'MEMORY',
        'BLACKHOLE',
        'MyISAM',
        'CSV',
        'ARCHIVE',
        'PERFORMANCE_SCHEMA',
        'FEDERATED'
      ],
      // 数据类型
      dataTypes : [
        'varchar',
        'char',
        'blob',
        'text',
        'int',
        'tinyint',
        'smallint',
        'mediumint',
        'bit',
        'bigint',
        'float',
        'double',
        'decimal',
        'boolean',
        'date',
        'time',
        'datetime',
        'timestamp',
        'year'],
      // 约束
      constricts:[
        'auto_increment',
        'unique',
        'zerofill'
      ],
      // 表的所有数据，用户渲染查看数据按钮展示的dialog里面的表格
      tableData:[],
      // 表的所有字段，用于渲染表格的第一行
      tableFields:[],
      // 编辑表数据时表单的渲染对象
      tableFieldData: {},
      // 编辑表数据时dialog的标识
      tableDataVisible: false,
      // 表数据的分页对象
      page: {
        current:1,
        size: 10,
        totalPage:null,
        totalSize: null
      },
      // 表的所有字段名，用于获取某一条数据的value
      propertyNames:[],
      tableFieldDataVisible:false,
      // table当前编辑的数据index
      tableIndex:null
    }
  },
  methods: {
    // 新建连接
    addConnection() {
      let config = this.dataSourceConfig;
      config.configId = null;
      config.userId = null;
      config.type = null;
      config.schemaName = '';
      config.url = '';
      config.username = '';
      config.password = '';
      config.disabled = null;
      config.createTime = null;
      config.updateTime = null;
      config.paramsMap = null;
      this.dialogVisible = true;
    },
    // 测试连接按钮
    handleTestConnBtn() {
      let dataSourceConfig = this.dataSourceConfig;
      reqTestConn(dataSourceConfig).then(res=>{
        this.$modal.msgSuccess("成功连接！")
      })
    },
    // 打开连接按钮，新建连接点击确定按钮
    connDatabase(node,data) {
      this.loading = true;
      let that = this;
      let dataSourceVo;
      if (data === undefined) {
        dataSourceVo = this.dataSourceConfig;
      } else {
        dataSourceVo = data;
      }
      reqConn(dataSourceVo).then(res=>{
        // console.log(data)
        console.log(res)
        let databaseRes = res.data;
        // 旧连接
        if (data !== undefined) {
          that.$set(data,'children',[])
          let len = databaseRes.children.length;
          for(let i = 0; i < len; i ++) {
            data.children.push(databaseRes.children[i])
          }
          data.disabled = false;
        } else { // 新连接
          this.databases.push(databaseRes)
          this.dialogVisible = false;

        }
        that.loading = false;
      })
    },
    // 为每一个tree渲染按钮
    renderContent(h, { node, data, store }) {
      return (
        <span class="custom-tree-node">
            <span>{node.label}</span>
            <span>
              <el-button size="mini" type="text" on-click={ () => this.handleAddBtn(node,data) }>{node.level===1?"添加数据库":node.level===2?"添加表":""}</el-button>
              <el-button size="mini" type="text" on-click={ () => this.handleDeleteBtn(node, data) }>删除</el-button>
              <el-button size="mini" type="text" on-click={ () => this.handleEditBtn(node, data) }>属性</el-button>
              <el-button size="mini" type="text" on-click={ () => this.connDatabase(node, data) }>{data.disabled?'打开连接':''}</el-button>
              <el-button size="mini" type="text" on-click={ () => this.handleCloseBtn(data) }>{!data.disabled?node.level===1?'关闭连接':'':''}</el-button>
              <el-button size="mini" type="text" on-click={ () => this.handleWatchBtn(node, data) }>{node.level===3?"查看数据":''}</el-button>
            </span>
          </span>);
    },
    // 删除节点的方法
    deleteNode(node,data) {
      const parent = node.parent;
      const children = parent.data.children || parent.data;
      let index;
      if (node.level === 1) {
        index = children.findIndex(d => d.configId === data.configId)
      } else if (node.level === 2 || node.level === 3) {
        index = children.findIndex(d => d.schemaName === data.schemaName);
      }
      // console.log(index)
      children.splice(index, 1);
      this.$modal.msgSuccess("删除成功！");
    },
    // 删除按钮
    handleDeleteBtn(node, data) {
      // console.log(node)
      // console.log(data)

      if (node.level === 1) { //删除连接
        let that = this;
        // console.log(that)
        this.$modal.confirm("确认删除该连接？").then( ()=> {
          let configId = data.configId;
          // console.log(configId)
          reqDeleteConfig(configId).then(res => {
            that.deleteNode(node, data);
          })
        })
      } else if (node.level === 2) { // 删除数据库
        let that = this;
        let configId = node.parent.data.configId;
        let schemaName = data.schemaName;
        this.$modal.confirm("确定删除数据库"+schemaName+"吗？").then( () => {
          reqDelDataBase(configId,schemaName).then(res=>{
            that.deleteNode(node,data);
          })
        })
      } else if (node.level === 3) { // 删除表
        let that = this;
        let configId = node.parent.parent.data.configId;
        let dataBaseName = node.parent.data.schemaName;
        let tableName = data.schemaName;
        this.$modal.confirm("确定删除表"+tableName+"吗？").then( () => {
          reqDelTable(configId,dataBaseName,tableName).then(res=>{
            that.deleteNode(node,data);
          })
        })
      }

    },
    // 属性按钮
    handleEditBtn(node, data) {
      // 数据源配置
      // console.log(data)
      // console.log(node)
      if (node.level === 1) {
        // 连接已经开启
        if (data.disabled === false) {
          this.handleCloseBtn(data,"修改之前需要关闭连接，是否关闭？");
        } else { // 连接未开启
          reqDataSourceConfig(data.configId).then(res=>{
            this.dataSourceConfig = res.data;
            this.dialogVisible = true;
          })
        }
      } else if(node.level === 2) {
        // 数据库配置
        // console.log(node)
        // console.log(data)
        this.edit = true;
        this.dataSourceConfig = node.parent.data;
        reqGetDataBase(this.dataSourceConfig.configId,data.schemaName).then(res=>{
          this.dataBaseConfig = res.data;
          this.addDatabaseFormVisible = true;
        })
      } else {
        // 表的配置
      }
    },
    // 关闭连接按钮
    handleCloseBtn(data, msg){
      let edit = true;
      if (msg == null) {
        msg = "是否关闭连接？";
        edit = false;
      }
      let that = this;
      this.$modal.confirm(msg).then(function () {
        reqCloseConn(data.configId).then(res => {
          that.$modal.msgSuccess("成功关闭连接！")
          // 讲当前节点的子节点置空
          data.children = [];
          data.disabled = true;
          // 如果是点击‘属性’调用的closeConn，就需要回显数据源信息，打开dialog
          if (edit) {
            that.dataSourceConfig = data;
            that.reDialogVisible = true;
          }
        })
      }).catch(() => {
        console.log("取消")
      })
    },
    // 添加按钮
    handleAddBtn(node, data) {
      let that = this;
      if (node.level === 1) { // 添加数据库
        if (data.disabled) {
          this.$modal.msgError("请先打开连接");
        } else {
          this.dataBaseConfig = {};
          this.edit = false;
          this.dataSourceConfig = data;
          that.addDatabaseFormVisible = true;
        }
      } else if (node.level === 2) { // 添加表
        this.table = {fields:[{}]};
        this.configId = node.parent.data.configId
        this.dataBaseConfig = data;
        this.addTableFormVisible = true;
      } else if (node.level === 3) { // 查看数据

      }
    },
    // 提交数据库信息表单按钮
    submitDataBaseForm() {
      let dataBaseConfig= this.dataBaseConfig;
      let dataSourceConfig = this.dataSourceConfig;
      // console.log(dataBaseConfig)
      // console.log(dataSourceConfig)
      if (this.edit) {
        reqEditDataBase(dataSourceConfig.configId, dataBaseConfig).then(res=>{
          console.log(res)
          this.$modal.msgSuccess("修改数据库信息成功！")
          this.addDatabaseFormVisible = false;
        })
      } else {
        reqAddDataBase(dataSourceConfig.configId, dataBaseConfig).then(res => {
          console.log(res)
          let data = res.data;
          dataSourceConfig.children.push(data);
          this.addDatabaseFormVisible = false;
          this.$modal.msgSuccess("添加数据库成功！")
        })
      }
    },
    // 提交表信息表单确定按钮
    submitCreateTableForm() {
      console.log(this.table)
      console.log(this.configId)
      console.log(this.dataBaseName)
      let that = this;
      reqAddTable(this.configId,this.dataBaseConfig.schemaName,this.table).then(res=>{
        console.log(res.data)
        that.dataBaseConfig.children.push(res.data);
        this.$modal.msgSuccess("添加成功")
        this.addTableFormVisible = false;
      })
    },
    // 选择字符集事件
    changeCharacter(e) {
      this.characterSets.forEach(character => {
        if (character.characterSetName === e) {
          console.log(character)
          this.collations = character.collationNames
          this.dataBaseConfig.collationName = character.defaultCollateName
        }
      })
    },
    // 查看按钮
    handleWatchBtn(node, data){
      let current = this.page.current = 1;
      let size = this.page.size = 10;
      let configId = this.configId = node.parent.parent.data.configId;
      let dataBaseName = this.dataBaseName = node.parent.data.schemaName;
      let tableName = this.tableName = data.schemaName;
      // console.log(configId)
      // console.log(dataBaseName)
      // console.log(tableName)
      reqGetTableData(configId,dataBaseName,tableName,current,size).then(res=>{
        // console.log(res)
        let data = res.data;
        this.tableData = data.list;
        this.page.totalPage = data.pages;
        this.page.totalSize = data.total;
        let obj = data.list[0];
        if (obj !== undefined) {
          let names = Object.getOwnPropertyNames(obj);
          this.propertyNames = names.filter(name=>{
            return name !== "__ob__";
          })
          this.tableFields = this.propertyNames;
          this.tableDataVisible = true;
        }
      })
    },
    delTableData(obj,index) {
      this.$modal.confirm("确定删除第"+(index+1)+"行的数据吗？").then(()=>{
        let map = {};
        this.propertyNames.forEach(name => {
          map[name] = obj[name];
        })
        reqDelTableData(this.configId,this.dataBaseName,this.tableName,map).then(res=>{
          this.tableData.splice(index,1);
          this.$modal.msgSuccess("删除成功！")
        });
      })
    },
    editTableData(obj,index) {
      // console.log(obj)
      this.tableIndex = index
      this.preData = obj;
      this.propertyNames.forEach(name => {
        this.$set(this.tableFieldData,name+'',obj[name])
        // this.tableFieldData[name] = obj[name];
      })
      console.log(this.tableFieldData)
      this.tableFieldDataVisible = true;
    },
    handlePageChange(current) {
      // console.log(current)
      // console.log(this.dataBaseName)
      // console.log(this.tableName)
      reqGetTableData(this.configId,this.dataBaseName,this.tableName,current,this.page.size).then(res=>{
        let data = res.data;
        console.log(data)
        this.tableData = data.list;
      })
    },
    closeDialog() {

    },
    removeField(item) {
      var index = this.table.fields.indexOf(item)
      if (index !== -1) {
        this.table.fields.splice(index, 1)
      }
    },
    handleSubmitTableField() {
      let map = {
        oldMap: this.preData,
        newMap: this.tableFieldData
      };
      reqUpdateTableData(this.configId,this.dataBaseName,this.tableName,map).then(res=>{
        this.tableData[this.tableIndex] = this.tableFieldData;
        this.tableFieldDataVisible = false;
        console.log(res);
      })
    },
    addField() {
      this.table.fields.push({
        key: Date.now()
      });
    }
  }
}
</script>

<style >
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.input_dog{
  width: 140px;
  margin-left: 20px;
}
.input_dog2{
  width: 140px;
}
</style>
