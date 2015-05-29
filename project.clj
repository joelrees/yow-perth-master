(defproject yow-workshop "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [org.clojure/clojurescript "0.0-3211"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [sablono "0.3.4"]
                 [datascript "0.11.2"]]

  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-figwheel "0.3.1"]]

  :source-paths ["src"]

  :cljsbuild {
              :builds [{:id "dev"
                        :source-paths ["src"]

                        :figwheel { :on-jsload "yow-workshop.core/on-js-reload" }

                        :compiler {:main yow-workshop.core
                                   :asset-path "js/compiled/out"
                                   :output-to "resources/public/js/compiled/yow_workshop.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :source-map-timestamp true
                                   
                                   ;;Ignore this warning for Datascript (https://github.com/tonsky/datascript/issues/57)
                                   :warnings {:single-segment-namespace false}}}]}

  :figwheel {
             ;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             :css-dirs ["resources/public/css"] ;; watch and update CSS
             })
