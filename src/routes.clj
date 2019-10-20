(ns routes
  (:require
   [io.pedestal.http.route :as route]
   [response]
   [bookmarks]))

(def routes
  (route/expand-routes (bookmarks/routes)))
