(ns free.core
  (:require [clojure.string :as string])
  (:use [keewiiVisual.basic_setting]
        [keewiiVisual.threads]
        [keewiiVisual.toolbox :only [def-keewii quit KTH-operator]] 
        [free.displayGui] 
        [free.optionsGui]
        [seesaw.core])) ; for calling KTH synthesizer

;; GUI for paradigm, subj id, start trial...
(do 
  (def all-options (promise)) 
  (create-options-gui all-options)
  @all-options)

;; GUI for experiment & fre-exploration
(def-keewii []
  (let [painter (partial render-vowel-map @all-options )
        cvs (canvas :id :canvas :background "#BBBBBB" :paint painter)
        t (timer (fn [e] (repaint! cvs)) :delay re-drawing-sleep-ms)]
    (frame 
      :title "Free-exploration" 
      :width (dim 0) :height (dim 1)
      :content cvs
      :on-close :exit
      :minimum-size [(dim 0) :by (dim 1)])))
(run :dispose)

;; Start multi-threads for panel painting and UDP reception
(send-off agent-udp udp-reception)
(Thread/sleep Delayed-start);Starting pause

; Main loop KTH + data logging
(dosync 
  (reset! Time (now))
  (KTH-operator true) 
  (Thread/sleep free-trial-duration)
  (KTH-operator false))
(quit)