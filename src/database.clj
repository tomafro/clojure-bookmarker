(ns database
  (:refer-clojure :exclude [find])
  (:require
   [config]
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as result-set]
   [ragtime.jdbc]
   [ragtime.repl]
   [honeysql.core :as sql]))

(def db (jdbc/get-datasource (:database config/config)))

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

(defn select-one
  [sql]
  (jdbc/execute-one! db (sql/format sql :namespace-as-table? true)))

(defn as-kebab-maps [rs opts]
  (let [kebab #(clojure.string/replace % #"_" "-")]
    (result-set/as-modified-maps rs (assoc opts :qualifier-fn kebab :label-fn kebab))))

(defn find
  [id & ids]
  )

(defn find-by
  [& {:as conditions}])

(defn find-all-by
  [& {:as conditions}])
