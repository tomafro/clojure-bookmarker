(ns database
  (:require
   [config]
   [next.jdbc :as jdbc]
   [ragtime.jdbc]
   [ragtime.repl]))

(def ds (jdbc/get-datasource (:database config/config)))

(defn ragtime-config
  []
  {:datastore  (ragtime.jdbc/sql-database (:database config/config))
   :migrations (ragtime.jdbc/load-resources "migrations")})

(defn migrate
  []
  (ragtime.repl/migrate (ragtime-config)))

(defn rollback
  []
  (ragtime.repl/rollback (ragtime-config)))
