package cafe.nes.dao

import org.flywaydb.core.Flyway

object DatabaseMigration {
    fun migrate() {
        val flyway = Flyway.configure().dataSource("jdbc:h2:file:./db", "", "").baselineOnMigrate(true).load()
        flyway.migrate()
    }
}