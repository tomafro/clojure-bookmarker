(ns http.bookmarks
  (:require
   [views.bookmarks :as views]
   [database]
   [response]))

(defn new-bookmark
  [request]
  (response/ok (views/new-bookmark)))

(defn show-bookmark
  [request]
  (let [bookmark-id (get-in request [:path-params :bookmark-id])]
    (if-let [bookmark (database/find-bookmark database/db (Integer/parseInt bookmark-id))]
      (response/ok (str "<a href=\"" (:bookmarks/url bookmark) "\">" (:bookmarks/title bookmark) "</a>"))
      (response/not-found "Not found"))))

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
   [[[(str "/" name) {:get 'http.bookmarks/index-bookmarks :post 'http.bookmarks/create-bookmark}
      ["/:bookmark-id" {:get 'http.bookmarks/new-or-show-bookmark}]]]]))
