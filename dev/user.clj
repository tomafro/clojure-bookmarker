(ns user
 (:require
  [clojure.spec.alpha :as s]
  [clojure.spec.gen.alpha :as gen]
  [clojure.tools.namespace.repl]))

(def refresh
  clojure.tools.namespace.repl/refresh)

(require 'dev)

; (def url-for
;   (io.pedestal.http.route/url-for-routes routes/routes))
