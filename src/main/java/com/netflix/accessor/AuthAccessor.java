package com.netflix.accessor;

import com.netflix.accessor.models.AuthDTO;
import com.netflix.exception.DependencyFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Repository
public class AuthAccessor {

    @Autowired
    private DataSource dataSource;

    public AuthDTO findByToken(String token) {
        String sql = "SELECT authId, token, userId from movies.auth where token = ?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, token);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return new AuthDTO(resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3));
            }
            return null;
        }
        catch(SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean storeToken(String userId, String token) {
        String sql = "INSERT INTO movies.auth values (?, ?, ?)";
        String uuid = UUID.randomUUID().toString();
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, uuid);
            pstmt.setString(2, token);
            pstmt.setString(3, userId);

            return pstmt.executeUpdate() == 1 ? true : false;
        }
        catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public AuthDTO getAuthByToken(final String token) {
        String query = "SELECT authId, token, userId from movies.auth where token=?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, token);
            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next()) {
                AuthDTO authDTO = AuthDTO.builder()
                        .authId(resultSet.getString(1))
                        .token(resultSet.getString(2))
                        .userId(resultSet.getString(3))
                        .build();
                return authDTO;
            }
            return null;
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new DependencyFailureException(ex);
        }
    }

    public void deleteAuthByToken(final String token) {
        String query = "DELETE from auth where token=?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, token);
            pstmt.execute();
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new DependencyFailureException(ex);
        }
    }

}
