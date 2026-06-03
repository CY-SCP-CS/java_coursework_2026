# Prompts Record — AI-Assisted Honor of Kings IMS

> **开发环境**: Claude Code CLI (底层模型: deepseek-v4-flash)
> **工具说明**: Claude Code 是一个 CLI 编程助手，通过工具调用协议与模型交互，可直接读写文件、执行命令、管理 Git
> **使用方式**: 通过与 Claude Code 的对话完成所有 AI 辅助开发

---

## Prompt 使用说明

### Agent 角色映射

由于使用 **Claude Code CLI** 作为唯一的 AI 工具，采用**分阶段切换角色**的方式来实现多 Agent 效果：


| 阶段     | 角色                         | 职责            | 对话策略                |
| ------ | -------------------------- | ------------- | ------------------- |
| 阶段 1-2 | **Architect Agent**        | 类设计、UML、模块规划  | 明确要求 AI 仅做设计，不写实现代码 |
| 阶段 3-6 | **Implementation Agent**   | 按功能模块逐步实现     | 每次只聚焦一个功能，提供完整上下文   |
| 阶段 7   | **Testing/Reviewer Agent** | 代码审查、找 bug、测试 | 要求 AI 以审查者视角检查已有代码  |
| 全周期    | **Code Review Agent**      | 每次提交后审查代码质量   | 关注风格、内聚、耦合、SOLID    |
| 全周期    | **Log Agent**              | 四级日志 + 日志分析   | 自动化问题检测和修复调度        |


### Prompt 编写原则

参考课程附录 B 的 Prompt Quality Guide：

- **具体**：明确类名、方法名、约束条件
- **分步**：一次只完成一个任务
- **可验证**：要求 AI 说明假设和边界情况
- **可审查**：拒绝不理解的代码

---

## Prompt 01 — Architect Agent: 初始类设计

**时间**: 2026-06-03 14:25
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Architect Agent
**相关提交**: 9dc43e2 (initial project structure)

### 我在 CLI 输入的 Prompt

```
开始按 Prompt 01 进行类设计，逻辑清晰，内容完整并且完全按照要求进行，
https://github.com/CY-SCP-CS/java_coursework_2026.git 这个是我的 git 仓库，
对其进行第一次提交，并且按照要求进行日志的填写
```

### AI 响应摘要

AI 建议了完整的类结构设计：

1. **Person (抽象类)**: 包含 id, name, role 字段，提供抽象方法 getDescription()
2. **Player extends Person**: 增加 teamId, level, winRate, ownedHeroes（ListHero），提供防御性拷贝
3. **Admin extends Person**: 增加 adminLevel 字段
4. **Hero**: heroId, name, heroType (枚举), baseStats (Map), compatibleEquipment (ListEquipment)
5. **Equipment**: equipmentId, name, equipmentType (枚举), stats (Map), usageCount
6. **Team**: teamId, name, members (ListPlayer)
7. **MatchRecord**: matchId, date (LocalDate), teamA, teamB, result (枚举), heroPicks (ListString)
8. **接口建议**: SearchableT, Persistable/Reportable
9. **包结构建议**: model, service, util

### 我的决定

**全部接受**，但做了以下调整：

- 新增 `Reportable` 接口替代 Persistable
- 接受 SearchableT 泛型接口的设计
- Team 的 getMembers() 采用防御性拷贝
- 所有类的集合字段初始化为空集合，避免 NPE
- 添加了 equals() 和 hashCode()（基于 ID 字段）

---

## Prompt 01b — 新增 Code Review Agent

**时间**: 2026-06-03 14:30
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Architect Agent (扩展)
**相关提交**: 9dc43e2

### 我在 CLI 输入的 Prompt

```
增加一个负责 review 的 agent，要求代码风格优雅，实现高内聚低耦合
```

### AI 响应摘要

增加了 Code Review Agent 角色，制定了审查标准：

- 代码风格：命名规范、缩进一致、方法长度控制
- 高内聚：单一职责、方法围绕核心功能
- 低耦合：依赖接口而非实现、无循环依赖
- SOLID 原则审查

