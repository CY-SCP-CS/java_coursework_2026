# Prompts Record — AI-Assisted Honor of Kings IMS

> **开发环境**: Claude Code CLI (底层模型: deepseek-v4-flash)
> **工具说明**: Claude Code 是一个 CLI 编程助手，通过工具调用协议与模型交互，可直接读写文件、执行命令、管理 Git
> **使用方式**: 通过与 Claude Code 的对话完成所有 AI 辅助开发

---

## Prompt 使用说明

### Agent 角色映射

由于使用 **Claude Code CLI** 作为唯一的 AI 工具，采用**分阶段切换角色**的方式来实现多 Agent 效果：

| 阶段 | 角色 | 职责 | 对话策略 |
|------|------|------|---------|
| 阶段 2 | **Architect Agent** | 类设计、UML、模块规划 | 明确要求 AI 仅做设计，不写实现代码 |
| 阶段 3-6 | **Implementation Agent** | 按功能模块逐步实现 | 每次只聚焦一个功能，提供完整上下文 |
| 阶段 7 | **Testing/Reviewer Agent** | 代码审查、找 bug、测试 | 要求 AI 以审查者视角检查已有代码 |

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
**相关提交**: 9dc43e2

### 我的 Prompt

```
我正在做一个 Java OOP 课程项目：王者荣耀信息管理系统 (Honor of Kings IMS)。

需求概要：
- 控制台应用程序
- 管理玩家、英雄、装备、战队、比赛记录
- 需要搜索、排名、数据管理、认证功能
- 必须使用继承、接口、集合、枚举、异常处理、文件 I/O

请帮我设计类结构和模块划分。具体包括：
1. 每个类的核心字段和方法
2. 类之间的关系（继承、关联、聚合）
3. 需要哪些接口
4. 包结构建议

已有需求类：
- Person（抽象父类）
- Player（继承 Person）
- Admin（继承 Person）
- Hero（英雄）
- Equipment（装备）
- Team（战队）
- MatchRecord（比赛记录）

限制：
- 先不做 GUI，仅控制台
- Player 和 Admin 继承 Person
- Team 包含多个 Player（聚合）
- Player 拥有多个 Hero（关联）
- Hero 可装备多个 Equipment（关联）
- 至少需要 1 个接口

请不要写完整的实现代码，只做设计建议。
```

### AI 响应摘要

AI 建议了完整的类结构设计：
1. **Person (抽象类)**: 包含 id, name, role 字段，提供抽象方法 getDescription()
2. **Player extends Person**: 增加 teamId, level, winRate, ownedHeroes（List\<Hero\>），提供防御性拷贝
3. **Admin extends Person**: 增加 adminLevel 字段
4. **Hero**: heroId, name, heroType (枚举), baseStats (Map), compatibleEquipment (List\<Equipment\>)
5. **Equipment**: equipmentId, name, equipmentType (枚举), stats (Map), usageCount
6. **Team**: teamId, name, members (List\<Player\>)，含平均等级、胜率、最佳队员计算方法
7. **MatchRecord**: matchId, date (LocalDate), teamA, teamB, result (枚举), heroPicks (List\<String\>)
8. **接口建议**: Searchable\<T\>（searchById/searchByName），Persistable/Reportable 可选
9. **包结构建议**: model, service, util

### 我的决定

**全部接受**，但做了以下调整：
- 新增 `Reportable` 接口替代 Persistable，因为当前阶段重点是信息展示而非持久化
- 接受 Searchable\<T\> 泛型接口的设计
- Team 的 getMembers() 采用防御性拷贝返回 ArrayList 拷贝
- 所有类的集合字段在构造方法中初始化为空集合，避免 NullPointerException
- 添加了 equals() 和 hashCode() 的重写（基于 ID 字段）

---

## Prompt 02 — Architect Agent: 接口与枚举设计

**时间**: 2026-06-03 14:40
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Architect Agent
**相关提交**: 7eac0a6

### 我的 Prompt

