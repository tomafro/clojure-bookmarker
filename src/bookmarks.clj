(ns bookmarks
  (:require
   [bookmarks.views :as views]
   [response]))

(defn new-bookmark
  [request]
  (response/ok (views/new-bookmark)))

(defn show-bookmark
  [request]
  (response/ok (views/show-bookmark)))

(defn new-or-show-bookmark
  [request]
  (if (= "new" (get-in request [:path-params :bookmark-id]))
    (new-bookmark request)
    (show-bookmark request)))

(defn create-bookmark
  [request]
  (response/created "created"))

(defn index-bookmarks
  [request]
  (response/ok "LIST"))

(defn routes
  ([] (routes "bookmarks"))
  ([name]
   [[[(str "/" name) {:get 'bookmarks/index-bookmarks :post 'bookmarks/create-bookmark}
      ["/new" {:get 'bookmarks/new-bookmark}]
      ["/:bookmark-id" {:get 'bookmarks/new-or-show-bookmark}]]]]))