### 我的决定

✅ 接受，Code Review Agent 成为标准的开发流程环节

---

## Prompt 02 — Architect Agent: 接口与枚举设计

**时间**: 2026-06-03 14:40
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Architect Agent
**相关提交**: 7eac0a6 (add Persistable interface)

### 我在 CLI 输入的 Prompt

```
按 Prompt 02 进行接口与枚举设计，并且再新增一个负责优化 prompt 的 agent
```

### AI 响应摘要

**接口设计：**

1. `Searchable<T>` — searchById / searchByName
2. `Persistable` — toCSVString / fromCSVString
3. `Reportable` — getInfo

**枚举常量：**

1. `Role`: ADMIN, PLAYER
2. `HeroType`: WARRIOR, MAGE, ASSASSIN, TANK, MARKSMAN, SUPPORT, JUNGLER
3. `EquipmentType`: OFFENSIVE, DEFENSIVE, MOVEMENT, MAGIC, JUNGLE
4. `MatchResult`: WIN, LOSE, DRAW

### 我的决定

✅ 全部接受，新增 Prompt Optimization Agent

---

## Prompt 03 — Implementation Agent: 模型类实现

**时间**: 2026-06-03 14:25
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: 39649b7 (implement model classes)

### 我在 CLI 输入的 Prompt

```
开始按 Prompt 03 实现模型类
```

### AI 响应摘要

AI 生成了全部 7 个模型类和 3 个接口的完整实现代码：

- Person (abstract), Player, Admin, Hero, Equipment, Team, MatchRecord
- SearchableT, Reportable, Persistable
- 4 个枚举：Role, HeroType, EquipmentType, MatchResult

### 我的决定

✅ 全部接受，并做了以下增强：

- ✅ 所有类实现无参 + 有参构造方法
- ✅ 防御性拷贝：getOwnedHeroes() 返回 new ArrayList
- ✅ 便捷方法：Player.addHero()/removeHero()
- ✅ 校验逻辑：level 1-30, winRate 0-100
- ✅ toCSVString() 使用 ID 引用而非对象序列化

---

## Prompt 03b — 建立审查规则

**时间**: 2026-06-03 14:50
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Human (规则制定)
**相关提交**: —

### 我在 CLI 输入的 Prompt

```
每运行一次 prompt 之后都要使用 review 的 agent 进行审查
```

### AI 响应摘要

确立了"实现 → 审查"的开发闭环规则：

- 每次 AI 实现完成后，自动触发 Code Review Agent
- 审查结果记录到 agent-log.md
- 重大问题立即修复，小问题记入待办

### 我的决定

✅ 确立为固定开发流程

---

## Prompt 04 — Implementation Agent: 数据初始化

**时间**: 2026-06-03 15:40
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: cdbc433 (+ [Human] commit)

### 我在 CLI 输入的 Prompt

```
按 Prompt 04 进行数据初始化，并且实现一个 [Human] 的 git 提交
```

### AI 响应摘要

AI 生成了完整的 DataInitializer 实现：

- **装备 20 件**: OFFENSIVE(6) + DEFENSIVE(5) + MOVEMENT(3) + MAGIC(4) + JUNGLE(2)
- **英雄 15 个**: 覆盖全部 7 种 HeroType
- **玩家 15 名**: 每人 3-4 个英雄，装备完整
- **战队 3 支**: 星辰、雷霆、明月，每队 5 人
- **比赛 10 条**: 5 WIN / 4 LOSE / 1 DRAW

### 我的决定

✅ 全部接受，人工调整：

- ✅ 添加 Player.equippedItemIds 追踪装备
- ✅ Player.getInfo() 展示每个英雄的已装备物品
- ✅ 真实王者荣耀出装配置
- ✅ 修正 P004 从 2 个英雄改为 3 个
- ✅ 运行验证全部通过

---

## Prompt 05 — Implementation Agent: 菜单系统与输入工具

**时间**: 2026-06-03
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: d19ba8a (+ [Human] commit)