```
基于上一步确定的类结构，请进一步设计：

1. 接口定义
   建议至少包含以下接口之一的具体方法签名：
   - Searchable: 搜索相关
   - Persistable: 持久化相关
   - Reportable: 报告/展示相关

2. 枚举定义
   建议以下枚举的常量值：
   - HeroType: 英雄类型
   - EquipmentType: 装备类型
   - MatchResult: 比赛结果
   - Role: 用户角色

3. 每个类的构造方法参数建议
4. equals() 和 hashCode() 的重写建议（哪些类需要）

请以 Java 代码片段形式展示接口和枚举的骨架。
```

### AI 响应摘要

AI 提出了以下接口和枚举设计方案：

**接口设计：**
1. `Searchable<T>` — `searchById(String id): T`, `searchByName(String name): List<T>`
2. `Persistable` — `toCSVString(): String`, `fromCSVString(String csvLine): void`
3. `Reportable` — `getInfo(): String`

**枚举常量：**
1. `Role`: ADMIN, PLAYER
2. `HeroType`: WARRIOR, MAGE, ASSASSIN, TANK, MARKSMAN, SUPPORT, JUNGLER
3. `EquipmentType`: OFFENSIVE, DEFENSIVE, MOVEMENT, MAGIC, JUNGLE
4. `MatchResult`: WIN, LOSE, DRAW

**构造方法建议：**
- 每个类提供无参构造 + 全参构造（ID 必填）
- 集合字段在无参构造中初始化为空集合

**equals/hashCode 建议：**
- 所有实体类基于 ID 字段重写 equals() 和 hashCode()
- 使用 Objects.equals() 和 Objects.hash() 工具方法

### 我的决定

**全部接受**，并做了以下实施：
- ✅ `Searchable<T>` — 泛型接口，searchById 返回 T，searchByName 返回 List<T>
- ✅ `Reportable` — getInfo() 返回格式化字符串，所有模型类均已实现
- ✅ `Persistable` — toCSVString() 已实现（含 stats map 序列化、集合 ID 引用），fromCSVString 留待 FileStorageService 阶段
- ✅ 4 个枚举均已创建，覆盖所有需求常量
- ✅ 每个类均有无参 + 有参构造方法，集合字段预初始化为空集合
- ✅ 所有实体类基于 ID 重写 equals() 和 hashCode()

---

## Prompt 03 — Implementation Agent: 模型类实现

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: （待填写）

### 我的 Prompt

```
请实现以下 Java 模型类。要求：
- 所有字段 private
- 提供 getter/setter（注意不变对象的处理）
- 有参构造方法 + 无参构造方法
- 合理的 toString()
- 必要的 equals() 和 hashCode()

类清单：
1. Person (abstract)
   - id: String, name: String, role: Role
   - 抽象方法: getDescription(): String

2. Role 枚举: ADMIN, PLAYER

3. Player extends Person
   - teamId: String
   - level: int (1-30)
   - winRate: double (0.0-100.0)
   - ownedHeroes: List<Hero>
   - ownedHeroes 用 ArrayList 初始化，getter 返回防御性拷贝

4. Admin extends Person
   - adminLevel: int
   - 不需要额外复杂字段

5. Team
   - teamId: String, name: String
   - members: List<Player>
   - 方法: addMember(), removeMember(), getAverageLevel(), getWinRate()

6. HeroType 枚举: WARRIOR, MAGE, ASSASSIN, TANK, MARKSMAN, SUPPORT, JUNGLER

7. Hero
   - heroId: String, name: String
   - heroType: HeroType
   - baseStats: Map<String, Integer> (如 "hp", "attack", "defense")
   - compatibleEquipment: List<Equipment>

8. EquipmentType 枚举: OFFENSIVE, DEFENSIVE, MOVEMENT, MAGIC, JUNGLE

9. Equipment
   - equipmentId: String, name: String
   - equipmentType: EquipmentType
   - stats: Map<String, Integer> (提供的属性加成)
   - usageCount: int

10. MatchResult 枚举: WIN, LOSE, DRAW

11. MatchRecord
    - matchId: String
    - date: LocalDate
    - teamA: String (战队 ID)
    - teamB: String (战队 ID)
    - result: MatchResult
    - heroPicks: List<String> (使用的英雄 ID 列表)

请生成上述所有类的完整代码，每个类一个独立文件。
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 04 — Implementation Agent: 数据初始化

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: （待填写）

### 我的 Prompt

```
请帮我创建一个 DataInitializer 类，用于初始化 Honor of Kings 系统的硬编码数据集。

