# Honor of Kings Information Management System

> AI-Assisted OOP Coursework — Java Console Application

An information management system for the game **Honor of Kings** (王者荣耀), built with Java OOP principles and AI-assisted development methodology. Supports player/hero/equipment management, match tracking, team statistics, and persistent CSV storage.

---

## Features

### Player Features
- **Profile Management** — View personal info, level, win rate, owned heroes
- **Hero Browser** — Browse owned heroes with equipped items
- **Match History** — View personal match history with W/L/D summary
- **Player Search** — Search by ID or name (fuzzy match)
- **Team Overview** — Search teams by ID or name, view members and stats
- **Hero Details** — Search heroes, view stats and compatible equipment
- **Equipment Statistics** — Equipment ranked by usage count or compatible hero count
- **Leaderboard** — Rankings by win rate, level, match count, or custom score (winRate × 0.5 + level × 2.0 + matches × 0.1)

### Admin Features
All Player features plus:
- **Player CRUD** — Add/remove players with full validation
- **Hero CRUD** — Add/remove heroes
- **Equipment CRUD** — Add/remove equipment
- **Team CRUD** — Add/remove teams (max 5 members)
- **Match CRUD** — Add/remove match records with date validation

### System Features
- **Persistent Storage** — CSV-based save/load (7 files, UTF-8 encoded)
- **4-Level Logging** — DEBUG/INFO/WARN/ERROR with file output and log analysis
- **Auto-Save** — Data automatically saved on logout and exit
- **Input Validation** — Range checking, null safety, EOF handling
- **Defensive Copying** — All collection getters return immutable copies

---

## Technology Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 25 (LTS) |
| Testing | JUnit 5 (Platform Console Standalone 1.11.4) |
| Build | Manual `javac` compilation |
| Storage | CSV files (UTF-8) |
| Dev Tool | Claude Code CLI (AI-assisted) |

---

## Project Structure

```
src/
├── Main.java                     # Entry point — menu-driven console UI
├── model/
│   ├── Person.java               # Abstract base class (Player + Admin)
│   ├── Player.java               # Player model with heroes + equipment
│   ├── Admin.java                # Admin model with permissions
│   ├── Hero.java                 # Hero model with stats + compatible equipment
│   ├── Equipment.java            # Equipment model with stats + usage count
│   ├── Team.java                 # Team model (aggregates Players, max 5)
│   ├── MatchRecord.java          # Match record with teams, result, hero picks
│   ├── Searchable.java           # Generic search interface
│   ├── Reportable.java           # Formatted display interface
│   ├── Persistable.java          # CSV serialization interface
│   ├── Role.java                 # ADMIN / PLAYER
│   ├── HeroType.java             # 7 hero categories
│   ├── EquipmentType.java        # 5 equipment categories
│   └── MatchResult.java          # WIN / LOSE / DRAW
├── service/
│   ├── AuthenticationService.java # Login/logout/session management
│   ├── SearchService.java         # Player/team/hero search + display
│   ├── RankingService.java        # Leaderboards + equipment rankings
│   ├── GameDataManager.java       # Central CRUD + data access
│   └── FileStorageService.java    # CSV persistence (7 files)
└── util/
    ├── DataInitializer.java       # Default dataset (15 heroes, 20 equipment, etc.)
    ├── GameLogger.java            # 4-level hierarchical logger
    └── InputHelper.java           # Console input with validation

test/                              # JUnit 5 test suite (179 tests)
├── PlayerTest.java
├── TeamTest.java
├── HeroEquipmentTest.java
├── MatchRecordTest.java
├── AdminTest.java
├── PersonTest.java
├── DataInitializerTest.java
├── AuthenticationServiceTest.java
├── SearchServiceTest.java
├── RankingServiceTest.java
├── GameDataManagerTest.java
├── GameLoggerTest.java
└── FileStorageServiceTest.java

data/                              # CSV data files (auto-generated)
├── equipment.csv                  # 20 equipment items
├── heroes.csv                     # 15 heroes
├── players.csv                    # 15 players
├── teams.csv                      # 3 teams
├── matches.csv                    # 10 match records
├── passwords.csv                  # Account credentials
└── equipped_items.csv            # Player-hero-equipment mappings

logs/                              # Log files (auto-created)
ai/
├── prompts.md                     # Complete prompt execution record
└── agent-log.md                   # AI Agent contribution log
```

---

## Default Data

### Initial Dataset
| Entity | Count | Details |
|--------|-------|---------|
| Equipment | 20 | 6 OFFENSIVE + 5 DEFENSIVE + 3 MOVEMENT + 4 MAGIC + 2 JUNGLE |
| Heroes | 15 | 3 WARRIOR + 3 MAGE + 2 ASSASSIN + 2 TANK + 2 MARKSMAN + 2 SUPPORT + 1 JUNGLER |
| Players | 15 | Each with 3-4 heroes + equipped items |
| Teams | 3 | 星辰战队 / 雷霆战队 / 明月战队 (5 members each) |
| Matches | 10 | 5 WIN + 4 LOSE + 1 DRAW |

