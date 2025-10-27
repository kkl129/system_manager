import api from './api'

export const roleService = {
  // 获取角色列表
  getRoleList() {
    return api.get('/role/list')
  },
  
  // 添加角色
  addRole(role) {
    return api.post('/role/add', role)
  },
  
  // 更新角色
  updateRole(role) {
    return api.put('/role/update', role)
  },
  
  // 删除角色
  deleteRole(id) {
    return api.delete(`/role/${id}`)
  },
  
  // 根据ID获取角色
  getRoleById(id) {
    return api.get(`/role/${id}`)
  },
  
  // 更新角色资源权限
  updateRoleResources(roleId, resourceIds) {
    return api.put(`/role/${roleId}/resources`, resourceIds)
  },
  
  // 检查角色名是否存在
  checkRoleName(roleName) {
    return api.get(`/role/check-rolename/${roleName}`)
  }
}