要求：
- 包路径: util
- 包含一个静态方法 initData(): GameDataManager
- 返回填充好数据的 GameDataManager 实例

数据要求（满足课程最低标准）：
1. 装备 20 件
   - 至少覆盖 OFFENSIVE, DEFENSIVE, MOVEMENT, MAGIC, JUNGLE 类型
   - 每件装备有合理的 stats map（如 "attack": 100, "defense": 50）
   - 示例：无尽战刃 (OFFENSIVE, +130 attack), 不祥征兆 (DEFENSIVE, +270 defense), 抵抗之靴 (MOVEMENT, +110 defense)

2. 英雄 15 个
   - 覆盖所有 HeroType
   - 每个英雄有基础 stats map
   - 每个英雄至少兼容 2 件装备
   - 示例：李白 (ASSASSIN), 鲁班七号 (MARKSMAN), 诸葛亮 (MAGE), 程咬金 (TANK)

3. 玩家 10 名
   - 每个玩家至少拥有 3 个英雄
   - 每个玩家的英雄至少装备 1 件装备
   - 合理的 level (1-30) 和 winRate (40.0-70.0)

4. 战队 3 支
   - 每队至少 5 名队员
   - 使用玩家 ID 进行分配

5. 比赛记录 10 条
   - 合理的结果分布
   - 包含使用的英雄 ID

6. 默认账号
   - admin / admin123 (ADMIN)
   - player1 / pass123 (PLAYER)
   - player2 / pass123 (PLAYER)

请使用中文游戏术语（英雄名、装备名）。
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 05 — Implementation Agent: 菜单系统与输入工具

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: （待填写）

### 我的 Prompt

```
请实现输入工具类和控制台菜单系统。

### InputHelper (util/InputHelper.java)
封装 Scanner 操作，提供以下静态方法：
1. readInt(String prompt): int — 显示提示，读取整数，处理非数字输入
2. readString(String prompt): String — 显示提示，读取非空字符串
3. readIntRange(String prompt, int min, int max): int — 读取范围内的整数
4. readYesNo(String prompt): boolean — 读取 Y/N 确认

要求：
- 处理无效输入（不是数字、超出范围、空字符串）
- 给出清晰的错误提示
- 使用 try-catch 处理 InputMismatchException

### 主菜单 (Main.java)
采用循环菜单结构：

=== Honor of Kings IMS ===
1. Login
2. Exit

登录后（Player 菜单）：
=== Player Menu ===
1. View My Profile
2. View My Heroes
3. View My Match History
4. Search Player
5. View Team
6. View Hero Details
7. Equipment Statistics
8. Leaderboard
9. Logout

登录后（Admin 菜单）：
=== Admin Menu ===
1. Manage Players
2. Manage Heroes
3. Manage Equipment
4. Manage Teams
5. Manage Match Records
6. View Player Lookup
7. View Team Overview
8. View Hero Details
9. Equipment Statistics
10. Leaderboard
11. Logout

要求：
- 使用 switch 或 if-else 分发菜单选项
- 每个选项调用对应的 service 方法
- 非法选项给出提示并重新显示菜单
- 不在此类中实现具体业务逻辑，只做分发
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 06 — Implementation Agent: 认证服务

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: （待填写）

### 我的 Prompt

```
请实现 AuthenticationService。

功能要求：
1. login(String id, String password): Person
   - 验证用户 ID 和密码
   - 成功返回 Person 对象，失败返回 null 或抛出异常
   - 记录当前登录用户

2. logout(): void
   - 清除当前登录会话

3. getCurrentUser(): Person
   - 返回当前登录用户

4. isLoggedIn(): boolean
   - 检查是否有用户登录

5. isAdmin(): boolean
   - 检查当前用户是否为 Admin

数据模型：
- 用户数据存储在 GameDataManager 中
- 每个 Person 有 id, password, role (ADMIN/PLAYER)

请同时实现一个简单的密码验证，密码用字符串存储即可（不需要加密）。
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 07 — Implementation Agent: 搜索与查询服务

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: （待填写）

