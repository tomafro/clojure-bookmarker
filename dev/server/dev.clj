(ns server.dev
  (:require
   [io.pedestal.http :as http]
   [io.pedestal.service-tools.dev]))

; (defn -main
;   [& args]
;   (let [routes (io.pedestal.service-tools.dev/watch-routes-fn #'routes/routes)]
;     (io.pedestal.service-tools.dev/watch)
;     (server/start (assoc (assoc server/service-map ::http/join? true) ::http/routes routes))))
