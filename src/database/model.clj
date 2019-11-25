(ns database.model
  (:refer-clojure :exclude [find count])
  (:require
   [config]
   [database]
   [next.jdbc.sql :as sql]))

(defn find
  ([table id] (find table database/db id))
  ([table db id] (sql/get-by-id db table id)))

(defn find-all
  ([table] (find-all table database/db))
  ([table db] (sql/query db [(str "SELECT * from " (name table) " ORDER BY id ASC")])))

(defn find-first
  ([table] (find-first table database/db))
  ([table db] (first (find-all table db))))

(defn count
  ([table] (count table database/db))
  ([table db] (:count (first (sql/query db [(str "SELECT COUNT(*) count FROM " (name table))])))))

(defn create
  ([table values] (create table database/db values))
  ([table db values] (sql/insert! db table values)))
