(ns routes
  (:require
   [io.pedestal.http.route :as route]
   [response]
   [bookmarks]))

(def routes (bookmarks/routes))
