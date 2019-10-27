(ns db.bookmarks
  (:refer-clojure :exclude [find count])
  (:require
   [database]
   [next.jdbc.sql :as sql]
   [next.jdbc.result-set :as result-set]
   [clojure.spec.alpha :as s]
   [specs]))

(defn find
  [id]
  (database/find-by-id database/db :bookmarks id))

(s/fdef find
  :args (s/cat :id :bookmarks/id)
  :ret (s/nilable :bookmarks/bookmark))

(defn create
  [values]
  (sql/insert! database/db :bookmarks values {:builder-fn database/as-kebab-maps}))

(s/fdef create
  :args (s/cat :values (s/keys :req [:bookmarks/url :bookmarks/title]))
  :ret :bookmarks/bookmark)

(defn count
  []
  (:count (first (sql/query database/db ["SELECT COUNT(*) FROM bookmarks"]))))

(s/fdef count
  :ret  :postgres/bigint)
