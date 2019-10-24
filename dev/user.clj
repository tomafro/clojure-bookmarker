(ns user
 (:require
  [main]
  [clojure.spec.alpha :as s]
  [clojure.spec.gen.alpha :as gen]
  [clojure.tools.namespace.repl]
  [routes]
  [io.pedestal.http.route]))

(def start-dev main/start-dev)

(def refresh
  clojure.tools.namespace.repl/refresh)

(def url-for
  (io.pedestal.http.route/url-for-routes routes/routes))
