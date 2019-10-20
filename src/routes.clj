(ns routes
  (:require
   [io.pedestal.http.route :as route]
   [response]
   [bookmarks]))

(def routes
  (route/expand-routes
   #{["/bookmarks/new" :get  'bookmarks/new-bookmark   ]
     ["/bookmarks" :post 'bookmarks/create-bookmark]
     ["/bookmarks/:bookmark-id" :get 'bookmarks/new-or-show-bookmark]}))
