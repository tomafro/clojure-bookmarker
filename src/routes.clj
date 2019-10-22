(ns routes
  (:require
   [io.pedestal.http.route :as route]
   [response]
   [bookmarks]
   [authorisation.basic]))

; (def blocked-route
;   (route/expand-routes [["/blocked" {:get (authorisation.basic/interceptor "tom" "rocks")}]]))

(def blocked-route
  (route/expand-routes [[["/blocked" {:get [(authorisation.basic/interceptor "tom" "rocks")]}]]]))

(def routes
  blocked-route)
