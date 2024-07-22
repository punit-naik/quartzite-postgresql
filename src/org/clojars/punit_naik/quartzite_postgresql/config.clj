(ns org.clojars.punit-naik.quartzite-postgresql.config
  (:import
   [java.util Properties]))

(defn setup-properties
  [{:keys [dbtype dbname host port user password]}]
  (let [^Properties properties (Properties.)]
    (.put
     properties
     "org.quartz.scheduler.classLoadHelper.class"
     "org.clojars.punit_naik.quartzite_postgresql.MyDynamicClassLoader")
    (.put
     properties
     "org.quartz.dataSource.db.driver"
     "org.postgresql.Driver")
    (.put
     properties
     "org.quartz.dataSource.db.URL"
     (format "jdbc:%s://%s:%s/%s" dbtype host port dbname))
    (.put
     properties
     "org.quartz.dataSource.db.user"
     user)
    (.put
     properties
     "org.quartz.dataSource.db.password"
     password)
    (.put
     properties
     "org.quartz.threadPool.threadCount"
     "3")
    (.put
     properties
     "org.quartz.jobStore.class"
     "org.quartz.impl.jdbcjobstore.JobStoreTX")
    (.put
     properties
     "org.quartz.jobStore.driverDelegateClass"
     "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate")
    (.put
     properties
     "org.quartz.jobStore.dataSource"
     "db")
    properties))