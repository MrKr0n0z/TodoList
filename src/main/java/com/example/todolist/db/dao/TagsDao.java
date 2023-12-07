package com.example.todolist.db.dao;

import com.example.todolist.db.MySQLConnection;
import com.example.todolist.models.Tags;
import javafx.collections.FXCollections;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class TagsDao extends MySQLConnection implements Dao<Tags> {
    Connection conn = getConnection();
    @Override
    public Optional<Tags> findById(int id) {
        Optional<Tags> optionalTask = Optional.empty();
        String query = "select * from tags where idTags = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if ( rs.next() )
            {
                Tags task = new Tags();
                task.setIdTags(rs.getInt("idTags"));
                task.setDescription(rs.getString("description"));

                optionalTask = Optional.of(task);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return optionalTask;
    }

    @Override
    public List<Tags> findAll() {
        List<Tags> taskList = FXCollections.observableArrayList();
        String query = "select description from tags";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next())
            {
                Tags task = new Tags();
                task.setDescription(rs.getString("description"));
                taskList.add(task);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return taskList;
    }

    @Override
    public boolean save(Tags tags) {

        String query = "insert into tags " +
                        " (description)" +
                        " values (?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, tags.getDescription());
            ps.execute();
            return true;
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }
        return false;
    }

     public Tags findByDescription(String description) {
        String query = "SELECT * FROM tags WHERE description = ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, description);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Tags tag = new Tags();
                tag.setIdTags(rs.getInt("idTags"));
                tag.setDescription(rs.getString("description"));
                // Otros campos de la etiqueta...
                return tag;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Manejar si no se encuentra la etiqueta
    }

    @Override
    public boolean update(Tags tags) {
        String query = "update tags description=? where id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, tags.getDescription());
            ps.setInt(2, tags.getIdTags());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String query = "delete from tags where id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
