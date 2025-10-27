<template>
  <div class="user-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="showAddDialog">添加用户</el-button>
        </div>
      </template>
      
      <el-table :data="users" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="phone" label="电话"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="roles" label="角色">
          <template #default="scope">
            <span v-if="scope.row.roles && scope.row.roles.length > 0">
              <el-tag v-for="role in scope.row.roles" :key="role.id" style="margin-right: 5px;">
                {{ role.roleName }}
              </el-tag>
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="editUser(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteUser(scope.row.id)">删除</el-button>
            <el-button type="info" size="small" @click="assignRoles(scope.row)">分配角色</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑用户对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="resetForm"
    >
      <el-form :model="formData" :rules="rules" ref="userForm" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item v-if="!formData.id" label="密码" prop="password">
          <el-input v-model="formData.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入电话"></el-input>
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

    <!-- 分配角色对话框 -->
    <el-dialog
      v-model="roleDialogVisible"
      title="分配角色"
      width="600px"
    >
      <el-form>
        <el-form-item label="用户名称">
          <el-input v-model="selectedUser.name" disabled></el-input>
        </el-form-item>
        <el-form-item label="选择角色">
          <el-checkbox-group v-model="selectedRoleIds">
            <el-checkbox v-for="role in roles" :key="role.id" :label="role.id">
              {{ role.roleName }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRoleAssignment">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { userService } from '../services/userService'
import { roleService } from '../services/roleService'

export default {
  name: 'UserPage',
  data() {
    return {
      users: [],
      roles: [],
      dialogVisible: false,
      roleDialogVisible: false,
      dialogTitle: '添加用户',
      formData: {
        id: null,
        username: '',
        name: '',
        password: '',
        email: '',
        phone: '',
        status: 1
      },
      selectedUser: {},
      selectedRoleIds: [],
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度至少为6位', trigger: 'blur' }
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ]
      }
    }
  },
  created() {
    this.loadUsers()
    this.loadRoles()
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
    
    // 加载角色列表
    async loadRoles() {
      try {
        this.roles = await roleService.getRoleList()
      } catch (error) {
        this.$message.error('获取角色列表失败')
      }
    },
    
    // 显示添加对话框
    showAddDialog() {
      this.dialogTitle = '添加用户'
      this.resetForm()
      this.dialogVisible = true
    },
    
    // 编辑用户
    editUser(user) {
      this.dialogTitle = '编辑用户'
      this.formData = { ...user }
      this.dialogVisible = true
    },
    
    // 删除用户
    async deleteUser(id) {
      try {
        await this.$confirm('确定要删除这个用户吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await userService.deleteUser(id)
        this.$message.success('删除成功')
        this.loadUsers()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },
    
    // 分配角色
    assignRoles(user) {
      this.selectedUser = { ...user }
      this.selectedRoleIds = user.roles ? user.roles.map(role => role.id) : []
      this.roleDialogVisible = true
    },
    
    // 提交角色分配
    async submitRoleAssignment() {
      try {
        await userService.updateUserRoles(this.selectedUser.id, this.selectedRoleIds)
        this.$message.success('角色分配成功')
        this.roleDialogVisible = false
        this.loadUsers()
      } catch (error) {
        this.$message.error('角色分配失败')
      }
    },
    
    // 重置表单
    resetForm() {
      this.formData = {
        id: null,
        username: '',
        name: '',
        password: '',
        email: '',
        phone: '',
        status: 1
      }
      if (this.$refs.userForm) {
        this.$refs.userForm.resetFields()
      }
    },
    
    // 提交表单
    async submitForm() {
      try {
        await this.$refs.userForm.validate()
        
        if (this.formData.id) {
          await userService.updateUser(this.formData)
          this.$message.success('更新成功')
        } else {
          // 检查用户名是否已存在
          const checkResult = await userService.checkUsername(this.formData.username)
          if (checkResult.exists) {
            this.$message.error('用户名已存在')
            return
          }
          await userService.addUser(this.formData)
          this.$message.success('添加成功')
        }
        
        this.dialogVisible = false
        this.loadUsers()
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
.user-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>