### 我的 Prompt

```
请实现 SearchService，提供以下查询功能。

现有模型类：
- Team: teamId, name, members (List<Player>)
- Player: id, name, teamId, level, winRate, ownedHeroes (List<Hero>)
- Hero: heroId, name, heroType, baseStats, compatibleEquipment (List<Equipment>)
- Equipment: equipmentId, name, equipmentType, stats, usageCount
- MatchRecord: matchId, date, teamA, teamB, result, heroPicks

### 方法列表

1. searchPlayerById(String id): Player
   - 精确查找，找不到抛出 PlayerNotFoundException

2. searchPlayerByName(String name): List<Player>
   - 模糊匹配（包含即可），大小写不敏感

3. searchTeamById(String id): Team
   - 精确查找

4. searchTeamByName(String name): Team
   - 精确匹配名称

5. searchHeroByName(String name): List<Hero>
   - 模糊匹配

6. displayPlayerDetail(Player player): String
   - 返回格式化的玩家详细信息字符串
   - 包含：ID、名称、战队、等级、胜率、拥有的英雄（含装备）

7. displayTeamDetail(Team team): String
   - 返回：战队名、所有成员、平均等级、总比赛场次、胜率、最佳队员

8. displayHeroDetail(Hero hero): String
   - 返回：英雄名、类型、基础属性、兼容装备、拥有该英雄的玩家

9. getMatchHistory(String playerOrTeamId, int n): List<MatchRecord>
   - 返回最近 N 场比赛
   - 根据 ID 前缀判断是玩家还是战队（或分别提供两个方法）

10. displayMatchHistory(List<MatchRecord> records, int n): String
    - 格式化显示比赛历史
    - 包含：对手、日期、结果、使用英雄

请用 ArrayList/HashMap 实现数据存储和查找。
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 08 — Implementation Agent: 排行榜与装备统计

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: （待填写）

### 我的 Prompt

```
请实现 RankingService，提供排行榜和装备统计功能。

### 排行榜方法

1. getLeaderboardByWinRate(int topN): List<Player>
   - 按胜率降序排列
   - 平局处理：胜率相同时按等级降序排列

2. getLeaderboardByLevel(int topN): List<Player>
   - 按等级降序排列
   - 平局处理：等级相同时按胜率降序排列

3. getLeaderboardByMatches(int topN): List<Player>
   - 按比赛场次降序排列
   - 平局处理：场次相同时按胜率降序排列

4. displayLeaderboard(List<Player> players, String metric): String
   - 格式化显示排行榜
   - 显示排名、玩家名、战队、对应指标值

### 装备统计方法

5. getEquipmentRankingByUsage(): List<Equipment>
   - 按 usageCount 降序排列

6. getEquipmentRankingByHeroCount(Map<String, Integer> heroCountMap): List<Equipment>
   - 按使用该装备的英雄数量降序排列

7. displayEquipmentRanking(List<Equipment> list, String metric): String
   - 格式化显示装备排名

### 自定义排名公式

8. getCustomScore(Player player): double
   公式：customScore = winRate * 0.5 + level * 2.0 + matchesCount * 0.1
   说明：
   - 胜率权重 0.5（强调胜率的重要性）
   - 等级权重 2.0（等级反映玩家投入度）
   - 场次权重 0.1（场次影响较小，防止刷场次）
   - 平局处理：customScore 相同时按胜率降序

### 辅助方法

9. getPlayerMatchCount(String playerId): int
   - 统计玩家参与的比赛场次

使用 Collections.sort() 或 List.sort() 配合 Comparator 实现排序。
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 09 — Implementation Agent: 数据管理（增删改）

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: （待填写）

### 我的 Prompt

