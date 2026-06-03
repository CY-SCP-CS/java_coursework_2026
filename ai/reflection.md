# Reflection — AI-Assisted Honor of Kings IMS

> 课程: Java OOP | 2026 Spring

---

## 1. 你使用了哪些 AI 工具或模型？

本项目全程使用 **Claude Code CLI**（Anthropic 的 Claude 模型），具体为 Claude Opus 4.6 和 Sonnet 4.6 模型。所有 AI 交互均通过 CLI 工具在终端中完成，未使用其他 AI 工具（如 ChatGPT、GitHub Copilot 等）。

## 2. 哪个 prompt 最有用？为什么？

**Prompt 01（Architect Agent — 类设计）** 是最有用的 prompt。原因：
- 它奠定了整个项目的架构基础，定义了 7 个核心模型类、3 个接口、4 个枚举
- 清晰的包结构（model/service/util）划分让后续开发有条不紊
- 接口设计（Searchable、Reportable、Persistable）为后续功能扩展提供了契约
- 这个 prompt 让项目从一开始就有良好的 OOP 结构，避免了后期重构

## 3. 哪个 AI 生成的建议是错误的、不完整的或有误导性的？

**Prompt 10（FileStorageService）** 的初始实现中有几个问题：
- CSV 解析时未处理行尾空格和空行，导致加载某些文件时报错
- `fromCSVString()` 方法被留为空 stub，接口契约未兑现，而是将实现逻辑放在了 FileStorageService 中
- 装备的物品（equipped items）与玩家的关联使用 CSV ID 引用，但加载顺序需要严格依赖（装备 → 英雄 → 玩家），如果顺序错了会加载失败

这些问题在后续的 Code Review 和 Testing 阶段被发现并修复。

## 4. 你如何检查 AI 生成的代码是否正确？

我使用了多种方法：
1. **编译检查**：每次 AI 生成代码后立刻 `javac` 编译，确保没有语法错误
2. **运行测试**：179 个 JUnit 测试覆盖了所有核心逻辑，任何回归都能被捕获
3. **Code Review Agent**：专门的 Review Agent 审查代码风格、设计模式、潜在 bug
4. **手动运行程序**：通过菜单系统实际操作，验证功能是否正确
5. **代码阅读**：逐行阅读 AI 生成的关键代码，确保理解每行逻辑

## 5. 你自己修复了哪些 bug 而没有让 AI 修复？

人工修复的 bug：
1. **Scanner nextLine() 残留问题**：AI 最初使用 `nextInt()` + `nextLine()` 混用，导致输入跳过。我统一改为 `nextLine()` + 手动 `Integer.parseInt()` 策略
2. **CSV 文件编码问题**：AI 在一些地方使用了平台默认编码，我统一改为 UTF-8
3. **测试数据污染**：某些测试修改了共享的 `GameDataManager` 静态实例，影响其他测试。我将这些测试改为使用隔离的数据或非存在 ID
4. **Javadoc 注释未闭合**：`RankingService.java` 中一个 `/**` 注释没有用 `*/` 关闭，导致后续代码被吞入注释块，编译报错

## 6. 使用 AI 后你更好地理解了哪个 Java 概念？

**防御性拷贝（Defensive Copying）** 和 **集合封装**：
AI 在 `getMembers()`、`getOwnedHeroes()` 等方法中返回 `new ArrayList<>(members)` 而不是直接返回内部引用。我最初不理解为什么要这样做，通过 AI 的解释我明白了：如果不防御性拷贝，外部代码可以修改集合内容，破坏封装性。这让我对"封装不是仅仅 `private` 字段"有了更深的理解。

## 7. 你对哪个 Java 概念仍然不确定？

**`Comparator` 链式比较** 和 **流式 API 的深层用法**：
在 `RankingService` 中，AI 使用了 `Comparator.comparing(...).thenComparing(...)` 链式写法来实现多级排序和 tie-breaking。我大致理解其工作原理，但如果要我手写一个复杂的多条件排序链，我仍然需要查阅文档。此外，`Collectors.groupingBy()` 和 `Collectors.mapping()` 的组合使用我也还在学习中。

## 8. AI 让项目变得更容易、更难，还是两者兼有？请解释。

**两者兼有。**

**更容易的地方：**
- 架构设计更快：AI 能快速给出合理的 OOP 结构建议
- 重复代码生成更快：DataInitializer 中 20 件装备、15 个英雄的初始化数据代码生成非常高效
- 测试覆盖率更高：AI 能快速生成大量边界测试用例

**更难的地方：**
- 需要审查 AI 输出：每段 AI 代码都需要理解后才能使用，有时理解 AI 生成的流式 API 代码比直接手写更花时间
- Bug 定位更复杂：AI 有时会在多个文件中引入不一致的代码，需要跨文件追踪问题
- Prompt 编写需要技巧：不好的 prompt 会产生无用的输出，需要学习如何写出好的 prompt

## 9. 最终项目中哪些部分主要是你自己写的？

人类编写的主要部分：
- **plan.md**：完整的需求分析和实现规划（基于课程要求但人工编写）
- **项目结构搭建**：目录创建、Git 初始化、包结构
- **InputHelper 的 Scanner 策略**：统一使用 nextLine() 处理输入
- **Main.java 中的菜单流程控制**：整体逻辑结构由我设计
- **ai/reflection.md**：反思内容全部由我根据实际经历编写
- **所有 AI prompt 的设计**：我构思并编写了所有发送给 AI 的 prompt

## 10. 哪些部分主要由 AI 生成或大量辅助？

AI 辅助的主要部分：
- **模型类实现**（Person, Player, Admin, Hero, Equipment, Team, MatchRecord）：AI 生成了主体代码，我进行了验证和调整
- **DataInitializer 数据集**：20 件装备、15 个英雄的数值和关联关系由 AI 生成
- **FileStorageService**：CSV 序列化/反序列化逻辑由 AI 实现
- **JUnit 测试套件**：179 个测试主要由 AI 生成，我添加了部分边界测试
- **排行榜算法**：RankingService 的自定义分数公式由 AI 设计
- **README.md**：文档结构由 AI 生成，我补充了具体内容
- **GameLogger**：4 级日志系统的实现框架