### Default Accounts
| Role | ID | Password |
|------|----|----------|
| Admin | `admin` | `admin123` |
| Player | `P001`–`P015` | `pass123` |

---

## Getting Started

### Prerequisites
- Java 17+
- Git

### Compile & Run
```bash
# Compile all source files
javac -d out -sourcepath src src/Main.java

# Run the application
java -cp out Main
```

### Run Tests
```bash
# Compile source + tests
javac -d out -sourcepath src -cp "lib/junit-platform-console-standalone-1.11.4.jar" src/Main.java
javac -d out -cp "out;lib/junit-platform-console-standalone-1.11.4.jar" test/*.java

# Execute all tests
java -jar "lib/junit-platform-console-standalone-1.11.4.jar" --class-path out --scan-class-path
```

### Clean Start (remove saved data, fresh init on next run)
```bash
rm -rf data/ logs/
```

---

## OOP Design Principles

| Principle | Implementation |
|-----------|---------------|
| **Encapsulation** | All fields `private`, getters/setters with defensive copying |
| **Inheritance** | `Person` abstract class → `Player`, `Admin` |
| **Polymorphism** | `Reportable.getInfo()` overridden in all model classes |
| **Abstraction** | `Searchable<T>` generic interface, `Persistable` CSV contract |
| **Aggregation** | `Team` aggregates `Player` objects; `Player` owns `Hero` objects |
| **Composition** | `MatchRecord` contains `MatchResult` enum; `Hero` has `Map<String, Integer>` stats |

### Design Patterns Used
- **DAO Pattern** — `GameDataManager` as central data access object
- **Service Layer** — `AuthenticationService`, `SearchService`, `RankingService`
- **Strategy Pattern** — Multiple `Comparator` strategies in `RankingService`
- **Factory Method** — `DataInitializer.initData()` as static factory

---

## AI-Assisted Development Process

This project was developed using **Claude Code CLI** with a multi-agent methodology:

### Agent Roles
| Role | Responsibility |
|------|---------------|
| **Architect Agent** | Class design, interface contracts, enum definitions |
| **Implementation Agent** | Code generation per functional module |
| **Code Review Agent** | Style checks, cohesion/coupling analysis, bug detection |
| **Testing/Reviewer Agent** | JUnit test suite, edge case coverage, regression |
| **Log Agent** | 4-level logging utility, automated log analysis |
| **Fix Agent** | Bug repair from review findings |
| **Documentation Agent** | README, prompt records, agent logs |
| **Prompt Optimization Agent** | Prompt quality analysis and improvement |

### Development Workflow
1. **Architect** designs the module → `[AI-Architect]` commit
2. **Implementation Agent** writes code → `[AI-Implementation]` commit
3. **Code Review Agent** inspects → `[AI-Review]` commit with fix suggestions
4. **Testing Agent** creates test cases → `[AI-Implementation]` commit
5. **Human** validates and merges → `[Human]` commit

Full details in [`ai/agent-log.md`](ai/agent-log.md) and [`ai/prompts.md`](ai/prompts.md).

---

## Test Coverage

**179 tests** across 13 test classes, all passing:

| Test Class | Tests | Coverage |
|-----------|-------|----------|
| PlayerTest | 30 | Constructors, boundary validation, add/remove heroes, equip items, defensive copy, equals/hashCode, CSV |
| TeamTest | 18 | Member add/remove, capacity limit (5), average level/win rate, top player, defensive copy, CSV |
| HeroEquipmentTest | 18 | Hero/Equipment constructors, stat management, compatible equipment, defensive copy, equals/hashCode, CSV |
| MatchRecordTest | 12 | Constructor, hero picks, dedup, defensive copy, MatchResult enum, CSV |
| AdminTest | 5 | Constructor, admin level, description, permissions |
| PersonTest | 8 | Polymorphism, Role/HeroType/EquipmentType/MatchResult enums |
| DataInitializerTest | 13 | Data counts (20/15/15/3/10), reference integrity, default passwords |
| AuthenticationServiceTest | 10 | Login success/fail, wrong password, case sensitivity, logout, session mgmt |
| SearchServiceTest | 20 | ID/name search, fuzzy match, team/hero search, display formatting, match history |
| RankingServiceTest | 12 | Win rate/level/match/custom score leaderboards, equipment ranking, tie-breakers |
| GameDataManagerTest | 20 | CRUD operations, duplicate detection, defensive copy, findPersonById |
| GameLoggerTest | 10 | Log levels, file output, null exception safety, log analysis |
| FileStorageServiceTest | 14 | CSV save/load cycles, full round-trip, UTF-8 encoding, graceful failure |

---

## Known Limitations

- No GUI — console-based interface only
- CSV-based storage (not a relational database)
- Data integrity depends on application-level validation
- No network/multi-user support