### 我在 CLI 输入的 Prompt

```
按 Prompt 05 实现菜单系统与输入工具，并提交
```

### AI 响应摘要

AI 生成了完整的输入工具类和菜单系统：

**InputHelper.java:** 5 个静态方法，统一 nextLine() 策略

**Main.java (~720 行):**

- 三级菜单：Main → Login → Player(9项) / Admin(11项)
- 所有 handler 方法均有空集合检查和 null 安全处理

**Human 修复：**

- 补充 readDouble() 方法
- 修复 heroCount 变量作用域问题

### 我的决定

- ✅ InputHelper 采用 nextLine() 统一策略
- ✅ 添加 readDouble(String prompt, double min, double max)
- ✅ 测试验证：编译通过，登录正常

---

## Prompt 05b — Log Agent: 四级分层日志

**时间**: 2026-06-03
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent (Log Agent)
**相关提交**: d19ba8a (同 Prompt 05 提交)

### 我在 CLI 输入的 Prompt

```
再实现一个 log agent，可以建立四级分层日志
并且这个 agent 可以做到通过 log 来找出问题，并且让其他 agent 进行修改
```

### AI 响应摘要

创建了 **GameLogger.java** — 4 级分层日志工具：

- DEBUG / INFO / WARN / ERROR 四级
- 优先级过滤（默认只输出 INFO 及以上）
- 控制台 + 文件双输出（auto-flush）
- analyzeLogs() 自动分析 ERROR/WARN 并生成修复建议

### 我的决定

- ✅ 4 级日志优先级过滤
- ✅ 控制台默认输出，可选文件输出
- ✅ 自动创建 logs/ 目录
- ✅ analyzeLogs() 返回结构化报告，可用于触发 Fix Agent

---

## Prompt 06 — Implementation Agent: 认证服务

**时间**: 2026-06-03
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: 513f78f

### 我在 CLI 输入的 Prompt

```
修复代码风格问题，实现 Prompt 06 07，并且 prompt 记录文档要包含我的所有 prompts
```

### AI 响应摘要

已实现 **AuthenticationService.java**：

- login(id, password) → Person（失败返回 null）
- logout() — 清除会话
- getCurrentUser() / isLoggedIn() / isAdmin()
- 集成 GameLogger 日志记录（登录成功/失败均记录）

### 我的决定

- ✅ 密码验证使用字符串比较（无需加密）
- ✅ 登录失败记录 WARN 日志
- ✅ 登录成功记录 INFO 日志
- ✅ 登出时记录日志

---

## Prompt 07 — Implementation Agent: 搜索与查询服务

**时间**: 2026-06-03
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: 513f78f

### 我在 CLI 输入的 Prompt

（同 Prompt 06 — 一次请求中同时实现 06 和 07）

### AI 响应摘要

已实现 **SearchService.java**：

- searchPlayerById / searchPlayerByName（模糊匹配）
- searchTeamById / searchTeamByName
- searchHeroByName（模糊匹配）
- displayPlayerDetail — ID/名称/战队/等级/胜率/英雄含装备
- displayTeamDetail — 成员/平均等级/胜率/最佳队员
- displayHeroDetail — 属性/兼容装备
- getMatchHistory(playerOrTeamId, n) — 最近 N 场
- displayMatchHistory — 含 W/L/D 统计

### 我的决定

- ✅ 所有搜索方法实现
- ✅ 格式化展示方法完整
- ✅ 比赛历史含胜负汇总

---

## Prompt 08 — Implementation Agent: 排行榜与装备统计

**时间**: 2026-06-03
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: 513f78f

### 我在 CLI 输入的 Prompt

（同 Prompt 06 — 包含在 "实现 Prompt 06 07" 的请求中，Prompt 08 作为配套服务一并实现）

### AI 响应摘要

已实现 **RankingService.java**：

