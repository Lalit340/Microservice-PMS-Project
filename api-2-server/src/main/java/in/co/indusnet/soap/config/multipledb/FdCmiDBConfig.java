package in.co.indusnet.soap.config.multipledb;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "fdcmiEntityManagerFactory", transactionManagerRef = "fdcmiTransactionManager", basePackages = {
		"in.co.indusnet.soap.repository.fdcmi" })
public class FdCmiDBConfig {

	@Bean(name = "fdcmiDataSource")
	@ConfigurationProperties(prefix = "spring.fdcmi.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "fdcmiEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean fdcmiEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("fdcmiDataSource") DataSource dataSource) {
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		properties.put("hibernate.physical_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
		return builder.dataSource(dataSource).properties(properties)
				.packages("in.co.indusnet.soap.model.fdcmi").persistenceUnit("Fdcmi").build();
	}

	@Bean(name = "fdcmiTransactionManager")
	public PlatformTransactionManager fdcmiTransactionManager(
			@Qualifier("fdcmiEntityManagerFactory") EntityManagerFactory fdcmiEntityManagerFactory) {
		return new JpaTransactionManager(fdcmiEntityManagerFactory);
	}
}
