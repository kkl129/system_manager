import api from './api'

export const userService = {
  // 获取用户列表
  getUserList() {
    return api.get('/user/list')
  },
  
  // 添加用户
  addUser(user) {
    return api.post('/user/add', user)
  },
  
  // 更新用户
  updateUser(user) {
    return api.put('/user/update', user)
  },
  
  // 删除用户
  deleteUser(id) {
    return api.delete(`/user/${id}`)
  },
  
  // 根据ID获取用户
  getUserById(id) {
    return api.get(`/user/${id}`)
  },
  
  // 更新用户角色
  updateUserRoles(userId, roleIds) {
    return api.put(`/user/${userId}/roles`, roleIds)
  },
  
  // 检查用户名是否存在
  checkUsername(username) {
    return api.get(`/user/check-username/${username}`)
  }
}