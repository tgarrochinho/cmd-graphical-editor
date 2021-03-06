(ns cge.core
  (:gen-class))

(def white-pixel "O")

; number of pixel columns (width <=> m)
; number of pixel rows (height <=> n)
(defn init [m n]
  "I M N. Create a new M x N image with all pixels coloured white (O)."
  (vec (repeat n (vec (repeat m white-pixel)))))

(defn clear [image]
  "C. Clears the table, setting all pixels to white (O)."
  (init (count (first image)) (count image)))

(defn- pixel-colour
  "Get pixel (X,Y) colour"
  ([image x y]
   (try (get-in image [(- y 1) (- x 1)])
        (catch IndexOutOfBoundsException e nil)))
  ([image [x y]]
   (pixel-colour image x y)))

(defn colour-pixel
  ([image x y c]
   "L X Y C. Colours the pixel (X,Y) with colour C."
   (if (some? (pixel-colour image x y))
     (assoc-in image [(- y 1) (- x 1)] c)
     image))
  ([image [x y] c]
   (colour-pixel image x y c)))

(defn show [image]
  "S. Show the contents of the current image"
  (doseq [row image]
    (println (apply str row))))

;; #############
;; Segments
;; #############

(defn draw-vertical-segment [image x y1 y2 c]
  "V X Y1 Y2 C. Draw a vertical segment of colour C
   in column X between rows Y1 and Y2 (inclusive)."
  (reduce #(colour-pixel %1 x %2 c) image (range y1 (+ y2 1))))

(defn draw-horizontal-segment [image x1 x2 y c]
  "H X1 X2 Y C. Draw a horizontal segment of colour C
   in row Y between columns X1 and X2 (inclusive)."
  (reduce #(colour-pixel %1 %2 y c) image (range x1 (+ x2 1))))

;; #############
;; Regions
;; #############

(defn- neighbor-pixels [x y]
  "Find neighbor pixels"
  (let [coordinates '([-1 -1] [-1 0] [-1 1] [0 -1] [0 1] [1 -1] [1 0] [1 1])]
    (map #(vec (map + [x y] %)) coordinates)))

(defn- filter-same-colour-neighbor-pixels [image [x y]]
  "Filter neighbor pixels containg the same colour"
  (let [pixel-current-colour (pixel-colour image x y)]
    (->> (neighbor-pixels x y)
         (filter #(= pixel-current-colour (pixel-colour image %))))))

(defn- fill-same-colour-connected-pixels [image connected-pixels c]
  "Fills connected pixels containing the same colour, with colour C"
  (if (empty? connected-pixels)
    image
    (let [connected-pixel (first connected-pixels)]
      (recur (colour-pixel image connected-pixel c)
             (->> (filter-same-colour-neighbor-pixels image connected-pixel)
                  (concat connected-pixels)
                  (distinct)
                  (rest))
             c))))

(defn fill-region [image x y c]
  "F X Y C. Fill the region R with the colour C.
  R is defined as: Pixel (X,Y) belongs to R.
  Any other pixel which is the same colour as (X,Y)
  and shares a common side with any pixel in R also
  belongs to this region."
  (let [pixel-current-colour (pixel-colour image x y)]
    (if (or (nil? pixel-current-colour)
            (= pixel-current-colour c))
      image
      (fill-same-colour-connected-pixels image (list [x y]) c))))
