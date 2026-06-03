# AI-Assisted Honor of Kings Information Management System

Java OOP Coursework 2026 — 20 marks

## Project Overview

A console-based information management system for Honor of Kings, supporting player lookup, team overview, hero details, equipment statistics, match history, leaderboards, data management, and authentication with Admin/Player roles.

## How to Run

```bash
# Compile
javac -d out src/**/*.java

# Run
java -cp out Main
```

## Default Login Accounts

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | Admin |
| player1 | pass123 | Player |
| player2 | pass123 | Player |

## Implemented Features

- Player Lookup (by ID/name)
- Team Overview
- Hero Details
- Equipment Statistics (ranked by usage)
- Match History (last N matches)
- Leaderboard (win rate / level / matches / custom score)
- Data Management (Admin CRUD)
- Authentication (Admin/Player roles)
- File Persistence (CSV format)

## Java Concepts Used

- **Inheritance**: Person -> Player, Admin
- **Interface**: Searchable, Reportable
- **Collections**: ArrayList, HashMap
- **Enum**: HeroType, EquipmentType, MatchResult, Role
- **Exception Handling**: Custom exception classes
- **File I/O**: CSV read/write
- **Polymorphism**: Person references pointing to subclass objects

## AI Usage Summary

Developed using Claude Code CLI. See `ai/` folder for prompt records, agent logs, and reflection.

## Testing Summary

See `docs/test-cases.md` for manual test cases.

## Known Limitations

*(To be filled after development)*
