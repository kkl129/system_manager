<template>
  <div class="resource-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>资源管理</span>
          <el-button type="primary" @click="showAddDialog">添加资源</el-button>
        </div>
      </template>
      
      <el-table :data="resources" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="resourceName" label="资源名称"></el-table-column>
        <el-table-column prop="resourceKey" label="资源标识"></el-table-column>
        <el-table-column prop="resourceType" label="资源类型" width="100">
          <template #default="scope">
            <el-tag :type="getResourceTypeTagType(scope.row.resourceType)">
              {{ getResourceTypeName(scope.row.resourceType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="url" label="URL"></el-table-column>
        <el-table-column prop="method" label="方法" width="100">
          <template #default="scope">
            <el-tag :type="getMethodTagType(scope.row.method)">
              {{ scope.row.method }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="parentId" label="父资源ID" width="100"></el-table-column>
        <el-table-column prop="sort" label="排序" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="editResource(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteResource(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑资源对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="formData" :rules="rules" ref="resourceForm" label-width="120px">
        <el-form-item label="资源名称" prop="resourceName">
          <el-input v-model="formData.resourceName" placeholder="请输入资源名称"></el-input>
        </el-form-item>
        <el-form-item label="资源标识" prop="resourceKey">
          <el-input v-model="formData.resourceKey" placeholder="请输入资源标识"></el-input>
        </el-form-item>
        <el-form-item label="资源类型" prop="resourceType">
          <el-select v-model="formData.resourceType" placeholder="请选择资源类型">
            <el-option label="菜单" value="menu"></el-option>
            <el-option label="按钮" value="button"></el-option>
            <el-option label="API" value="api"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="URL" prop="url">
          <el-input v-model="formData.url" placeholder="请输入URL"></el-input>
        </el-form-item>
        <el-form-item label="请求方法" prop="method">
          <el-select v-model="formData.method" placeholder="请选择请求方法">
            <el-option label="GET" value="GET"></el-option>
            <el-option label="POST" value="POST"></el-option>
            <el-option label="PUT" value="PUT"></el-option>
            <el-option label="DELETE" value="DELETE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="父资源" prop="parentId">
          <el-select v-model="formData.parentId" placeholder="请选择父资源" allow-create filterable default-first-option>
            <el-option :value="0" label="根资源"></el-option>
            <el-option
              v-for="resource in parentResources"
              :key="resource.id"
              :label="resource.resourceName"
              :value="resource.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" :step="1"></el-input-number>
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="formData.icon" placeholder="请输入图标类名（可选）"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="formData.status" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { resourceService } from '../services/resourceService'

export default {
  name: 'ResourcePage',
  data() {
    return {
      resources: [],
      parentResources: [],
      dialogVisible: false,
      dialogTitle: '添加资源',
      formData: {
        id: null,
        resourceName: '',
        resourceKey: '',
        resourceType: 'menu',
        url: '',
        method: 'GET',
        parentId: 0,
        sort: 0,
        icon: '',
        status: 1
      },
      rules: {
        resourceName: [
          { required: true, message: '请输入资源名称', trigger: 'blur' }
        ],
        resourceKey: [
          { required: true, message: '请输入资源标识', trigger: 'blur' }
        ],
        resourceType: [
          { required: true, message: '请选择资源类型', trigger: 'change' }
        ],
        url: [
          { required: true, message: '请输入URL', trigger: 'blur' }
        ],
        method: [
          { required: true, message: '请选择请求方法', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.loadResources()
  },
  methods: {
    // 加载资源列表
    async loadResources() {
      try {
        this.resources = await resourceService.getResourceList()
        this.parentResources = this.resources.filter(r => r.resourceType === 'menu')
      } catch (error) {
        this.$message.error('获取资源列表失败')
      }
    },
    
    // 获取资源类型标签类型
    getResourceTypeTagType(type) {
      const typeMap = {
        'menu': 'success',
        'button': 'primary',
        'api': 'info'
      }
      return typeMap[type] || 'default'
    },
    
    // 获取资源类型名称
    getResourceTypeName(type) {
      const typeMap = {
        'menu': '菜单',
        'button': '按钮',
        'api': 'API'
      }
      return typeMap[type] || type
    },
    
    // 获取请求方法标签类型
    getMethodTagType(method) {
      const methodMap = {
        'GET': 'primary',
        'POST': 'success',
        'PUT': 'warning',
        'DELETE': 'danger'
      }
      return methodMap[method] || 'default'
    },
    
    // 显示添加对话框
    showAddDialog() {
      this.dialogTitle = '添加资源'
      this.resetForm()
      this.dialogVisible = true
    },
    
    // 编辑资源
    editResource(resource) {
      this.dialogTitle = '编辑资源'
      this.formData = { ...resource }
      this.dialogVisible = true
    },
    
    // 删除资源
    async deleteResource(id) {
      try {
        await this.$confirm('确定要删除这个资源吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await resourceService.deleteResource(id)
        this.$message.success('删除成功')
        this.loadResources()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },
    
    // 重置表单
    resetForm() {
      this.formData = {
        id: null,
        resourceName: '',
        resourceKey: '',
        resourceType: 'menu',
        url: '',
        method: 'GET',
        parentId: 0,
        sort: 0,
        icon: '',
        status: 1
      }
      if (this.$refs.resourceForm) {
        this.$refs.resourceForm.resetFields()
      }
    },
    
    // 提交表单
    async submitForm() {
      try {
        await this.$refs.resourceForm.validate()
        
        if (this.formData.id) {
          await resourceService.updateResource(this.formData)
          this.$message.success('更新成功')
        } else {
          // 检查资源标识是否已存在
          const checkResult = await resourceService.checkResourceKey(this.formData.resourceKey)
          if (checkResult.exists) {
            this.$message.error('资源标识已存在')
            return
          }
          await resourceService.addResource(this.formData)
          this.$message.success('添加成功')
        }
        
        this.dialogVisible = false
        this.loadResources()
      } catch (error) {
        if (error !== false) { // 排除表单验证失败的情况
          this.$message.error('操作失败')
        }
      }
    }
  }
}
</script>

<style scoped>
.resource-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>