(defproject cmd-graphical-editor "0.1.0-SNAPSHOT"
  :description "Command line graphical editor"
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :main ^:skip-aot cge.console
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
