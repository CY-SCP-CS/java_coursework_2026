# Honor of Kings Information Management System / 王者荣耀信息管理系统

> AI-Assisted OOP Coursework — Java Console Application
> AI 辅助 OOP 课程项目 — Java 控制台应用程序

---

## 1. Project Overview / 项目概述

An information management system for the game **Honor of Kings** (王者荣耀), built with Java OOP principles and AI-assisted development methodology. Supports player/hero/equipment management, match tracking, team statistics, and persistent CSV storage.

基于 **Java OOP** 原则和 **AI 辅助开发** 方法论的王者荣耀信息管理系统。支持玩家/英雄/装备管理、比赛追踪、战队统计和 CSV 持久化存储。

---

## 2. Features / 功能特性

### Player Features / 玩家功能

| English | 中文 |
|---------|------|
| **Profile Management** — View personal info, level, win rate, owned heroes | **个人信息** — 查看个人资料、等级、胜率、拥有的英雄 |
| **Hero Browser** — Browse owned heroes with equipped items | **英雄浏览** — 查看已拥有英雄及装备物品 |
| **Match History** — View personal match history with W/L/D summary | **比赛历史** — 查看个人比赛记录及胜/负/平汇总 |
| **Player Search** — Search by ID or name (fuzzy match) | **玩家搜索** — 按 ID 或名称模糊搜索 |
| **Team Overview** — Search teams by ID or name, view members and stats | **战队概览** — 按 ID 或名称搜索战队，查看成员和统计 |
| **Hero Details** — Search heroes, view stats and compatible equipment | **英雄详情** — 搜索英雄，查看属性和兼容装备 |
| **Equipment Statistics** — Equipment ranked by usage or hero count | **装备统计** — 按使用次数或兼容英雄数排名 |
| **Leaderboard** — Rankings by win rate, level, matches, or custom score | **排行榜** — 按胜率/等级/场次/自定义分数排名 |

### Admin Features / 管理员功能

All Player features plus / 包含所有玩家功能，额外拥有：

| English | 中文 |
|---------|------|
| **Player CRUD** — Add/remove/edit players with validation | **玩家 CRUD** — 添加/删除/编辑玩家 |
| **Hero CRUD** — Add/remove/edit heroes | **英雄 CRUD** — 添加/删除/编辑英雄 |
| **Equipment CRUD** — Add/remove/edit equipment | **装备 CRUD** — 添加/删除/编辑装备 |
| **Team CRUD** — Add/remove teams (max 5 members) | **战队 CRUD** — 添加/删除战队（最多 5 人） |
| **Match CRUD** — Add/remove match records with date validation | **比赛 CRUD** — 添加/删除比赛记录（日期验证） |

### System Features / 系统功能

| English | 中文 |
|---------|------|
| **Persistent Storage** — CSV-based save/load (7 files, UTF-8) | **持久化存储** — 基于 CSV 的保存/加载（7 个文件，UTF-8） |
| **4-Level Logging** — DEBUG/INFO/WARN/ERROR with file output | **4 级日志** — DEBUG/INFO/WARN/ERROR，支持文件输出和日志分析 |
| **Auto-Save** — Data saved on logout and exit | **自动保存** — 登出和退出时自动保存数据 |
| **Input Validation** — Range check, null safety, EOF handling | **输入验证** — 范围检查、空安全、EOF 处理 |
| **Defensive Copying** — All collection getters return copies | **防御性拷贝** — 所有集合 getter 返回副本 |

---

## 3. Technology Stack / 技术栈

| Layer / 层 | Technology / 技术 |
|-----------|------------------|
| Language / 语言 | Java 25 (LTS) |
| Testing / 测试 | JUnit 5 (Platform Console Standalone 1.11.4) |
| Build / 构建 | Manual `javac` compilation |
| Storage / 存储 | CSV files / UTF-8 |
| Dev Tool / 开发工具 | Claude Code CLI (AI-assisted) |

---

## 4. Project Structure / 项目结构

