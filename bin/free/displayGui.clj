(ns free.displayGui
  (:require [clojure.string :as string])
  (:use [keewiiVisual.basic_setting] 
        [keewiiVisual.toolbox]
        [keewiiVisual.vowelList]
        [seesaw.color]))

(defn render-vowel-map [{is-polar :is-polar} frame g]
  (let [WIDTH (.getWidth frame) ;1382 in my laptop
        HEIGHT (.getHeight frame) ;744 in my laptop
        img (buffered-image WIDTH HEIGHT)
        bg (.getGraphics img)
        y (/ (* HEIGHT (- @F1 (formant_lim 0))) (- (formant_lim 1) (formant_lim 0))) ;cartesian coordinate
        x (/ (* WIDTH (- (formant_lim 3) @F2)) (- (formant_lim 3) (formant_lim 2)))] ;cartesian coordinate
    (doto bg
      (.setColor (color :white)) ;background
      (.fillRect 0 0 (.getWidth img)  (.getHeight img)))  
    (if is-polar
      (doto bg 
      (.setColor (color :green))
      (.drawLine (/ WIDTH 2) (/ HEIGHT 2) x y))) ;line only for polar coordinate
    (doto bg
      (.setColor (color :blue)) ;blue square cursor
      (.fillRect (int (- x 10) )  (int (- y 10) )  20 20));
    (doto bg
      (. setFont Font) ;word
      (.setColor (color :black))
      ;f2 f1 pair
      (.drawString (A :name) (formant2pixel A WIDTH 2) (formant2pixel A HEIGHT 1)) ;a
      (.drawString (E :name) (formant2pixel E WIDTH 2) (formant2pixel E HEIGHT 1)) ;e
      (.drawString (I :name) (formant2pixel I WIDTH 2) (formant2pixel I HEIGHT 1))
      (.drawString (O :name) (formant2pixel O WIDTH 2) (formant2pixel O HEIGHT 1)) ;o
      (.drawString (U :name) (formant2pixel U WIDTH 2) (formant2pixel U HEIGHT 1)))
    (.drawImage g img 0 0 nil)
    (.dispose bg)))

  

