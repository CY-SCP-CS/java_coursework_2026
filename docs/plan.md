# AI-Assisted Honor of Kings Information Management System — 实现文档

> **课程**: Java OOP | 总分: 20 分
> **核心**: Java 面向对象设计 + 负责任 AI 使用 + Git 过程证据
> **本文档面向**: 学生本人 + AI 辅助开发

---

## 目录

1. [项目目标](#1-项目目标)
2. [需求分析](#2-需求分析)
3. [Java 设计概念要求](#3-java-设计概念要求)
4. [类设计](#4-类设计)
5. [UML 类图](#5-uml-类图)
6. [初始数据集](#6-初始数据集)
7. [功能需求详解](#7-功能需求详解)
8. [AI 使用要求与证据](#8-ai-使用要求与证据)
9. [Git 过程要求](#9-git-过程要求)
10. [测试要求](#10-测试要求)
11. [提交要求与目录结构](#11-提交要求与目录结构)
12. [额外加分功能](#12-额外加分功能)
13. [评分标准详解](#13-评分标准详解)
14. [分数等级划分](#14-分数等级划分)
15. [最低通过清单](#15-最低通过清单)
16. [开发时间线](#16-开发时间线)
17. [Prompt 质量指南](#17-prompt-质量指南)

---

## 1. 项目目标

### 1.1 系统概述

构建一个 **王者荣耀 (Honor of Kings) 信息管理系统**，使用 Java 控制台应用程序，管理玩家、英雄、装备、战队和比赛记录。系统支持搜索、报告、数据管理、身份认证、排行榜等功能。

### 1.2 目标用户

| 角色 | 权限描述 |
|------|---------|
| **Admin (管理员)** | 可创建、修改、删除所有数据（玩家、英雄、装备、战队、比赛记录） |
| **Player (玩家)** | 可查看个人信息、编辑有限个人信息、查看自己的英雄和比赛历史、查看公共信息 |

### 1.3 核心目标

- 展示 Java OOP 能力（继承、接口、集合、封装、多态、异常处理、文件 I/O、枚举）
- 展示负责任 AI 使用能力（记录 prompts、多 agent 协作、验证 AI 输出、反思）
- 展示 Git 过程管理能力（有意义的多次提交、分类前缀）

---

## 2. 需求分析

### 2.1 功能需求总览

| # | 功能 | 优先级 | 说明 |
|---|------|--------|------|
| F1 | 玩家查询 (Player Lookup) | 必须 | 按 ID 或名称搜索玩家，显示详细信息 |
| F2 | 战队概览 (Team Overview) | 必须 | 按 ID 或名称搜索战队，显示成员及统计 |
| F3 | 英雄详情 (Hero Details) | 必须 | 按名称搜索英雄，显示属性与兼容装备 |
| F4 | 装备统计 (Equipment Statistics) | 必须 | 按至少一个指标排名装备 |
| F5 | 比赛历史 (Match History) | 必须 | 查询玩家或战队最近 N 场比赛 |
| F6 | 排行榜 (Leaderboard) | 必须 | 按胜率/等级/场次/自定义分数排名 |
| F7 | 数据管理 (Data Management) | 必须 | Admin 增删改数据，Player 有限编辑 |
| F8 | 身份认证 (Authentication) | 必须 | 登录/登出系统，Admin/Player 双角色 |

### 2.2 实现策略

- 采用 **控制台菜单驱动** 界面
- 使用 `Scanner` 处理用户输入
- 菜单循环：显示选项 → 获取输入 → 执行功能 → 返回菜单
- 按功能模块划分 service 类，避免所有逻辑堆在 Main 中

---

## 3. Java 设计概念要求

### 3.1 必须展示的概念

| 概念 | 预期证据 | 评分要点 |
|------|---------|---------|
| **继承 (Inheritance)** | `Player` 和 `Admin` 继承 `Person` | 父类应为抽象类 |
| **关联 (Association)** | `Player` 拥有多个 `Hero` 对象；`Hero` 可使用多个 `Equipment` 对象 | 双向/单向关联均可 |
| **聚合/组合 (Aggregation/Composition)** | `Team` 包含多个 `Player` 对象 | 理解生命周期差异 |
| **接口 (Interface)** | 至少一个有意义接口，如 `Searchable`、`Reportable`、`Persistable`、`Authenticatable` | 接口方法要有实际用途 |
| **封装 (Encapsulation)** | 字段为 `private`，通过 getter/setter 控制访问 | 不要直接用 `public` 字段 |
| **多态 (Polymorphism)** | 使用父类或接口引用，如将用户存为 `Person` 类型 | 在集合或方法参数中体现 |
| **集合 (Collections)** | `ArrayList`、`HashMap`、`HashSet`、`TreeMap` 等 | 选择合适的数据结构 |
| **异常处理 (Exception Handling)** | 处理无效输入、找不到记录、重复 ID、文件加载错误 | 不要只 `catch` 不处理 |
| **文件 I/O (File I/O)** | 从文本/CSV/JSON 文件保存和加载数据 | 格式需在文档中说明 |
| **枚举 (Enums)** | `HeroType`、`MatchResult`、`Role`、`EquipmentType` 等 | 替代魔法字符串 |

### 3.2 评分红线

> **⚠️ 重要**: 将所有逻辑写在单个大 `Main` 类中的程序将获得**低 Java 设计分**，即使菜单功能正常。

---

## 4. 类设计

### 4.1 模型类 (Model)

#### `Person` (抽象类)
```
字段: id (String), name (String), role (Role枚举)
方法: 抽象方法 getDescription()
```

#### `Player extends Person`
```
字段: teamId (String), level (int), winRate (double), ownedHeroes (List<Hero>)
关联: Player -> Hero (一对多)
```

#### `Admin extends Person`
```
字段: adminLevel (int), permissions (List<String>)
```

#### `Hero`
```
字段: heroId, name, heroType (HeroType枚举), baseStats (Map<String, Integer>)
关联: Hero -> Equipment (多对多，通过兼容性列表)
```

#### `Equipment`
```
字段: equipmentId, name, equipmentType (EquipmentType枚举), stats (Map<String, Integer>), usageCount
```

#### `Team`
```
字段: teamId, name, members (List<Player>)
聚合: Team -> Player
方法: getAverageLevel(), getTotalMatches(), getWinRate(), getTopPlayer()
```

#### `MatchRecord`
```
字段: matchId, date, teamA, teamB, result (MatchResult枚举), heroPicks (List<String>)
```

### 4.2 服务类 (Service)

| 类名 | 职责 |
|------|------|
| `GameDataManager` | 管理所有数据的增删改查，协调各数据源 |
| `AuthenticationService` | 处理登录/登出，管理当前会话用户 |
| `SearchService` | 实现各种搜索功能（玩家、战队、英雄） |
| `RankingService` | 排行榜排序逻辑，定义排名公式 |
| `FileStorageService` | 从文件保存/加载数据 |

### 4.3 工具类 (Util)

| 类名 | 职责 |
|------|------|
| `InputHelper` | 封装用户输入获取，提供带验证的输入方法 |
| `DataInitializer` | 初始化硬编码数据集（供开发和测试使用） |

### 4.4 额外可选类

`Skin`、`Rune`、`Skill`、`CombatSimulator`、`RecommendationEngine`

---

## 5. UML 类图

```
┌─────────────────────────────────────────────────────────────┐
│                         <<interface>>                       │
│                          Searchable                         │
├─────────────────────────────────────────────────────────────┤
│ + searchById(id: String): List<T>                          │
│ + searchByName(name: String): List<T>                      │
└─────────────────────────────────────────────────────────────┘
                              ▲
                              │ implements
                              │
┌─────────────────────────────────────────────────────────────┐
│                      Person (abstract)                      │
├─────────────────────────────────────────────────────────────┤
│ # id: String                                                │
│ # name: String                                              │
│ # role: Role                                                │
├─────────────────────────────────────────────────────────────┤
│ + getDescription(): abstract String                         │
└─────────────────────────────────────────────────────────────┘
          ▲                    ▲
          │                    │
┌──────────────────┐  ┌──────────────────┐
│     Player       │  │      Admin       │
├──────────────────┤  ├──────────────────┤
│ - teamId: String │  │ - adminLevel: int│
│ - level: int     │  │ - permissions    │
│ - winRate: double│  └──────────────────┘
│ - ownedHeroes    │
└──────────────────┘
         │ owns *
         ▼
┌──────────────────┐  uses *  ┌──────────────────┐
│      Hero        │◄────────►│    Equipment     │
├──────────────────┤          ├──────────────────┤
│ - heroId: String │          │ - equipmentId    │
│ - name: String   │          │ - name: String   │
│ - heroType: HeroType│       │ - equipmentType  │
│ - baseStats      │          │ - stats: Map     │
└──────────────────┘          │ - usageCount     │
                              └──────────────────┘

┌──────────────────┐
│      Team        │ contains * ──► Player
├──────────────────┤
│ - teamId: String │
│ - name: String   │
│ - members: List  │
└──────────────────┘

┌──────────────────┐
│   MatchRecord    │
├──────────────────┤
│ - matchId: String│
│ - date: LocalDate│
│ - teamA: String  │
│ - teamB: String  │
│ - result: MatchResult│
│ - heroPicks      │
└──────────────────┘
```

---

## 6. 初始数据集

### 6.1 最小数据要求

| 数据类型 | 最低数量 | 说明 |
|---------|---------|------|
| 战队 (Teams) | **3** 支 | 每队至少 5 名队员 |
| 玩家 (Players) | **10** 名 | 每人至少拥有 3 个英雄 |
| 英雄 (Heroes) | **15** 个 | 每个英雄至少可装备 2 件装备 |
| 装备 (Equipment) | **20** 件 | — |
| 比赛记录 (Match Records) | **10** 条 | — |

### 6.2 数据存储

- 开发初期：硬编码在 `DataInitializer` 类中
- 最终版本：通过 `FileStorageService` 从文件保存/加载
- 建议格式：CSV 或自定义文本格式

---

## 7. 功能需求详解

### 7.1 F1: 玩家查询 (Player Lookup)

**操作**: 按 ID 或名称搜索玩家

**显示内容**:
- 玩家 ID 和名称
- 所属战队
- 等级和胜率
- 拥有的英雄列表
- 每个英雄的已装备物品

**异常处理**: 找不到玩家时给出明确提示

### 7.2 F2: 战队概览 (Team Overview)

**操作**: 按 ID 或名称搜索战队

**显示内容**:
- 战队名称
- 所有成员列表
- 平均等级
- 总比赛场次
- 胜率
- 战队最佳队员

### 7.3 F3: 英雄详情 (Hero Details)

**操作**: 按名称搜索英雄

**显示内容**:
- 英雄名称和类型
- 基础属性
- 可用/兼容装备
- 拥有该英雄的玩家
- （可选）推荐装备

### 7.4 F4: 装备统计 (Equipment Statistics)

**操作**: 按至少一个指标排名装备

**可选指标**:
- 使用次数 (usageCount)
- 平均评分 (averageRating)
- 使用该装备的英雄数量
- 胜率贡献 (winRateContribution)
- 自定义分数

> **评分要求**: 必须在文档中**解释排名公式**。

### 7.5 F5: 比赛历史 (Match History)

**操作**: 查询玩家或战队最近 N 场比赛

**显示内容**:
- 对手
- 日期
- 结果 (胜/负)
- 使用的英雄
- 胜/负记录
- 英雄选用率

### 7.6 F6: 排行榜 (Leaderboard)

**操作**: 显示前 X 名玩家

**排名依据**:
- 胜率 (win rate)
- 等级 (level)
- 比赛场次 (number of matches)
- 自定义分数

> **评分要求**: 必须说明**平局处理方式**。

### 7.7 F7: 数据管理 (Data Management)

**Admin 权限**:
- 添加、删除、编辑玩家
- 添加、删除、编辑英雄
- 添加、删除、编辑装备
- 添加、删除、编辑战队
- 添加、删除、编辑比赛记录

**Player 权限**:
- 查看自己的信息
- 编辑有限的个人信息
- 查看自己的英雄和比赛历史
- 查看公共信息（英雄、战队、排行榜）

### 7.8 F8: 身份认证 (Authentication)

**功能**:
- 登录 (输入 ID/用户名 + 密码)
- 登出
- 会话管理（记录当前登录用户）

**角色**:
- `Admin`: 所有数据管理权限
- `Player`: 查看和编辑有限信息

**默认账号**:
| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | Admin |
| player1 | pass123 | Player |
| ... | ... | Player |

---

## 8. AI 使用要求与证据

### 8.1 核心原则

> **AI 辅助是允许的。隐藏 AI 辅助是不允许的。**
> 学生因有用的 prompts、可见的决策过程、良好的 Git 证据、准确的反思和可验证的 Java 理解而获得分数。

### 8.2 AI 文件夹结构

```
ai/
├── prompts.md        # 所有重要 prompts 的记录
├── agent-log.md      # 多 Agent 协作证据
└── reflection.md     # 最终反思
```

### 8.3 prompt.md 记录格式

每条 prompt 记录必须包含：

| 字段 | 说明 |
|------|------|
| **日期时间** | 如 `2026-05-12 21:30` |
| **AI 工具/模型** | 如 `ChatGPT / GPT-5.5`、`Claude 4` |
| **Agent 角色** | 如 `Java Architect`、`Implementation Agent` |
| **相关提交** | 关联的 Git commit hash |
| **我的 Prompt** | 实际的 prompt 原文 |
| **AI 响应摘要** | AI 响应的简要总结 |
| **我的决定** | 接受/修改/拒绝，并解释原因 |

**示例**:
```markdown
## Prompt 03

Time: 2026-05-12 21:30
Tool/Model: ChatGPT / GPT-5.5
Agent Role: Java Architect
Related Commit: a13f91c

### My Prompt
I am designing a Java OOP coursework project for an Honor of Kings
information system. Please suggest a class structure using inheritance,
interfaces, and collections. Do not write full code yet.

### AI Response Summary
The AI suggested Person, Player, Admin, Hero, Equipment, Team, and
MatchRecord classes with Searchable and Persistable interfaces.

### My Decision
Accepted general structure but renamed Match to MatchRecord.
```

### 8.4 agent-log.md 多 Agent 证据

**必须包含至少 3 种 Agent 角色**:

| Agent 角色 | 职责 | 最少关联提交数 |
|-----------|------|--------------|
| **Architect Agent** | 类设计、UML 建议、模块规划 | 3 次 |
| **Implementation Agent** | 实现特定 Java 方法或类 | 3 次 |
| **Testing/Reviewer Agent** | 找 bug、写测试用例、代码审查 | 2 次 |

**可选额外 Agent**:
- Refactoring Agent (重构)
- Documentation Agent (文档)
- Security Agent (安全)
- Performance Agent (性能)

**agent-log.md 格式**:
```markdown
## Architect Agent

Main contribution:
Suggested the initial OOP structure and relationships.

Human decision:
I accepted the abstract Person class but rejected GameAccount class
because it was unnecessary for the required functions.

Related commits:
- 1a2b3c4 initial class design
- 2b3c4d5 add Person, Player, Admin

## Testing Agent

Main contribution:
Found that deleting a Hero did not remove it from Player-owned hero lists.

Human decision:
I fixed the bug manually by updating deleteHero in GameDataManager.

Related commits:
- 9f8e7d6 fix hero deletion consistency
```

### 8.5 reflection.md 反思问题

必须回答以下 **10 个问题**:

1. 你使用了哪些 AI 工具或模型？
2. 哪个 prompt 最有用？为什么？
3. 哪个 AI 生成的建议是错误的、不完整的或有误导性的？
4. 你如何检查 AI 生成的代码是否正确？
5. 你自己修复了哪些 bug 而没有让 AI 修复？
6. 使用 AI 后你更好地理解了哪个 Java 概念？
7. 你对哪个 Java 概念仍然不确定？
8. AI 让项目变得更容易、更难，还是两者兼有？请解释。
9. 最终项目中哪些部分主要是你自己写的？
10. 哪些部分主要由 AI 生成或大量辅助？

### 8.6 Prompt 质量标准

| 维度 | 弱 Prompt (不要这样) | 强 Prompt (推荐) |
|------|---------------------|-----------------|
| **设计** | "Write my Java project." | "Suggest a class design for a Java OOP Honor of Kings system. Include inheritance, interfaces, collections. Do not write full code." |
| **实现** | "Code this." | "Implement only the player lookup method using existing Player, Hero, Team classes. Explain assumptions and edge cases." |
| **调试** | "Fix my code." | "The following method crashes when searching an unknown hero. Identify the cause and suggest a minimal fix. Do not rewrite unrelated code." |
| **审查** | "Is this good?" | "Review this Java class for OOP design, encapsulation, collection usage, and null pointer bugs. Give specific comments." |

---

## 9. Git 过程要求

### 9.1 最低 Git 要求

| 要求 | 数量 |
|------|------|
| **有意义的提交总数** | 至少 **12** 次 |
| **明确由人类编写的提交**（规划/调试/重构） | 至少 **4** 次 |
| **关联 Architect Agent 的提交** | 至少 **3** 次 |
| **关联 Implementation Agent 的提交** | 至少 **3** 次 |
| **关联 Testing/Reviewer Agent 的提交** | 至少 **2** 次 |

> **注意**: 仅有一次最终提交是不可接受的。

### 9.2 提交前缀

| 前缀 | 使用场景 |
|------|---------|
| `[Human]` | 人类编写的规划、手动修改、重构 |
| `[AI-Architect]` | Architect Agent 辅助的设计 |
| `[AI-Implementation]` | Implementation Agent 辅助的实现 |
| `[AI-Review]` | Testing/Reviewer Agent 的审查结果 |
| `[AI-Refactor]` | AI 辅助的重构 |
| `[Docs]` | 文档更新 |
| `[Test]` | 测试相关 |
| `[Fix]` | Bug 修复 |

### 9.3 示例提交序列

```
[Human] create initial project structure and README
[AI-Architect] draft OOP class structure for Person Hero Team
[Human] revise class design based on UML feedback
[AI-Implementation] implement player lookup menu
[Test] add manual test cases for team overview
[AI-Review] review data deletion consistency
[Fix] fix hero deletion bug found during review
[Docs] update plan.md and reflection.md
```

### 9.4 必须提交的文件

将 `git log --oneline --graph --decorate --all` 的输出保存到 **`git-history.txt`**

---

## 10. 测试要求

### 10.1 测试文件

`docs/test-cases.md`

### 10.2 最低要求

至少 **10 个测试用例**。

### 10.3 每条测试用例格式

| 字段 | 说明 |
|------|------|
| **Test ID** | 如 `TC-001` |
| **测试功能** | 哪个功能 |
| **输入** | 具体输入值 |
| **期望输出** | 系统应该显示什么 |
| **实际输出** | 运行后实际结果 |
| **通过/失败** | Pass / Fail |
| **发现的 Bug** | 如果有 |

### 10.4 示例测试用例

```markdown
## TC-004: Player Lookup by Name

Input:
Search player name: "Li Bai"

Expected:
The system displays Li Bai's team, level, owned heroes, and equipment.

Actual:
The system displayed player information correctly.

Result:
Pass
```

### 10.5 JUnit 加分

有意义的 JUnit 测试可获得**额外加分**。

### 10.6 测试覆盖建议

| 功能 | 最少测试数 | 测试要点 |
|------|-----------|---------|
| 玩家查询 | 2 | 存在/不存在的玩家 |
| 战队概览 | 1 | 正常显示战队信息 |
| 英雄详情 | 1 | 正常显示英雄信息 |
| 装备统计 | 1 | 排名是否正确 |
| 比赛历史 | 1 | N 场比赛的过滤 |
| 排行榜 | 1 | 排序是否正确 |
| 登录认证 | 2 | 成功/失败登录 |
| 数据管理 | 1 | Admin 添加玩家 |
| 异常输入 | 2 | 无效 ID、空输入 |
| 文件 I/O | 1 | 数据加载验证 |

---

## 11. 提交要求与目录结构

### 11.1 最终目录结构

```
project-root/
├── src/
│   ├── Main.java
│   ├── model/
│   │   ├── Person.java
│   │   ├── Player.java
│   │   ├── Admin.java
│   │   ├── Hero.java
│   │   ├── Equipment.java
│   │   ├── Team.java
│   │   └── MatchRecord.java
│   ├── service/
│   │   ├── GameDataManager.java
│   │   ├── AuthenticationService.java
│   │   ├── SearchService.java
│   │   ├── RankingService.java
│   │   └── FileStorageService.java
│   └── util/
│       ├── InputHelper.java
│       └── DataInitializer.java
├── docs/
│   ├── plan.md           ← 本文档
│   ├── design.md          ← 设计文档
│   ├── uml.png            ← UML 类图图片
│   └── test-cases.md      ← 测试用例文档
├── ai/
│   ├── prompts.md         ← Prompt 记录
│   ├── agent-log.md       ← 多 Agent 日志
│   └── reflection.md      ← 反思文档
├── data/                  ← 数据文件（可选）
│   ├── players.csv
│   ├── heroes.csv
│   └── ...
├── git-history.txt        ← Git 日志输出
├── README.md              ← 项目说明
└── plan.md                ← 规划文档（根目录也可放一份）
```

### 11.2 README.md 建议结构

```markdown
# AI-Assisted Honor of Kings Information Management System
## 1. Project Overview
## 2. How to Run
## 3. Default Login Accounts
## 4. Implemented Features
## 5. Java Concepts Used
## 6. AI Usage Summary
## 7. Testing Summary
## 8. Known Limitations
```

### 11.3 提交形式

- `.zip` 文件 **或** Git 仓库链接
- 必须包含：完整源代码、plan.md、design.md、UML 图、prompts.md、agent-log.md、reflection.md、git-history.txt、测试文档、运行说明

### 11.4 ❌ 不要提交

- 只有截图 ❌
- 只有 AI 聊天记录而没有源代码 ❌
- 自己无法解释的代码 ❌

---

## 12. 额外加分功能

### 12.1 战斗模拟 (Combat Simulation) — 加分

实现基于英雄属性、装备和随机因素的**回合制战斗模拟器**：
- 伤害计算
- 暴击/闪避
- 胜/负结果
- 战斗报告

### 12.2 推荐引擎 (Recommendation Engine) — 加分

根据以下因素推荐英雄或装备：
- 英雄类型
- 玩家偏好
- 胜率
- 装备使用率
- 战队组成

> 必须说明推荐公式。

### 12.3 GUI 界面 — 加分

使用 Swing 或 JavaFX 实现图形界面：
- 至少支持：玩家查询、战队概览、英雄详情、排行榜

### 12.4 数据持久化 (Data Persistence) — 加分

- 使用文本文件 / CSV / JSON / JDBC 数据库
- 允许外部库（需明确文档说明）

### 12.5 高级 AI 反思 (Advanced AI Reflection) — 加分

比较两种不同 AI 模型或两种 Agent 角色解决同一问题：
- 例如：让 Implementation Agent 和 Reviewer Agent 分别实现排行榜排序
- 比较：正确性、可读性、bug、学习价值

---

## 13. 评分标准详解

### 13.1 评分维度

| 类别 | 分值 | 评分标准 |
|------|------|---------|
| **Java 设计与理解** | **5 分** | 优秀的 OOP 设计、继承、接口、集合、封装、异常处理、文件 I/O、可解释的设计决策 |
| **功能完整性** | **4 分** | 玩家查询、战队概览、英雄详情、装备统计、比赛历史、排行榜、数据管理、认证全部正常 |
| **AI 使用证据** | **4 分** | 实际的 prompt 记录、多 Agent 工作流、深刻反思、对 AI 建议的接受/修改/拒绝有清晰说明 |
| **Git 过程证据** | **3 分** | 清晰的迭代 Git 历史，包含人类规划、AI 辅助实现、审查、修复、测试和文档 |
| **plan.md 与文档** | **2 分** | 详细规划、UML、设计说明、测试计划、实现笔记、准确的最终文档 |
| **测试与可靠性** | **1 分** | 清晰的测试用例或自动化测试，附带调试证据 |
| **额外加分/创意** | **1 分** | GUI、持久化、推荐引擎、战斗模拟、高级 AI 反思等有意义的功能 |

**总分: 20 分**

### 13.2 扣分点

| 情况 | 扣分 |
|------|------|
| 所有逻辑写在单个大 Main 类中 | 低 Java 设计分 |
| 提交 AI 生成的代码但无法解释 | 即使程序能跑也得低分 |
| 只有一次最终 Git 提交 | 不可接受 |
| 无实际 prompt 记录，只写 "I used ChatGPT" | AI 证据部分扣分 |
| 代码无法编译或无法运行 | 严重扣分 |

---

## 14. 分数等级划分

| 等级 | 分数 | 描述 |
|------|------|------|
| **A** | **16-20** | 核心功能正常，Java 设计强，AI 和 Git 证据详细，文档专业，至少实现一个额外功能 |
| **B** | **10-15** | 大多数核心功能正常，Java 结构可接受，AI 证据和文档存在但不优秀 |
| **C** | **6-9** | 部分功能不完整，Java 设计较浅，文档或 AI 证据薄弱 |
| **F** | **0-5** | 主要功能缺失，代码无法运行，工作杂乱无章，AI 使用被隐藏，或文档不足 |

---

## 15. 最低通过清单

提交前逐项确认：

### 程序功能
- [ ] 程序能运行
- [ ] 菜单系统工作
- [ ] 玩家查询 (Player Lookup) 工作
- [ ] 战队概览 (Team Overview) 工作
- [ ] 英雄详情 (Hero Details) 工作
- [ ] 装备统计 (Equipment Statistics) 工作
- [ ] 排行榜 (Leaderboard) 工作
- [ ] Admin/Player 登录工作

### 类与数据
- [ ] 至少 7 个必需类
- [ ] 至少 10 名玩家
- [ ] 至少 15 个英雄
- [ ] 至少 20 件装备
- [ ] 至少 3 支战队
- [ ] 至少 10 条比赛记录

### 文档
- [ ] plan.md 存在且详细
- [ ] prompts.md 包含实际 prompts
- [ ] agent-log.md 显示至少 3 种 Agent 角色
- [ ] reflection.md 回答所有 10 个问题
- [ ] 测试文档包含至少 10 个测试用例

### Git
- [ ] Git 至少 12 次有意义提交
- [ ] 至少 4 次人类提交
- [ ] 至少 3 次 Architect Agent 提交
- [ ] 至少 3 次 Implementation Agent 提交
- [ ] 至少 2 次 Testing/Reviewer Agent 提交
- [ ] git-history.txt 已包含

---

## 16. 开发时间线

### 阶段 1: 规划 (Day 1)
- [ ] 通读需求文档
- [ ] 创建 Git 仓库
- [ ] 编写初始 plan.md
- [ ] 创建目录结构

### 阶段 2: 架构设计 (Day 2)
- [ ] 向 Architect Agent 请求设计反馈
- [ ] 手动修改类设计
- [ ] 确定 UML 类图
- [ ] 提交 `[Human]` `[AI-Architect]` 相关提交

### 阶段 3: 模型类与数据 (Day 3-4)
- [ ] 实现 Person、Player、Admin
- [ ] 实现 Hero、Equipment
- [ ] 实现 Team、MatchRecord
- [ ] 实现枚举类
- [ ] 实现 DataInitializer（硬编码数据集）
- [ ] **编译并测试**

### 阶段 4: 菜单与搜索功能 (Day 5-6)
- [ ] 实现主菜单循环
- [ ] 实现 Player Lookup
- [ ] 实现 Team Overview
- [ ] 实现 Hero Details
- [ ] 实现 InputHelper
- [ ] **编译并测试**

### 阶段 5: 认证与权限 (Day 7)
- [ ] 实现 AuthenticationService
- [ ] 实现登录/登出
- [ ] 实现 Admin/Player 权限控制
- [ ] **编译并测试**

### 阶段 6: 高级功能 (Day 8-9)
- [ ] 实现 Equipment Statistics（含排名公式）
- [ ] 实现 Leaderboard（含平局处理）
- [ ] 实现 Match History
- [ ] 实现 Data Management（增删改）
- [ ] 实现 FileStorageService（文件持久化）
- [ ] **编译并测试**

### 阶段 7: 审查与修复 (Day 10)
- [ ] 使用 Testing/Reviewer Agent 审查代码
- [ ] 修复发现的 bug
- [ ] 记录审查结果
- [ ] 提交 `[AI-Review]` `[Fix]` `[Test]` 相关提交

### 阶段 8: 文档与最终检查 (Day 11-12)
- [ ] 更新 prompts.md（记录所有 prompts）
- [ ] 更新 agent-log.md（记录所有 Agent 交互）
- [ ] 编写 reflection.md（回答 10 个问题）
- [ ] 编写 test-cases.md（至少 10 个测试用例）
- [ ] 生成 UML 图 (uml.png)
- [ ] 编写 README.md
- [ ] 导出 git-history.txt
- [ ] 最终检查最低通过清单

---

## 17. Prompt 质量指南

### 17.1 设计类 Prompt（给 Architect Agent）

```
I am designing a Java OOP coursework project for an Honor of Kings
information management system. I need the following classes: Person
(abstract), Player, Admin, Hero, Equipment, Team, MatchRecord.

Please suggest:
1. Field and method designs for each class
2. Relationship types (inheritance, association, aggregation)
3. Which interfaces would be meaningful

Constraints:
- Must use inheritance, interfaces, collections, enums
- Console application, no GUI yet
- Player and Admin extend Person
- A Team contains multiple Players
- A Player owns multiple Heroes

Do NOT write full implementations. Focus on design only.
```

### 17.2 实现类 Prompt（给 Implementation Agent）

```
I need to implement the [class/method name] in my Java Honor of Kings
system. Here is the context:

[Paste existing relevant code or class structure]

Requirements:
- [Specific requirement 1]
- [Specific requirement 2]

Please generate the implementation with:
- Proper encapsulation (private fields, getters/setters)
- Exception handling for edge cases
- Comments for non-trivial logic

Explain your assumptions and any design trade-offs.
```

### 17.3 审查/测试 Prompt（给 Testing/Reviewer Agent）

```
Please review the following Java code for my Honor of Kings system:

[Paste code]

Check for:
1. OOP design issues
2. Encapsulation violations
3. Collection misuse
4. Potential null pointer or exception issues
5. Logic errors

[If testing:] Also generate test cases for this code covering:
- Normal case
- Edge case (empty/null input)
- Error case (invalid data)

Give specific, actionable feedback. Do NOT rewrite the entire code.
```

### 17.4 调试 Prompt（给 AI）

```
The following [method/class] crashes when [specific scenario]:

[Paste code]

Error message / Symptom:
[Describe what happens]

Please:
1. Identify the root cause
2. Explain why it happens
3. Suggest a minimal fix
4. Do NOT rewrite unrelated code

I want to understand the bug, not just get a fix.
```

---

## 附录: 与 AI 协作的注意事项

### ✅ 正确做法

1. **按功能拆分 prompt**：不要一次让 AI 实现整个项目，而是每次一个功能
2. **提供上下文**：粘贴相关类的代码，让 AI 理解现有结构
3. **审查 AI 输出**：每段 AI 生成的代码都要理解后再使用
4. **记录每次交互**：每个 prompt 都要记录到 prompts.md
5. **频繁编译**：每次 AI 生成代码后立刻编译检查
6. **保留 Git 历史**：每个阶段都提交，不要 squash

### ❌ 错误做法

1. 让 AI 一次性生成所有代码
2. 不审查就直接复制粘贴 AI 代码
3. 不记录 prompts
4. 用一个提交完成所有工作
5. 提交自己无法解释的代码

### AI 辅助范围

| 允许 ✅ | 不允许 ❌ |
|---------|-----------|
| 理解需求 | 提交不理解代码 |
| 头脑风暴设计 | 删除或隐藏 AI prompts |
| 生成代码片段 | 伪造 Git 历史 |
| 调试 | 声称 AI 作品完全是自己写的 |
| 重构 | 使用 AI 伪造测试结果 |
| 编写测试用例 | 不诚实地写反思 |
| 改进文档 | 提交其他人的代码 |

---

> **最后提醒**: 你必须能够解释最终项目中的每一行代码。评分者会问你"为什么这样设计"和"这段代码是做什么的"——AI 不能替你回答这些问题。
