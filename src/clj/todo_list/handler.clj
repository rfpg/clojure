(ns todo-list.handler
  (:require [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.util.response :as response]
            [selmer.parser :as selmer]))

(def state (atom [{:id "1718739873776", :name "moo", :status :to-do}]))

(defn add-task [state n]
  (swap! state conj {:id (str (System/currentTimeMillis))
                     :name n
                     :status :to-do}))

(defn get-tasks [state status]
  (filter (fn [task]
            (= (:status task)
               status))
          @state))

(defn set-task-status [state task-id status]
  (map (fn [task]
         (if (= (:id task) task-id)
           (assoc task :status status)
           task))
       state))

(defn render-task [task]
  (selmer/render
   "<div class='task' draggable='true' ondragstart='drag(event)' id='{{id}}'>{{name}}</div>"
   {:id (:id task)
    :name (:name task)}))

(defroutes app-routes
  (GET "/state" []
    {:status 200
     :headers {"Content-Type" "text/html"},
     :body (str
            "<div id='to-do-tasks' hx-swap-oob='innerHTML'>" (apply str (map render-task (get-tasks state :to-do))) "</div>"
            "<div id='in-progress-tasks' hx-swap-oob='innerHTML'>" (apply str (map render-task (get-tasks state :in-progress))) "</div>"
            "<div id='done-tasks' hx-swap-oob='innerHTML'>" (apply str (map render-task (get-tasks state :done))) "</div>")})

  (POST "/task/:status/:id" [status id]
    (swap! state set-task-status id (keyword status))
    {:status 204})

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes {:static {:resources "public"
                                      :files     "resources/public"}}))
