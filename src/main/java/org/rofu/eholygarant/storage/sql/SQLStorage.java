package org.rofu.eholygarant.storage.sql;

import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.deal.Deal;
import org.rofu.eholygarant.core.model.DealStats;
import org.rofu.eholygarant.storage.AbstractStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

public abstract class SQLStorage extends AbstractStorage {
    protected final String url;
    protected final String username;
    protected final String password;

    public SQLStorage(EHolyGarant plugin, String url, String username, String password) {
        super(plugin);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    protected abstract void loadDriver() throws ClassNotFoundException;

    @Override
    public void init() {
        try {
            loadDriver();
            createTables();
        } catch (ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "Database driver not found", e);
        }
    }

    protected Connection getConnection() throws SQLException {
        if (username != null && !username.isEmpty()) {
            return DriverManager.getConnection(url, username, password);
        } else {
            return DriverManager.getConnection(url);
        }
    }

    protected void createTables() {
        String dealsTable = "CREATE TABLE IF NOT EXISTS deals (" +
                "id VARCHAR(36) PRIMARY KEY, " +
                "player_id VARCHAR(36) NOT NULL, " +
                "player_name VARCHAR(64) NOT NULL, " +
                "description TEXT NOT NULL, " +
                "created_at VARCHAR(64) NOT NULL, " +
                "price DOUBLE NOT NULL, " +
                "status VARCHAR(32) NOT NULL, " +
                "moderator_id VARCHAR(36), " +
                "moderator_name VARCHAR(64), " +
                "accepted_at VARCHAR(64))";
        String statsTable = "CREATE TABLE IF NOT EXISTS stats (" +
                "moderator_id VARCHAR(36) PRIMARY KEY, " +
                "success_count INT DEFAULT 0, " +
                "cancelled_count INT DEFAULT 0, " +
                "total_earned DOUBLE DEFAULT 0)";
        String indexPlayer = "CREATE INDEX IF NOT EXISTS idx_deals_player ON deals(player_id)";
        String indexModerator = "CREATE INDEX IF NOT EXISTS idx_deals_moderator ON deals(moderator_id)";
        String indexStatus = "CREATE INDEX IF NOT EXISTS idx_deals_status ON deals(status)";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(dealsTable);
            stmt.executeUpdate(statsTable);
            stmt.executeUpdate(indexPlayer);
            stmt.executeUpdate(indexModerator);
            stmt.executeUpdate(indexStatus);
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create tables", e);
        }
    }

    @Override
    public void saveDeal(Deal deal) {
        String sql = "REPLACE INTO deals (id, player_id, player_name, description, created_at, price, status, moderator_id, moderator_name, accepted_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, deal.getId().toString());
            stmt.setString(2, deal.getPlayerId().toString());
            stmt.setString(3, deal.getPlayerName());
            stmt.setString(4, deal.getDescription());
            stmt.setString(5, deal.getCreatedAt().format(FORMATTER));
            stmt.setDouble(6, deal.getPrice());
            stmt.setString(7, deal.getStatus().name());
            stmt.setString(8, deal.getModeratorId() != null ? deal.getModeratorId().toString() : null);
            stmt.setString(9, deal.getModeratorName());
            stmt.setString(10, deal.getAcceptedAt() != null ? deal.getAcceptedAt().format(FORMATTER) : null);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save deal " + deal.getId(), e);
        }
    }

    @Override
    public Optional<Deal> getDeal(UUID dealId) {
        String sql = "SELECT * FROM deals WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dealId.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(dealFromResultSet(rs));
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to get deal " + dealId, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Deal> getActiveDeals() {
        List<Deal> deals = new ArrayList<>();
        String sql = "SELECT * FROM deals WHERE status IN ('WAITING', 'IN_PROGRESS')";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) deals.add(dealFromResultSet(rs));
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to get active deals", e);
        }
        return deals;
    }

    @Override
    public List<Deal> getDealsByPlayer(UUID playerId) {
        List<Deal> deals = new ArrayList<>();
        String sql = "SELECT * FROM deals WHERE player_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, playerId.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) deals.add(dealFromResultSet(rs));
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to get deals by player " + playerId, e);
        }
        return deals;
    }

    @Override
    public List<Deal> getDealsByModerator(UUID moderatorId) {
        List<Deal> deals = new ArrayList<>();
        String sql = "SELECT * FROM deals WHERE moderator_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, moderatorId.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) deals.add(dealFromResultSet(rs));
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to get deals by moderator " + moderatorId, e);
        }
        return deals;
    }

    @Override
    public void removeDeal(UUID dealId) {
        String sql = "DELETE FROM deals WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dealId.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to remove deal " + dealId, e);
        }
    }

    @Override
    public DealStats getStats(UUID moderatorId) {
        String sql = "SELECT * FROM stats WHERE moderator_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, moderatorId.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return buildStats(moderatorId,
                            rs.getInt("success_count"),
                            rs.getInt("cancelled_count"),
                            rs.getDouble("total_earned"));
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to get stats for " + moderatorId, e);
        }
        return buildStats(moderatorId, 0, 0, 0.0);
    }

    @Override
    public void saveStats(DealStats stats) {
        String sql = "REPLACE INTO stats (moderator_id, success_count, cancelled_count, total_earned) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stats.getModeratorId().toString());
            stmt.setInt(2, stats.getSuccessCount());
            stmt.setInt(3, stats.getCancelledCount());
            stmt.setDouble(4, stats.getTotalEarned());
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save stats for " + stats.getModeratorId(), e);
        }
    }

    @Override
    public void load() {}

    @Override
    public void save() {}

    @Override
    public void shutdown() {}

    private Deal dealFromResultSet(ResultSet rs) throws SQLException {
        return buildDeal(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("player_id")),
                rs.getString("player_name"),
                rs.getString("description"),
                rs.getString("created_at"),
                rs.getDouble("price"),
                rs.getString("status"),
                rs.getString("moderator_id"),
                rs.getString("moderator_name"),
                rs.getString("accepted_at")
        );
    }
}