```
请完善 GameDataManager 类，实现 Admin 的数据管理功能。

### GameDataManager 已有数据存储
- Map<String, Player> players
- Map<String, Admin> admins
- Map<String, Hero> heroes
- Map<String, Equipment> equipmentMap
- Map<String, Team> teams
- List<MatchRecord> matchRecords
- Map<String, String> userPasswords (ID -> password)

### 需要实现的方法

#### 玩家管理
1. addPlayer(Player player, String password): void
   - ID 不能重复，重复抛出 DuplicateIdException
2. removePlayer(String playerId): void
   - 同时从战队中移除
   - 找不到抛出 RecordNotFoundException
3. updatePlayer(Player player): void
   - 按 ID 更新玩家信息

#### 英雄管理
4. addHero(Hero hero): void
5. removeHero(String heroId): void
   - 同时从所有玩家的 ownedHeroes 中移除
6. updateHero(Hero hero): void

#### 装备管理
7. addEquipment(Equipment equipment): void
8. removeEquipment(String equipmentId): void
9. updateEquipment(Equipment equipment): void

#### 战队管理
10. addTeam(Team team): void
11. removeTeam(String teamId): void
12. updateTeam(Team team): void

#### 比赛记录管理
13. addMatchRecord(MatchRecord record): void
14. removeMatchRecord(String matchId): void

#### 通用
15. getPlayerById(String id): Player
16. getHeroById(String id): Hero
17. getTeamById(String id): Team
18. getAllPlayers(): List<Player>
19. getAllHeroes(): List<Hero>

### 自定义异常类
- DuplicateIdException extends Exception
- RecordNotFoundException extends Exception

请实现这些自定义异常类，并在合适的地方使用。
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 10 — Implementation Agent: 文件持久化

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Implementation Agent
**相关提交**: （待填写）

### 我的 Prompt

```
请实现 FileStorageService，支持从 CSV 文件保存和加载数据。

### 文件格式
使用 CSV 格式，每个实体一个文件：
- data/players.csv
- data/heroes.csv
- data/equipment.csv
- data/teams.csv
- data/matches.csv
- data/passwords.csv

### 方法签名

1. saveAllData(GameDataManager data): void
   - 将所有数据写入 CSV 文件
   - 使用 try-with-resources 确保资源释放

2. loadAllData(): GameDataManager
   - 从 CSV 文件读取所有数据
   - 如果文件不存在，返回空的 GameDataManager
   - 使用 BufferedReader 逐行读取

3. savePlayers(List<Player> players): void
4. loadPlayers(): List<Player>
   （每条记录格式：id,name,role,teamId,level,winRate）

5. saveHeroes(List<Hero> heroes): void
6. loadHeroes(): List<Hero>
   （每条记录格式：heroId,name,heroType,hp,attack,defense,compatibleIds）

7. saveEquipment(List<Equipment> equipment): void
8. loadEquipment(): List<Equipment>

9. saveTeams(List<Team> teams): void
10. loadTeams(): List<Team>

11. saveMatchRecords(List<MatchRecord> records): void
12. loadMatchRecords(): List<MatchRecord>

### 要求
- 处理 FileNotFoundException, IOException
- 每个方法独立处理异常，不向上抛出
- 如果数据目录不存在则创建
- 提供清晰的错误消息
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 11 — Testing/Reviewer Agent: 整体代码审查

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Testing/Reviewer Agent
**相关提交**: （待填写）

### 我的 Prompt

```
请以代码审查者的角色，全面审查以下 Honor of Kings IMS 项目的 Java 代码。

请检查以下几个方面：

1. OOP 设计问题
   - 封装是否合理（字段是否 private，是否暴露了内部数据结构）
   - 继承层次是否正确
   - 接口使用是否合理
   - 是否有不必要的重复代码

2. 集合使用
   - 是否选择了合适的数据结构
   - 遍历时是否有 ConcurrentModificationException 风险

3. 异常处理
   - 是否捕获了不应该捕获的异常
   - 是否有吞掉异常的情况（空的 catch 块）
   - 用户输入验证是否充分

4. 空指针风险
   - 是否有未做 null 检查的地方
   - 方法返回 null 时调用方是否处理了

5. 逻辑错误
   - 排行榜排序是否正确
   - 平局处理是否实现
   - 数据删除时关联数据是否一致更新

请给出具体的文件:行号 和建议。

项目源代码在 src/ 目录下。
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 12 — Testing/Reviewer Agent: 测试用例生成

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Testing/Reviewer Agent
**相关提交**: （待填写）

### 我的 Prompt

```
请为 Honor of Kings IMS 系统生成手动测试用例。