```
src/
├── Main.java                     # Entry point — menu-driven console UI / 控制台菜单入口
├── model/
│   ├── Person.java               # Abstract base class / 抽象基类
│   ├── Player.java               # Player model / 玩家模型
│   ├── Admin.java                # Admin model / 管理员模型
│   ├── Hero.java                 # Hero model / 英雄模型
│   ├── Equipment.java            # Equipment model / 装备模型
│   ├── Team.java                 # Team model / 战队模型
│   ├── MatchRecord.java          # Match record / 比赛记录
│   ├── Searchable.java           # Generic search interface / 通用搜索接口
│   ├── Reportable.java           # Display interface / 显示接口
│   ├── Persistable.java          # CSV serialization / CSV 序列化接口
│   ├── Role.java                 # ADMIN / PLAYER enum
│   ├── HeroType.java             # 7 hero types / 7 种英雄类型
│   ├── EquipmentType.java        # 5 equipment types / 5 种装备类型
│   └── MatchResult.java          # WIN / LOSE / DRAW enum
├── service/
│   ├── AuthenticationService.java # Login/logout / 登录认证服务
│   ├── SearchService.java         # Search & display / 搜索显示服务
│   ├── RankingService.java        # Leaderboards / 排行榜服务
│   ├── GameDataManager.java       # Central CRUD / 中央数据管理
│   └── FileStorageService.java    # CSV persistence / CSV 持久化
└── util/
    ├── DataInitializer.java       # Default dataset / 初始数据集
    ├── GameLogger.java            # 4-level logger / 4 级日志工具
    └── InputHelper.java           # Console input / 控制台输入工具

test/                              # JUnit 5 test suite / 测试套件 (179 tests)
├── PlayerTest.java                # 30 tests
├── TeamTest.java                  # 18 tests
├── HeroEquipmentTest.java         # 18 tests
├── MatchRecordTest.java           # 12 tests
├── AdminTest.java                 # 5 tests
├── PersonTest.java                # 8 tests
├── DataInitializerTest.java       # 13 tests
├── AuthenticationServiceTest.java # 10 tests
├── SearchServiceTest.java         # 20 tests
├── RankingServiceTest.java        # 12 tests
├── GameDataManagerTest.java       # 20 tests
├── GameLoggerTest.java            # 10 tests
└── FileStorageServiceTest.java    # 14 tests

data/                              # CSV data files / 数据文件 (auto-generated)
├── equipment.csv                  # 20 equipment items / 20 件装备
├── heroes.csv                     # 15 heroes / 15 个英雄
├── players.csv                    # 15 players / 15 名玩家
├── teams.csv                      # 3 teams / 3 支战队
├── matches.csv                    # 10 match records / 10 条比赛记录
├── passwords.csv                  # Account credentials / 账号密码
└── equipped_items.csv            # Player-hero-equipment mappings / 装备映射

logs/                              # Log files / 日志文件 (auto-created)
ai/
├── prompts.md                     # Prompt records / Prompt 记录
└── agent-log.md                   # Agent contribution log / Agent 日志
```

---

## 5. Default Data / 默认数据

### Initial Dataset / 初始数据集

| Entity / 类型 | Count / 数量 | Details / 详情 |
|--------------|-------------|----------------|
| Equipment / 装备 | 20 | 6 OFFENSIVE + 5 DEFENSIVE + 3 MOVEMENT + 4 MAGIC + 2 JUNGLE |
| Heroes / 英雄 | 15 | 3 WARRIOR + 3 MAGE + 2 ASSASSIN + 2 TANK + 2 MARKSMAN + 2 SUPPORT + 1 JUNGLER |
| Players / 玩家 | 15 | Each with 3-4 heroes + equipped items / 每人 3-4 个英雄 |
| Teams / 战队 | 3 | 星辰 / 雷霆 / 明月 (5 members each / 每队 5 人) |
| Matches / 比赛 | 10 | 5 WIN + 4 LOSE + 1 DRAW |

### Default Accounts / 默认账号

