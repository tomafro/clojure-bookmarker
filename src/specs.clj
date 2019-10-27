(ns specs
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [lambdaisland.uri :as uri]))

(def non-empty-string
  (s/and string? not-empty))

(s/def :postgres/smallint (s/int-in -32768 32767))
(s/def :postgres/integer (s/int-in -2147483648 2147483647))
(s/def :postgres/bigint (s/int-in -9223372036854775808 9223372036854775807))

(s/def :url/scheme #{"http" "https"})
(s/def :url/user non-empty-string)
(s/def :url/password non-empty-string)
(s/def :url/port (s/int-in 0 65535))
(s/def :url/host non-empty-string)
(s/def :url/path non-empty-string)
(s/def :url/query non-empty-string)
(s/def :url/fragment non-empty-string)

(s/def :specs/url-parts
  (s/keys
   :req-un [:url/scheme :url/host]
   :opt-un [:url/user :url/password :url/port :url/path :url/query :url/fragment]))

(defn url-gen
  []
  (gen/fmap
   (comp str uri/map->URI)
   (s/gen :specs/url-parts)))

(s/def :specs/url
  (s/with-gen
    string?
    url-gen))

(s/def :db/connectable any?)

(s/def :db/serial    (s/int-in 1 2147483647))
(s/def :db/bigserial (s/int-in 1 9223372036854775807))

(s/def :db/attributes (s/map-of keyword? any?))

(s/def :bookmarks/id :db/serial)
(s/def :bookmarks/title non-empty-string)
(s/def :bookmarks/url
  (s/with-gen
    string?
    url-gen))
(s/def :bookmarks/bookmark
  (s/keys :req [:bookmarks/id :bookmarks/url :bookmarks/title]))
