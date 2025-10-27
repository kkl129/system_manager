<template>
  <div class="permission-check-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>权限验证</span>
        </div>
      </template>
      
      <el-form :model="formData" :rules="rules" ref="checkForm" label-width="120px">
        <el-form-item label="用户ID" prop="userId">
          <el-select v-model="formData.userId" placeholder="请选择用户" filterable>
            <el-option
              v-for="user in users"
              :key="user.id"
              :label="user.name + ' (' + user.username + ')"
              :value="user.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资源URL" prop="url">
          <el-input v-model="formData.url" placeholder="请输入要验证的资源URL"></el-input>
        </el-form-item>
        <el-form-item label="请求方法" prop="method">
          <el-select v-model="formData.method" placeholder="请选择请求方法">
            <el-option label="GET" value="GET"></el-option>
            <el-option label="POST" value="POST"></el-option>
            <el-option label="PUT" value="PUT"></el-option>
            <el-option label="DELETE" value="DELETE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="checkPermission">验证权限</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 验证结果展示 -->
      <div v-if="showResult" class="result-section">
        <h3>验证结果</h3>
        <el-card :type="permissionResult ? 'success' : 'danger'" class="result-card">
          <div class="result-content">
            <p><strong>用户：</strong>{{ getUserNameById(formData.userId) }}</p>
            <p><strong>资源：</strong>{{ formData.url }}</p>
            <p><strong>方法：</strong>{{ formData.method }}</p>
            <p><strong>是否有权限：</strong>
              <span :class="permissionResult ? 'has-permission' : 'no-permission'">
                {{ permissionResult ? '✅ 有权限' : '❌ 无权限' }}
              </span>
            </p>
          </div>
        </el-card>

        <!-- 用户拥有的所有权限 -->
        <div class="user-permissions">
          <h4>用户拥有的所有权限</h4>
          <el-table v-if="userResources.length > 0" :data="userResources" style="width: 100%">
            <el-table-column prop="resourceName" label="资源名称"></el-table-column>
            <el-table-column prop="url" label="URL"></el-table-column>
            <el-table-column prop="method" label="方法" width="100">
              <template #default="scope">
                <el-tag :type="getMethodTagType(scope.row.method)">
                  {{ scope.row.method }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="resourceType" label="资源类型" width="100">
              <template #default="scope">
                <el-tag :type="getResourceTypeTagType(scope.row.resourceType)">
                  {{ getResourceTypeName(scope.row.resourceType) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
          <div v-else class="no-permissions">
            该用户没有任何权限
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { userService } from '../services/userService'
import { resourceService } from '../services/resourceService'

export default {
  name: 'PermissionCheckPage',
  data() {
    return {
      users: [],
      userResources: [],
      formData: {
        userId: '',
        url: '',
        method: 'GET'
      },
      permissionResult: false,
      showResult: false,
      rules: {
        userId: [
          { required: true, message: '请选择用户', trigger: 'change' }
        ],
        url: [
          { required: true, message: '请输入资源URL', trigger: 'blur' }
        ],
        method: [
          { required: true, message: '请选择请求方法', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.loadUsers()
  },
  methods: {
    // 加载用户列表
    async loadUsers() {
      try {
        this.users = await userService.getUserList()
      } catch (error) {
        this.$message.error('获取用户列表失败')
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
    
    // 根据ID获取用户名称
    getUserNameById(userId) {
      const user = this.users.find(u => u.id === userId)
      return user ? user.name + ' (' + user.username + ')' : ''
    },
    
    // 验证权限
    async checkPermission() {
      try {
        await this.$refs.checkForm.validate()
        
        // 验证权限
        const result = await resourceService.checkPermission(
          this.formData.userId,
          this.formData.url,
          this.formData.method
        )
        
        this.permissionResult = result.hasPermission
        this.showResult = true
        
        // 获取用户所有权限
        await this.loadUserResources(this.formData.userId)
      } catch (error) {
        if (error !== false) { // 排除表单验证失败的情况
          this.$message.error('验证失败')
        }
      }
    },
    
    // 加载用户拥有的资源权限
    async loadUserResources(userId) {
      try {
        this.userResources = await resourceService.getResourcesByUserId(userId)
      } catch (error) {
        this.$message.error('获取用户权限失败')
      }
    },
    
    // 重置表单
    resetForm() {
      this.formData = {
        userId: '',
        url: '',
        method: 'GET'
      }
      this.permissionResult = false
      this.showResult = false
      this.userResources = []
      if (this.$refs.checkForm) {
        this.$refs.checkForm.resetFields()
      }
    }
  }
}
</script>

<style scoped>
.permission-check-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.result-section {
  margin-top: 30px;
}

.result-section h3 {
  margin-bottom: 20px;
  color: #333;
}

.result-card {
  margin-bottom: 30px;
}

.result-content p {
  margin: 10px 0;
  font-size: 16px;
}

.has-permission {
  color: #67c23a;
  font-weight: bold;
}

.no-permission {
  color: #f56c6c;
  font-weight: bold;
}

.user-permissions h4 {
  margin-bottom: 20px;
  color: #333;
}

.no-permissions {
  text-align: center;
  padding: 40px;
  color: #909399;
  background-color: #f5f7fa;
  border-radius: 4px;
}
</style>