| Role / 角色 | ID | Password / 密码 |
|------------|----|-----------------|
| Admin / 管理员 | `admin` | `admin123` |
| Player / 玩家 | `P001`–`P015` | `pass123` |

---

## 6. Getting Started / 快速开始

### Prerequisites / 前提条件
- Java 17+
- Git

### Compile & Run / 编译与运行

```bash
# Compile all source files / 编译所有源文件
javac -d out -sourcepath src src/Main.java

# Run the application / 运行程序
java -cp out Main
```

### Run Tests / 运行测试

```bash
# Compile source + tests / 编译源文件和测试
javac -d out -sourcepath src -cp "lib/junit-platform-console-standalone-1.11.4.jar" src/Main.java
javac -d out -cp "out;lib/junit-platform-console-standalone-1.11.4.jar" test/*.java

# Execute all tests / 执行所有测试
java -jar "lib/junit-platform-console-standalone-1.11.4.jar" --class-path out --scan-class-path
```

### Clean Start / 全新启动 (remove saved data, fresh init on next run / 删除已保存数据，下次运行重新初始化)

```bash
rm -rf data/ logs/
```

---

## 7. OOP Design Principles / OOP 设计原则

| Principle / 原则 | Implementation / 实现 |
|-----------------|----------------------|
| **Encapsulation / 封装** | All fields `private`, getters/setters with defensive copying |
| **Inheritance / 继承** | `Person` abstract class → `Player`, `Admin` |
| **Polymorphism / 多态** | `Reportable.getInfo()` overridden in all model classes |
| **Abstraction / 抽象** | `Searchable<T>` generic interface, `Persistable` CSV contract |
| **Aggregation / 聚合** | `Team` aggregates `Player` objects |
| **Composition / 组合** | `MatchRecord` contains `MatchResult` enum |

### Design Patterns Used / 使用的设计模式

| Pattern / 模式 | Location / 位置 | Description / 说明 |
|----------------|----------------|-------------------|
| **DAO Pattern** | `GameDataManager` | Central data access object / 中央数据访问对象 |
| **Service Layer** | `AuthenticationService`, `SearchService`, `RankingService` | Business logic separation / 业务逻辑分离 |
| **Strategy Pattern** | `RankingService` | Multiple `Comparator` strategies for ranking / 多种排序策略 |
| **Factory Method** | `DataInitializer.initData()` | Static factory for default dataset / 静态工厂创建初始数据 |

---

## 8. Ranking Formula / 排行榜公式

### Custom Score / 自定义分数

```
customScore = winRate × 0.5 + level × 2.0 + matchCount × 0.1
```

| Parameter / 参数 | Weight / 权重 | Range / 范围 |
|-----------------|--------------|-------------|
| winRate / 胜率 | 0.5 | 0–100 |
| level / 等级 | 2.0 | 1–30 |
| matchCount / 比赛场次 | 0.1 | ≥ 0 |

### Tie-Breaking / 平局处理

Primary sort by metric descending; secondary sort by ID ascending (deterministic).
按指标降序排列，指标相同时按 ID 升序排列（确定性排序）。

---

## 9. AI-Assisted Development / AI 辅助开发

This project was developed using **Claude Code CLI** with a multi-agent methodology involving 8 agent roles:

本项目使用 **Claude Code CLI** 开发，采用多 Agent 协作方法论，共 8 种 Agent 角色：

| Role / 角色 | Responsibility / 职责 |
|------------|---------------------|
| **Architect Agent / 架构师** | Class design, interfaces, enums / 类设计、接口、枚举 |
| **Implementation Agent / 实现者** | Code generation per module / 功能模块代码生成 |
| **Code Review Agent / 审查者** | Style checks, bug detection / 代码风格检查、Bug 检测 |
| **Testing/Reviewer Agent / 测试者** | JUnit test suite / JUnit 测试套件 |
| **Log Agent / 日志代理** | 4-level logging utility / 4 级日志工具 |
| **Fix Agent / 修复者** | Bug repair from reviews / 根据审查结果修复 Bug |
| **Documentation Agent / 文档者** | README, prompt records / README、Prompt 记录 |
| **Prompt Optimization Agent / Prompt 优化者** | Prompt quality analysis / Prompt 质量分析 |