阅读 src/ 目录下的源代码，然后按照以下格式生成至少 10 个测试用例。

要求覆盖以下功能：
1. 玩家查询（按 ID / 按名称，包括存在和不存在的情况）
2. 战队概览（正常显示）
3. 英雄详情（正常显示）
4. 装备统计（排名是否正确）
5. 比赛历史（最近 N 场过滤）
6. 排行榜（排序 + 平局）
7. 登录认证（成功 / 失败）
8. Admin 数据管理（添加玩家）
9. 异常输入（无效 ID、空输入、越界数字）
10. 文件持久化（保存后加载验证）

每个测试用例格式：
```markdown
## TC-XXX: 测试标题

**测试功能**: 功能名称
**前置条件**: 测试前需要满足的条件
**输入**: 具体操作步骤和输入值
**期望输出**: 系统应该显示什么
**实际输出**: （运行时填写）
**结果**: Pass / Fail
**发现的 Bug**: （如果有）
```
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 13 — Code Review Agent: 代码风格与高内聚低耦合审查

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Code Review Agent
**相关提交**: （待填写）

### 我的 Prompt

```
请作为 Code Review Agent，对以下 Java 代码进行**代码质量审查**。

审查重点：

### 1. 代码风格 (Code Style)
- 命名规范：类名 PascalCase，方法/变量名 camelCase，常量 UPPER_SNAKE_CASE
- 缩进和空格是否一致
- 是否有过长的方法（超过 30 行应该考虑拆分）
- 是否有过深的嵌套（超过 3 层应该考虑提取方法）
- 是否有 magic number / magic string 未定义为常量
- Javadoc 注释是否完整（所有 public 方法需要有 @param @return 注释）

### 2. 高内聚 (High Cohesion)
- 每个类的职责是否单一？一个类是否做了太多不相关的事情？
- 类中的方法是否都围绕同一核心职责？
- 是否有可以提取到独立类中的功能簇？

### 3. 低耦合 (Low Coupling)
- 类之间的依赖关系是否合理？
- 是否过度依赖具体实现而不是接口/抽象类？
- 是否存在循环依赖？（A -> B -> A）
- 是否可以直接访问其他类的内部数据（破坏封装）？

### 4. 设计原则 (SOLID)
- 单一职责原则：每个类/方法是否只有一个改变理由
- 开闭原则：是否对扩展开放、对修改封闭
- 里氏替换：子类是否能完全替代父类
- 接口隔离：接口是否足够小和专一
- 依赖倒置：是否依赖抽象而非具体实现

请针对 src/ 目录下的代码，给出：
1. 整体评价（评分 1-10）
2. 每个问题定位到具体文件:行号
3. 每个问题的严重程度（critical / major / minor）
4. 每个问题的具体改进建议
5. 重构优先级排序

格式要求：
```markdown
## [严重程度] 问题描述
- **文件**: src/model/Xxx.java
- **行号**: 25-30
- **问题**: ...
- **建议**: ...
```
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 14 — Documentation Agent: README 生成

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Documentation Agent
**相关提交**: （待填写）

### 我的 Prompt

```
请根据以下信息生成 README.md。

项目名称：AI-Assisted Honor of Kings Information Management System
课程：Java OOP Coursework 2026
总分：20 marks

### 功能清单
- 玩家查询（按 ID/名称）
- 战队概览
- 英雄详情
- 装备统计（按使用率排名）
- 比赛历史（最近 N 场）
- 排行榜（胜率/等级/场次/自定义分数）
- 数据管理（Admin 增删改）
- 认证系统（Admin/Player 双角色）
- 文件持久化（CSV 格式）

### Java 概念使用
- 继承：Person -> Player, Admin
- 接口：Searchable / Persistable
- 集合：ArrayList, HashMap
- 枚举：HeroType, EquipmentType, MatchResult, Role
- 异常处理：自定义 DuplicateIdException, RecordNotFoundException
- 文件 I/O：CSV 读写
- 多态：Person 引用指向子类对象

### 默认账号
| 用户名 | 密码 | 角色 |
| admin | admin123 | Admin |
| player1 | pass123 | Player |
| player2 | pass123 | Player |

