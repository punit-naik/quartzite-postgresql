(ns org.clojars.punit-naik.quartzite-postgresql.core
  (:require
   [org.clojars.punit-naik.quartzite-postgresql.config :as config]
   [org.clojars.punit-naik.quartzite-postgresql.migrations :as migrations])
  (:import
   [java.util Properties]
   [org.quartz Scheduler]
   #_[org.quartz SchedulerException]
   [org.quartz SchedulerFactory]
   [org.quartz.impl StdSchedulerFactory]))

(defn make-scheduler!
  []
  (let [^Properties
        props (config/setup-properties
               (migrations/db-config))
        ^SchedulerFactory
        scheduler-factory (StdSchedulerFactory. props)
        ^Scheduler
        scheduler (.getScheduler scheduler-factory)]
    scheduler))