(ns server
  (:require
   [aero.core :refer (read-config)]
   [io.pedestal.http :as http]
   [io.pedestal.http.route :as route]
   [io.pedestal.interceptor.helpers :as interceptor]
   [io.pedestal.test :as test]
   [routes]))

(defn uuid [] (.toString (java.util.UUID/randomUUID)))

(def x-request-id
  (interceptor/around
    ::x-request-id 
    (fn [context] (assoc-in context [:request  :headers "x-request-id"] (get-in context [:request :headers "x-request-id"] (uuid))))
    (fn [context] (assoc-in context [:response :headers "X-Request-Id"] (get-in context [:request :headers "x-request-id"])))))

(defn app-interceptors
  [service]
  (update-in service [::http/interceptors]
             #(vec (->> %
                        (cons x-request-id)))))

(def server (atom nil))

(def service-map
  (-> {::http/routes routes/routes
       ::http/type   :jetty
       ::http/port   8890
       ::http/join?  false}
      (http/default-interceptors)
      (app-interceptors)))

(defn start
  []
  (reset! server
          (http/start (http/create-server service-map))))

(defn stop
  []
  (http/stop @server))

(defn restart
  []
  (stop)
  (start))
