(ns service
  (:require [com.stuartsierra.component :as component]
            [http.server]
            [config]
            [next.jdbc :as jdbc]))

(defrecord Database [url datasource]
  component/Lifecycle 

  (start [component]
    (println (format "Using database '%s'" url))
    (let [datasource (jdbc/get-datasource url)]
      (assoc component :datasource datasource)))

  (stop [component]
    (assoc component :datasource nil)))

(defn system [config]
  (let [{:keys [database]} config]
    (component/system-map
     :database (map->Database {:url database})
     :http (component/using
            (http.server/new-server)
            [:database]))))

(defn start []
  (component/start (system (config/load-config))))
