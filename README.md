# HelloHibernateAnnotation
This is a hello world example that demonstrate the usage of Hibernate and Spring using Annotation. I simplify this project with minimal configuration.

1. Create a maven project using IntelliJ IDEA
2. In the pom.xml we will need to add the following dependencies:
+ spring-context: will provide us with @Configuration, @Bean
+ spring-orm: will provide us with LocalSessionFactoryBean, HibernateTransationManager
+ spring-tx: will need for transaction management @EnableTransationManager or @Transactional
+ hibernate-core: not sure why we need it? :-) 
+ postgresql or hsqldb (if we want to use in-memory db)

3. Similar to HelloSpringAnnotation project we need a configuration class that handles all beans declaration. We name it AppConfig.java and annotates it with 2 annotations: @Configuration, @EnableTransactionManagement
- @Configuration: to say that this is a class that contains @Beans
- @EnableTransactionManagement: there will be a transactionManager bean in this class

4. In AppConfig.java, we have to define a very important bean namely sessionFactory. This is the key to any Hibernate-Spring application. 
To construct this bean, Spring need the following properties:
+ dataSource: we have to specify driverClassName (what db), url (what db name), username, password
+ hibernateProperties: not very important info, we can skip but we may need hibernate.hbm2ddl=create-drop to automatically create tables in db
+ packageToScan: this will tell Spring where to look for entity class

        @Bean
        public LocalSessionFactoryBean sessionFactory(){

            Properties properties = new Properties();
            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.put("hibernate.show_sql", true);
            properties.put("hibernate.hbm2ddl.auto", "create-drop");

            DriverManagerDataSource dataSource = new DriverManagerDataSource();

            //To use postgresql
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl("jdbc:postgresql://localhost:5432/hello");
            dataSource.setUsername("postgres");
            dataSource.setPassword("rmit");

            LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
            sessionFactoryBean.setDataSource(dataSource);

            sessionFactoryBean.setHibernateProperties(properties);
            sessionFactoryBean.setPackagesToScan("rmit.entity");


            return  sessionFactoryBean;
        }

5. The rest is very easy, we have a bean namely PersonService that has a dependency to sessionFactory. That is why we have to set @Autowire. We also need to annotate this class with @Transational as it is related to sessionFactory.

        @Transactional
        public class PersonService {

            @Autowired
            private SessionFactory sessionFactory;

            public void setSessionFactory(SessionFactory sessionFactory) {
                this.sessionFactory = sessionFactory;
            }

            public void savePerson(Person person){
                sessionFactory.getCurrentSession().save(person);
            }
        }

6. It is now ready to get a PersonService bean and start to use it:

        public static void main(String[] args){

            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

            PersonService personService = (PersonService) context.getBean("personService");

            Person person = new Person();
            person.setName("Hien");

            personService.savePerson(person);

        }

Special notes:
- We can externalize the dataSource properties by using Environtment bean and a properties file. For simplicity, I have removed it and hardcoded it by actual string
- We can define dataSource and hibernateProperties as beans but I personally don't think it is useful
- In PersonService class, we can add other CRUD methods such as update, get, getAll, delete etc. 
- Usually, we need to make PersonService as an interface and implement it by creating a class such as PersonServiceImpl or PersonServiceHibernate etc. 






"# HibernateAnnotation-Fourth" 
