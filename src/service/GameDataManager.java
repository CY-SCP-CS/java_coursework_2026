package service;

import model.*;
import util.GameLogger;

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
        GameLogger.debug("GameDataManager", "Player added: " + player.getId() + "/" + player.getName());
    }

    public Player getPlayerById(String id) {
        return players.get(id);
    }

    public Collection<Player> getAllPlayers() {
        return new ArrayList<>(players.values());
    }

    public void removePlayer(String playerId) {
        Player removed = players.remove(playerId);
        userPasswords.remove(playerId);
        if (removed != null) {
            GameLogger.debug("GameDataManager", "Player removed: " + playerId + "/" + removed.getName());
        }
    }

    public void updatePlayer(Player updatedPlayer) {
        if (!players.containsKey(updatedPlayer.getId())) {
            throw new IllegalArgumentException("Player not found: " + updatedPlayer.getId());
        }
        players.put(updatedPlayer.getId(), updatedPlayer);
        GameLogger.debug("GameDataManager", "Player updated: " + updatedPlayer.getId());
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
        GameLogger.debug("GameDataManager", "Hero added: " + hero.getHeroId() + "/" + hero.getName());
    }

    public Hero getHeroById(String id) {
        return heroes.get(id);
    }

    public Collection<Hero> getAllHeroes() {
        return new ArrayList<>(heroes.values());
    }

    public void removeHero(String heroId) {
        Hero removed = heroes.remove(heroId);
        if (removed != null) {
            GameLogger.debug("GameDataManager", "Hero removed: " + heroId + "/" + removed.getName());
        }
    }

    public void updateHero(Hero updatedHero) {
        if (!heroes.containsKey(updatedHero.getHeroId())) {
            throw new IllegalArgumentException("Hero not found: " + updatedHero.getHeroId());
        }
        heroes.put(updatedHero.getHeroId(), updatedHero);
        GameLogger.debug("GameDataManager", "Hero updated: " + updatedHero.getHeroId());
    }

    // === Equipment Methods ===

    public void addEquipment(Equipment equipment) {
        equipmentMap.put(equipment.getEquipmentId(), equipment);
        GameLogger.debug("GameDataManager", "Equipment added: " + equipment.getEquipmentId() + "/" + equipment.getName());
    }

    public Equipment getEquipmentById(String id) {
        return equipmentMap.get(id);
    }

    public Collection<Equipment> getAllEquipment() {
        return new ArrayList<>(equipmentMap.values());
    }

    public void removeEquipment(String equipmentId) {
        Equipment removed = equipmentMap.remove(equipmentId);
        if (removed != null) {
            GameLogger.debug("GameDataManager", "Equipment removed: " + equipmentId + "/" + removed.getName());
        }
    }

    public void updateEquipment(Equipment updatedEquipment) {
        if (!equipmentMap.containsKey(updatedEquipment.getEquipmentId())) {
            throw new IllegalArgumentException("Equipment not found: " + updatedEquipment.getEquipmentId());
        }
        equipmentMap.put(updatedEquipment.getEquipmentId(), updatedEquipment);
        GameLogger.debug("GameDataManager", "Equipment updated: " + updatedEquipment.getEquipmentId());
    }

    // === Team Methods ===

    public void addTeam(Team team) {
        if (teams.containsKey(team.getTeamId())) {
            throw new IllegalArgumentException("Team ID already exists: " + team.getTeamId());
        }
        teams.put(team.getTeamId(), team);
        GameLogger.debug("GameDataManager", "Team added: " + team.getTeamId() + "/" + team.getName());
    }

    public Team getTeamById(String id) {
        return teams.get(id);
    }

    public Collection<Team> getAllTeams() {
        return new ArrayList<>(teams.values());
    }

    public void removeTeam(String teamId) {
        Team removed = teams.remove(teamId);
        if (removed != null) {
            GameLogger.debug("GameDataManager", "Team removed: " + teamId + "/" + removed.getName());
        }
    }

    public void updateTeam(Team updatedTeam) {
        if (!teams.containsKey(updatedTeam.getTeamId())) {
            throw new IllegalArgumentException("Team not found: " + updatedTeam.getTeamId());
        }
        teams.put(updatedTeam.getTeamId(), updatedTeam);
        GameLogger.debug("GameDataManager", "Team updated: " + updatedTeam.getTeamId());
    }

    // === MatchRecord Methods ===

    public void addMatchRecord(MatchRecord record) {
        matchRecords.add(record);
        GameLogger.debug("GameDataManager", "Match added: " + record.getMatchId());
    }

    public List<MatchRecord> getAllMatchRecords() {
        return new ArrayList<>(matchRecords);
    }

    public boolean removeMatchRecord(String matchId) {
        boolean removed = matchRecords.removeIf(m -> m.getMatchId().equals(matchId));
        if (removed) {
            GameLogger.debug("GameDataManager", "Match removed: " + matchId);
        }
        return removed;
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
