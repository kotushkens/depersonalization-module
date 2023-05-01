package repositories

import io.github.classgraph.ClassGraph
import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import org.hibernate.cfg.Environment
import javax.sql.DataSource

object JPA {
    fun createSessionFactory(dataSource: DataSource, vararg packageNames: String): SessionFactory {
        val configuration = Configuration()
        val classLoader = javaClass.classLoader

        ClassGraph()
            .enableAnnotationInfo()
            .enableClassInfo()
            .acceptPackages(*packageNames)
            .scan().use { scanResult ->
                for (classInfo in scanResult.getClassesWithAnnotation("javax.persistence.Entity")) {
                    configuration.addAnnotatedClass(classLoader.loadClass(classInfo.name))
                }
            }

        return configuration.buildSessionFactory(
            StandardServiceRegistryBuilder()
                .applySettings(configuration.properties)
                .applySetting(Environment.DATASOURCE, dataSource)
                .build()
        )
    }
}