import api from './api'

export const resourceService = {
  // 获取资源列表
  getResourceList() {
    return api.get('/resource/list')
  },
  
  // 添加资源
  addResource(resource) {
    return api.post('/resource/add', resource)
  },
  
  // 更新资源
  updateResource(resource) {
    return api.put('/resource/update', resource)
  },
  
  // 删除资源
  deleteResource(id) {
    return api.delete(`/resource/${id}`)
  },
  
  // 根据ID获取资源
  getResourceById(id) {
    return api.get(`/resource/${id}`)
  },
  
  // 根据用户ID获取资源
  getResourcesByUserId(userId) {
    return api.get(`/resource/user/${userId}`)
  },
  
  // 检查权限
  checkPermission(userId, url, method) {
    return api.post('/resource/check-permission', {
      userId,
      url,
      method
    })
  },
  
  // 检查资源标识是否存在
  checkResourceKey(resourceKey) {
    return api.get(`/resource/check-resourcekey/${resourceKey}`)
  }
}