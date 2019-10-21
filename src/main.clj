(ns main
  (:require
   [aero.core :refer (read-config)]
   [io.pedestal.http :as http]
   [io.pedestal.http.route :as route]
   [io.pedestal.test :as test]
   [bookmarks]
   [routes]))

(def server (atom nil))

(def service-map
  {::http/routes routes/routes
   ::http/type   :jetty
   ::http/port   8890})

(defn create-server []
  (http/create-server service-map))                                                           

(defn start []
  (swap! server
         (constantly (http/start (create-server))))                                 
  nil)

(defn stop []
  (swap! server http/stop)                                                         
  nil)

(defn start-dev []
  (reset! server
          (http/start (http/create-server
                       (assoc service-map
                              ::http/join? false)))))

(defn stop-dev []
  (http/stop @server))

(defn restart []
  (stop-dev)
  (start-dev))
