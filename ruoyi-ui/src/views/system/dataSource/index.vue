<template>
  <div v-loading="loading">
    <div v-if="databases.length === 0">
      你还没有添加数据库连接！<el-button @click="dialogVisible = true">添加连接</el-button>

    </div>

    <div v-else>
      <el-button @click="dialogVisible = true">添加连接</el-button>
      <el-tree
        :data="databases"
        :expand-on-click-node="false"
        :props="defaultProps"
        @node-click="handleNodeClick"
        :render-content="renderContent"
      ></el-tree>
    </div>
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
          <el-button @click="testConn">测试连接</el-button>
          <el-button type="primary" @click="connDatabase">连接</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>

import request from "../../../utils/request";

export default {
  name: "index",
  created() {
    request({
      url: "/system/dataSource/history",
      method:"get"
    }).then(res=>{
      console.log(res)
      this.databases = res.data;
      this.loading = false;
    })
    request({
      url:"/system/database/all",
      method:"get"
    }).then(res=>{
      console.log(res)
      this.databaseTypes = res.data;
    })
  },
  data() {
    return {
      loading: true,
      databases:[],
      databaseTypes:[],
      dialogVisible: false,
      dataSourceConfig: {
        schemaName:'',
        url:"localhost",
        username:"root",
        password:"qwer"
      },
      defaultProps: {
        label:'schemaName',
        children:'children'
      }
    }
  },
  methods: {
    testConn() {
      let dataSourceConfig = this.dataSourceConfig;
      request({
        url:"/system/dataSource/test",
        method: "post",
        data: dataSourceConfig
      }).then(res=>{
        this.$modal.msgSuccess("成功连接！")
      })
    },
    connDatabase() {
      let dataSourceVo = this.dataSourceConfig;
      request({
        url:"/system/dataSource/databases",
        method: "post",
        data: dataSourceVo
      }).then(res=>{
        console.log(res)
        let databaseRes = res.data;
        this.databases.push(databaseRes)
        this.dialogVisible = false;
      })
    },
    renderContent(h, { node, data, store }) {
      return (
        <span class="custom-tree-node">
            <span>{node.label}</span>
            <span>
              <el-button size="mini" icon="el-icon-plus" type="text" on-click={ () => this.append(data) }>添加</el-button>
              <el-button size="mini" icon="el-icon-delete" type="text" on-click={ () => this.remove(node, data) }>删除</el-button>
              <el-button size="mini" icon="el-icon-s-data" type="text" on-click={ () => this.edit(node, data) }>属性</el-button>
            </span>
          </span>);
    },
    deleteNode(node,data) {
      const parent = node.parent;
      const children = parent.data.children || parent.data;
      const index = children.findIndex(d => d.id === data.id);
      children.splice(index, 1);
    },
    remove(node, config) {
      console.log(node)
      let configId = config.configId;
      console.log(configId)
      request({
        url:"/system/dataSource/"+configId,
        method:"delete",
      }).then(res=>{
        this.deleteNode(node,config)
        this.$modal.msgSuccess("删除成功！");
      })
    },
    edit(node, data) {
      // 数据源配置
      if (node.level === 1) {
        this.dataSourceConfig.schemaName = data.schemaName;
        this.dataSourceConfig.url = data.url;
        this.dataSourceConfig.password = data.password;
        this.dataSourceConfig.username = data.username;
        this.dialogVisible = true;
      } else if(node.level === 2) {
        // 数据库配置
      } else {
        // 表的配置
      }
    },
    closeDialog() {

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
</style>
