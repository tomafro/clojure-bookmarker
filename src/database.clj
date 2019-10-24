(ns database
  (:refer-clojure :exclude [find])
  (:require
   [config]
   [next.jdbc :as jdbc]
   [next.jdbc.sql :as sql]
   [next.jdbc.result-set :as result-set]
   [ragtime.jdbc]
   [ragtime.repl]
   [tick.alpha.api :as t]
   [clojure.spec.alpha :as s]
   [specs]))

(def db (jdbc/get-datasource (:database config/config)))

(defn ragtime-config
  ([] (ragtime-config config/env))
  ([env]
   {:datastore  (ragtime.jdbc/sql-database (:database (config/load-config env)))
    :migrations (ragtime.jdbc/load-resources "migrations")}))

(defn migrate
  ([] (migrate config/env))
  ([env]
   (ragtime.repl/migrate (ragtime-config env))))

(defn rollback
  ([] (rollback config/env))
  ([env]
   (ragtime.repl/rollback (ragtime-config env))))

(defn as-kebab-maps [rs opts]
  (let [kebab #(clojure.string/replace % #"_" "-")]
    (result-set/as-modified-maps rs (assoc opts :qualifier-fn kebab :label-fn kebab))))

(defn find-bookmark
  [db id]
  {:pre  [(s/valid? :bookmarks/id id)]
   :post [(or (nil? %) (s/valid? :bookmarks/bookmark %))]}
  (sql/get-by-id db :bookmarks id {:builder-fn as-kebab-maps}))

(defn create-bookmark
  [db values]
  {:pre  [(s/valid? (s/keys :req-un [:bookmarks/url :bookmarks/title]) values)]
   :post [(or (nil? %) (s/valid? :bookmarks/bookmark %))]}
  (sql/insert! db :bookmarks values {:builder-fn as-kebab-maps}))