### Development Workflow / 开发流程

1. **Architect** designs → `[AI-Architect]` commit
2. **Implementation Agent** codes → `[AI-Implementation]` commit
3. **Code Review Agent** inspects → `[AI-Review]` commit
4. **Testing Agent** creates tests → `[AI-Implementation]` commit
5. **Human** validates and merges → `[Human]` commit

Full details in / 详细信息请查看:
- [`ai/agent-log.md`](ai/agent-log.md) — Agent contributions / Agent 贡献记录
- [`ai/prompts.md`](ai/prompts.md) — Complete prompt history / 完整 Prompt 记录
- [`ai/reflection.md`](ai/reflection.md) — Development reflection / 开发反思

---

## 10. Test Coverage / 测试覆盖率

**179 tests** across 13 test classes, all passing / **179 个测试**，13 个测试类，全部通过：

| Test Class / 测试类 | Tests | Coverage / 覆盖内容 |
|-------------------|-------|-------------------|
| PlayerTest | 30 | Constructors, validation, heroes, equipment, CSV |
| TeamTest | 18 | Members, capacity, avg level/win rate, CSV |
| HeroEquipmentTest | 18 | Constructors, stats, compatible equipment, CSV |
| MatchRecordTest | 12 | Constructor, hero picks, dedup, CSV |
| AdminTest | 5 | Constructor, level, permissions |
| PersonTest | 8 | Polymorphism, enums |
| DataInitializerTest | 13 | Data counts, reference integrity, passwords |
| AuthenticationServiceTest | 10 | Login/logout, wrong password, session |
| SearchServiceTest | 20 | ID/name search, fuzzy match, display |
| RankingServiceTest | 12 | Leaderboards, equipment ranking, tie-breakers |
| GameDataManagerTest | 20 | CRUD, duplicate detection, defensive copy |
| GameLoggerTest | 10 | Log levels, file output, null safety |
| FileStorageServiceTest | 14 | CSV save/load, UTF-8, graceful failure |

---

## 11. Known Limitations / 已知限制

| English | 中文 |
|---------|------|
| No GUI — console-based interface only | 无图形界面 — 仅控制台交互 |
| CSV-based storage (not a relational database) | 基于 CSV 存储（非关系型数据库） |
| Data integrity depends on application-level validation | 数据完整性依赖应用层验证 |
| No network/multi-user support | 不支持网络/多用户 |

---

## 12. Git History / Git 提交历史 (28 commits)

```
* [AI-Review] fix code review issues: charset, defensive copies, unused imports
* [AI-Architect] final project audit: verify all assignment requirements
* [Docs] add design doc, test cases, reflection, git history
* [Docs] Prompt 14 (README) + Prompt 16 (prompt optimization) records
* [Docs] Prompt 14 — comprehensive README
* [AI-Review] fix 5 code review issues
* [Docs] record Prompt 11 (architect review) and Prompt 12 (test suite)
* [AI-Implementation] implement test suite (179 tests) + fix validation bugs
* [AI-Implementation] fix all code quality issues from architect review
* [AI-Architect] comprehensive code review: identify 10 issues across 22 files
* [Human] implement Prompt 10 — file persistence with CSV save/load
* [Human] fix code review issues, enhance services, update docs
* [AI-Review] code review: menu system and input toolkit (8/10)
* [Human] implement menu system, input toolkit, and 4-level logger
* [AI-Review] review DataInitializer: data completeness verified 9/10
* [Human] implement data initialization with 15 heroes 20 equipment 15 players
* [AI-Review] code review: fix CSV separator ambiguity and import style
* [AI-Implementation] implement model classes with validation
* [AI-Architect] add Persistable interface and CSV serialization
* [Human] create initial project structure and OOP class design
```

Full log in / 完整日志见 `git-history.txt`
