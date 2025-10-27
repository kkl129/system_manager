<template>
  <div class="app-container">
    <el-container class="main-container">
      <el-header class="app-header">
        <h1>权限管理系统</h1>
      </el-header>
      <el-container>
        <el-aside width="200px" class="app-aside">
          <el-menu
            :default-active="$route.path"
            class="el-menu-vertical"
            @select="handleMenuSelect"
          >
            <el-menu-item index="/user">
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="/role">
              <el-icon><Role /></el-icon>
              <span>角色管理</span>
            </el-menu-item>
            <el-menu-item index="/resource">
              <el-icon><Box /></el-icon>
              <span>资源管理</span>
            </el-menu-item>
            <el-menu-item index="/permission-check">
              <el-icon><Lock /></el-icon>
              <span>权限验证</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main class="app-main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { User, Role, Box, Lock } from '@element-plus/icons-vue'

export default {
  name: 'App',
  components: {
    User,
    Role,
    Box,
    Lock
  },
  methods: {
    handleMenuSelect(key) {
      this.$router.push(key)
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

#app {
  height: 100vh;
}

.app-container {
  height: 100%;
}

.main-container {
  height: 100%;
}

.app-header {
  background-color: #1890ff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
}

.app-aside {
  background-color: #545c64;
  height: calc(100vh - 60px);
}

.el-menu-vertical {
  background-color: #545c64;
  border-right: none;
}

.el-menu-vertical .el-menu-item {
  color: #ddd;
  height: 60px;
  line-height: 60px;
}

.el-menu-vertical .el-menu-item:hover {
  background-color: #409eff;
  color: white;
}

.el-menu-vertical .el-menu-item.is-active {
  background-color: #409eff;
  color: white;
}

.app-main {
  padding: 20px;
  overflow-y: auto;
}
</style>