### 运行方式
1. 编译：javac src/**/*.java -d out
2. 运行：java -cp out Main

请按以下结构生成 README：
1. Project Overview
2. How to Run
3. Default Login Accounts
4. Implemented Features
5. Java Concepts Used
6. AI Usage Summary
7. Testing Summary
8. Known Limitations

注意：Known Limitations 部分留空，等开发完成后再填写。
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 15 — Fix/Refactor Agent: Bug 修复

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Refactoring/Fix Agent
**相关提交**: （待填写）

### 我的 Prompt

```
以下代码存在一个 bug：[描述 bug 现象]

[粘贴有 bug 的代码]

请：
1. 指出 bug 的根本原因
2. 解释为什么会出现这个 bug
3. 给出最小的修复方案
4. 不要重写不相关的代码

我想理解这个 bug 而不是仅仅得到一个修复。
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 16 — Prompt Optimization Agent: Prompt 质量优化

**时间**: 2026-06-（待填写）
**工具/模型**: Claude Code (deepseek-v4-flash)
**Agent 角色**: Prompt Optimization Agent
**相关提交**: （待填写）

### 我的 Prompt

```
请作为 Prompt Optimization Agent，对以下我准备发给 AI 的 prompt 进行审查和优化。

我的原始 prompt：
```
[粘贴原始 prompt]
```

请从以下维度分析和优化：

### 1. 清晰度 (Clarity)
- 目标是否明确？AI 是否知道要输出什么？
- 是否有模糊的表述需要具体化？
- 是否指定了输出格式？（代码/解释/设计/建议）

### 2. 上下文 (Context)
- 是否提供了足够的背景信息？
- 是否给出了相关代码或现有类结构？
- 是否说明了当前开发阶段？

### 3. 约束 (Constraints)
- 是否明确了限制条件？（不要写什么、不要用什么技术）
- 是否指定了 Java 版本、包名等？
- 是否要求 AI 说明假设和边界情况？

### 4. 可验证性 (Verifiability)
- 能否判断 AI 的输出是否正确？
- 是否要求 AI 解释而不是只给代码？
- 是否要求 AI 指出不确定的地方？

### 5. 分步 (Granularity)
- 是否一次只请求一个功能/任务？
- 是否可以将 prompt 拆分为更小的子任务？

请输出：
1. 原始 prompt 评分（每维度 1-10 分）
2. 各维度主要问题
3. 优化后的 prompt 版本
4. 优化说明
```

### AI 响应摘要

（待填写）

### 我的决定

（待填写）

---

## Prompt 使用记录汇总

| # | 日期 | Agent 角色 | 目的 | 状态 |
|---|------|-----------|------|------|
| 01 | 2026-06-03 | Architect Agent | 初始类设计 | ✅ 已完成 |
| 02 | 2026-06-03 | Architect Agent | 接口与枚举设计 | ✅ 已完成 |
| 03 | 2026-06- | Implementation Agent | 模型类实现 | 待完成 |
| 04 | 2026-06- | Implementation Agent | 数据初始化 | 待完成 |
| 05 | 2026-06- | Implementation Agent | 菜单系统与输入工具 | 待完成 |
| 06 | 2026-06- | Implementation Agent | 认证服务 | 待完成 |
| 07 | 2026-06- | Implementation Agent | 搜索与查询服务 | 待完成 |
| 08 | 2026-06- | Implementation Agent | 排行榜与装备统计 | 待完成 |
| 09 | 2026-06- | Implementation Agent | 数据管理（增删改） | 待完成 |
| 10 | 2026-06- | Implementation Agent | 文件持久化 | 待完成 |
| 11 | 2026-06- | Testing/Reviewer Agent | 整体代码审查 | 待完成 |
| 12 | 2026-06- | Testing/Reviewer Agent | 测试用例生成 | 待完成 |
| 13 | 2026-06- | Code Review Agent | 代码风格与高内聚低耦合审查 | 待完成 |
| 14 | 2026-06- | Documentation Agent | README 生成 | 待完成 |
| 15 | 2026-06- | Fix Agent | Bug 修复 | 待完成 |
| 16 | 2026-06- | Prompt Optimization Agent | Prompt 质量审查与优化 | 待完成 |
