(ns ^:figwheel-always yow-workshop.core
    (:require-macros [yow-workshop.macros :refer [p pp]])
    (:require [sablono.core :refer-macros [html]]))


(enable-console-print!)

(defn on-js-reload
  [x]
  (p "App reloaded."))




(defonce !app-state
  (atom {:state "Login"}))


(defn app-template
  [app]
  (html
   [:div
    [:h1 "Hello world"]]))




(defn main
  []
  (let [container (.getElementById js/document "container")

        render! (fn [component]
                  (.render js/React component container))]

    (render! (app-template @!app-state))

    (add-watch !app-state :re-render (fn [_ _ _ new-app]
                                       (render! (app-template new-app))))))

(main)