- getLeaderboardByWinRate(topN) — 平局按等级降序
- getLeaderboardByLevel(topN) — 平局按胜率降序
- getLeaderboardByMatches(topN) — 平局按胜率降序
- getEquipmentRankingByUsage()
- getEquipmentRankingByHeroCount(heroCountMap)
- getCustomScore(Player) — 公式：winRate*0.5 + level*2.0 + matches*0.1
- getLeaderboardByCustomScore(topN)
- getPlayerMatchCount(playerId)

### 我的决定

- ✅ 所有排名方法实现含平局处理
- ✅ 自定义评分公式集成到 Main.java 排行榜
- ✅ 装备统计支持按使用次数和英雄数量排名

---

## Prompt 09 — Implementation Agent: 数据管理（增删改）

**时间**: 2026-06-03
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: 513f78f

### 我在 CLI 输入的 Prompt

（同 Prompt 06 — 数据管理作为 Prompt 05 菜单系统的必要依赖，已在前期完成）

### AI 响应摘要

已实现 **GameDataManager.java**：

- 玩家 CRUD：addPlayer / removePlayer / updatePlayer
- 英雄 CRUD：addHero / removeHero / updateHero
- 装备 CRUD：addEquipment / removeEquipment / updateEquipment
- 战队 CRUD：addTeam / removeTeam / updateTeam
- 比赛记录：addMatchRecord / removeMatchRecord（返回 boolean）
- 认证辅助：getPassword / findPersonById
- 集成 GameLogger 日志记录

### 我的决定

- ✅ 全 CRUD 实现
- ✅ update 方法检查实体存在性
- ✅ remove 方法返回 boolean（removeMatchRecord）
- ✅ 所有修改操作记录 DEBUG 日志

---

## Prompt 10 — Implementation Agent: 文件持久化

**时间**: 2026-06-03
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: c2aa9aa

### 我在 CLI 输入的 Prompt

```
按 Prompt 10 实现文件持久化
```

### AI 响应摘要

完整实现了 **FileStorageService.java**（~280 行）：

**CSV 文件清单（data/ 目录）：**
| 文件 | 格式 | 记录数 |
|------|------|--------|
| equipment.csv | equipmentId,name,type,stats,usageCount | 20 |
| heroes.csv | heroId,name,type,stats,compatibleEqIds | 15 |
| players.csv | id,name,teamId,level,winRate,heroIds | 15 |
| teams.csv | teamId,name,memberIds | 3 |
| matches.csv | matchId,date,teamA,teamB,result,heroPicks | 10 |
| passwords.csv | userId,password | 16 |
| equipped_items.csv | playerId,heroId,equipmentIds | 42 |

**保存方法：**
- saveAllData(GameDataManager): 一键保存所有数据
- 每个实体类型有独立的 save 方法
- 使用 try-with-resources 确保资源释放
- 使用 UTF-8 编码，PrintWriter + autoFlush
- 自动创建 data/ 目录

**加载方法：**
- loadAllData(): GameDataManager — 按依赖顺序加载
- 加载顺序：equipment → heroes → passwords → players → equipped_items → teams → matches
- 文件不存在时返回空管理器，不崩溃
- 解析错误时跳过单条记录并记录 WARN 日志

**集成到 Main.java：**
- 启动时尝试加载已有数据，无数据时使用 DataInitializer
- 退出时自动保存（case 2）
- 登出时自动保存

### 我的决定

- ✅ 所有 7 个 CSV 文件验证生成正确
- ✅ 首次运行（无数据）→ DataInitializer → 保存
- ✅ 第二次运行（有数据）→ 加载 → 显示 → 保存
- ✅ 玩家信息、英雄装备、战队成员关系在加载/保存后完整保持
- ✅ 中文数据正确写入和读取

---

## Prompt 11 — Architect Agent: 整体代码审查

**时间**: 2026-06-03
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Architect Agent → Testing/Reviewer Agent
**相关提交**: c081347, 1a07c73

### 我在 CLI 输入的 Prompt

```
实现Prompt 11和12，在过程中实现[AI-Implementation]和[AI-Architect]的提交，并且调用review agent检查、优化我的代码风格
```

### AI 响应摘要

完成了对所有 22 个 Java 源文件的全面审查，发现 10 项问题：

