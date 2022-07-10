package com.netflix.accessor;

import com.netflix.accessor.models.ProfileDTO;
import com.netflix.accessor.models.ProfileType;
import com.netflix.exception.DependencyFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.sql.Date;
import java.sql.ResultSet;

@Component
public class ProfileAccessor {

    @Autowired
    private DataSource dataSource;

    public void addNewProfile(final String userId, final String name, final ProfileType type) {
        String sql = "INSERT INTO profile values (?,?,?,?,?)";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, name);
            pstmt.setString(3, type.toString());
            pstmt.setDate(4, new Date(System.currentTimeMillis()));
            pstmt.setString(5,userId);
            pstmt.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new DependencyFailureException(ex);
        }
    }

    public void deleteProfile(final String profileId) {
        String query = "DELETE from profile where profileId = ?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, profileId);
            pstmt.execute();
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new DependencyFailureException(ex);
        }
    }

    public ProfileDTO getProfileByProfileId(final String profileId) {
        String query = "SELECT name, type, createdAt, userId from profile where profileId = ?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, profileId);
            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next()) {
                ProfileDTO profileDTO = ProfileDTO.builder()
                        .profileId(profileId)
                        .name(resultSet.getString(1))
                        .type(ProfileType.valueOf(resultSet.getString(2)))
                        .createdAt(resultSet.getDate(3))
                        .userId(resultSet.getString(4))
                        .build();
                return profileDTO;
            } else {
                return null;
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new DependencyFailureException(ex);
        }
    }

}
