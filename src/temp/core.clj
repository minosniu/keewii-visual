(ns temp.core
  (:require [clojure.string :as string]
            [keewiiVisual.toolbox :as toolbox])
  (:use [keewiiVisual.vars]
        [keewiiVisual.threads]
        [free.displayGui] 
        [free.optionsGui])
  (:import (java.awt Dimension)
           (javax.swing JPanel JFrame)
           (java.lang.Runtime)))

;; GUI for paradigm, subj id, start trial...
(do 
  (def all-options (promise)) 
  (create-options-gui all-options)
  @all-options)

;;Draw panel and frame for 2D vowel map 
(def #^{:private true} frame)
(def ^JPanel panel (doto (proxy [JPanel] [] (paint [g] (render-vowel-map g @all-options frame)))
                         (.setPreferredSize (Dimension. (first dim) (last dim)))));only when we restore down the window
(def frame
  (doto (JFrame.) 
      (.add panel) .pack .show (. setAlwaysOnTop true) (. toFront)
      (. setExtendedState JFrame/MAXIMIZED_BOTH)
      (.addKeyListener (toolbox/input-listener))))

;; Start multi-threads for panel painting and UDP reception
(defn re-drawing [x]
  (when true
    (send-off *agent* #'re-drawing)
    (repaint-panel panel)
    (Thread/sleep re-drawing-sleep-ms) nil))
(send-off agent-drawing re-drawing)
(defn udp-reception [x]       
  (when @running
    (udp-receive)
    (send-off *agent* #'udp-reception) 
    nil))
(send-off agent-udp udp-reception)

(. Thread (sleep 5000));Starting pause

; Main loop KTH + data logging
(dosync 
  (reset! Time (now))
  (. (Runtime/getRuntime) exec "wish C:\\Code\\emg_speech_local\\speech_5vowels.tcl")
  (Thread/sleep free-trial-duration)
  (. (Runtime/getRuntime) exec "taskkill /F /IM  wish.exe"))
(System/exit 0)