**设计问题：**
- `fromCSVString()` 在所有 Persistable 类中为空实现（接口契约未完全履行）
- `DataInitializer.addEquip()` 使用 `Object...` varargs 存在类型转换风险
- `GameLogger.readLogFile()` 使用默认字符集，与其他 UTF-8 I/O 不一致

**代码质量：**
- `InputHelper.java` 有未使用的 import
- `analyzeLogs()` 的字符串前缀匹配方式 `[ERROR]`/`[WARN ]` 较脆弱
- `SearchService.displayHeroDetail()` 硬编码 HP/Attack/Defense 而非遍历全部属性
- `handleMatchHistory()` 中玩家和战队过滤逻辑重复
- `getEquipmentRankingByHeroCount()` 需要外部传入 Map，调用方重复排名逻辑

**代码风格：**
- Main.java 未使用 GameLogger
- Team.addMember() 缺少容量上限检查

**修复内容：**
| # | 修复 | 文件 |
|---|------|------|
| 1 | 移除未使用的 import | InputHelper.java |
| 2 | readLogFile() 改为 UTF-8 | GameLogger.java |
| 3 | analyzeLogs() 改用 contains() | GameLogger.java |
| 4 | displayHeroDetail() 遍历全部属性 | SearchService.java |
| 5 | 新增无参 getEquipmentRankingByHeroCount() | RankingService.java |
| 6 | 提取 filterMatchesByTeam() 公共方法 | Main.java |
| 7 | 添加 MAX_MEMBERS 容量检查 | Team.java |
| 8 | 添加 GameLogger 日志记录 | Main.java |

### 我的决定

- ✅ 接受全部审查结果，按优先级修复
- ✅ 所有修复完成后进行代码风格复审

---

## Prompt 12 — Testing/Reviewer Agent: 测试用例生成

**时间**: 2026-06-03
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Testing/Reviewer Agent
**相关提交**: 990ff11

### 我在 CLI 输入的 Prompt

```
实现Prompt 11和12，在过程中实现[AI-Implementation]和[AI-Architect]的提交，并且调用review agent检查、优化我的代码风格
```

### AI 响应摘要

创建了完整的 JUnit 5 测试套件，共 179 个测试用例，覆盖 13 个测试类：

**模型层测试（6 类）：**
| 测试类 | 测试数 | 覆盖内容 |
|--------|--------|----------|
| PlayerTest | 30 | 构造/边界验证、添加/移除英雄、装备管理、防御性拷贝、equals/hashCode、CSV |
| TeamTest | 18 | 队员增删、容量上限(MAX_MEMBERS=5)、平均等级/胜率、最佳队员、防御性拷贝 |
| HeroEquipmentTest | 18 | Hero/Equipment 构造、属性管理、防御性拷贝、兼容装备、equals/hashCode |
| MatchRecordTest | 12 | 比赛记录构造、英雄选择、去重、MatchResult 枚举 |
| AdminTest | 5 | 构造、权限描述 |
| PersonTest | 8 | 多态、Role/HeroType/EquipmentType 枚举 |

**数据层测试（1 类）：**
| 测试类 | 测试数 | 覆盖内容 |
|--------|--------|----------|
| DataInitializerTest | 13 | 数据量验证(20/15/15/3/10)、管理员/密码、英雄≥3/战队5人、引用完整性 |

**服务层测试（5 类）：**
| 测试类 | 测试数 | 覆盖内容 |
|--------|--------|----------|
| AuthenticationServiceTest | 10 | 登录成功/失败/大小写/重复登录/登出 |
| SearchServiceTest | 20 | ID/名称搜索、模糊匹配、不存在处理、详情展示、比赛历史 |
| RankingServiceTest | 12 | 胜率/等级/场次/自定义评分排名、装备排名、平局处理 |
| GameDataManagerTest | 20 | 全 CRUD、重复/不存在处理、防御性拷贝、findPersonById |
| GameLoggerTest | 10 | 日志级别、文件输出、异常分析、空指针保护 |

