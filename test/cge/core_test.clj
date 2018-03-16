(ns cge.core-test
  (:require [clojure.test :refer :all]
            [cge.core :refer :all]))

(def o "O")
(def b "B")
(def r "R")
(def a "A")
(def j "J")
(def w "W")
(def z "Z")

(deftest test-init
  (testing "Init"
    (is (= (init 0 0) []))
    (is (= (init 3 3) [[o o o] [o o o] [o o o]]))
    (is (= (init 1 3) [[o] [o] [o]]))
    (is (= (init 3 1) [[o o o]]))
    (is (= (init 5 6) [[o o o o o] [o o o o o] [o o o o o]
                       [o o o o o] [o o o o o] [o o o o o]]))))

(deftest test-colour-pixel
  (testing "Colour pixel"
    (testing "Inside"
      (is (= (colour-pixel (init 3 3) 1 1 b) [[b o o] [o o o] [o o o]]))
      (is (= (colour-pixel (init 3 3) 3 3 b) [[o o o] [o o o] [o o b]]))
      (is (= (colour-pixel (init 3 3) 2 2 b) [[o o o] [o b o] [o o o]]))
      (is (= (colour-pixel (init 3 3) 3 3 r) [[o o o] [o o o] [o o r]])))
    (testing "Outside"
      (is (= (colour-pixel (init 0 0) 1 1 b) []))
      (is (= (colour-pixel (init 3 3) 4 4 b) [[o o o] [o o o] [o o o]])))))

(deftest test-draw-vertical-segment
  (testing "Draw vertical segment"
    (testing "Inside"
      (is (= (draw-vertical-segment (init 3 3) 2 1 2 b) [[o b o] [o b o] [o o o]]))
      (is (= (draw-vertical-segment (init 3 3) 1 1 3 b) [[b o o] [b o o] [b o o]])))
    (testing "Outside"
      (is (= (draw-vertical-segment (init 3 3) 3 1 4 b) [[o o b] [o o b] [o o b]]))
      (is (= (draw-vertical-segment (init 3 3) 4 1 3 b) [[o o o] [o o o] [o o o]]))
      (is (= (draw-vertical-segment (init 3 3) 1 4 5 b) [[o o o] [o o o] [o o o]])))
    (testing "Inverted Y"
      (is (= (draw-vertical-segment (init 3 3) 1 3 2 b) [[o o o] [o o o] [o o o]])))))

(deftest test-draw-horizontal-segment
  (testing "Draw horizontal segment"
    (testing "Inside"
      (is (= (draw-horizontal-segment (init 3 3) 1 2 2 b) [[o o o] [b b o] [o o o]]))
      (is (= (draw-horizontal-segment (init 3 3) 1 2 3 b) [[o o o] [o o o] [b b o]])))
    (testing "Outside"
      (is (= (draw-horizontal-segment (init 3 3) 1 4 3 b) [[o o o] [o o o] [b b b]]))
      (is (= (draw-horizontal-segment (init 3 3) 4 5 3 b) [[o o o] [o o o] [o o o]]))
      (is (= (draw-horizontal-segment (init 3 3) 1 4 5 b) [[o o o] [o o o] [o o o]])))
    (testing "Inverted X"
      (is (= (draw-horizontal-segment (init 3 3) 3 2 3 b) [[o o o] [o o o] [o o o]])))))

(deftest test-fill-region
  (testing "Fill region"
    (testing "Inside"
      (is (= (fill-region (init 3 1) 1 1 b)) [[b b b]])
      (is (= (fill-region (init 3 3) 1 1 b)) [[b b b] [b b b] [b b b]])
      (is (= (fill-region (colour-pixel (init 3 3) 2 1 r) 3 1 b)) [[b r b] [b b b] [b b b]]))
    (testing "Outside"
      (is (= (fill-region (init 3 1) 4 4 b)) [[o o o]]))))
      ;(is (= (fill-region (init 3 3) 4 4 b)) [[o o o] [o o o] [o o o]]))))

(deftest test-clear
  (testing "Clear"
    (is (= (clear (fill-region (init 3 1) 1 1 b)) [[o o o]]))
    (is (= (clear (fill-region (init 3 3) 1 1 b)) [[o o o] [o o o] [o o o]]))))

(deftest test-multiple-combinations
  (testing "Multiple combinations"
    (is (= (-> (init 5 6)
               (colour-pixel 2 3 "A")
               (fill-region 3 3 "J")
               (draw-vertical-segment 2 3 4 "W")
               (draw-horizontal-segment 3 4 2 "Z"))
           [[j j j j j] [j j z z j] [j w j j j]
            [j w j j j] [j j j j j] [j j j j j]]))))
