# Design Document — Honor of Kings IMS

> AI-Assisted OOP Coursework

---

## 1. 系统架构

### 1.1 三层架构

```
┌─────────────────────────────────────────────────────────────┐
│                      Presentation Layer                      │
│                   Main.java (Menu System)                    │
│                   InputHelper.java (Input)                   │
├─────────────────────────────────────────────────────────────┤
│                       Service Layer                          │
│  AuthenticationService  SearchService  RankingService        │
│  GameDataManager  FileStorageService                         │
├─────────────────────────────────────────────────────────────┤
│                        Model Layer                           │
│  Person  Player  Admin  Hero  Equipment  Team  MatchRecord   │
│  Searchable  Reportable  Persistable                         │
│  Role  HeroType  EquipmentType  MatchResult                  │
├─────────────────────────────────────────────────────────────┤
│                       Utility Layer                          │
│  DataInitializer  GameLogger                                 │
└─────────────────────────────────────────────────────────────┘
```

### 1.2 设计模式

| 模式 | 使用位置 | 说明 |
|------|---------|------|
| **DAO 模式** | GameDataManager | 中央数据访问对象，封装所有 CRUD 操作 |
| **服务层模式** | AuthenticationService, SearchService, RankingService | 业务逻辑与展示分离 |
| **策略模式** | RankingService | 多种排序策略（按胜率/等级/场次/自定义分数） |
| **工厂方法** | DataInitializer.initData() | 静态工厂创建初始数据集 |

---

## 2. 类设计详述

### 2.1 继承体系

```
Person (abstract)
├── id: String
├── name: String
├── role: Role
├── abstract getDescription(): String
│
├── Player extends Person
│   ├── teamId: String
│   ├── level: int (1-30)
│   ├── winRate: double (0-100)
│   ├── ownedHeroes: List<Hero>
│   └── equippedItems: Map<String, List<String>>
│
└── Admin extends Person
    ├── adminLevel: int
    └── permissions: List<String>
```

**设计决策**:
- Person 设为 abstract，因为系统中不存在独立的"人"，只有具体的 Player 和 Admin
- Role 枚举用于 `getInfo()` 输出，而非用于权限控制（权限通过 instanceof 检查）

### 2.2 关联关系

| 关系 | 类型 | 说明 |
|------|------|------|
| Player → Hero | 1 对多 (关联) | Player 拥有多个 Hero 对象 |
| Hero → Equipment | 多对多 (关联) | Hero 有兼容装备列表 |
| Player → Equipment | 1 对多 (间接) | 通过 equippedItems (Map) 跟踪 |
| Team → Player | 1 对多 (聚合) | Team 包含 Player，Player 可脱离 Team 存在 |

### 2.3 接口设计

| 接口 | 方法 | 实现类 |
|------|------|--------|
| `Searchable<T>` | `searchById()`, `searchByName()` | SearchService |
| `Reportable` | `getInfo()` | Player, Admin, Hero, Equipment, Team, MatchRecord |
| `Persistable` | `toCSVString()`, `fromCSVString()` | Player, Admin, Hero, Equipment, Team, MatchRecord |

### 2.4 枚举设计

| 枚举 | 常量 | 用途 |
|------|------|------|
| `Role` | ADMIN, PLAYER | 区分用户类型 |
| `HeroType` | WARRIOR, MAGE, ASSASSIN, TANK, MARKSMAN, SUPPORT, JUNGLER | 英雄分类 |
| `EquipmentType` | OFFENSIVE, DEFENSIVE, MOVEMENT, MAGIC, JUNGLE | 装备分类 |
| `MatchResult` | WIN, LOSE, DRAW | 比赛结果 |

---

## 3. 数据持久化设计

### 3.1 CSV 文件格式

7 个 CSV 文件，UTF-8 编码，存放在 `data/` 目录：

| 文件 | 字段 | 示例 |
|------|------|------|
| `equipment.csv` | equipmentId,name,type,stat1:val1\|stat2:val2,usageCount | E001,无尽战刃,OFFENSIVE,attack:100,critRate:25,0 |
| `heroes.csv` | heroId,name,type,stat1:val1\|...,equipId1\|equipId2\|... | H001,赵云,WARRIOR,hp:3500,attack:180|E001|E002|E003 |
| `passwords.csv` | id,password | admin,admin123 |
| `players.csv` | id,name,password,teamId,level,winRate,heroId1\|... | P001,Player001,pass123,T001,15,52.5|H001|H002|H003 |
| `equipped_items.csv` | playerId,heroId,equipId1\|equipId2\|... | P001,H001,E001|E002|E003 |
| `teams.csv` | teamId,name,playerId1\|playerId2\|... | T001,星辰战队,P001|P002|P003|P004|P005 |
| `matches.csv` | matchId,date,teamA,teamB,result,heroPick1\|... | M001,2026-05-15,T001,T002,WIN|H001|H002|H003|H004|H005 |

### 3.2 加载顺序

依赖关系要求严格的加载顺序：
1. Equipment（无依赖）
2. Heroes（依赖 Equipment 引用）
3. Passwords（无依赖）
4. Players（依赖 Heroes 引用）
5. Equipped Items（依赖 Players + Heroes + Equipment）
6. Teams（依赖 Players 引用）
7. Matches（依赖 Teams + Heroes 引用）

---

## 4. 排行榜公式

### 4.1 自定义分数

```
customScore = winRate × 0.5 + level × 2.0 + matchCount × 0.1
```

| 参数 | 权重 | 说明 |
|------|------|------|
| winRate | 0.5 | 胜率，范围 0-100 |
| level | 2.0 | 等级，范围 1-30 |
| matchCount | 0.1 | 比赛场次 |

### 4.2 平局处理

多级排序（tie-breaking）：
1. 按主要指标降序排列
2. 若主要指标相同，按 ID 升序排列（确保确定性）
3. 例如：胜率相同时，等级高的优先；等级也相同，ID 小的优先

---

## 5. 安全设计

- 密码以明文存储在 passwords.csv 中（课程项目，不涉及真实安全需求）
- 会话管理：AuthenticationService 维护当前登录用户引用
- 权限控制：Admin 可执行 CRUD，Player 仅可查看
- 输入验证：所有用户输入经过 InputHelper 验证（范围检查、null 检查）
- 防御性拷贝：所有集合类型 getter 返回不可修改的副本

---

## 6. 日志设计

GameLogger 4 级日志系统：

| 级别 | 优先级 | 用途 |
|------|--------|------|
| DEBUG | 0 | 开发调试信息（CRUD 操作等） |
| INFO | 1 | 一般操作信息（登录、登出） |
| WARN | 2 | 警告信息（登录失败、文件缺失） |
| ERROR | 3 | 错误信息（异常、操作失败） |

日志输出格式：`[LEVEL] [timestamp] [source] message`
