(ns database
  (:require
   [config]
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as result-set]
   [ragtime.jdbc]
   [ragtime.repl]
   [honeysql.core :as sql]))

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

(defn select-one
  [sql]
  (jdbc/execute-one! ds (sql/format sql :namespace-as-table? true)))

(defn find-bookmark
  [id]
  (select-one
   (sql/build :select :* :from :bookmarks :where [:= :bookmarks/id id])))

(defn as-kebab-maps [rs opts]
  (let [kebab #(clojure.string/replace % #"_" "-")]
    (result-set/as-modified-maps rs (assoc opts :qualifier-fn kebab :label-fn kebab))))

(defn bookmark-find-by
  [& {:as conditions}]
  (next.jdbc.sql/find-by-keys ds :bookmarks conditions {:builder-fn as-kebab-maps}))

(defn bookmark-find-all
  [& {:as conditions}]
  (next.jdbc.sql/find-by-keys ds :bookmarks conditions {:builder-fn as-kebab-maps}))
