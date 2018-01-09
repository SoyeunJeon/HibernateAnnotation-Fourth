package rmit.config;


import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import rmit.service.PersonService;


import java.util.Properties;

/**
 * Created by CoT on 10/13/17.
 */

@Configuration
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public PersonService personService(){
        return new PersonService();
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(){
        // session factory is the most important class in hibernate
        // session factory is a place that create factory for u
        // in order to use session factory, u need data source, hibernate properties, package to scan

        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.hbm2ddl.auto", "create-drop"); //create-drop generate the database. once it is generated, change into update will update the data changes


        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        //To use postgresql
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/hello"); // where is the database
        dataSource.setUsername("postgres");
        dataSource.setPassword("rmit");

        //To use local hsqldb
//        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
//        dataSource.setUrl("jdbc:hsqldb:mem:testdb");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("");

        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);

        sessionFactoryBean.setHibernateProperties(properties);
        sessionFactoryBean.setPackagesToScan("rmit.entity");


        return  sessionFactoryBean;
    }


    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory){
        HibernateTransactionManager tx = new HibernateTransactionManager(sessionFactory);

        return tx;
    }


}
