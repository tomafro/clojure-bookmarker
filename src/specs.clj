(ns specs
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [lambdaisland.uri :as uri]))

(def non-empty-string
  (s/and string? not-empty))

(s/def :specs.url/scheme #{"http" "https"})
(s/def :specs.url/user non-empty-string)
(s/def :specs.url/password non-empty-string)
(s/def :specs.url/port (s/int-in 0 65535))
(s/def :specs.url/host non-empty-string)
(s/def :specs.url/path non-empty-string)
(s/def :specs.url/query non-empty-string)
(s/def :specs.url/fragment non-empty-string)

(s/def :specs/url-parts
  (s/keys
   :req-un [:specs.url/scheme :specs.url/host]
   :opt-un [:specs.url/user :specs.url/password :specs.url/port :specs.url/path :specs.url/query :specs.url/fragment]))

(defn url-gen
  []
  (gen/fmap
   (comp str uri/map->URI)
   (s/gen :specs/url-parts)))

(s/def :specs/url
  (s/with-gen
    string?
    url-gen))

(s/def :bookmarks/id (s/int-in 1 2147483647))
(s/def :bookmarks/title non-empty-string)
(s/def :bookmarks/url
  (s/with-gen
    string?
    url-gen))
(s/def :bookmarks/bookmark
  (s/keys :req [:bookmarks/id :bookmarks/url :bookmarks/title]))
