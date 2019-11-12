(ns bookmarks.db
  (:refer-clojure :exclude [find count])
  (:require
   [database]
   [next.jdbc.sql :as sql]
   [clojure.spec.alpha :as s]
   [specs]))

(defn find
  [id]
  (database/find-by-id database/db :bookmarks id))

(s/fdef find
  :args (s/cat :id :bookmarks/id)
  :ret (s/nilable :bookmarks/bookmark))

(defn find-first
  []
  (database/find-first database/db :bookmarks))

(defn create
  [values]
  (database/create database/db :bookmarks values))
  
(s/fdef create
  :args (s/cat :values (s/keys :req [:bookmarks/url :bookmarks/title]))
  :ret :bookmarks/bookmark)

(defn count
  ([] (count [database/db]))
  ([db] (database/count db :bookmarks)))

(s/fdef count
  :ret :postgres/bigint)