**测试发现的 Bug：**
1. **Player 构造器绕过 setter**：`new Player(id, name, teamId, 0, 50.0)` 不会抛出 IllegalArgumentException，因为构造器直接赋值而非调用 setLevel()
2. **GameLogger.error(null e) 空指针**：`error("src", "msg", null)` 在 `e.getClass().getSimpleName()` 处 NPE

### 我的决定

- ✅ 179 个测试全部通过
- ✅ Player 构造器改为使用 setter（level 1-30, winRate 0-100 校验生效）
- ✅ GameLogger.error(null exception) 提前返回，不再 NPE
- ✅ 测试作为回归保障，后续修改必须通过全部测试

---

## Prompt 13 — Code Review Agent: 代码风格审查

**时间**: 2026-06-03 15:10
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Code Review Agent
**相关提交**: 39649b7, 9f105de

### 我的 Prompt

（自动触发 — 每次实现后的固定审查环节）

### AI 响应摘要

审查了 src/model/ 下全部 14 个文件：


| 严重程度  | 问题                       | 修复            |
| ----- | ------------------------ | ------------- |
| Major | CSV stats 分隔符歧义          | entry 间改用 `|` |
| Minor | `java.util.Objects` 全限定名 | 改为 import     |


**总体评分：8/10**

### 我的决定

✅ 全部修复：Hero.java 分隔符修正 + Team.java import 修正

---

## Prompt 14 — Documentation Agent: README 生成

**时间**: 2026-06-03
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Documentation Agent
**相关提交**: a027047

### 我在 CLI 输入的 Prompt

```
实现Prompt 14和16，继续使用git上传代码
```

### AI 响应摘要

生成了完整的 README.md，包含：

- 项目概述与技术栈
- 完整功能列表（Player 功能 8 项 + Admin 功能 5 项 + 系统功能 5 项）
- 项目目录结构（src 23 文件 + test 13 文件）
- 默认数据集与账号
- 编译运行与测试指南
- OOP 设计原则与设计模式说明
- AI 辅助开发流程说明（8 个 Agent 角色）
- 测试覆盖概览（179 测试）
- 已知限制

### 我的决定

- ✅ README 覆盖全部模块
- ✅ 编译/运行/测试命令已验证正确
- ✅ 项目结构与实际一致

---

## Prompt 15 — Fix Agent: Bug 修复

**时间**: 2026-06-03
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Refactoring/Fix Agent
**相关提交**: 513f78f

### 我在 CLI 输入的 Prompt

```
修复代码风格问题（这是用户当前请求的一部分）
```

### 修复内容

根据 Code Review Agent 的审查结果，修复了以下问题：


| 严重程度     | 问题                                | 文件                   | 修复                         |
| -------- | --------------------------------- | -------------------- | -------------------------- |
| Critical | LocalDate.parse() 无 try-catch 会崩溃 | Main.java            | 添加 while 循环 + try-catch 验证 |
| Critical | handleMatchHistory() 死代码          | Main.java            | 接入 Player/Admin 菜单         |
| Major    | topN 接受 0/负数导致崩溃                  | Main.java            | 改用 readIntRange(1, 999)    |
| Major    | 无 EOF 处理，nextLine() 返回 null 会崩溃   | InputHelper.java     | 添加 null 检查 + System.exit   |
| Major    | prompt 参数无 null 检查                | InputHelper.java     | 添加 Objects.requireNonNull  |
| Major    | error() 丢失完整堆栈                    | GameLogger.java      | 添加堆栈跟踪（top 10 frames）      |
| Major    | fileWriter 线程不安全                  | GameLogger.java      | synchronized(LOCK) 保护      |
| Major    | removeMatchRecord 不反馈是否存在         | GameDataManager/Main | 改为返回 boolean + 提示          |


### 我的决定

✅ 全部修复验证通过

---

## Prompt 16 — Prompt Optimization Agent: Prompt 质量优化

**时间**: 2026-06-03
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Prompt Optimization Agent
**相关提交**: a027047, 2ce158e

### 我在 CLI 输入的 Prompt

