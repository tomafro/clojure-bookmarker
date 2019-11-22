(ns dev.repl
  (:require
   [service]
   [http.routes]
   [http.server]
   [io.pedestal.http :as http]
   [io.pedestal.service-tools.dev]))

(defn -main
  [& args]
  (let [routes (io.pedestal.service-tools.dev/watch-routes-fn #'http.routes/routes)]
    (io.pedestal.service-tools.dev/watch)
    (server/start (assoc (assoc http.server/service-map ::http/join? true) ::http/routes routes))))
