(ns todo-list.handler
  (:require [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as response]))

(defroutes app-routes
   (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes (assoc site-defaults :static {:resources "public"
                                                             :files     "resources/public"})))