```
实现Prompt 14和16，继续使用git上传代码
```

### AI 响应摘要

#### 总体评估

对 prompts.md 中全部 16 条 Prompt 依据课程附录 B 的 Prompt Quality Guide 进行了系统分析。

#### 评分标准

| 维度 | 说明 |
|------|------|
| **具体性** | 是否包含明确的类名、方法名、约束条件 |
| **分步性** | 是否一次只完成一个任务 |
| **可验证性** | 产出是否可验证/可测试 |
| **上下文** | 是否为 AI 提供了足够的背景信息 |
| **约束条件** | 是否有明确的边界和规则 |
| **可审查性** | 产出是否可被审查 |

#### 逐条分析

| # | Prompt | 具体性 | 分步性 | 可验证 | 上下文 | 约束 | 总评 | 建议 |
|---|--------|--------|--------|--------|--------|------|------|------|
| 01 | 初始类设计 | ★★★☆ | ★★☆☆ | ★★☆☆ | ★★★★ | ★★☆☆ | **6/10** | 拆分设计/提交/日志为三步；明确类设计的具体要求 |
| 01b | 新增 Review Agent | ★★★★ | ★★★★ | ★★★☆ | ★★★☆ | ★★★☆ | **7/10** | "代码风格优雅"过于主观，建议量化标准 |
| 02 | 接口与枚举设计 | ★★★★ | ★★☆☆ | ★★★☆ | ★★★★ | ★★★★ | **7/10** | 两个任务应分两次执行 |
| 03 | 实现模型类 | ★★☆☆ | ★★★★ | ★★☆☆ | ★★☆☆ | ★★☆☆ | **5/10** | 完全依赖外部 "Prompt 03"，无独立可用信息 |
| 03b | 审查规则 | ★★★★★ | ★★★★★ | ★★★★ | ★★★★ | ★★★★★ | **9/10** | 最佳实践——清晰、具体、可执行 |
| 04 | 数据初始化 | ★★★★ | ★★★★ | ★★★★ | ★★★☆ | ★★★☆ | **8/10** | 方法明确，结果可验证，但缺乏数据量预期说明 |
| 05 | 菜单系统 | ★★★★ | ★★★★ | ★★★★ | ★★★☆ | ★★★☆ | **8/10** | 较好，编译和运行测试验证 |
| 05b | Log Agent | ★★★★ | ★★★★ | ★★★☆ | ★★★☆ | ★★★☆ | **7/10** | "让其他 agent 修改"在静态工具中难以实现 |
| 06 | 认证服务 | ★★★☆ | ★★☆☆ | ★★★☆ | ★★★★ | ★★★☆ | **6/10** | 三个任务合一(修复+实现+记录)，应拆分 |
| 07 | 搜索服务 | ★★★☆ | ★★★★ | ★★★☆ | ★★★☆ | ★★★☆ | **7/10** | 含在 Prompt 06 中，未独立执行 |
| 08 | 排行榜 | ★★★☆ | ★★★★ | ★★★☆ | ★★★☆ | ★★★☆ | **7/10** | 含在 Prompt 06 中，未独立执行 |
| 09 | 数据管理 | ★★★☆ | ★★★★ | ★★★☆ | ★★★☆ | ★★★☆ | **7/10** | 作为依赖已完成，无独立 Prompt |
| 10 | 文件持久化 | ★★★★★ | ★★★★★ | ★★★★★ | ★★★★ | ★★★★ | **9/10** | 最佳实践——独立、具体、可验证 |
| 11 | 代码审查 | ★★★★ | ★★★☆ | ★★★★★ | ★★★★ | ★★★★ | **8/10** | 好，但审查和测试应分两次执行 |
| 12 | 测试用例 | ★★★★ | ★★★☆ | ★★★★★ | ★★★★ | ★★★★ | **8/10** | 好，明确的提交格式要求 |
| 14 | README 生成 | ★★★★★ | ★★★★★ | ★★★★★ | ★★★★ | ★★★★ | **9/10** | 任务明确，产出直接可审查 |
| 16 | 质量优化 | ★★★★ | ★★★★★ | ★★★★★ | ★★★★★ | ★★★★ | **9/10** | 当前 Prompt 本身的自我评估 |

