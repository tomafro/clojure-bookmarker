(ns routes
  (:require
   [io.pedestal.http.route :as route]
   [response]
   [http.bookmarks]))

(def routes
  [(concat []
          (http.bookmarks/routes))])

(def url-for
  (io.pedestal.http.route/url-for-routes (route/expand-routes routes)))
