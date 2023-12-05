package com.example.todolist.db.dao;

import com.example.todolist.db.MySQLConnection;
import com.example.todolist.models.tasks_tags;
import javafx.collections.FXCollections;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class tasks_tagsDao extends MySQLConnection implements Dao<tasks_tags> {

    Connection conn = getConnection();

    @Override
    public Optional<tasks_tags> findById(int id) {
        Optional<tasks_tags> optionalTask = Optional.empty();
        String query = "select * from tasks_tags where id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if ( rs.next() )
            {
                tasks_tags v_task_tags = new tasks_tags();
                v_task_tags.setIdTasks(rs.getInt("idTask"));
                v_task_tags.setIdTag(rs.getInt("idTag"));
                optionalTask = Optional.of(v_task_tags);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return optionalTask;
    }

    @Override
    public List<tasks_tags> findAll() {

        List<tasks_tags> taskList = FXCollections.observableArrayList();
        String query = "select * from tasks";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next())
            {
                tasks_tags v_task_tags = new tasks_tags();
                v_task_tags.setIdTasks(rs.getInt("idTask"));
                v_task_tags.setIdTag(rs.getInt("idTag"));
                taskList.add(v_task_tags);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return taskList;
    }

    @Override
    public boolean save(tasks_tags p_tasksTags) {

        String query = "insert into tasks_tags " +
                        " (idTask, idTag)" +
                        " values (?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, p_tasksTags.getIdTasks());
            ps.setInt(2, p_tasksTags.getIdTag());
            ps.execute();
            return true;
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }
        return false;
    }



    @Override
    public boolean update(tasks_tags record) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
