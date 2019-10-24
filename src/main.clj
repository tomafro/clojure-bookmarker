(ns main
  (:require
   [aero.core :refer (read-config)]
   [io.pedestal.http :as http]
   [io.pedestal.http.route :as route]
   [io.pedestal.interceptor.helpers :as interceptor]
   [io.pedestal.test :as test]
   [routes]))

(def server (atom nil))

(defn uuid [] (.toString (java.util.UUID/randomUUID)))

(defn add-x-request-id
  [response]
  (assoc response :headers (assoc (:headers response) "X-Request-Id" (uuid))))

(defn app-interceptors
  [service]
  (update-in service [::http/interceptors]
             #(vec (->> %
                        (cons (interceptor/on-response add-x-request-id))))))

  ; (update-in service [::http/interceptors]
  ;            #(vec (->> %
  ;                       (cons cors/dev-allow-origin)
  ;                       (cons servlet-interceptor/exception-debug)))))

(def service-map
  (-> {::http/routes routes/routes
       ::http/type   :jetty
       ::http/port   8890
       ::http/join?  false}
      (http/default-interceptors)
      (app-interceptors)))

(defn start-dev []
  (reset! server
          (http/start (http/create-server service-map))))

(defn stop-dev []
  (http/stop @server))

(defn restart []
  (stop-dev)
  (start-dev))
