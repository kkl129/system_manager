<template>
  <div class="role-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="showAddDialog">添加角色</el-button>
        </div>
      </template>
      
      <el-table :data="roles" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="roleName" label="角色名称"></el-table-column>
        <el-table-column prop="roleDesc" label="角色描述"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="resources" label="资源数量">
          <template #default="scope">
            {{ scope.row.resources && scope.row.resources.length > 0 ? scope.row.resources.length : 0 }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="editRole(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteRole(scope.row.id)">删除</el-button>
            <el-button type="info" size="small" @click="assignResources(scope.row)">分配权限</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑角色对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="resetForm"
    >
      <el-form :model="formData" :rules="rules" ref="roleForm" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="formData.roleName" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="角色描述" prop="roleDesc">
          <el-input v-model="formData.roleDesc" placeholder="请输入角色描述"></el-input>
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

    <!-- 分配资源权限对话框 -->
    <el-dialog
      v-model="resourceDialogVisible"
      title="分配权限"
      width="800px"
    >
      <el-form>
        <el-form-item label="角色名称">
          <el-input v-model="selectedRole.roleName" disabled></el-input>
        </el-form-item>
        <el-form-item label="选择资源">
          <el-tree
            :data="resourceTreeData"
            show-checkbox
            node-key="id"
            :default-expanded-keys="expandedKeys"
            :default-checked-keys="selectedResourceIds"
            @check="handleCheckChange"
            check-strictly
          >
            <template #default="{ node, data }">
              <span class="custom-tree-node">
                <span>{{ node.label }}</span>
                <span v-if="data.resourceType !== 'menu'" style="margin-left: 10px; color: #909399;">
                  ({{ data.method }}) {{ data.url }}
                </span>
              </span>
            </template>
          </el-tree>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resourceDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitResourceAssignment">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { roleService } from '../services/roleService'
import { resourceService } from '../services/resourceService'

export default {
  name: 'RolePage',
  data() {
    return {
      roles: [],
      resources: [],
      resourceTreeData: [],
      dialogVisible: false,
      resourceDialogVisible: false,
      dialogTitle: '添加角色',
      formData: {
        id: null,
        roleName: '',
        roleDesc: '',
        status: 1
      },
      selectedRole: {},
      selectedResourceIds: [],
      expandedKeys: [],
      rules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadRoles()
    this.loadResources()
  },
  methods: {
    // 加载角色列表
    async loadRoles() {
      try {
        this.roles = await roleService.getRoleList()
      } catch (error) {
        this.$message.error('获取角色列表失败')
      }
    },
    
    // 加载资源列表
    async loadResources() {
      try {
        const resources = await resourceService.getResourceList()
        this.resources = resources
        this.generateResourceTree()
      } catch (error) {
        this.$message.error('获取资源列表失败')
      }
    },
    
    // 生成资源树
    generateResourceTree() {
      const treeData = []
      const resourceMap = new Map()
      
      // 先创建所有节点的映射
      this.resources.forEach(resource => {
        resourceMap.set(resource.id, {
          ...resource,
          label: resource.resourceName,
          children: []
        })
      })
      
      // 构建树结构
      this.resources.forEach(resource => {
        if (resource.parentId === 0 || resource.parentId === null) {
          treeData.push(resourceMap.get(resource.id))
          this.expandedKeys.push(resource.id) // 默认展开根节点
        } else {
          const parent = resourceMap.get(resource.parentId)
          if (parent) {
            parent.children.push(resourceMap.get(resource.id))
          }
        }
      })
      
      this.resourceTreeData = treeData
    },
    
    // 显示添加对话框
    showAddDialog() {
      this.dialogTitle = '添加角色'
      this.resetForm()
      this.dialogVisible = true
    },
    
    // 编辑角色
    editRole(role) {
      this.dialogTitle = '编辑角色'
      this.formData = { ...role }
      this.dialogVisible = true
    },
    
    // 删除角色
    async deleteRole(id) {
      try {
        await this.$confirm('确定要删除这个角色吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await roleService.deleteRole(id)
        this.$message.success('删除成功')
        this.loadRoles()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },
    
    // 分配资源权限
    assignResources(role) {
      this.selectedRole = { ...role }
      this.selectedResourceIds = role.resources ? role.resources.map(resource => resource.id) : []
      this.resourceDialogVisible = true
    },
    
    // 处理树节点勾选变化
    handleCheckChange() {
      // 这里可以添加额外的处理逻辑
    },
    
    // 提交资源权限分配
    async submitResourceAssignment() {
      try {
        await roleService.updateRoleResources(this.selectedRole.id, this.selectedResourceIds)
        this.$message.success('权限分配成功')
        this.resourceDialogVisible = false
        this.loadRoles()
      } catch (error) {
        this.$message.error('权限分配失败')
      }
    },
    
    // 重置表单
    resetForm() {
      this.formData = {
        id: null,
        roleName: '',
        roleDesc: '',
        status: 1
      }
      if (this.$refs.roleForm) {
        this.$refs.roleForm.resetFields()
      }
    },
    
    // 提交表单
    async submitForm() {
      try {
        await this.$refs.roleForm.validate()
        
        if (this.formData.id) {
          await roleService.updateRole(this.formData)
          this.$message.success('更新成功')
        } else {
          // 检查角色名是否已存在
          const checkResult = await roleService.checkRoleName(this.formData.roleName)
          if (checkResult.exists) {
            this.$message.error('角色名称已存在')
            return
          }
          await roleService.addRole(this.formData)
          this.$message.success('添加成功')
        }
        
        this.dialogVisible = false
        this.loadRoles()
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
.role-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.custom-tree-node {
  display: flex;
  flex-direction: column;
}
</style>