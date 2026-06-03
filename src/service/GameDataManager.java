package service;

import model.*;

import java.util.*;

/**
 * 游戏数据管理器
 * 管理所有数据的存储、增删改查
 */
public class GameDataManager {
    private Map<String, Player> players;
    private Map<String, Admin> admins;
    private Map<String, Hero> heroes;
    private Map<String, Equipment> equipmentMap;
    private Map<String, Team> teams;
    private List<MatchRecord> matchRecords;
    private Map<String, String> userPasswords;  // userId -> password

    public GameDataManager() {
        this.players = new HashMap<>();
        this.admins = new HashMap<>();
        this.heroes = new HashMap<>();
        this.equipmentMap = new HashMap<>();
        this.teams = new HashMap<>();
        this.matchRecords = new ArrayList<>();
        this.userPasswords = new HashMap<>();
    }

    // === Player Methods ===

    public void addPlayer(Player player, String password) {
        if (players.containsKey(player.getId())) {
            throw new IllegalArgumentException("Player ID already exists: " + player.getId());
        }
        players.put(player.getId(), player);
        userPasswords.put(player.getId(), password);
    }

    public Player getPlayerById(String id) {
        return players.get(id);
    }

    public Collection<Player> getAllPlayers() {
        return players.values();
    }

    public void removePlayer(String playerId) {
        players.remove(playerId);
        userPasswords.remove(playerId);
    }

    // === Admin Methods ===

    public void addAdmin(Admin admin, String password) {
        admins.put(admin.getId(), admin);
        userPasswords.put(admin.getId(), password);
    }

    public Admin getAdminById(String id) {
        return admins.get(id);
    }

    // === Hero Methods ===

    public void addHero(Hero hero) {
        if (heroes.containsKey(hero.getHeroId())) {
            throw new IllegalArgumentException("Hero ID already exists: " + hero.getHeroId());
        }
        heroes.put(hero.getHeroId(), hero);
    }

    public Hero getHeroById(String id) {
        return heroes.get(id);
    }

    public Collection<Hero> getAllHeroes() {
        return heroes.values();
    }

    public void removeHero(String heroId) {
        heroes.remove(heroId);
    }

    // === Equipment Methods ===

    public void addEquipment(Equipment equipment) {
        equipmentMap.put(equipment.getEquipmentId(), equipment);
    }

    public Equipment getEquipmentById(String id) {
        return equipmentMap.get(id);
    }

    public Collection<Equipment> getAllEquipment() {
        return equipmentMap.values();
    }

    public void removeEquipment(String equipmentId) {
        equipmentMap.remove(equipmentId);
    }

    // === Team Methods ===

    public void addTeam(Team team) {
        teams.put(team.getTeamId(), team);
    }

    public Team getTeamById(String id) {
        return teams.get(id);
    }

    public Collection<Team> getAllTeams() {
        return teams.values();
    }

    public void removeTeam(String teamId) {
        teams.remove(teamId);
    }

    // === MatchRecord Methods ===

    public void addMatchRecord(MatchRecord record) {
        matchRecords.add(record);
    }

    public List<MatchRecord> getAllMatchRecords() {
        return new ArrayList<>(matchRecords);
    }

    public void removeMatchRecord(String matchId) {
        matchRecords.removeIf(m -> m.getMatchId().equals(matchId));
    }

    // === Authentication Methods ===

    public String getPassword(String userId) {
        return userPasswords.get(userId);
    }

    public Person findPersonById(String id) {
        if (players.containsKey(id)) return players.get(id);
        if (admins.containsKey(id)) return admins.get(id);
        return null;
    }
}
