package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;


@Repository
public class StudentService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Student> getStudents() {
        final String sql = "SELECT * FROM student";
        return jdbcTemplate.query(sql, new StudentMapper());
    }
    public Student addStudent(final Student student){
        final String sql = "INSERT INTO student(name, email, dob)"
                                     +"VALUES(?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            Timestamp dob = Timestamp.valueOf(student.getDob().atStartOfDay());
            statement.setTimestamp(3, dob);
            return statement;
        }, keyHolder);
        student.setId(keyHolder.getKey().intValue());
        return student;
    }

    public Student findById(final int id) {
        final String sql = "select * from student where id = ?;";
        return jdbcTemplate.queryForObject(sql, new StudentMapper(), id);
    }


    public boolean update(Student student) {
        final String sql = "UPDATE student SET "
                +"name = ?, "
                +"email = ?, "
                +"dob = ? "
                +"WHERE id = ?;";
        return jdbcTemplate.update(sql,
                student.getName(),
                student.getEmail(),
                student.getDob(),
                student.getId()) > 0;
    }

    public boolean deleteById(int id) {
        final String sql = "DELETE FROM student WHERE id = ?;";
        return jdbcTemplate.update(sql, id) > 0;
    }

    private static final class StudentMapper implements RowMapper<Student> {

        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
          Student st = new Student();
          st.setId(rs.getInt("id"));
          st.setName(rs.getString("name"));
          st.setEmail(rs.getString("email"));
          LocalDate dob = rs.getTimestamp("dob").toLocalDateTime().toLocalDate();
          st.setDob(dob);
          st.setAge(Period.between(dob, LocalDate.now()).getYears());
          return st;
        }
    }

}
