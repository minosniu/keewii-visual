(ns singleTarget.displayGui
  (:require [clojure.string :as string])
  (:use [keewiiVisual.vars] 
        [keewiiVisual.toolbox]
        [keewiiVisual.vowelList]
        [seesaw.color]))

(defn render-vowel-map [{is-polar :is-polar} frame g]
  (let [WIDTH (.getWidth frame) ;1382 in my laptop
        HEIGHT (.getHeight frame) ;744 in my laptop
        img (buffered-image WIDTH HEIGHT) 
        bg (.getGraphics img)
        y (/ (* HEIGHT (- @F1 (formant_lim 0))) (- (formant_lim 1) (formant_lim 0))) 
        x (/ (* WIDTH (- (formant_lim 3) @F2)) (- (formant_lim 3) (formant_lim 2))) 
        VOWEL  @alphabet
        x_pos (formant2pixel VOWEL WIDTH 2)
        y_pos (formant2pixel VOWEL HEIGHT 1)
        CHAR (VOWEL :name)]
    (doto bg
      (.setColor (color :white)) ;background
      (.fillRect 0 0 (.getWidth img)  (.getHeight img)))
    
    (if @CUR_TGT_sleep
      (doseq []  ;trial
        (when is-polar
          (doto bg
            (.setColor (color :green))
            (.drawLine (/ WIDTH 2) (/ HEIGHT 2) x y)));only for polar coordinate
        (doto bg
          (.setColor (color :blue)) ;blue square
          (.fillRect (int (- x 10) )  (int (- y 10) )  20 20))
        (doto bg
          (. setFont Font)
          (.setColor (color :black))
          (.drawString CHAR x_pos y_pos)))
      (doto bg ; interval / relax status
        (.setFont Font)
        (.setColor (color :black))
        (.drawString (R :name) 
          (formant2pixel R WIDTH 2);1700 = actual position
          (formant2pixel R HEIGHT 1)))) 
    (.drawImage g img 0 0 nil)
    (.dispose bg)))