package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

     @Override
    public TimeEntry create(TimeEntry any) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                                    "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                    ps.setLong(1, any.getProjectId());
                    ps.setLong(2, any.getUserId());
                    ps.setDate(3, Date.valueOf(any.getDate()));
                    ps.setInt(4, any.getHours());
                    return ps;
                },generatedKeyHolder);

        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",new Object[]{timeEntryId},extractor);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries",mapper);
    }

    @Override
    public TimeEntry update(long eq, TimeEntry any) {
         jdbcTemplate.update("UPDATE time_entries " +
                        "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                        "WHERE id = ?",any.getProjectId(),any.getUserId(),any.getDate(),any.getHours(),eq);

            return find(eq);
    }

    @Override
    public void delete(long timeEntryId) {
            jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?",timeEntryId);
    }

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }

    private final RowMapper<TimeEntry> mapper = (rs,rownum) -> new TimeEntry (
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );


    private ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs,1):null;

}
