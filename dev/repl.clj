(ns repl
  (:require
   [clojure.tools.namespace.repl]
   [io.pedestal.http.route :as route]
   [http.routes]))

(defn show-routes
  []
  (route/print-routes (route/expand-routes http.routes/routes)))