#### 关键问题总结

1. **多任务合一（最常见问题）**：Prompt 01（设计+提交+日志）、Prompt 06（修复+实现+记录）都包含了多个独立任务。这增加了 AI 出错的概率。
2. **外部依赖过强**：Prompts 03/04/05/10 都直接说"按 Prompt XX 进行"，但 AI 并不知道这些外部 Prompt 的内容。应内联关键要求。
3. **主观评价标准**："逻辑清晰"、"代码风格优雅"这类表述无法量化，应替换为具体规则（如"方法不超过 50 行"、"无循环依赖"）。
4. **缺少验证方法**：多数 Prompt 没有明确的 "完成标准"，如"编译通过"、"所有测试通过"。

#### 优化后的 Prompt 模板

```
## 任务：[任务名称]
## 角色：[Agent 角色]

### 目标
[1-2 句话说明要做什么]

### 具体要求
- [具体约束 1]
- [具体约束 2]
- [具体约束 3]

### 输入
[已有的相关代码/数据]

### 产出要求
- [产出格式/文件]
- [提交类型：[AI-Architect]/[AI-Implementation]/[AI-Review]/[Human]]
- [验证方法：如"编译通过"、"测试通过"]

### 禁止事项
- [不应做的操作]
```

### 我的决定

- ✅ 接受质量评分体系
- ✅ 后续 Prompt 按优化模板编写，避免多任务合一
- ✅ "按 Prompt XX 进行"的写法应改为内联关键要求
- ✅ 每条 Prompt 应包含明确的验证方法

---

## Prompt 使用记录汇总


| #   | 日期         | Agent 角色                  | 目的                   | 状态    |
| --- | ---------- | ------------------------- | -------------------- | ----- |
| 01  | 2026-06-03 | Architect Agent           | 初始类设计                | ✅ 已完成 |
| 01b | 2026-06-03 | Architect Agent           | 新增 Code Review Agent | ✅ 已完成 |
| 02  | 2026-06-03 | Architect Agent           | 接口与枚举设计              | ✅ 已完成 |
| 03  | 2026-06-03 | Implementation Agent      | 模型类实现                | ✅ 已完成 |
| 03b | 2026-06-03 | Human                     | 建立"实现→审查"规则          | ✅ 已完成 |
| 04  | 2026-06-03 | Implementation Agent      | 数据初始化                | ✅ 已完成 |
| 05  | 2026-06-03 | Implementation Agent      | 菜单系统与输入工具            | ✅ 已完成 |
| 05b | 2026-06-03 | Log Agent                 | 四级分层日志工具             | ✅ 已完成 |
| 06  | 2026-06-03 | Implementation Agent      | 认证服务                 | ✅ 已完成 |
| 07  | 2026-06-03 | Implementation Agent      | 搜索与查询服务              | ✅ 已完成 |
| 08  | 2026-06-03 | Implementation Agent      | 排行榜与装备统计             | ✅ 已完成 |
| 09  | 2026-06-03 | Implementation Agent      | 数据管理（增删改）            | ✅ 已完成 |
| 10  | 2026-06-03 | Implementation Agent      | 文件持久化                | ✅ 已完成 |
| 11  | 2026-06-03 | Architect Agent           | 整体代码审查               | ✅ 已完成 |
| 12  | 2026-06-03 | Testing/Reviewer Agent    | 测试用例生成               | ✅ 已完成 |
| 13  | 2026-06-03 | Code Review Agent         | 代码风格审查               | ✅ 已完成 |
| 14  | 2026-06-03 | Documentation Agent       | README 生成            | ✅ 已完成 |
| 15  | 2026-06-03 | Fix Agent                 | Bug 修复（代码风格问题）       | ✅ 已完成 |
| 16  | 2026-06-03 | Prompt Optimization Agent | Prompt 质量优化          | ✅ 已完成 |


