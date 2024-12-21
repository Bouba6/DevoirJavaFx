package detteproject.Repository.Bd;

import java.util.List;

import detteproject.State.Etat;
import detteproject.State.Role;
import detteproject.core.Model;
import detteproject.core.RepositorieUser;
import detteproject.core.RepositoryBdImpl;
import detteproject.data.entities.Client;
import detteproject.data.entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserRepositoryBD extends RepositoryBdImpl<User> implements RepositorieUser {

    public UserRepositoryBD() {
        super(User.class);
    }

    protected List<String> getColumnNames() {
        return List.of("email", "login", "password", "roleId", "Etat"); // Mettez à jour selon votre modèle
    }

    @Override
    public boolean insert(User objet) {
        return super.insert(objet);
    }

    @Override
    public void update(User objet) {
        super.update(objet);
    }

    @Override
    public List<User> selectAll() {
        try {
            return super.selectAll(null);
        } catch (InstantiationException e) {

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getById(int id) {
        String sql = String.format("SELECT * FROM %s WHERE id = ?", getTableName());
        try {
            super.innit(sql);
            stm.setInt(1, id);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return creatObject(resultSet);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> getByRole(Role role) {
        String sql = String.format("SELECT * FROM %s WHERE \"roleId\" = ?", getTableName());
        try {
            super.innit(sql);
            stm.setInt(1, role.ordinal());
            ResultSet resultSet = stm.executeQuery();
            List<User> list = new ArrayList<>();
            while (resultSet.next()) {
                User user = creatObject(resultSet);
                list.add(user);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> getByState(Etat etat) {
        String sql = String.format("SELECT * FROM %s WHERE \"Etat\" = ?", getTableName());
        try {
            super.innit(sql);
            stm.setInt(1, etat.ordinal());
            ResultSet resultSet = stm.executeQuery();
            List<User> list = new ArrayList<>();
            while (resultSet.next()) {
                User user = creatObject(resultSet);
                list.add(user);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            super.closeConnexion();
        }
    }

    @Override
    protected String getTableName() {
        return "users";
    }

    public User creatObject(ResultSet resultSet) throws SQLException {
        User object = new User(); // Create a new Client object
        object.setId(resultSet.getInt("id"));
        object.setEmail(resultSet.getString("email"));
        object.setLogin(resultSet.getString("login"));
        object.setPassword(resultSet.getString("password"));

        switch (resultSet.getInt("Etat")) {
            case 0:
                object.setEtat(Etat.Activer);
                break;
            case 1:
                object.setEtat(Etat.Desactiver);
                break;
            default:
                break;
        }

        switch (resultSet.getInt("roleId")) {
            case 0:
                object.setRole(Role.Admin);
                break;
            case 1:
                object.setRole(Role.Client);
                break;

            default:
                object.setRole(Role.Boutiquier); // Gestion des rôles non reconnus
                break;
        }
        return object;

    }

    @Override
    public User getByLogin(String login, String password) {
        String sql = String.format("SELECT * FROM %s WHERE login = ? AND password = ?", getTableName());
        try (Connection connection = setConnexion()) {
            super.innit(sql);
            stm.setString(1, login);
            stm.setString(2, password);
            stm.executeQuery();
            ResultSet resultSet = stm.getResultSet();
            if (resultSet.next()) {
                return creatObject(resultSet);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected List<String> excludedFieldsInsert() {
        return List.of("id", "updateAt", "userUpdate", "client");
    }

    @Override
    protected List<String> excludedFieldsUpdate() {
        return List.of("id", "createAt", "userCreate", "client");
    }

    @Override
    protected String[] column() {
        return new String[] { "id", "email", "login", "password", "roleId", "Etat" };
    }

    @Override
    protected List<String> excludedFieldsSelect() {
        return List.of("createAt", "userCreate", "client");
    }

}