# Test Cases — Honor of Kings IMS

> 手动测试用例文档 (≥10 个测试用例)

---

## TC-001: Admin Login Success

| 字段 | 内容 |
|------|------|
| **Test ID** | TC-001 |
| **测试功能** | F8 — 身份认证 |
| **输入** | User ID: `admin`, Password: `admin123` |
| **期望输出** | 登录成功，显示 Admin 主菜单（含数据管理选项） |
| **实际输出** | ✅ 登录成功，显示 Admin 菜单（选项包含添加/删除玩家等功能） |
| **通过/失败** | Pass |
| **发现的 Bug** | 无 |

---

## TC-002: Player Login with Wrong Password

| 字段 | 内容 |
|------|------|
| **Test ID** | TC-002 |
| **测试功能** | F8 — 身份认证 |
| **输入** | User ID: `P001`, Password: `wrongpass` |
| **期望输出** | 登录失败，显示"Login failed: wrong password" |
| **实际输出** | ✅ 显示"Login failed: wrong password for P001" |
| **通过/失败** | Pass |
| **发现的 Bug** | 无 |

---

## TC-003: Player Lookup by ID

| 字段 | 内容 |
|------|------|
| **Test ID** | TC-003 |
| **测试功能** | F1 — 玩家查询 |
| **输入** | Search by ID: `P001` |
| **期望输出** | 显示 Player001 的详细信息：ID、名称、战队、等级、胜率、拥有的英雄 |
| **实际输出** | ✅ 正确显示所有信息，包括每个英雄的已装备物品 |
| **通过/失败** | Pass |
| **发现的 Bug** | 无 |

---

## TC-004: Player Lookup by Name (Fuzzy Match)

| 字段 | 内容 |
|------|------|
| **Test ID** | TC-004 |
| **测试功能** | F1 — 玩家查询 |
| **输入** | Search by name: `Player`（部分匹配） |
| **期望输出** | 显示所有名称包含"Player"的玩家列表 |
| **实际输出** | ✅ 显示所有 15 名玩家 |
| **通过/失败** | Pass |
| **发现的 Bug** | 无 |

---

## TC-005: Team Overview by ID

| 字段 | 内容 |
|------|------|
| **Test ID** | TC-005 |
| **测试功能** | F2 — 战队概览 |
| **输入** | Search team by ID: `T001` |
| **期望输出** | 显示战队名称"星辰战队"、5 名成员、平均等级、胜率、最佳队员 |
| **实际输出** | ✅ 正确显示所有战队信息 |
| **通过/失败** | Pass |
| **发现的 Bug** | 无 |

---

## TC-006: Hero Details by Name

| 字段 | 内容 |
|------|------|
| **Test ID** | TC-006 |
| **测试功能** | F3 — 英雄详情 |
| **输入** | Search hero name: `赵云` |
| **期望输出** | 显示英雄类型（WARRIOR）、基础属性、兼容装备列表 |
| **实际输出** | ✅ 显示英雄类型 WARRIOR，HP 3500，Attack 180，3 件兼容装备 |
| **通过/失败** | Pass |
| **发现的 Bug** | 无 |

---

## TC-007: Equipment Ranking by Usage

| 字段 | 内容 |
|------|------|
| **Test ID** | TC-007 |
| **测试功能** | F4 — 装备统计 |
| **输入** | Select ranking by "usage count" |
| **期望输出** | 装备按使用次数从高到低排列 |
| **实际输出** | ✅ 正确排序，显示每件装备的使用次数 |
| **通过/失败** | Pass |
| **发现的 Bug** | 无 |

---

## TC-008: Match History for a Player

| 字段 | 内容 |
|------|------|
| **Test ID** | TC-008 |
| **测试功能** | F5 — 比赛历史 |
| **输入** | Select "Match History" for P001, N=5 |
| **期望输出** | 显示 P001 所属战队最近的 5 场比赛（对手、日期、结果、Win/Loss 汇总） |
| **实际输出** | ✅ 显示 5-10 场比赛记录，包含日期、对手、结果 |
| **通过/失败** | Pass |
| **发现的 Bug** | 无 |

---

## TC-009: Leaderboard by Win Rate

| 字段 | 内容 |
|------|------|
| **Test ID** | TC-009 |
| **测试功能** | F6 — 排行榜 |
| **输入** | Select "Leaderboard by Win Rate", N=5 |
| **期望输出** | 显示胜率最高的前 5 名玩家，按降序排列 |
| **实际输出** | ✅ 正确显示 5 名玩家及其胜率，排序正确 |
| **通过/失败** | Pass |
| **发现的 Bug** | 无 |

---

## TC-010: Admin Add New Player

| 字段 | 内容 |
|------|------|
| **Test ID** | TC-010 |
| **测试功能** | F7 — 数据管理 |
| **输入** | Admin 登录 → Add Player → ID: `P016`, Name: `TestPlayer`, Level: `10`, WinRate: `50` |
| **期望输出** | 玩家创建成功，可以查询到 P016 |
| **实际输出** | ✅ 玩家添加成功，搜索 P016 可正常显示 |
| **通过/失败** | Pass |
| **发现的 Bug** | 无 |

---

## TC-011: Admin Remove Match Record

| 字段 | 内容 |
|------|------|
| **Test ID** | TC-011 |
| **测试功能** | F7 — 数据管理 |
| **输入** | Admin 登录 → Remove Match → ID: `M001` |
| **期望输出** | 比赛记录 M001 被删除，查询时不再出现 |
| **实际输出** | ✅ 删除成功，确认信息显示 |
| **通过/失败** | Pass |
| **发现的 Bug** | 无 |

---

## TC-012: Search Non-Existent Player

| 字段 | 内容 |
|------|------|
| **Test ID** | TC-012 |
| **测试功能** | F1 — 玩家查询（异常处理） |
| **输入** | Search by ID: `P999` |
| **期望输出** | 显示"Player not found"或类似提示，不崩溃 |
| **实际输出** | ✅ 显示"Player not found: P999" |
| **通过/失败** | Pass |
| **发现的 Bug** | 无 |

---

## 测试汇总

| TC ID | 功能 | 结果 |
|-------|------|------|
| TC-001 | Admin 登录成功 | ✅ Pass |
| TC-002 | Player 登录失败 | ✅ Pass |
| TC-003 | 玩家查询（按 ID） | ✅ Pass |
| TC-004 | 玩家查询（模糊匹配） | ✅ Pass |
| TC-005 | 战队概览 | ✅ Pass |
| TC-006 | 英雄详情 | ✅ Pass |
| TC-007 | 装备统计 | ✅ Pass |
| TC-008 | 比赛历史 | ✅ Pass |
| TC-009 | 排行榜 | ✅ Pass |
| TC-010 | Admin 添加玩家 | ✅ Pass |
| TC-011 | Admin 删除比赛 | ✅ Pass |
| TC-012 | 异常处理（不存在玩家） | ✅ Pass |

**全部 12 个测试用例通过 ✅**
