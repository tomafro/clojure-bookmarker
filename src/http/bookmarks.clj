(ns http.bookmarks
  (:require
   [views.bookmarks :as views]
   [database]
   [response]
   [io.pedestal.http.route :as route]))

(defn new-bookmark
  [request]
  (response/ok (views/new-bookmark)))

(defn show-bookmark
  [request]
  (let [bookmark-id (get-in request [:path-params :bookmark-id])]
    (if-let [bookmark (database/find-bookmark database/db (Integer/parseInt bookmark-id))]
      (response/ok (str "<a href=\"" (:bookmarks/url bookmark) "\">" (:bookmarks/title bookmark) "</a>"))
      (response/not-found "Not found"))))

(defn create-bookmark
  [request]
  (response/created "created"))

(defn index-bookmarks
  [request]
  (response/ok "LIST"))

(defn resource-routes
  [name-singular name-plural])

(defn routes
  []
  [["/bookmarks" {:get [:bookmarks `index-bookmarks] :post [:bookmarks/create `create-bookmark]}
     ["/new" {:get [:bookmarks/new `new-bookmark]}]]
    ["/bookmark/:bookmark-id" {:get [:bookmark `show-bookmark]}]])
