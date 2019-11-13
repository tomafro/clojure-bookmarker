(ns bookmarks.db
  (:refer-clojure :exclude [find count])
  (:require
   [database.model]
   [clojure.spec.alpha :as s]
   [specs]))

(def find (partial database.model/find :bookmarks))
(def find-first (partial database.model/find-first :bookmarks))
(def find-all (partial database.model/find-all :bookmarks))
(def create (partial database.model/create :bookmarks))
(def count (partial database.model/count :bookmarks))

(s/fdef find
  :args (s/cat :id :bookmarks/id)
  :ret (s/nilable :bookmarks/bookmark))

(s/fdef create
  :args (s/cat :values (s/keys :req [:bookmarks/url :bookmarks/title]))
  :ret :bookmarks/bookmark)

(s/fdef count
  :ret :postgres/bigint)
