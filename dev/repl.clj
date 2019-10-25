(ns repl
  (:require
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as gen]
   [clojure.tools.namespace.repl]
   [io.pedestal.http.route :as route]
   [routes]))

(defn show-routes
  []
  (route/print-routes (route/expand-routes routes/routes)))
