# Intelligent-Approval-Management-System

智能审批管理系统

基于 `Vue3 + SpringBoot + Flowable + LangChain` 构建的智能化审批系统，实现审批流程管理、工单流转、AI 辅助审批等功能。

---

# 1. 项目简介

本项目是一个智能审批管理平台，采用前后端分离架构：

* 前端基于 Vue3 + Vite
* 后端基于 SpringBoot
* 工作流引擎采用 Flowable
* AI 能力采用 LangChain

系统主要实现：

* 用户登录认证
* 审批流程管理
* 工单流转
* AI 智能辅助审批
* 审批状态追踪
* 流程节点控制

---

# 2. 技术架构

## 前端技术栈

| 技术           | 说明      |
| ------------ | ------- |
| Vue3         | 前端框架    |
| Vite         | 构建工具    |
| Vue Router   | 路由管理    |
| Pinia        | 状态管理    |
| Axios        | HTTP 请求 |
| Element Plus | UI 组件库  |

---

## 后端技术栈

| 技术           | 说明     |
| ------------ | ------ |
| SpringBoot   | 后端框架   |
| MyBatis Plus | ORM 框架 |
| Flowable     | 工作流引擎  |
| MySQL        | 数据库    |
| JWT          | 用户认证   |

---

## AI 技术栈

| 技术                 | 说明      |
| ------------------ | ------- |
| LangChain          | AI 编排框架 |
| OpenAI API         | 大模型能力   |
| Prompt Engineering | 提示词工程   |

---

# 3. 系统架构

```text
用户
 │
 ▼
Vue3 前端
 │
 ▼
SpringBoot 后端
 │
 ├── Flowable 工作流
 │
 ├── MySQL 数据库
 │
 ▼
LangChain AI 服务
```

---

# 4. 核心功能模块

## 4.1 用户模块

* 用户登录
* 权限认证
* Token 管理
* 用户信息维护

---

## 4.2 审批流程模块

* 流程创建
* 流程审批
* 流程驳回
* 流程撤回
* 流程追踪

---

## 4.3 工单模块

* 工单创建
* 工单流转
* 工单状态管理
* 工单历史记录

---

## 4.4 AI 智能审批模块

* AI 自动分析审批内容
* 风险识别
* 智能建议
* 自动生成审批意见

---

# 5. 项目结构

```bash
前端
src
├── api                 # 请求 api
├── assets              # 静态资源
├── components          # 公共组件
├── router              # 路由管理
├── stores              # Pinia 状态管理
├── utils               # 工具类
├── views               # 页面
│   ├── home            # 首页
│   ├── layout          # 布局页
│   ├── login           # 登录页
│   └── work_order      # 工单模块
├── App.vue             # 根组件
└── main.js             # 项目入口
```

```bash
后端
backend-java
├── src.main
│   ├── java.com.iams
│   │   │
│   │   ├── annotations          # 自定义注解
│   │   │   └── AutoFilled.java
│   │   │
│   │   ├── aop                  # AOP切面
│   │   │   └── DbDateTimeAop.java
│   │   │
│   │   ├── config               # 系统配置
│   │   │   ├── FlowableConfig.java
│   │   │   ├── MybatisConfig.java
│   │   │   ├── RestTemplateConfig.java
│   │   │   ├── SecurityConfig.java
│   │   │   ├── SwaggerConfig.java
│   │   │   └── WebMvcConfig.java
│   │   │
│   │   ├── context              # 用户上下文
│   │   │   └── UserContext.java
│   │   │
│   │   ├── controller           # 控制层
│   │   ├── enums                # 枚举定义
│   │   ├── interceptors         # 请求拦截器
│   │   ├── json                 # Jackson配置
│   │   ├── mapper               # Mapper层
│   │   ├── params               # 请求参数对象
│   │   ├── pojo                 # 实体对象
│   │   ├── service              # 业务逻辑层
│   │   ├── utils                # 工具类
│   │   ├── vo                   # 返回对象
│   │   │
│   │   └── IamsApplication.java
│   │
│   └── resources
│
└── pom.xml
```

```bash
ai服务端
app
├── agents                  # Agent 智能体
│   └── default_agent.py
│
├── api                     # 接口层
│   └── v1
│       └── chat_api.py
│
├── config                  # 配置文件
│   └── config.yaml
│
├── context                 # 上下文管理
│   └── context_handler.py
│
├── core                    # 核心模块
│   └── security.py
│
├── middleware              # 中间件
│   └── agent_middleware.py
│
├── resources               # 资源文件
│   ├── prompt              # Prompt 模板
│   │   └── default_sys_prompt.txt
│   └── schema
│
├── schemas                 # 数据模型
│   └── chat_param.py
│
├── tools                   # Agent 工具
│   └── agent_tool.py
│
├── utils                   # 工具类
│   ├── beanFactory_handler.py
│   ├── configfile_handler.py
│   ├── logger_handler.py
│   ├── path_handler.py
│   └── prompt_handler.py
│
├── main.py                 # 项目启动入口
│
├── .env                    # 环境变量
├── pyproject.toml          # Python 项目配置
├── uv.lock                 # uv 依赖锁
└── README.md
```

---

# 6. 工作流说明

系统基于 Flowable 实现审批流：

```text
发起申请
   │
   ▼
部门审批
   │
   ▼
领导审批
   │
   ▼
AI 风险分析
   │
   ▼
审批完成
```

---

# 7. AI 功能说明

系统接入 LangChain：

* 对审批内容进行语义分析
* 自动识别高风险申请
* 生成审批建议
* 提高审批效率

---

# 8. 环境要求

## 前端

```bash
Node.js >= 18
pnpm >= 8
```

## 后端

```bash
JDK = 11
MySQL = 8
```

---

# 9. 项目启动

## 前端启动

```bash
pnpm install

pnpm dev
```

## 后端启动

```bash
mvn clean install

mvn spring-boot:run
```

## ai服务端启动

```bash
uv run uvicorn app.main:app --port 8000
```

---

# 10. 后续优化方向

* RBAC 权限系统
* BPMN 可视化流程设计
* AI 自动审批
* 审批数据分析
* 多租户支持
* 消息通知系统

---

# 11. 项目亮点

* 前后端分离
* 工作流引擎集成
* AI 智能审批
* 模块化设计
* 可扩展性强



