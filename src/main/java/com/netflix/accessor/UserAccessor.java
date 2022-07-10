package com.netflix.accessor;

import com.netflix.accessor.models.*;
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
public class UserAccessor {

    @Autowired
    private DataSource dataSource;

    public boolean updatePassword(String userId, String newPassword) {
        String query = "update movies.user set password = ? where userId = ?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, userId);

            return preparedStatement.executeUpdate() == 1 ? true : false ;
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    public UserDTO getUserByEmail(final String email) {
        UserDTO userDTO = null;

        String query = "SELECT userId, name, email, password, phoneNo, state, role, emailVerificationStatus, phoneVerificationStatus from movies.user where email = ?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userDTO = new UserDTO();
                userDTO.setUserId(resultSet.getString(1));
                userDTO.setName(resultSet.getString(2));
                userDTO.setEmail(resultSet.getString(3));
                userDTO.setPassword(resultSet.getString(4));
                userDTO.setPhoneNo(resultSet.getString(5));
                userDTO.setUserState(UserState.valueOf(resultSet.getString(6)));
                userDTO.setRole(UserRole.valueOf(resultSet.getString(7)));
                userDTO.setEmailVerificationStatus(EmailVerificationStatus.valueOf(resultSet.getString(8)));
                userDTO.setPhoneVerificationStatus(PhoneVerificationStatus.valueOf(resultSet.getString(9)));
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            return userDTO;
        }
    }

    public void addNewUser(final String email, final String name, final String password, final String phoneNo, final UserState userState, final UserRole userRole) {
        String insertQuery = "INSERT INTO user (?,?,?,?,?,?,?)";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(insertQuery);
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, phoneNo);
            pstmt.setString(6, userState.name());
            pstmt.setString(7, userRole.name());
            pstmt.execute();
        } catch (SQLException ex) {
            throw new DependencyFailureException(ex);
        }
    }

    public UserDTO getUserByPhoneNo(final String phoneNo) {
        UserDTO userDTO = null;

        String query = "SELECT userId, name, email, password, phoneNo, state, role from movies.user where phoneNo = ?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phoneNo);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userDTO = new UserDTO();
                userDTO.setUserId(resultSet.getString(1));
                userDTO.setName(resultSet.getString(2));
                userDTO.setEmail(resultSet.getString(3));
                userDTO.setPassword(resultSet.getString(4));
                userDTO.setPhoneNo(resultSet.getString(5));
                userDTO.setUserState(UserState.valueOf(resultSet.getString(6)));
                userDTO.setRole(UserRole.valueOf(resultSet.getString(7)));
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            return userDTO;
        }
    }

    public void updateUserRole(final String userId, final UserRole updatedRole) {
        String query = "UPDATE user set role = ? where userId = ?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,updatedRole.toString());
            pstmt.setString(2,userId);
            pstmt.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new DependencyFailureException(ex);
        }
    }

    public void updateEmailVerificationStatus(final String userId, final EmailVerificationStatus newStatus) {
        String query = "UPDATE user set emailVerificationStatus=? where userId=?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,newStatus.toString());
            pstmt.setString(2,userId);
            pstmt.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new DependencyFailureException(ex);
        }
    }

    public void updatePhoneVerificationStatus(final String userId, final PhoneVerificationStatus newStatus) {
        String query = "UPDATE user set phone VerificationStatus=? where userId=?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,newStatus.toString());
            pstmt.setString(2,userId);
            pstmt.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new DependencyFailureException(ex);
        }
    }

}
