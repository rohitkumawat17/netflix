package com.netflix.accessor;

import com.netflix.accessor.models.WatchHistoryDTO;
import com.netflix.exception.DependencyFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class WatchHistoryAccessor {

    @Autowired
    private DataSource dataSource;

    public void updateWatchedLength(final String profileId, final String videoId, final int watchLength) {
        String query = "UPDATE watchHistory set watchLength = ?, lastWatchedAt = ? where profileId = ? and video videoId = ?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, watchLength);
            pstmt.setDate(2, new Date(System.currentTimeMillis()));
            pstmt.setString(3,profileId);
            pstmt.setString(4,videoId);
            pstmt.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new DependencyFailureException(ex);
        }
    }

    public void insertWatchHistory(final String profileId, final String videoId, final double rating, final int watchLength) {
        String query = "INSERT INTO watchHistory values (?,?,?,?,?,?)";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            Date currentDate = new Date(System.currentTimeMillis());
            pstmt.setString(1, profileId);
            pstmt.setString(2, videoId);
            pstmt.setDouble(3, rating);
            pstmt.setInt(4, watchLength);
            pstmt.setDate(5, currentDate);
            pstmt.setDate(6, currentDate);
            pstmt.execute();
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new DependencyFailureException(ex);
        }
    }

    public WatchHistoryDTO getWatchHistory(final String profileId, final String videoId) {
        String query = "SELECT rating, watchLength, lastWatchedAt, firstWatchedAt from watchHistory where profileId = ? and videoId = ?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, profileId);
            pstmt.setString(2, videoId);
            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next()) {
                WatchHistoryDTO watchHistoryDTO = WatchHistoryDTO.builder()
                        .profileId(profileId)
                        .videoId(videoId)
                        .rating(resultSet.getInt(1))
                        .watchLength(resultSet.getInt(2))
                        .lastWatchedAt(resultSet.getDate(3))
                        .firstWatchedAt(resultSet.getDate(4))
                        .build();
                return watchHistoryDTO;
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new DependencyFailureException(ex);
        }
        return null;
    }

}
