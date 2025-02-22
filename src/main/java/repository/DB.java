package repository;

import org.h2.jdbcx.JdbcDataSource;
import javax.sql.DataSource;

public class DB {
    private String schema;
    private String user;
    private String password;

    private JdbcDataSource dataSource;

    public DB(String schema, String user, String password) {
        this.schema = schema;
        this.user = user;
        this.password = password;
        setDataSource();
    }
    private void setDataSource(){
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:./"+schema);
        dataSource.setUser(user);
        dataSource.setPassword(password);
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
