package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 比赛记录类
 * 表示一场比赛的结果、参与者和英雄选择
 */
public class MatchRecord implements Reportable {
    private String matchId;
    private LocalDate date;
    private String teamA;              // 战队 A 的 ID
    private String teamB;              // 战队 B 的 ID
    private MatchResult result;        // 比赛结果
    private List<String> heroPicks;    // 使用的英雄 ID 列表

    public MatchRecord() {
        this.heroPicks = new ArrayList<>();
    }

    public MatchRecord(String matchId, LocalDate date, String teamA, String teamB, MatchResult result) {
        this.matchId = matchId;
        this.date = date;
        this.teamA = teamA;
        this.teamB = teamB;
        this.result = result;
        this.heroPicks = new ArrayList<>();
    }

    // === Getters and Setters ===

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public MatchResult getResult() {
        return result;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }

    public List<String> getHeroPicks() {
        return new ArrayList<>(heroPicks);
    }

    public void setHeroPicks(List<String> heroPicks) {
        this.heroPicks = (heroPicks == null) ? new ArrayList<>() : new ArrayList<>(heroPicks);
    }

    /**
     * 添加一个使用的英雄
     */
    public void addHeroPick(String heroId) {
        if (heroId != null && !heroPicks.contains(heroId)) {
            this.heroPicks.add(heroId);
        }
    }

    // === equals, hashCode ===

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchRecord that = (MatchRecord) o;
        return Objects.equals(matchId, that.matchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId);
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Match Record ===\n");
        sb.append("Match ID: ").append(matchId).append("\n");
        sb.append("Date: ").append(date).append("\n");
        sb.append("Team A: ").append(teamA).append("\n");
        sb.append("Team B: ").append(teamB).append("\n");
        sb.append("Result: ").append(result).append("\n");
        sb.append("Hero Picks (").append(heroPicks.size()).append("):\n");
        for (String h : heroPicks) {
            sb.append("  - ").append(h).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "MatchRecord{id='" + matchId + "', date=" + date + ", result=" + result + "}";
    }
}
