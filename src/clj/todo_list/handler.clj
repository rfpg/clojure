(ns todo-list.handler
  (:require [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.util.response :as response]
            [selmer.parser :as selmer]))

(defonce todo-list-atom (atom [{:id "1718739873776", :name "moo", :status :to-do}]))


(defn add-task 
  "Adds a new task with the name n to the state atom." 
  [state-atom n]
  (swap! state-atom conj {:id (str (System/currentTimeMillis))
                          :name n
                          :status :to-do}))

(defn get-tasks [state-atom status]
  (filter (fn [task]
            (= (:status task)
               status))
          @state-atom))

(defn set-task-status [state task-id status]
  (map (fn [task]
         (if (= (:id task) task-id)
           (assoc task :status status)
           task))
       state))

; this is making the status change; it returns a new state object with status changes 
(defn bulk-change-status [state from-status to-status]
  (map (fn [task]
         (if (= (:status task) from-status)
           (assoc task :status to-status)
           task))
       state))

; this is applying the change / swap; calling function above (making status changes) and updating the value of atom
(defn bulk-change-status! [state-atom from-status to-status]
  (swap! state-atom bulk-change-status from-status to-status))

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
            "<div id='to-do-tasks' hx-swap-oob='innerHTML'>" (apply str (map render-task (get-tasks todo-list-atom :to-do))) "</div>"
            "<div id='in-progress-tasks' hx-swap-oob='innerHTML'>" (apply str (map render-task (get-tasks todo-list-atom :in-progress))) "</div>"
            "<div id='done-tasks' hx-swap-oob='innerHTML'>" (apply str (map render-task (get-tasks todo-list-atom :done))) "</div>")})

  (POST "/task/:status/:id" [status id]
    (swap! todo-list-atom set-task-status id (keyword status))
    {:status 204})

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes {:static {:resources "public"
                                      :files     "resources/public"}}))

(comment
  (add-task todo-list-atom "Final project")
  (add-task todo-list-atom "Exam") 
  (add-task todo-list-atom "Quiz") 
  @todo-list-atom
  (bulk-change-status! todo-list-atom :in-progress :done)
  
  )

