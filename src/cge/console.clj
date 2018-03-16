(ns cge.console
  (:use [cge.core
         :only [init clear colour-pixel show fill-region
                draw-vertical-segment draw-horizontal-segment]]
        [clojure.string :only [split]])
  (:gen-class))

(defn- parse-int [number-string]
  (try (Integer/parseInt number-string)
       (catch Exception _ nil)))

(defn- parse-input [input]
  (split input #" "))

(defmulti execute-command
          (fn[command _] (first command)))

(defmethod execute-command "I" [command image]
  (let [[_ arg1 arg2] command
        m (parse-int arg1)
        n (parse-int arg2)]
    (if (and (some? m) (>= m 1)
             (some? n) (>= n 1) (<= n 250))
      (init m n)
      image)))

(defmethod execute-command "C" [_ image]
  (clear image))

(defmethod execute-command "L" [command image]
  (let [[_ arg1 arg2 arg3] command
        x (parse-int arg1)
        y (parse-int arg2)
        c arg3]
    (if (and (some? x) (some? y)
             (some? c) (= 1 (count c)))
        (colour-pixel image x y c)
        image)))

(defmethod execute-command "V" [command image]
  (let [[_ arg1 arg2 arg3 arg4] command
        x (parse-int arg1)
        y1 (parse-int arg2)
        y2 (parse-int arg3)
        c arg4]
    (if (and (some? x) (some? y1) (some? y2)
             (some? c) (= 1 (count c)))
      (draw-vertical-segment image x y1 y2 c)
      image)))

(defmethod execute-command "H" [command image]
  (let [[_ arg1 arg2 arg3 arg4] command
        x1 (parse-int arg1)
        x2 (parse-int arg2)
        y (parse-int arg3)
        c arg4]
    (if (and (some? x1) (some? x2) (some? y)
             (some? c) (= 1 (count c)))
      (draw-horizontal-segment image x1 x2 y c)
      image)))

(defmethod execute-command "F" [command image]
  (let [[_ arg1 arg2 arg3] command
        x (parse-int arg1)
        y (parse-int arg2)
        c arg3]
    (if (and (some? x) (some? y)
             (some? c) (= 1 (count c)))
      (fill-region image x y c)
      image)))

(defmethod execute-command "S" [_ image]
  (do
    (println "=>")
    (show image)
    (flush)
    image))

(defmethod execute-command "X" [_ _]
  nil)

(defmethod execute-command :default [_ image]
  image)

(defn execute-input-command [image input-command]
  (execute-command (parse-input input-command) image))

(defn execute-input-commands [image input-commands]
  (reduce execute-input-command image input-commands))

(defn graphical-editor [image]
   (do
    (print "> ")
    (flush)
    (if-let [edited-image (execute-input-command image (read-line))]
      (recur edited-image))))

(defn -main [& _]
  (graphical-editor (init 0 0)))
