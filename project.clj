(defproject todo-list "0.1.0-SNAPSHOT"
  :description "TODO with Clojure"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [compojure "1.6.2"]
                 [ring/ring-defaults "0.5.0"]
                 [ring/ring-core "1.12.1"]
                 [ring/ring-jetty-adapter "1.12.1"]]
  :plugins [[lein-ring "0.12.6"]]
  :source-paths ["src/clj"]
  :resource-paths ["resources"]
  :clean-targets ^{:protect false} ["target"]
  :ring {:handler todo-list.handler/app})
