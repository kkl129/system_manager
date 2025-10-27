# 权限管理系统

一个基于前后端分离架构的权限管理系统，支持用户管理、角色管理、资源管理和权限验证功能。

## 技术栈

- **后端**: Java + Spring Boot + Spring Data JPA + MySQL
- **前端**: Vue 3 + Element Plus + Vue Router + Axios

## 系统功能

1. **用户管理**: 添加、编辑、删除系统管理人员，支持用户角色分配
2. **角色管理**: 创建、编辑、删除角色，为角色分配资源权限
3. **资源管理**: 添加、编辑、删除系统资源（菜单、按钮、API）
4. **权限验证**: 验证用户对特定资源的访问权限

## 项目结构

```
system_manager/
├── backend/           # 后端Java项目
│   ├── src/main/java/com/system/manager/
│   │   ├── controller/    # 控制器
│   │   ├── service/       # 服务层
│   │   ├── dao/           # 数据访问层
│   │   ├── entity/        # 实体类
│   │   ├── config/        # 配置类
│   │   └── util/          # 工具类
│   ├── src/main/resources/
│   │   └── application.properties  # 配置文件
│   └── pom.xml           # Maven配置
├── frontend/          # 前端Vue项目
│   ├── src/
│   │   ├── components/    # 组件
│   │   ├── pages/         # 页面
│   │   ├── services/      # API服务
│   │   ├── utils/         # 工具函数
│   │   ├── assets/        # 静态资源
│   │   ├── App.vue        # 根组件
│   │   ├── main.js        # 入口文件
│   │   └── router.js      # 路由配置
│   ├── index.html         # HTML模板
│   ├── package.json       # NPM配置
│   └── vite.config.js     # Vite配置
└── README.md          # 项目说明
```

## 运行说明

### 后端运行

1. 确保已安装 JDK 1.8+ 和 Maven
2. 创建 MySQL 数据库 `system_manager`
3. 修改 `backend/src/main/resources/application.properties` 中的数据库连接配置
4. 运行后端应用：
   ```bash
   cd backend
   mvn spring-boot:run
   ```

### 前端运行

1. 确保已安装 Node.js 16+
2. 安装依赖：
   ```bash
   cd frontend
   npm install
   ```
3. 运行前端应用：
   ```bash
   npm run dev
   ```
4. 访问 http://localhost:3000

## API接口说明

### 用户管理
- GET /api/user/list - 获取用户列表
- POST /api/user/add - 添加用户
- PUT /api/user/update - 更新用户
- DELETE /api/user/{id} - 删除用户
- PUT /api/user/{id}/roles - 更新用户角色

### 角色管理
- GET /api/role/list - 获取角色列表
- POST /api/role/add - 添加角色
- PUT /api/role/update - 更新角色
- DELETE /api/role/{id} - 删除角色
- PUT /api/role/{id}/resources - 更新角色资源权限

### 资源管理
- GET /api/resource/list - 获取资源列表
- POST /api/resource/add - 添加资源
- PUT /api/resource/update - 更新资源
- DELETE /api/resource/{id} - 删除资源
- GET /api/resource/user/{userId} - 获取用户拥有的资源
- POST /api/resource/check-permission - 检查用户权限

## 注意事项

1. 首次运行时，系统会自动创建数据库表结构
2. 建议先添加资源，然后创建角色并分配权限，最后创建用户并分配角色
3. 生产环境使用时，请修改数据库密码和JWT密钥等敏感信息